#include <string.h>
#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <unistd.h>
#include <arpa/inet.h>
#include <pthread.h>
#include <signal.h>

/**
 * Function to handle the errors and exits for the various exit states.
 *
 * Takes in the error code associated with the error.
 *
 * Exits with   - status of 0 for a normal exit
 *              - status of 1 for an incorrect client useage
 *              - status of 2 for comms error.
 *              - status of 3 if the user had been kicked
 *              - status of 4 if the user failed authentication
 */
void exit_handle(int error) {
    if (error == 0) {
        exit(0);
    } else if (error == 1) {
        fprintf(stderr, "Usage: client name authfile port\n");
        fflush(stderr);
        exit(1);
    } else if (error == 2) {
        fprintf(stderr, "Communications error\n");
        fflush(stderr);
        exit(2);
    } else if (error == 3) {
        fprintf(stderr, "Kicked\n");
        fflush(stderr);
        exit(3);
    } else if (error == 4) {
        fprintf(stderr, "Authentication Error\n");
        fflush(stderr);
        exit(4);
    }
}

/**
 * Function to receive input from a specified stream. Goes through the process
 * of expanding buffer to the appropriate size.
 *
 * Takes in the FILE pointer for the input stream and the error code that 
 * should be returned if no input is received (this is to differentiate input
 * during auth, name and general server messages).
 *
 * Returns the received input string
 * */
char* get_input(FILE* inStream, int error) {
    char* input = "";
    char buffer[100];
    int currentStringLength = 0;
    int invalid = 1;
    while (fgets(buffer, sizeof(buffer), inStream)) {
        invalid = 0;
        currentStringLength += strlen(buffer) + 1;
        char* reallocBuffer = NULL;
        reallocBuffer = realloc(reallocBuffer, (currentStringLength *
                sizeof(char)));
        memset(reallocBuffer, 0, currentStringLength * sizeof(char));
        strcpy(reallocBuffer, input);
        input = reallocBuffer;
        strcat(input, buffer);
        if (feof(inStream)) {
            exit_handle(error);
        }
        if (buffer[strlen(buffer) - 1] == '\n') {
            break;
        }
    }     
    if (invalid) {
        exit_handle(error);
    }
    return input;
}

/**
 * Function to parse a given * command. That is, any statement beginning with
 * a * given through stdin.
 *
 * Takes in the command input
 *
 * Returns the command input without the * character.
 */
char* command_parse(char* input) {
    memmove(input, input + 1, strlen(input));
    return input;
}

/**
 * Function to handle user input. Sits in a loop that reads stdin and sends
 * the message off to the server, parsed either with a SAY: append for text
 * messages or without the * symbol for server commands.
 *
 * Takes in the FILE pointer used to send to the server.
 *
 * Exits with status code of 0 if the client sends the 'LEAVE:' command.
 */
void input_handle(FILE* toServer) { 
    while (1) {
        char* output = NULL;
        char* input = get_input(stdin, 0);
        if (input != NULL && input[0] != '\0') {
            if (input[0] == '*') {
                output = command_parse(input);
                //If sending a leave message, the client must exit afterwards
                if (!strcmp(output, "LEAVE:\n")) {
                    fprintf(toServer, "%s", output);
                    fflush(toServer); 
                    exit(0);
                } else {
                    fprintf(toServer, "%s", output);
                    fflush(toServer);
                }
            } else {
                char* prepend = "SAY:";
                fprintf(toServer, "%s%s\n", prepend, input);
                fflush(toServer);
            }
        }
    }
}

/**
 * Function to handle output from the server to the client. Parsing the 
 * different formats for 'ENTER', 'LEAVE', 'MSG' and 'LIST' commands, printing
 * out the appropriate responses to stdout.
 *
 * Takes in the file pointer to read from the server.
 *
 * Exits with error code of 3 if the current client has been kicked.
 */
void output_handle(FILE* fromServer) { 
    char* sig;
    char* name;
    char* msg;
    while (1) {
        char* bufferOut = get_input(fromServer, 2);
        char* saver = bufferOut;
        sig = strtok_r(bufferOut, ":", &saver);
        //Go through command parsing with server message
        if (!strcmp(sig, "KICK")) {
            exit_handle(3);
        }
        //Remove trailing newline if present
        if (saver[strlen(saver) - 1] == '\n') {
            saver[strlen(saver) - 1] = '\0';
        }
        if (!strcmp(sig, "ENTER")) {
            fprintf(stdout, "(%s has entered the chat)\n", saver);
            fflush(stdout);
        } else if (!strcmp(sig, "LEAVE")) {
            fprintf(stdout, "(%s has left the chat)\n", saver);
            fflush(stdout);
        } else if (!strcmp(sig, "MSG")) {
            msg = saver;
            name = strtok_r(saver, ":", &msg);
            fprintf(stdout, "%s: %s\n", name, msg);
            fflush(stdout);
        } else if (!strcmp(sig, "LIST")) {
            fprintf(stdout, "(current chatters: %s)\n", saver);
            fflush(stdout);
        }
    }
}

/**
 * Function to handle connecting to the server.
 *
 * Takes in the port number to connect to.
 *
 * Returns the client socket fd used to communicate with the server.
 *
 * Exits with error status of 2 is any errors occur during connection.
 */
