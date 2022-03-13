#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>
#include <sys/socket.h>
#include <sys/types.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <ctype.h>
#include <netdb.h>
#include <signal.h>
#include <limits.h>
#include <netinet/in.h>
#include <errno.h>

/**
 * Struct to represent a client in the linked list of the server
 * Holds necessary information concerning the connection, name, stats and the
 * next client in the linked list.
 * The head client is initialised as and remains empty to ensure Shared has a
 * valid client to always point to.
 */
typedef struct Clients {
    int clientSocket;
    struct Clients* nextClient;
    char* clientName;
    int sayCount;
    int kickCount;
    int listCount;
    FILE* toClient;
    FILE* fromClient;
    int valid;
} Clients;

/**
 * Struct used to hold shared information in the server.
 * Contains the server stats, the authcode, a link to the head client and the
 * server's mutex lock.
 */
typedef struct Shared {
    int sayCount;
    int nameCount;
    int authCount;
    int kickCount;
    int listCount;
    int leaveCount;
    char* authCode;
    struct Clients* headClient;
    pthread_mutex_t clientsLock;
} Shared; 

/**
 * Struct used to pass information between the primary thread and the new
 * client thread.
 * Passes the clients socket and a pointer to the Shared server info struct.
 */
typedef struct Passer {
    int clientSocket;
    struct Shared* sharedInfo;
} Passer;

/**
 * Struct used to pass info to the SIGHUP handler thread.
 * Contains a pointer to the Shared server info struct (for stats) and the 
 * SIGHUP handler info.
 */
typedef struct SigPass {
    struct Shared* sharedPass;
    sigset_t hangSig;
} SigPass;

void* client_handler(void* arg);

/**
 * Function to print out the server statistics when the SIGHUP interupt is
 * received. Loops through and prints out the client stats first and then
 * printfs out the server stats.
 *
 * Takes in: The sharedInfo struct
 */
void sig_stats(struct Shared* sharedInfo) {
    pthread_mutex_lock(&sharedInfo->clientsLock);
    fprintf(stderr, "@CLIENTS@\n");
    fflush(stderr);
    struct Clients* client = sharedInfo->headClient;
    //Goes through and prints of each clients stats
    while (client->nextClient != NULL) {
        client = client->nextClient;
        fprintf(stderr, "%s:SAY:%i:KICK:%i:LIST:%i\n", client->clientName, 
                client->sayCount, client->kickCount, client->listCount);
        fflush(stderr);
    }
    //Prints of the server's stats
    fprintf(stderr, "@SERVER@\n");
    fflush(stderr);
    fprintf(stderr, 
            "server:AUTH:%i:NAME:%i:SAY:%i:KICK:%i:LIST:%i:LEAVE:%i\n",
            sharedInfo->authCount, sharedInfo->nameCount, sharedInfo->
            sayCount, sharedInfo->kickCount, sharedInfo->listCount,
            sharedInfo->leaveCount);
    fflush(stderr);
    pthread_mutex_unlock(&sharedInfo->clientsLock);
}

/**
 * Function to close the clients socket and FILEs when the client is leaving.
 *
 * Takes in: The client struct who has left the server.
 */
void close_client(struct Clients* client) {
    fclose(client->toClient);
    fclose(client->fromClient);
    close(client->clientSocket);
}

/**
 * Function to check the given string for invalid characters, specifically 
 * chars under the value of 32. Replaces identified characters with '?'
 *
 * Takes in: A string pointer corresponding to the input to be
 * checked and updated.
 */
void check_input(char** retInput) {
    char* local = *retInput;
    size_t len = strlen(local);
    for (int i = 0; i < len; i++) {
        if (local[i] != '\n' && local[i] != '\0' && local[i] != ' ') {
            if (((int) local[i]) < 32) {
                //Replaces necessary characters with '?' symbol
                local[i] = '?';
            }
        }
    }
    *retInput = local;
}

