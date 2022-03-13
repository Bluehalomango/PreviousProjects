SPECIAL_TILES = {
    "S": "start",
    "E": "end",
    "L": "locked"
}

PIPES = {
    "ST": "straight",
    "CO": "corner",
    "CR": "cross",
    "JT": "junction-t",
    "DI": "diagonals",
    "OU": "over-under"
}

START = {
    0: "N",
    1: "E",
    2: "S",
    3: "W"
    }

END = {
    0: "S",
    1: "W",
    2: "N",
    3: "E"
    }

CONNECTIONS = {
    "straight" : {
    0: ("N","S"),
    1: ("E","W"),
    2: ("N","S"),
    3: ("E","W")
    },
    "corner" : {
    0: ("N","E"),
    1: ("E","S"),
    2: ("W","S"),
    3: ("N","W")
    },
    "cross" : {
    0:(("N","S", "E","W")), 
    1:(("N","S","E","W")),
    2:(("N","S","E","W")),
    3:(("N","S","E","W"))
    },
    "junction-t" : {    
    0: ("E","S","W"),
    1: ("N","S","W"),
    2: ("E","N","W"),
    3: ("E","S","N")
    },
    "diagonals" : {
    0:(("N","E"),("W","S")), 
    1:(("E","S"),("N","W")),
    2:(("W","S"),("N","E")),
    3:(("N","W"),("E","S"))
    },
    "over-under" : {
    0:(("N","S"),("W","E")), 
    1:(("N","S"),("W","E")),
    2:(("N","S"),("W","E")),
    3:(("N","S"),("W","E"))
    }
    }

DIRECTION = {
    "N": ("S",-1, 0),
    "E": ("W",0, 1),
    "S": ("N",1, 0),
    "W": ("E",0,-1)
    }
    

class Tile(object):

    """
    Class for Tile object. Is used for the empty tiles on the game board.
    """
    def __init__(self, name, selectable=True):
        """
        Initialises the Tile Class

        Sets name and selectable status
        
        Parameters:
            name (str): Name of the tile.
            selectable (bool): Whether the tile is selectable or not
        """
        self._name = name
        self._selectable = selectable

    def get_name(self):
        """
        Returns name of Tile

        Returns:
            (str) : Name of tile
        """
        return self._name

    def get_id(self):
        """
        Returns ID of Tile

        Returns:
            (str) : ID of tile
        """
        return self.__class__.__name__.lower()

    def set_select(self, select):
        """
        Sets a tiles selectable status

        Parameters:
            select (bool): Whether the tile is selectable or not
        """
        self._selectable = select

    def can_select(self):
        """
        Returns selectable status of tile

        Returns:
            (bool) : Whether the tile is selectable or not
        """
        if self._selectable == True:
            return True
        else:
            return False
    
    def __str__(self):
        """
        Returns str of Tile

        Returns:
            (str) : Name and selectable status of tile
        """
        return "Tile('{0}', {1})".format(self._name, self._selectable)

    def __repr__(self):
        """
        Returns str of Tile

        Returns:
            (str) : Name and selectable status of tile
        """
        return "Tile('{0}', {1})".format(self._name, self._selectable)


