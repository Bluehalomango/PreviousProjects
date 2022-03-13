from a1_support import *

def display_game(game, grid_size):
        
        """
        Function to print the board for the game.

        Goes through and adds each column and row specific to the grid size.
        Gets called after each player action and shows the changes to the game string/board.

        Parameters:
        game (str): Game string.
        grid_size (int): Size of game.

        Prints:
        Game board/string
        """
        
        char_pos=0
        wall_length = grid_size + 1
        index = 0
        print (' ',WALL_VERTICAL,end ='')   
        for i in range (1,grid_size + 1):
                #The first loop is to print the column numbers
            if i < 10:
                print ('',i, WALL_VERTICAL, end ='')
            else:
                print ('',i, end = WALL_VERTICAL)
                
        for i in range(0,grid_size):
                #The second loop prints the Alpha character for each row.
            print ('')
            print (WALL_HORIZONTAL * wall_length * 4)
            print (ALPHA[i], WALL_VERTICAL, end='')
            char_pos+=1
            
            for i in range(0, grid_size):
                    #The last loop fills in each row with the respective game string value.
                print ('',game[index], WALL_VERTICAL, end='')
                index +=1
                
        print ('')
        print (WALL_HORIZONTAL * wall_length * 4)

def parse_position(player_input, grid_size):
        
    """
    Takes the players input and goes through various checks to validate it.

    Parameters:
        grid_size (int): Size of game.
        player_input (str): The player's guess/input from the main function.

    Returns:
        None: The input is invalid
        (str): The location of the selected cell on the game board
    """
    
    for i,c in enumerate(ALPHA):
            #Checks if the Alpha character is within the grid range.
        if c in player_input:
            if i >= grid_size:
                return None
            else:
                digits = ''
                for num in player_input :
                    if num.isdigit() :
                        digits += num
                if digits == '':
                    return None
                if int(digits) <= grid_size and digits != '':
                        #Checks that the input number is valid        
                    position = (i,int(digits) -1)
                    if len(str(i)) + len(digits) == len(player_input) or player_input[:2] == 'f ':
                            #Check to make sure each character/digit in the input has been used
                        return position
                    else:
                        return None
                else:
                    return None
    else:
        return None

def position_to_index(position,grid_size):
        
    """
    Takes the cell position and translates it into the game string index.

    Parameters:
        position (str): The location of the selected cell on the game board.
        grid_size (int): Size of game.
        
    Returns:
        (int): The index of the selected cell in reference to the game string
    """
    
    index = position[0] * grid_size + position[1]
    return index

def replace_character_at_index(game, index, char):
        
    """
    Replaces the character in the game string with the new visible character.

    New placement is for three reasons.
    The first is to reveal the cells that have become visible from a player's guess (big search function).
    The second is when a player flags a cell.
    The third is if a player reveals a pokemon, in which case the game will reveal all the pokemon locations.
    
    Parameters:
        game (str): Game string.
        index (int): The index of the selected cell in reference to the game string.
        char (str): The character that the cell is being replaced with.

    Returns:
        (str): An updated version of the game string with the neccessary characters replaced.
    """
    
    game = game[:index] + char + game[index + 1:]
    return game

def index_in_direction(index, grid_size, DIRECTIONS):
        
    """
    This functions is used to find adjacent cells.
    It takes the current index and the direction and checks whether
    the cell in the set direction is adjacent to the current index.
    
    Parameters:
        index (int): The index of the selected cell in reference to the game string.
        grid_size (int): Size of game.
        direction (str): The direction to be checked for adjacency.
        
    Returns:
        None: if the new index is not adjacent to the current index
        (int): The new index from the adjacent cell
    """
    
    if DIRECTIONS == 'up':
        newindex = index - grid_size
        if index >= grid_size:
            return newindex
        else:
            return None
    if DIRECTIONS == 'down':
        newindex = index + grid_size
        if newindex < grid_size * grid_size:
            return newindex
        else:
            return None
    if DIRECTIONS == 'left':
        newindex = index -1
        if index % grid_size !=0:
            return newindex
        else:
            return None
    if DIRECTIONS == 'right':
        newindex = index + 1
        if newindex % grid_size !=0:
            return newindex
        else:
            return None
    if DIRECTIONS == 'up-left':
        newindex = index - grid_size
        newindex -= 1
        if index < grid_size or index % grid_size ==0:
            return None
        else:
            return newindex
    if DIRECTIONS == 'up-right':
        newindex = index - grid_size + 1
        if index < grid_size or newindex % grid_size == 0:
            return None
        else:
            return newindex
    if DIRECTIONS == 'down-left':
        newindex = index + grid_size - 1
        if newindex < grid_size * grid_size and index % grid_size !=0:
            return newindex
        else:
            return None
    if DIRECTIONS == 'down-right':
        newindex = index + grid_size + 1
        if newindex < grid_size * grid_size and newindex % grid_size !=0:
            return newindex
        else:
            return None