/**
 * Function to get input from the given client. Also checks input before
 * finishing the function.
 *
 * Takes in: The FILE pointer corresponding to receiving from the client and
 * a string pointer to update with the input received.
 *
 * Returns: 1 if the connection/client has become invalid (i.e. disconnected)
 *          0 if the client connection is still valid and/or a message was
 *          received
 */
int get_input(FILE* fromClient, char** receivedInput) {
    char* input = "";
    char buffer[100];
    int currentStringLength = 0;
    int invalid = 1;
    while (fgets(buffer, sizeof(buffer), fromClient) != 0) {
        invalid = 0;
        currentStringLength += strlen(buffer) + 1;
        char* reallocBuffer = NULL;
        reallocBuffer = realloc(reallocBuffer, currentStringLength);
        memset(reallocBuffer, 0, currentStringLength);
        strcpy(reallocBuffer, input);
        input = strdup(reallocBuffer);
        strcat(input, buffer);
        if (feof(fromClient)) {
            break;
        }
        if (buffer[strlen(buffer) - 1] == '\n') {
            break;
        }
    }
    *receivedInput = input;
    //Checks the input for any invalid characters before returning the string
    check_input(receivedInput);
    return invalid;
}

/**
 * Function to setup the actual server itself. Goes through the process of
 * creating, binidng and listening through the server socket.
 *
 * Takes in: The server port
 *
 * Returns: The server socket fd
 *
 * Exits with error code of 2 if any errors occur during server setup.
 */
int listener(const char* serverPort) {
    int listenSocket;
    int opt = 1;
    struct addrinfo* result = 0;
    struct addrinfo hints;
    memset(&hints, 0, sizeof(struct addrinfo));
    hints.ai_family = AF_INET;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE;
    int err;
    if ((err = getaddrinfo(NULL, serverPort, &hints, &result))) {
        fprintf(stderr, "Communications error\n");
        fflush(stderr);
        exit(2);
    }
    if ((listenSocket = socket(AF_INET, SOCK_STREAM, 0)) == 0) {
        fprintf(stderr, "Communications error\n");
        fflush(stderr);
        exit(2);
    }
    if (setsockopt(listenSocket, SOL_SOCKET, SO_REUSEADDR, &opt,
            sizeof(int)) < 0) {
        fprintf(stderr, "Communications error\n");
        fflush(stderr);
        exit(2);
    }   
    if (bind(listenSocket, (struct sockaddr*)result->ai_addr,
            sizeof(struct sockaddr)) < 0) {
        fprintf(stderr, "Communications error\n");
        fflush(stderr);
        exit(2);
    }
    if (listen(listenSocket, INT_MAX) < 0) {
        perror("Listen");
        fprintf(stderr, "Communications error\n");
        fflush(stderr);
        exit(2);
    }
    return listenSocket;
}

/**
 * Function to send a formatted message across to all clients in the server.
 *
 * Takes in: The sharedInfo struct and the content to be sent to the clients.
 */
void server_message(struct Shared* threadShared, char* content) {
    struct Clients* head = threadShared->headClient->nextClient;
    if (head != NULL) {
        fprintf(head->toClient, "%s\n", content);
        fflush(head->toClient);
        while (head->nextClient != NULL) {
            head = head->nextClient;
            fprintf(head->toClient, "%s\n", content);
            fflush(head->toClient);
        }
    }
}

/**
 * Function to remove the given client from the linked list of clients. Goes
 * through the list until it finds the client and removes them from that point.
 *
 * Takes in: The sharedInfo struct and the client name for who is being removed
 *
 * Returns: Either the removed client's struct or NULL if the client does not
 * exist in the server
 */
struct Clients* remove_client(struct Shared* threadShared, char* name) {
    struct Clients* head = threadShared->headClient;
    struct Clients* previous = threadShared->headClient;
    //Goes through the list of clients to find the client to remove
    while (head->nextClient != NULL) {
        previous = head;
        head = head->nextClient;
        if (!strcmp(head->clientName, name)) {
            if (head->nextClient == NULL) {
                previous->nextClient = NULL;
            } else {
                previous->nextClient = head->nextClient;
            }
            return head;
        }
    }
    //If no valid client is found, NULL means that the name is invalid
    return NULL;
}