class Pipe(Tile):
    
    """
    Class for Pipe object, child of Tile class. Is used for the various pipes utilised.
    These are stored in PIPES dictionary.
    """
    
    def __init__(self, name, orientation=0, selectable=True):
        """
        Initialises the Pipe Class

        Sets name, orientation and selectable status
        Name and selectable status are initialised through Tile class
        
        Parameters:
            name (str): Name of the pipe.
            orientation (int): Sets the orientation of the pipe object. If no input, defaults to 0
            selectable (bool): Whether the pipe is selectable or not
        """
        super().__init__(name, selectable)
        self._orientation = orientation

    def get_connected(self, side):
        """
        Checks the currently selected pipe for available connections in each direction (N, S, E and W)
        
        Parameters:
            side (str): The side of the pipe being checked

        Returns:
            (list): A list containing all the directions that the selected side connects to
        """
        pipename = super().get_name()
        connect = []
        #Goes through the connections dictionary to find the appropriate pipe information
        for x in CONNECTIONS:
            if x == pipename:
                #Checks if the current pipe has more than one tuple (diagonal or over-under pipe) in order to direct them through specific code
                #Diagonals and over-unders have multiple tuples in their dictionary slot so a different method is needed to find the connections
                if (len(CONNECTIONS[x][0][0])) > 1:
                    for i, c in enumerate(CONNECTIONS[x][self.get_orientation()]):
                        if side in c:
                            for z in CONNECTIONS[x][self.get_orientation()][i]:
                                if z != side:
                                    #Goes through the directions in the dictionary for the current pipe and adds those that connect
                                    connect.append(z) 
                else:
                    if side in CONNECTIONS[x][self.get_orientation()]:
                        for i in CONNECTIONS[x][self.get_orientation()]:
                            if i != side:
                                #Goes through the directions in the dictionary for the current pipe and adds those that connect
                                connect.append(i)                           
        return connect

    def rotate(self, direction):
        """
        Rotates the currently selected pipes orientation
                
        Parameters:
            direction (int): The direction to rotate (Positive is clockwise, negative is anti-clockwise)
        """
        if direction > 0:
            #Checks for clockwise
            self._orientation = self._orientation + 1
            if self._orientation > 3:
                self._orientation = 0
        else:
            #Anti-clockwise
            self._orientation = self._orientation - 1
            if self._orientation < 0:
                self._orientation = 3

    def get_orientation(self):
        """
        Returns the orientation of the selected pipe

        Returns:
            (int): The orientation of the pipe
        """
        return self._orientation

    def __str__(self):
        """
        Returns str of Pipe

        Returns:
            (str) : Name and orientation of the pipe
        """
        return "Pipe('{0}', {1})".format(self._name, self._orientation)

    def __repr__(self):
        """
        Returns str of Pipe

        Returns:
            (str) : Name and orientation of the pipe
        """
        return "Pipe('{0}', {1})".format(self._name, self._orientation)
    
class SpecialPipe(Pipe):
    """
    Class for special pipes, child of Pipe.
    These are special pipes that have different variables to normal pipes. They are the start and end pipes.
    """
    def __init__(self, name, orientation=0, selectable=False):
        """
        Initialises the Special pipe Class

        Sets name, orientation and selectable status (which is always False) through the Pipe method
        
        Parameters:
            name (str): Name of the pipe.
            orientation (int): Sets the orientation of the pipe object. If no input, defaults to 0
        """
        super().__init__(name, orientation, False)
        
    def __repr__(self):
        """
        Returns str of Special pipe

        Returns:
            (str) : Name and orientation of the Special pipe
        """
        return "{0}({1})".format(self.__class__.__name__, self._orientation)

    def __str__(self):
        """
        Returns str of Special pipe

        Returns:
            (str) : Name and orientation of the Special pipe
        """
        return "{0}({1})".format(self.__class__.__name__, self._orientation)

    def get_id(self):
        """
        Returns ID of Special pipe (set as "special_pipe")

        Returns:
            (str) : ID of Special pipe
        """
        return ("special_pipe")
    

class StartPipe(SpecialPipe):
    """
    Class for start pipe, child of Special Pipe.
    Is used for the starting pipe in the game board.
    """
    def __init__(self, orientation=0):
        """
        Initialises the StartPipe Class

        Sets name, orientation and selectable status (which is always False)
        
        Parameters:
            orientation (int): Sets the orientation of the pipe object. If no input, defaults to 0
        """
        super().__init__('start', orientation, False)

    def get_connected(self, side=None):
        """
        Checks the start pipe for its connection point (Direction it is facing)
        
        Parameters:
            side (str): The side of the pipe being checked. Sets to None is nothing is specified

        Returns:
            (list): A list containing the direction that the start pipe connects to
        """
        connect = []
        con_checks = []
        con_checks = (START[self.get_orientation()])
        if side == con_checks or side == None:
            connect.append(con_checks)
        return connect


class EndPipe(SpecialPipe):
    """
    Class for end pipe, child of Special Pipe.
    Is used for the ending pipe in the game board.
    """
    def __init__(self, orientation=0):
        """
        Initialises the EndPipe Class

        Sets name, orientation and selectable status (which is always False)
        
        Parameters:
            orientation (int): Sets the orientation of the pipe object. If no input, defaults to 0
        """
        super().__init__('end', orientation, False)

    def get_connected(self, side=None):
        """
        Checks the end pipe for its connection point (Opposite to direction it is facing)
        
        Parameters:
            side (str): The side of the pipe being checked. Sets to None is nothing is specified

        Returns:
            (list): A list containing the direction that the end pipe connects to
        """
        connect = []
        con_checks = []
        con_checks = (END[self.get_orientation()])
        if side == con_checks or side == None:
            connect.append(con_checks)
        return connect