def neighbour_directions(index, grid_size):
        
    """
    Runs through a loop that checks each direction for which cells should be revealed.
    
    Parameters:
        grid_size (int): Size of game.
        index (int): Index of the currently selected cell

    Returns:
        (list<int>): List of the neighbouring directions that contain a cell that should be revealed.
    """
    
    newindex = 0
    indexlist = []
    for i in range(0,8):
            #Runs through the directions list that contains each of the eight directions.
        newindex = index_in_direction(index, grid_size, DIRECTIONS[i])
        if newindex != None:
            indexlist += (newindex, )
    return(indexlist)

    
def number_at_cell(game, pokemon_locations, grid_size, index):
        
    """
    Takes the index of a cell and finds all neighbouring cells that contain a pokemon.

    Parameters:
        game (str): Game string.
        grid_size (int): Size of game.
        pokemon_locations (tuple<int, ...>): Tuple of all Pokemon's locations.
        index (int): Index of the currently selected cell

    Returns:
        (int): How many pokemon are adjacent to the currently selected cell (index).
    """
    
    indexlist = neighbour_directions(index, grid_size)
    pokemons = 0
    
    for i, c in enumerate (indexlist):
        for n, m in enumerate (pokemon_locations):
            if c == pokemon_locations[n]:
                #If the neighbouring cell and the pokemon location match, then there is a pokemon in that position.
                pokemons +=1
    return pokemons

def big_fun_search(game, grid_size, pokemon_locations, index):

    """
    Searching adjacent cells to see if there are any Pokemon"s present.

    Using some sick algorithms.

    Find all cells which should be revealed when a cell is selected.

    For cells which have a zero value (i.e. no neighbouring pokemons) all the cell"s
    neighbours are revealed. If one of the neighbouring cells is also zero then
    all of that cell"s neighbours are also revealed. This repeats until no
    zero value neighbours exist.

    For cells which have a non-zero value (i.e. cells with neightbour pokemons), only
    the cell itself is revealed.

    Parameters:
        game (str): Game string.
        grid_size (int): Size of game.
        pokemon_locations (tuple<int, ...>): Tuple of all Pokemon's locations.
        index (int): Index of the currently selected cell

    Returns:
        (list<int>): List of cells to turn visible.
    """
    
    queue = [index]
    discovered = [index]
    visible = []

    if game[index] == FLAG:
        return queue

    number = number_at_cell(game, pokemon_locations, grid_size, index)
    if number != 0:
        return queue

    while queue:
        node = queue.pop()
        for neighbour in neighbour_directions(node, grid_size):
            if neighbour in discovered:
                continue

            discovered.append(neighbour)
            if game[neighbour] != FLAG:
                number = number_at_cell(game, pokemon_locations, grid_size, neighbour)
                if number == 0:
                    queue.append(neighbour)
            visible.append(neighbour)
    return visible


def flag_cell(game, index):

    """
    Function to flag cells. Runs replace_character_at_index to replace the index.

    Parameters:
        game (str): Game string.
        index (int): Index of the currently selected cell

    Returns:
        (str): Returns an updated game string with either a flag added or removed.
    """
    
    if game[index] == FLAG:
            #If there is already a flag, it removes the flag and replaces it with an unexposed character.
        char = UNEXPOSED       
        game = replace_character_at_index(game, index, char)
        return game
    else:
        game = replace_character_at_index(game, index, FLAG)
        return game

def check_win (game, pokemon_locations):

    """
    Function to check if the player has won the game.
    Goes through various checks to verify the win condition.
    If none return false, then the player has won the game and the function returns True.

    Parameters:
        game (str): Game string.
        pokemon_locations (tuple<int, ...>): Tuple of all Pokemon's locations.

    Returns:
        True (bool): If the win condition is valid.
        False (bool): If the win condition is not valid.
    """
    
    for i,c in enumerate(game[::-1]):
            #Goes through the game string and checks that there are no unexposed characters.
        if c == UNEXPOSED:
            return False
    if game.count(FLAG) == len(pokemon_locations):
            #Checks that the amount of flags in the game string is equal to the amount of pokemon.
        for i, c in enumerate(pokemon_locations):
        #Loop that checks every flag location with the pokemon locations. Returns false is location mismatches.
            if game[pokemon_locations[i]] != FLAG:
                return False
        return True
    else:
        return False