/**
 * Function to handle client authenication. Sends the AUTH command and replies
 * with the OK command if the string given by the client matches the severs
 * auth code. Otherwise, terminates the connection with the client and closes 
 * the thread.
 *
 * Takes in: The sharedInfo and current client structs
 */
void authenticate(struct Shared* sharedInfo, struct Clients* client) {
    FILE* toClient = client->toClient;
    FILE* fromClient = client->fromClient;
    char* authCode = sharedInfo->authCode;
    char* buffer;
    char* authSig = "AUTH:";
    char* okSig = "OK:";
    char* saver;
    int valid = 0;
    fprintf(toClient, "%s\n", authSig);
    fflush(toClient);
    while (!valid) {
        int error = get_input(fromClient, &buffer);
        if (error) {
            pthread_exit(NULL);
        }
        buffer[strcspn(buffer, "\n")] = '\0';
        saver = buffer;
        char* sig = strtok_r(buffer, ":", &saver);
        //Checks for valid AUTH token
        if (!strcmp(sig, "AUTH")) {
            pthread_mutex_lock(&sharedInfo->clientsLock);
            sharedInfo->authCount += 1;
            pthread_mutex_unlock(&sharedInfo->clientsLock);
            //Checks if the authcode is empty
            if (authCode[0] == '\n' || authCode[0] == '\0') {
                fprintf(toClient, "%s\n", okSig);
                fflush(toClient);
                valid = 1;
            } else {
                //Checks if the the code is invalid. If so, client is kicked.
                if (strcmp(authCode, saver)) {
                    close_client(client);
                    pthread_exit(NULL);
                } else {
                    //If the code is valid, the client is added.
                    fprintf(toClient, "%s\n", okSig);
                    fflush(toClient);
                    valid = 1;
                }
            }
        }    
    }
}

/**
 * Function to check the given name against the current clients already in 
 * the server to prevent duplication.
 *
 * Takes in: The name to check for and the head client to start from.
 *
 * Returns: 0 if the name is already present and shouldn't be allowed
 * 1 if the name is fine and there are no other clients with that name
 */
int name_checker(char* name, struct Clients* headClient) {
    if (name[0] == '\n' || name[0] == '\0' || name[0] == ' ') {
        return 0;
    }
    if (headClient->nextClient == NULL) {
        return 1;
    }
    headClient = headClient->nextClient;
    if (!strcmp(headClient->clientName, name)) {
        return 0;
    }
    while (headClient->nextClient != NULL) {
        headClient = headClient->nextClient;
        if (!strcmp(headClient->clientName, name)) {
            return 0;
        }
    }
    return 1;
}

/**
 * Function to handle the naming of the client. Goes through the process of
 * sending WHO, NAME_TAKEN and OK tokens to the client until it finds an
 * appropriate name.
 *
 * Takes in: The sharedInfo and current client structs
 *
 * Returns: The name for the current client
 */