class PipeGame:
    """
    A game of Pipes.
    """
    def __init__(self, game_file='game_1.csv'):
        """
        Construct a game of Pipes from a file name.

        Parameters:
            game_file (str): name of the game file.
        """

        #########################COMMENT THIS SECTION OUT WHEN DOING load_file#######################
        #board_layout = [[Tile('tile', True), Tile('tile', True), Tile('tile', True), Tile('tile', True), \
        #Tile('tile', True), Tile('tile', True)], [StartPipe(1), Tile('tile', True), Tile('tile', True), \
        #Tile('tile', True), Tile('tile', True), Tile('tile', True)], [Tile('tile', True), Tile('tile', True), \
        #Tile('tile', True), Pipe('junction-t', 0, False), Tile('tile', True), Tile('tile', True)], [Tile('tile', True), \
        #Tile('tile', True), Tile('tile', True), Tile('tile', True), Tile('locked', False), Tile('tile', True)], \
        #[Tile('tile', True), Tile('tile', True), Tile('tile', True), Tile('tile', True), EndPipe(3), \
        #Tile('tile', True)], [Tile('tile', True), Tile('tile', True), Tile('tile', True), Tile('tile', True), \
        #Tile('tile', True), Tile('tile', True)]]

        #playable_pipes = {'straight': 1, 'corner': 1, 'cross': 1, 'junction-t': 1, 'diagonals': 1, 'over-under': 1}    
        #########################COMMENT THIS SECTION OUT WHEN DOING load_file#######################

        ### add code here ###

        board_layout, playable_pipes = self.load_file(game_file)
        self._board_layout = board_layout
        self._playable_pipes = playable_pipes
        self.end_pipe_positions()
        
    def get_board_layout(self):
        """
        Returns the layout of the board

        Returns:
            (list): A List of lists that represents the board layout of the game
        """
        return self._board_layout

    def get_playable_pipes(self):
        """
        Returns the playable amount of each pipe type

        Returns:
            (dictionary): Dictionary where the string is the type of pipe and links to the int of playable amount
        """
        return self._playable_pipes

    def change_playable_amount(self, pipe_name, number):
        """
        Changes the amount of playable pipes there are for a set pipe

        Parameters:
            pipe_name (str): name of the pipe type to change
            number (int): the amount of playable pipes to add
        """
        for x in self._playable_pipes:
            if x == pipe_name:
                self._playable_pipes[x] += number

    def set_pipe(self, pipe, position):
        """
        Sets the selected pipe at the specified position

        Parameters:
            pipe(Pipe class object): Pipe type to place
            position (tuple <int, int>):  The position to place the pipe
        """
        x,y = position
        self._board_layout[x][y] = pipe
        self.change_playable_amount(pipe.get_name(), -1)

    def remove_pipe(self, position):
        """
        Removes the selected pipe from the specified position

        Parameters:
            pipe(Pipe class object): Pipe type to remove
            position (tuple <int, int>):  The position to remove the pipe
        """
        x,y = position
        pipe = self._board_layout[x][y]
        self.change_playable_amount(pipe.get_name(), 1)
        self._board_layout[x][y] = Tile('tile', True)

        
    def pipe_in_position(self, position):
        """
        Checks what pipe is in the specified position

        Parameters:
            position (tuple <int, int>):  The position of the selected pipe

        Returns:
            (str): Name of the pipe type at the position
                or
            None(bool): If there is no pipe at the position or the position is invalid
        """
        try:
            #Tries to return the specified pipe
            x,y = position
            check = self._board_layout[x][y]
            if check.get_id() != 'tile':
                return self._board_layout[x][y]
        except Exception:
            return None

    def position_in_direction(self, direction, position):
        """
        Checks in given direction for a valid tile. Returns either the tile and direction or None if invalid.

        Parameters:
            direction (str): The direction to check
            position (tuple <int, int>):  The position of the current tile

        Returns:
            (tuple< str,tuple <int, int>>): The valid direction and the x,y position
                or
            None(bool): If there is no tile in the direction or the position is invalid
        """
        x,y = position
        for n in DIRECTION:
            #Associated information is within the DIRECTION dictionary
            #This dictionary has the x and y position changes and the corresponding direction
            if n == direction:
                x += DIRECTION[n][1]
                y += DIRECTION[n][2]
                pos = (x, y)
                if x >= 0 and y >= 0:
                    return DIRECTION[n][0], pos
        return None
        

    def end_pipe_positions(self):
        """
        Determines and sets the locations of both the start and end pipe
        """
        for i, c in enumerate (self._board_layout):
            for x,y in enumerate (self._board_layout[i]):
                #Checks what type(class) the pipe is (start or end)
                if type(y) == StartPipe:
                    self._start = i, x
                elif type(y) == EndPipe:
                    self._end = i, x

    def get_starting_position(self):
        """
        Gets the position of the start pipe

        Returns:
            (tuple <int, int>): A tuple representing the x and y position of the start pipe
        """
        return self._start
        
    def get_ending_position(self):
        """
        Gets the position of the end pipe

        Returns:
            (tuple <int, int>): A tuple representing the x and y position of the end pipe
        """
        return self._end

    def get_pipe(self, position):
        """
        Checks the given position for a tile or pipe

        Parameters:
            position (tuple <int, int>):  The position to check for a pipe/tile

        Returns:
            (Pipe or Tile class object): The instance of the tile or pipe at the selected board position
        """
        x,y = position
        return self._board_layout[x][y]

    def load_file(self, game_file):
        """
        Loads an external csv file and obtains the game board layout and playable pipe amounts.

        Is called in the initialisation of the pipe game.

        Parameters:
            game_file (str):  The imported file set in .csv format that contains the layout and playable pipe.

        Returns:
            (list): A List of lists that represents the board layout of the game imported from the file
            (dictionary): Dictionary where the string is the type of pipe and links to the int of playable amount

        """
        board_layout = []
        playable_pipes = {}
        board_layout_hold = []
        with open(game_file) as info:
            for line in info:
                words = line.strip()
                for i,c in enumerate(line):
                    #Strips line and removes commas
                    if c == ",":
                        line = line.replace(c,'')
                        break
                for pos, words in enumerate(line):
                    if words == "#":
                        # The '#' is the symbol for the empty tile, so a tile object is added to the board
                        board_layout_hold.append(Tile('tile', True))
                    elif words in SPECIAL_TILES:
                        #Checks if the word is part of the special tiles class (Locked, start or end)
                        if words == 'L':
                            board_layout_hold.append(Tile('locked', False))
                        else:
                            if words == 'S':
                                spec = StartPipe
                            elif words == 'E':
                                spec = EndPipe
                            try:
                                #The try is used incase the start/end pipe is given an orientation
                                #As such, it checks if the next word along the line is a number
                                #If so, it uses that number as the orientation when adding the pipe
                                orientation = int(line[pos+1])
                                board_layout_hold.append(spec(orientation))
                            except Exception:
                                board_layout_hold.append(spec(0))
                    elif words.isalpha():
                        #Checks if the input is a letter that isn't in the Special Tiles category
                        #This means that the tile should be a locked pipe
                        try:
                            #All the pipes have two-letter codes in the 'PIPES' dictionary
                            #This will try and grab both the current and next letter
                            #If successful, will add the pipe to the layout
                            words += line[pos+1]
                            for m in PIPES:
                                orientation = 0
                                if m == words:
                                    if line[pos+2].isdigit == True:
                                        #Checks if the pipe has a given orientation to include
                                        orientation = line[pos+2]
                                    board_layout_hold.append(Pipe (PIPES[m], orientation, False))
                        except Exception:
                            pass
                    else:
                        #If none of the other statements are fulfiled
                        #Then the input is most likely for the amount of playable tiles
                        try:
                            #This try statement and the inclosed code goes through the whole line
                            #Adding each succesive input to the playable_pipes dictionary as an int
                            #If it can't go through the whole line and complete the dictionary, it passes
                            num = 0
                            for pipe in PIPES:
                                playable_pipes[PIPES[pipe]] = int(line[num])
                                num+=1
                        except Exception:
                            pass
                #Adds and clears the temporary 'board_layout_hold' to the board_layout proper
                #Checks if the board_layout_hold is empty. If so, doesn't add it.
                if board_layout_hold != []:
                    board_layout.append(board_layout_hold)
                board_layout_hold = []
        return(board_layout, playable_pipes)


    def check_win(self):
        """
        (bool) Returns True  if the player has won the game False otherwise.
        """
        position = self.get_starting_position()
        pipe = self.pipe_in_position(position)
        queue = [(pipe, None, position)]
        discovered = [(pipe, None)]
        while queue:
            pipe, direction, position = queue.pop()
            for direction in pipe.get_connected(direction):
                if self.position_in_direction(direction, position) is None:
                    new_direction = None 
                    new_position = None
                else:
                    new_direction, new_position = self.position_in_direction(direction, position)
                if new_position == self.get_ending_position() and direction == self.pipe_in_position(
                        new_position).get_connected()[0]:
                    return True

                pipe = self.pipe_in_position(new_position)
                if pipe is None or (pipe, new_direction) in discovered:
                    continue
                discovered.append((pipe, new_direction))
                queue.append((pipe, new_direction, new_position))
        return False    
        
def main():
    print("Please run gui.py instead")


if __name__ == "__main__":
    main()