def main():

    """
    The main function for the game that runs everything.
    At the start, it asks the player for a grid size and number of pokemon.
    Using this information, it will run generate_pokemons to randomly assign the pokemon locations.

    Next, it will start the main loop that it returns to after each player action.
    This starts by running display_game to print the game setup and running
    the check_win function to check the win condition (mainly used after the players action).

    Now, it will ask for the players input and run through several checks to determine the action.
    After, the returned position will be put through position_to_index to convert it to a game string index.
    
    Here, it checks whether the player wants to flag a location or guess a location.
    If so, the flag_cell function will run and the game will loop back with the returned game string.

    Otherwise, it will check if the selected cell contains a pokemon.
    If so, the game will reveal all pokemon, set lose to true and print the lose text.

    If none of these checks stop the game, the function will run the player's guess through several functions.
    These will reveal all neccessary cells.

    After this, the function will go back and continue the game if conditions are valid.
    """
    
    grid_size = int(input("Please input the size of the grid: "))
    if grid_size > 26:
        #If the input grid size is over 26 (Length of the alphabet and row), it will ask for a smaller number.
            while grid_size > 26:
                    print ('That is not a valid grid size. The grid size ends at 26. Please enter a new size.')
                    grid_size = int(input("Please input the size of the grid: "))
    number_of_pokemons = int(input("Please input the number of pokemons: "))
    if number_of_pokemons >= grid_size * grid_size:
        #If the input number of pokemon is greater than or equal to the square of the grid size
        #(leaving no unexposed space), it will return invalid and ask for a smaller number.
            while  number_of_pokemons >= grid_size * grid_size:
                    print ('That is not a valid number of pokemon. There needs to be at least one free space. Please enter a new number.')
                    number_of_pokemons = int(input("Please input the number of pokemons: "))
    pokemon_locations = generate_pokemons(grid_size, number_of_pokemons)
    cell_count = grid_size * grid_size
    game = UNEXPOSED * cell_count
    lose = False
    win = False
    
    while win == False and lose == False:
        display_game(game,grid_size)       
        win = check_win(game, pokemon_locations)
        if check_win(game, pokemon_locations) == True:
            print('You win.')
            
        else:
            print('')
            player_input = str(input("Please input action: "))
            if player_input == 'h':
                    #If the player inputs 'h', the game will display the help text.    
                print(HELP_TEXT)
            elif player_input == ':)':
                #If the player inputs ':)', the game will restart with new pokemon locations.
                print ("It's rewind time.")
                pokemon_locations = generate_pokemons(grid_size, number_of_pokemons)
                game = UNEXPOSED * cell_count                
            elif player_input == 'q':
                    #If the player inputs 'q', the game will ask whether the player wants to quit or not.
                player_input = str(input("You sure about that buddy? (y/n): "))
                if player_input == 'y':
                        #If they respond with 'y', the game will stop.
                    print ("Catch you on the flip side.")
                    break
                elif player_input =='n':
                        #If they respond with 'n', it will loop back to ask the player for another input.
                    print("Let's keep going.")
                else:
                    print (INVALID)

            else:
                position = parse_position(player_input, grid_size)
                if position == None:
                    print(INVALID)
                else:
                    index = position_to_index(position,grid_size)
                    if player_input[0] == 'f':
                            #Check for whether the player is flagging a location
                        game = flag_cell(game, index)
                        
                    else:
                        if game[index] != FLAG:
                            for i in pokemon_locations:
                                if index == i:
                                        #Checks if the current index contains a pokemon.
                                    for i,c in enumerate(pokemon_locations):
                                        game = replace_character_at_index(game, pokemon_locations[i], POKEMON)
                                        #Reveals all the pokemon locations if the index contains a pokemon.
                                    display_game(game,grid_size)                      
                                    print('You have scared away all the pokemons.')
                                    lose = True
                                    break
                                
                            else:
                                visible = big_fun_search(game, grid_size, pokemon_locations, index)
                                char = str(number_at_cell(game, pokemon_locations, grid_size, index))
                                game = replace_character_at_index(game, index, char)
                                    #Reveals current index
                                for i,c in enumerate(visible):
                                    #Runs through the list of cells to be turned visible from the search function
                                    index = visible[i]
                                    if game[visible[i]] != FLAG:
                                        char = str(number_at_cell(game, pokemon_locations, grid_size, index))
                                        game = replace_character_at_index(game, index, char)
                                            #Replaces the character of each cell to be turned visible.
                        
        
if __name__ == "__main__":
    main()