char* client_namer(struct Shared* sharedInfo, struct Clients* client) { 
    struct Clients* headClient = sharedInfo->headClient;
    FILE* toClient = client->toClient;
    FILE* fromClient = client->fromClient;
    char* buffer;
    char* whoSig = "WHO:";
    char* nameSig = "NAME";
    char* takenSig = "NAME_TAKEN:";
    char* okSig = "OK:";
    char* saver = NULL;
    fprintf(toClient, "%s\n", whoSig);
    fflush(toClient);
    while (1) {
        int error = get_input(fromClient, &buffer);
        if (error) {
            pthread_exit(NULL);
        }
        saver = buffer;
        char* sig = strtok_r(buffer, ":", &saver);
        //Checks for the valid NAME signature in the message
        if (!strcmp(sig, nameSig)) {
            pthread_mutex_lock(&sharedInfo->clientsLock);
            sharedInfo->nameCount += 1;
            pthread_mutex_unlock(&sharedInfo->clientsLock);
            if (saver[strlen(saver) - 1] == '\n') {
                saver[strlen(saver) - 1] = '\0';
            }
            //If the name is valid as is, client is allowed to enter
            if (name_checker(saver, headClient)) {
                fprintf(toClient, "%s\n", okSig);
                fflush(toClient);
                return saver;
            } else {   
                //Else, the client is given the NAME TAKEN token 
                fprintf(toClient, "%s\n", takenSig);
                fflush(toClient);
                fprintf(toClient, "%s\n", whoSig);
                fflush(toClient);
            }
        }
    }
}

/**
 * Function to create and format the LIST message to send back to the client.
 *
 * Takes in: The sharedInfo struct
 *
 * Returns: The formatted LIST message
 */
char* send_list(struct Shared* threadShared) {
    struct Clients* headClient = threadShared->headClient->nextClient;
    char* list;
    char* prevList = "LIST:";
    list = malloc(strlen(prevList) + strlen(headClient->clientName) + 1);
    sprintf(list, "%s%s", prevList, headClient->clientName);
    //Goes through the list of clients and adds them to the current string
    while (headClient->nextClient != NULL) {
        headClient = headClient->nextClient;
        prevList = strdup(list);
        list = malloc(strlen(prevList) + strlen(headClient->clientName) + 1);
        sprintf(list, "%s,%s", prevList, headClient->clientName);
    }
    return list;
}

/**
 * Function to handle when the client specifically leaves the server. Removes
 * the client and prints/sends the formatted leave message out to all clients.
 *
 * Takes in: The sharedInfo and the leaving client structs
 */
void leave_handle(struct Shared* sharedInfo, struct Clients* client) {
    pthread_mutex_lock(&sharedInfo->clientsLock);
    sharedInfo->leaveCount += 1;
    char message[strlen("LEAVE:") + strlen(client->clientName) + 1];  
    sprintf(message, "LEAVE:%s", client->clientName);
    client = remove_client(sharedInfo, client->clientName);
    pthread_mutex_unlock(&sharedInfo->clientsLock);
    server_message(sharedInfo, message);
    fprintf(stdout, "(%s has left the chat)\n", client->clientName);
    fflush(stdout);
    close_client(client);
    pthread_exit(NULL);
}

/**
 * Function to handle SAY commands from the client. Parses the message and 
 * then calls the necessary function to send the message to all clients.
 *
 * Takes in: The sharedInfo and current client structs and the client message.
 */
void say_handle(struct Shared* sharedInfo, struct Clients* client,
        char* saver) {
    pthread_mutex_lock(&sharedInfo->clientsLock);
    sharedInfo->sayCount += 1;
    pthread_mutex_unlock(&sharedInfo->clientsLock);
    client->sayCount += 1;
    saver[strlen(saver) - 1] = '\0';
    //Prepares message to send to entire server
    char message[strlen("MSG:") + strlen(client->clientName) +
            strlen(saver) + 1];       
    sprintf(message, "MSG:%s:%s", client->clientName, saver);
    server_message(sharedInfo, message);
    fprintf(stdout, "%s: %s\n", client->clientName, saver);
    fflush(stdout);
}

/**
 * Function to handle kick commands from the client. Kicks and removes the
 * given client name if they exist.
 *
 * Takes in: The sharedInfo and current client structs and the client name.
 */