int connect_server(int port) {
    int clientSocket = 0;
    struct sockaddr_in serverAddress;
    if ((clientSocket = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        exit_handle(2);
    }
    serverAddress.sin_family = AF_INET;
    serverAddress.sin_port = htons(port);
    if (inet_pton(AF_INET, "127.0.0.1", &serverAddress.sin_addr) <= 0) {
        exit_handle(2);
    }
    if (connect(clientSocket, (struct sockaddr*) &serverAddress,
            sizeof(serverAddress)) < 0) {
        exit_handle(2);
    }
    return clientSocket;
}

/**
 * Function called after creating the input thread to specifically handle the
 * clients stdin input
 *
 * Takes in void pointer to the FILE pointer used to send to the server.
 *
 * Returns null if the input handle finishes.
 */
void* input_thread(void* arg) {
    FILE* toServer = (FILE*)arg;
    input_handle(toServer);
    return NULL;
}

/**
 * Function called after creating the outut thread to specifically handle the
 * messages sent to this client from the server.
 *
 * Takes in void pointer to the FILE pointer used to recieve from server.
 *
 * Returns null if the output handle finishes.
 */
void* output_thread(void* arg) {
    FILE* fromServer = (FILE*)arg;
    output_handle(fromServer);
    return NULL;
}

/**
 * Function used to handle name negotiation with the server. Takes the given
 * name string and sends it to the server to check it. Repeatedly appends a 
 * growing int value each time it receives the 'NAME_TAKEN' token. Finishes
 * when the server accepts the name.
 *
 * Takes in the FILE pointer used to send to the server and receive from the 
 * server as well as the string name for the current client.
 */
void namer(FILE* fromServer, FILE* toServer, char* name) {
    char* buffer;
    int nameSet = 0, nameIteration = 0;
    char* nameSig = "NAME:";
    char* sig;
    char* outName = strdup(name);
    while (!nameSet) {
        buffer = get_input(fromServer, 2);
        char* saver = buffer;
        sig = strtok_r(buffer, ":", &saver);
        //Handle the different responses that the client can currently receive
        if (!strcmp(sig, "WHO")) {
            fprintf(toServer, "%s%s\n", nameSig, name);
            fflush(toServer);
        } else if (!strcmp(sig, "NAME_TAKEN")) {
            int versionLength = snprintf(NULL, 0, "%d", nameIteration);
            char nameAppend[versionLength + 1];
            sprintf(nameAppend, "%d", nameIteration);
            nameIteration++;
            char newname[strlen(nameAppend) + strlen(outName) + 1];
            sprintf(newname, "%s%s", outName, nameAppend);
            name = strdup(newname);
        } else if (!strcmp(buffer, "OK")) {
            nameSet = 1;
            break;
        }
    }
}

/**
 * Handles the authnetication process with the server. Sends the specified 
 * AUTH code to the server and either continues if it receives the 'OK' signal
 * or exits if the code is incorrect ('KICK' command in this case).
 *
 * Exits with error status of 4 if the AUTH code is incorrect.
 * 
 * Takes in the FILE pointer used to send to the server and receive from the 
 * server as well as the FILE pointer for the AUTH code to join the server.
 */
void authenticate(FILE* fromServer, FILE* toServer, FILE* auth) {
    char* buffer;
    int waiting = 1;
    char* authSig = "AUTH:";
    char* sig;
    while (waiting) {
        buffer = get_input(fromServer, 4);
        char* saver = buffer;
        sig = strtok_r(buffer, ":", &saver);
        if (!strcmp(sig, "AUTH")) {
            waiting = 0;
            break;
        } 
    }
    char authCode[400];
    //Gets the AUTH code from the given auth text file.
    fgets(authCode, 400, auth);
    authCode[strcspn(authCode, "\n")] = '\0';
    fprintf(toServer, "%s%s\n", authSig, authCode);
    fflush(toServer);
    waiting = 1;
    while (waiting) {
        buffer = get_input(fromServer, 4);
        char* saver = buffer;
        sig = strtok_r(buffer, ":", &saver);
        if (!strcmp(sig, "OK")) {
            waiting = 0;
            break;
        }
    }
}

/**
 * Main function of client. If given the correct number of arguements, starts
 * the client functionality by connecting to the server, completing the
 * AUTH and name negotiation and then sitting idly to send and receive 
 * messages.
 *
 * Takes in the arguements for the client name, text file for the auth code 
 * and the server port to connect to.
 *
 * Exits with status code of 1 if given the incorrect number of arguements or
 * when given an invalid AUTH file.
 */
int main(int argc, char** argv) {
    if (argc == 4) {
        int port = atoi(argv[3]);
        FILE* auth = fopen(argv[2], "r");
        if (auth == NULL) {
            exit_handle(1);
        }
        char* name = argv[1];

        //Create connection with server
        int clientSocket = connect_server(port);
        int clientSocketDup = dup(clientSocket);

        FILE* fromServer = fdopen(clientSocket, "r");
        FILE* toServer = fdopen(clientSocketDup, "w");
        
        //Creates a sig handler to deal with losing connection with the server
        struct sigaction lostConnection;
        lostConnection.sa_handler = exit_handle;
        lostConnection.sa_flags = 0;
        sigaction(SIGPIPE, &lostConnection, NULL);

        //Handle authentication and name negotiation with server
        authenticate(fromServer, toServer, auth);
        namer(fromServer, toServer, name);
       
        //Create the two separate input and output handle threads
        pthread_t inputThread;
        pthread_create(&inputThread, NULL, input_thread, toServer);

        pthread_t outputThread;
        pthread_create(&outputThread, NULL, output_thread, fromServer);

        pthread_join(inputThread, NULL);
        pthread_join(outputThread, NULL);
    } else {
        exit_handle(1);
    }
}