void kick_handle(struct Shared* sharedInfo, struct Clients* client, 
        char* kickedName) {
    pthread_mutex_lock(&sharedInfo->clientsLock);
    sharedInfo->kickCount += 1;
    client->kickCount += 1;
    kickedName[strlen(kickedName) - 1] = '\0';
    struct Clients* kickedClient = remove_client(sharedInfo, kickedName);
    pthread_mutex_unlock(&sharedInfo->clientsLock);
    //Check the the kickedClient is a valid client 
    if (kickedClient != NULL) {
        char* kickMessage = "KICK:";
        fprintf(kickedClient->toClient, "%s\n", kickMessage);
        fflush(kickedClient->toClient);
        kickedClient->valid = 0;
        char message[strlen("LEAVE:") + strlen(kickedClient->clientName) + 1];
        sprintf(message, "LEAVE:%s", kickedClient->clientName);
        server_message(sharedInfo, message);
        fprintf(stdout, "(%s has left the chat)\n", kickedClient->clientName);
        fflush(stdout);
    }
}

/**
 * Function to handle list commands from the client. Sends the ordered and 
 * formatted current chatters list back to the current clients.
 *
 * Takes in: The sharedInfo and current client structs
 */
void list_handle(struct Shared* sharedInfo, struct Clients* client) {
    pthread_mutex_lock(&sharedInfo->clientsLock);
    sharedInfo->listCount += 1;
    pthread_mutex_unlock(&sharedInfo->clientsLock);
    client->listCount += 1;
    //Prepares the client list to send to the client
    char* message = send_list(sharedInfo);
    fprintf(client->toClient, "%s\n", message);
    fflush(client->toClient);
}

/**
 * Function to handle messaging to and from the client. Sits in a loop while 
 * the client is valid (i.e. hasn't been kicked or anything similar) and reads
 * messages from the client. Calls the necessary function for each operation.
 * Closes the current thread after the client has finished.
 *
 * Takes in: The sharedInfo and current client structs
 */
void message_handle(struct Shared* sharedInfo, struct Clients* client) {
    while (client->valid == 1) {
        char* buffer;
        int error = get_input(client->fromClient, &buffer);
        if (!(client->valid)) {
            //If the client is invalid, the thread should cease and terminate
            break;
        }
        char* saver = NULL;
        char* sig;
        //Goes through message handling to enact appropriate response
        if (error) {
            //Corresponds to a client mysteriously leaving/disconnecting
            sig = "LEAVE";
        } else {
            sig = strtok_r(buffer, ":", &saver);
        }
        if (!strcmp(sig, "LEAVE")) {
            if (saver == NULL || saver[0] == '\0') {
                leave_handle(sharedInfo, client);
            }
        } else if (!strcmp(sig, "SAY")) {
            say_handle(sharedInfo, client, saver);
        } else if (!strcmp(sig, "KICK")) {
            kick_handle(sharedInfo, client, saver);
        } else if (!strcmp(sig, "LIST")) {
            if (saver == NULL || saver[0] == '\0' || saver[0] == '\n') {
                list_handle(sharedInfo, client);
            }
        }
        usleep(100000);
    }
    close_client(client);
    pthread_exit(NULL);
}

/**
 * Function to add clients to the linked list in lexicographical order
 *
 * Takes in: The struct corresponding to the head client and the struct for
 * the current client.
 *
 * Returns: The struct for the current client after it has been added.
 */
struct Clients* add_client(struct Clients* headClient,
        struct Clients* clientHolder) {
    struct Clients* current = clientHolder;
    //If there is no client in the server, appends the client onto the head
    if (headClient->nextClient == NULL) {
        headClient->nextClient = current;
        return current;
    } else {
        //If there is at least one client, goes through the list to find 
        //alphabetical position to be added in at
        struct Clients* previousClient = headClient;
        while (previousClient->nextClient != NULL) {
            struct Clients* nextClient = previousClient->nextClient;
            if ((strcmp(nextClient->clientName,
                    clientHolder->clientName) > 0)) {
                //Had found the correct alphabetical position and is added
                current->nextClient = nextClient;
                previousClient->nextClient = current;
                return current;
            }
            previousClient = nextClient;
        }
        previousClient->nextClient = current;
        return current;
    } 
}

/**
 * Function used to handle and organise a client once they join. Goes through
 * the process of creating the associated data structs for the client and
 * then authenticating and naming the client. Once the client has joined, 
 * starts the function used to handle messaging with the client.
 *
 * Takes in: A void pointer corressponding to the Passer struct used to convey
 * the necessary info.
 *
 * Returns: NULL if the function reaches the end
 */
void* client_handler(void* arg) {
    char* name;
    struct Passer* threadPasser = arg;
    struct Shared* sharedInfo = threadPasser->sharedInfo;
    int fd = threadPasser->clientSocket;
    FILE* toClient = fdopen(dup(fd), "w");
    FILE* fromClient = fdopen(dup(fd), "r");
    free(arg);

    //Creates and preps the necessary struct for the new client
    struct Clients* headClient = sharedInfo->headClient;
    struct Clients* clientHolder = (Clients*) malloc(sizeof(struct Clients));
    clientHolder->toClient = toClient;
    clientHolder->fromClient = fromClient;
    clientHolder->clientSocket = fd;

    //Goes through authentication and name negotiation
    authenticate(sharedInfo, clientHolder);
    name = client_namer(sharedInfo, clientHolder);

    //Once the client has joined, goes through final struct prep
    pthread_mutex_lock(&sharedInfo->clientsLock);
    clientHolder->clientName = name;
    clientHolder->nextClient = NULL;
    clientHolder->valid = 1;
    clientHolder->sayCount = 0;
    clientHolder->kickCount = 0;
    clientHolder->listCount = 0;
    //Add clients to the linked list of clients
    struct Clients* current = add_client(headClient, clientHolder);
    pthread_mutex_unlock(&sharedInfo->clientsLock);
    
    //Sends out entry message to the server
    char message[strlen("ENTER:") + strlen(name) + 1];        
    sprintf(message, "ENTER:%s", name);
    server_message(sharedInfo, message);
    fprintf(stdout, "(%s has entered the chat)\n", name);
    fflush(stdout);

    //Starts the message handle to control the client from here on
    message_handle(sharedInfo, current);
    return NULL;
}

/**
 * Function to handle the SIGHUP interupts. Sits in a permanent loop and 
 * calls the necessary function when SIGHUP occurs.
 *
 * Takes in: void pointer corresponding to the SigPas struct used to pass the
 * necessary info to this function.
 */
void* hang_handle(void* arg) {
    struct SigPass* sigPasser = arg;
    sigset_t hangInfo = sigPasser->hangSig;
    struct Shared* sharedInfo = sigPasser->sharedPass;
    int sigCode;
    //Sits in a loop and waits for the SIGHUP error code
    while (1) {
        sigwait(&hangInfo, &sigCode);
        if (sigCode == SIGHUP) {
            sig_stats(sharedInfo);
        }
    }
    return NULL;
}

/**
 * Function to handle client connections once the server is prepped and ready.
 * Sits in a loop and crreates a new thread for each client that connects. 
 *
 * Takes in: the server socket value and sharedInfo struct
 */
void client_connects(int serverSocket, struct Shared* sharedInfo) {
    int fd;
    struct sockaddr_in clientAddress;
    socklen_t clientAddressSize;
    while (1) {
        clientAddressSize = sizeof(struct sockaddr_in);
        //Sits and waits to accept new client connection
        fd = accept(serverSocket, (struct sockaddr*)&clientAddress,
                &clientAddressSize);
        struct Passer* threadPasser = NULL;
        threadPasser = (struct Passer*)malloc(sizeof(struct Passer)); 
        threadPasser->sharedInfo = sharedInfo;
        threadPasser->clientSocket = fd;
        pthread_t threadId;
        //Creates the new thread for the client
        pthread_create(&threadId, NULL, client_handler, threadPasser);
    }
}

/**
 * Function to initialise the Shared data structure.
 *
 * Takes in: The set head client struct and the string auth code.
 *
 * Returns: The setup SharedInfo struct.
 */
struct Shared* shared_init(struct Clients* headClient, char* auth) {
    struct Shared* sharedInfo = NULL;
    sharedInfo = (struct Shared*) malloc(sizeof(struct Shared));
    sharedInfo->headClient = headClient;
    sharedInfo->authCode = auth;
    sharedInfo->sayCount = 0;
    sharedInfo->kickCount = 0;
    sharedInfo->listCount = 0;
    sharedInfo->nameCount = 0;
    sharedInfo->authCount = 0;
    sharedInfo->leaveCount = 0;
    headClient->clientName = "NULL";
    pthread_mutex_init(&sharedInfo->clientsLock, NULL);
    return sharedInfo;
}

/**
 * Function to prep the server before allowing clients to join. Mainly deals 
 * with creating data structs and  handlers for SIGPIPE and SIGHUP interupts,
 * with the former being ignored and the later prompting the required
 * function. Finally, prints out the server port once prep is complete.
 *
 * Takes in the fd corresponding to the server socket and the string auth code.
 *
 * Exits with error status of 1 if an issue occurs with the server port.
 */
void server_prep(int serverSocket, char* auth) {
    struct sigaction clientAction;
    clientAction.sa_handler = SIG_IGN;
    clientAction.sa_flags = 0;
    sigaction(SIGPIPE, &clientAction, NULL);
    struct Clients* headClient = NULL;
    headClient = (struct Clients*)malloc(sizeof(struct Clients));
    struct Shared* sharedInfo = shared_init(headClient, auth);
    pthread_t sigThread;
    sigset_t hangSet;
    struct SigPass* sigPasser = NULL;
    sigemptyset(&hangSet);
    sigaddset(&hangSet, SIGHUP);
    int error = pthread_sigmask(SIG_BLOCK, &hangSet, NULL);
    if (error) {
        exit(1);
    }    
    sigPasser = (struct SigPass*) malloc(sizeof(struct SigPass));
    sigPasser->sharedPass = sharedInfo;
    sigPasser->hangSig = hangSet;
    error = pthread_create(&sigThread, NULL, hang_handle, sigPasser);
    if (error) {
        exit(1);
    }    
    struct sockaddr_in serverPort;
    memset(&serverPort, 0, sizeof(struct sockaddr_in));
    socklen_t len = sizeof(struct sockaddr_in);
    if (getsockname(serverSocket, (struct sockaddr*) &serverPort, &len)) {
        exit(1);
    }
    //Prints out the port to stderr when the server is setup
    fprintf(stderr, "%u\n", ntohs(serverPort.sin_port));
    fflush(stderr);
    client_connects(serverSocket, sharedInfo);
}

/**
 * Function to get the server auth code.
 *
 * Takes in: the FILE pointer to the text file from where to get the code.
 *
 * Returns: the servers authentication code.
 */
char* get_auth(FILE* authFile) {
    char authCode[400];
    fgets(authCode, 400, authFile);
    char* auth = strdup(authCode);
    auth[strcspn(auth, "\n")] = '\0';
    return auth;
}

/**
 * Main function for the server. Starts up the server and then preps the 
 * server to connect with clients.
 *
 * Takes in the args corresponding to the location for the auth code file and
 * the optional arguement of a set port number.
 */
int main(int argc, char** argv) {
    int serverSocket;
    const char* serverPort;
    if (argc == 3) {
        serverPort = argv[2];
    } else if (argc == 2) {
        serverPort = "0";
    } else {
        fprintf(stderr, "Usage: server authfile [port]\n");
        fflush(stderr);
        exit(1);
    }
    FILE* authFile = fopen(argv[1], "r");
    if (authFile == NULL) {
        fprintf(stderr, "Usage: server authfile [port]\n");
        fflush(stderr);
        exit(1);
    }
    char* authCode = get_auth(authFile);
    serverSocket = listener(serverPort);
    server_prep(serverSocket, authCode);
}
