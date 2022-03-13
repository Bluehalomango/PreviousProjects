import tkinter as tk
from tkinter import messagebox
from PIL import Image, ImageTk
import PIL.Image
import random
from random import randint
from tkinter import *
from tkinter import filedialog

TASK_ONE = False
TASK_TWO = True


class BoardModel (object):

    """
    Class for BoardModel object. Used to handle the model side of the game.
    """

    def __init__ (self, grid_size, num_pokemon):
        """
        Initialises the BoardModel Class

        Sets various variables and starts important functions.

        These function create the board, sets the directions dictionary and generate pokemon.
        
        Parameters:
            grid_size (int): The grid size of the game.
            num_pokemon (int): How many pokemon should be generated.
        """
        self._grid_size = grid_size
        self._num_pokemon = num_pokemon
        self._board = self.generate_board()
        self._con = False
        self._catches = 0
        self.set_directs()
        self.generate_pokemon()

    def generate_board(self):
        """
        Generates the game board.

        Returns:
            (str) : The game string is returned and set as variable.
        """
        board = ""
        for i in range(self._grid_size * self._grid_size ):
            board += '~'
        return board

    def get_game(self):
        """
        Returns Game string

        Returns:
            (str) : String of Game
        """
        return self._board

    def get_pokemon_locations(self):
        """
        Returns pokemon locatins

        Returns:
            (list) : list of pokemon locations
        """
        return self._pokeloct

    def get_num_attempted_catches(self):
        """
        Returns number of attempted pokemon catches

        Returns:
            (int) : Number of attempted catches
        """
        return self._catches

    def get_num_pokemons(self):
        """
        Returns number of pokemon 

        Returns:
            (int) : Number of pokemon
        """
        return self._num_pokemon

    def check_loss(self):
        """
        Returns the self._con variable, which determines if a game is lost.

        Returns:
            (boolean) : True or false depending on game loss state
        """
        return self._con

    def board_char (self, index):
        """
        Returns the character of the game string at the specified index
        
        Parameters:
            index (int): Currently selected index in game string

        Returns:
            (str) : Character from game string
        """
        return self._board[index]

    def set_game(self, number, loct, board):
        """            
        Sets the game from loaded information.
        Strips unnnecessary components.
        Loads the number of pokemon, game board and pokemon locations.
        
        Parameters:
            number (str): The loaded number of pokemon
            loct (str): The loaded locations of pokemon
            board (str): The loaded game board
        """
        self._num_pokemon = int(number)
        self._board = board
        loct = loct.strip('(')
        loct = loct.strip(')')
        loct = loct.strip('[')
        loct = loct.strip(']')
        self._pokeloct = ()
        self._pokeloct = loct.split(', ')
        #Splits the loaded information into segments that are in a readable format
        for i,c in enumerate(self._pokeloct):
            self._pokeloct[i] = int(c)
            
 
    def set_directs(self):
        """
        Sets the dictionary for directions
        """
        UP = "up"
        DOWN = "down"
        LEFT = "left"
        RIGHT = "right"
        self._DIRECTIONS = (UP, DOWN, LEFT, RIGHT,
              f"{UP}-{LEFT}", f"{UP}-{RIGHT}",
              f"{DOWN}-{LEFT}", f"{DOWN}-{RIGHT}")

    def check_catches (self):
        """
        A check that verifies the amount of pokemon catches when loading file

        Returns:
            (int) : Number of attempted catches
        """
        self._catches = 0
        for i in self.get_game():
            if i == 'F':
                self._catches += 1
        return self._catches

    def index_to_position(self, index):
        """
        Converts the index input into a x, y position
        
        Parameters:
            index (int): Currently selected index in game string
            
        Returns:
            (tuple) : The x and y position of the index
        """
        for i in range (0, self._grid_size*self._grid_size, self._grid_size):
            if index >= i and index < i + self._grid_size:
                y = i / self._grid_size
                for n in range (self._grid_size):
                    if index-i == n:
                        x = n
        pos = (y, x)
        return (pos)

    def generate_pokemon(self):
        """Pokemons will be generated and given a random index within the game.
        Sets the pokemon locations into a variable.
        """
        cell_count = self._grid_size ** 2
        pokemon_locations = ()

        for _ in range(self._num_pokemon):
            if len(pokemon_locations) >= cell_count:
                break
            index = random.randint(0, cell_count-1)

            while index in pokemon_locations:
                index = random.randint(0, cell_count-1)

            pokemon_locations += (index,)
        self._pokeloct = pokemon_locations


    def position_to_index(self, position):
        """Convert the row, column coordinate in the grid to the game strings index.

        Parameters:
            position (tuple<int, int>): The row, column position of a cell.

        Returns:
            (int): The index of the cell in the game string.
        """
        if position == None:
            return None
        x, y = position
        return (int(x * self._grid_size + y))


    def index_in_direction(self, index, DIRECTIONS):
        """
        This functions is used to find adjacent cells.
        It takes the current index and the direction and checks whether
        the cell in the set direction is adjacent to the current index.
        
        Parameters:
            index (int): The index of the selected cell in reference to the game string.
            direction (str): The direction to be checked for adjacency.
            
        Returns:
            None: if the new index is not adjacent to the current index
            (tuple): The x and y position of the adjacent cell
        """
        x, y= self.index_to_position(index)
        if DIRECTIONS == 'up':
            if y - 1 >= 0:
                return (x, y -1)
            else:
                return None
        if DIRECTIONS == 'down':
            if y + 1 < self._grid_size:
                return (x, y + 1)
            else:
                return None
        if DIRECTIONS == 'left':
            if x - 1 >= 0:
                return (x-1, y)
            else:
                return None
        if DIRECTIONS == 'right':
            if x + 1 < self._grid_size:
                return (x+1,y)
            else:
                return None
        if DIRECTIONS == 'up-left':
            if y - 1 >= 0 and x - 1 >= 0:
                return (x-1,y-1)
            else:
                return None
        if DIRECTIONS == 'up-right':
            if y-1 >= 0 and x + 1 < self._grid_size:
                return (x+1, y-1)
            else:
                return None
        if DIRECTIONS == 'down-left':
            if y + 1 < self._grid_size and x - 1 >= 0:
                return (x-1, y+1)
            else:
                return None
        if DIRECTIONS == 'down-right':
            if y + 1 < self._grid_size and x + 1 < self._grid_size:
                return (x+1, y+1)
            else:
                return None

    def neighbour_directions(self, index):
        """
        Runs through a loop that checks each direction for which cells should be revealed.
        
        Parameters:
            index (int): Index of the currently selected cell

        Returns:
            (list<int>): List of the neighbouring directions that contain a cell that should be revealed.
        """
    
        newindex = 0
        indexlist = []
        for i in range(0,8):
                #Runs through the directions list that contains each of the eight directions.
            newindex = self.position_to_index (self.index_in_direction(index , self._DIRECTIONS[i]))
            if newindex != None:
                indexlist += (newindex, )
        return(indexlist)

    def number_at_cell(self, index):
        """
        Takes the index of a cell and finds all neighbouring cells that contain a pokemon.

        Parameters:
            index (int): Index of the currently selected cell

        Returns:
            (int): How many pokemon are adjacent to the currently selected cell (index).
        """
        indexlist = self.neighbour_directions(index)
        pokemons = 0
        for i, c in enumerate (indexlist):
            for n, m in enumerate (self.get_pokemon_locations()):
                if c == m:
                    #If the neighbouring cell and the pokemon location match,
                    #then there is a pokemon in that position.
                    pokemons +=1
        return pokemons

    def big_fun_search(self, index):
        """
        Searching adjacent cells to see if there are any Pokemon"s present.

        Find all cells which should be revealed when a cell is selected.

        For cells which have a zero value (i.e. no neighbouring pokemons) all the cell"s
        neighbours are revealed. If one of the neighbouring cells is also zero then
        all of that cell"s neighbours are also revealed. This repeats until no
        zero value neighbours exist.

        For cells which have a non-zero value (i.e. cells with neightbour pokemons), only
        the cell itself is revealed.

        Parameters:
            index (int): Index of the currently selected cell

        Returns:
            (list<int>): List of cells to turn visible.
        """
    
        queue = [index]
        discovered = [index]
        visible = []
        if self.board_char(index) == 'F':
            return queue

        number = self.number_at_cell(index)
        if number != 0:
            return queue

        while queue:
            node = queue.pop()
            for neighbour in self.neighbour_directions(node):
                if neighbour in discovered:
                    continue

                discovered.append(neighbour)
                if self.board_char(neighbour) != 'F':
                    number = self.number_at_cell(neighbour)
                    if number == 0:
                        queue.append(neighbour)
                    visible.append(neighbour)
        return visible

    def mod_board (self, index):
        """
        Oversees the modification of the game board and the actions with the board.
        Runs several other functions that together compose a player's search action.
        Reveals cells specified from the search functions.
        
        Parameters:
            index (int): Currently selected index in game string

        """
        cont = True
        #Only changes board if square isn't flagged
        if self.board_char(index) != 'F':
            for i in self.get_pokemon_locations():
                if index == i:
                    self._con = True
                    cont = False
                    #Stops the program here if the selected cell contains a pokemon
            if cont:
                visible = self.big_fun_search(index)
                char = self.number_at_cell(index)
                self._board = self.get_game()[:index] + str(char) + self.get_game()[index + 1:]
                for i in visible:
                    char = self.number_at_cell(i)
                    self._board = self.get_game()[:i] + str(char) + self.get_game()[i + 1:]
                    
    def flag (self, index):
        """
        Function to flag or unflag cells. Changes the self._board game string in function.
        Updates the attempted catches accordingly.

        Parameters:
            index (int): Index of the currently selected cell

        """
        if self.get_game()[index] == "F":
            self._board = self.get_game()[:index] + "~" + self.get_game()[index + 1:]
            self._catches -= 1
        else:
            if self.get_num_attempted_catches() == self.get_num_pokemons():
                #can't flag more cells than there are pokemon
                return None
            else:
                self._board = self.get_game()[:index] + "F" + self.get_game()[index + 1:]
                self._catches += 1

    def reveal (self):
        """
        Is called when the player reveals a pokemon. Goes through and sets the
        game string to include pokemon locations
        """
        for i in self.get_pokemon_locations():
            self._board = self.get_game()[:i] + "P" + self.get_game()[i + 1:]

    def check_win (self):
        """
        Function to check if the player has won the game.
        Goes through various checks to verify the win condition.
        If none return false, then the player has won the game and the function returns True.

        Returns:
            True (bool): If the win condition is valid.
            False (bool): If the win condition is not valid.
        """
        flagged = []
        correct = 0
        for i, c in enumerate(self.get_game()):
            if c == '~':
                return False
            elif c == 'F':
                flagged.append (i)
                #Adds flagged locations to a list
        if self.get_num_attempted_catches() == self.get_num_pokemons():
            for i in self.get_pokemon_locations():
                #Checks pokemon locations against list of flagged locations
                for n in flagged:
                    if i == n:
                        correct += 1
            if correct == self.get_num_pokemons():
                return True

    def new_game (self):
        """
        Resests the variables and starts the functions necessary for a new game
        This includes generating new pokemon locations
        """
        self._board = self.generate_board()
        self._con = False
        self._catches = 0
        self.generate_pokemon()

    def restart (self):
        """
        Resests the variables and starts the functions necessary for a restarted game
        The pokemon locations stay the same in this situation
        """
        self._board = self.generate_board()
        self._con = False
        self._catches = 0

    
class PokemonGame(object):
    """
    Class for PokemonGame object. Used to handle the control side of the game.
    """

    def __init__ (self, master, grid_size=10, num_pokemon=15, task=TASK_TWO):
        
        """
        Initialises the PokemonGame Class

        Calls the various other classes and functions to begin the game.
        It binds button 1, 2 and 3 to their appropriate actions.

        It also checks which task the game is initialised with.
        If it is task one, it sets the view as the normal BoardView,
        If it is task two, it sets the view as ImageBoardView, creates the status bar
        and adds the filemenu, including the relevant interactions.
        
        Parameters:
            master (tk.Widget): Widget within which to place the game
            grid_size (int): The grid size of the game.
            num_pokemon (int): How many pokemon should be generated.
            task (boolean): Represents task. If true, starts task two. If false, task one.
        """
        self._master = master
        self._grid_size = grid_size
        self._num_pokemon = num_pokemon
        self._model = BoardModel(self._grid_size, self._num_pokemon)
        self._task = task
        if task == True:
            self._view = ImageBoardView(master,  self._grid_size)
            self._status = StatusBar (master, self._model, control = self, bg='white')
            menubar = tk.Menu(self._master)
            self._master.config(menu=menubar)
            filemenu = tk.Menu(menubar, tearoff=False)
            menubar.add_cascade(label="File", menu=filemenu)
            filemenu.add_command(label="New Game", command=self.new_game)
            filemenu.add_command(label="Load Game", command=self.load_file)
            filemenu.add_command(label="Save Game", command=self.save_file)
            filemenu.add_command(label="Restart Game", command=self.restart)
            filemenu.add_command(label="Quit", command=self.quit)
            self._filename = None
        else:
            self._view = BoardView(master, self._grid_size)
        self._view.draw_board(self._model.get_game())
        self._view.bind("<Button-1>", self.press1)
        self._view.bind("<Button-2>", self.press2)
        self._view.bind("<Button-3>", self.press2)
        

    def press1 (self, e):
        """
        Handles the search action.
        Trys to convert the given x and y values to a position on the board.
        If successful, runs the mod_board function in the model to handle the search.
        Also runs check to determine win/loss state after search

        Parameters:
            e (event): The mouse click event information

        """
        #Try is used as one method of preventing off-board clicks
        try:
            pos = (self._view.pixel_to_position((e.x,e.y)))
            if pos is None:
                pass
            else:
                self._model.mod_board(pos)
                self.check()
        except:
            pass
         
    def press2 (self, e):
        """
        Handles the flag action.
        Trys to convert the given x and y values to a position on the board.
        If successful, runs the flag function in the model to handle the flagging.
        Also runs check to determine win/loss state after search and updates statusbar visuals

        Parameters:
            e (event): The mouse click event information

        """
        #Try is used as one method of preventing off-board clicks
        try:
            self._model.flag((self._view.pixel_to_position((e.x,e.y))))
            if self._task == True:
                self._status.draw_images()
            self.check()
        except:
            pass

    def check (self):
        """
        Check that runs different functions depending on current win/loss state.
        """
        if self._model.check_loss() == True:
            self.loss()
        if self._model.check_win() == True:
            self.win()
        else:
            self._view.draw_board(self._model.get_game())
            
    def loss (self):
        """
        Handles and calls the necessary functions for when a game is lost.
        Reveals the board and, if in task 2, stops the time and displays a popup.
        This either starts a new game or closes it.

        If just in task 1, runs the end game function in BoardView that displays a lossing message.
        
        """
        self._model.reveal()
        self._view.draw_board(self._model.get_game())
        if self._task == True:
            self._status.stop_time()
            self._view.end(True)
            ans = messagebox.askokcancel('Game Over', 'You lost. Want to try again?')
            if ans:
                self.new_game()
            else:
                self._master.destroy()
        else:
            self._view.end(False)
            
    def win (self):
        """
        Handles and calls the necessary functions for when a game is won.
        If in task 2, stops the time and displays a popup that either starts a new game or closes it.

        If just in task 1, runs the end game function in BoardView that displays a winning message.
        
        """
        self._view.draw_board(self._model.get_game())
        if self._task == True:
            self._status.stop_time()
            self._view.end(True)
            ans = messagebox.askokcancel('Game Over', 'You Won! Want to play again?')
            if ans:
                self.new_game()
            else:
                self._master.destroy()
        else:
            self._view.end(True)
        
    def new_game(self):
        """
        Cleans the file name and starts functions for when a new game is started.
        Changes pokemon locations on new game.
        """
        self._view.end(False)
        self._filename = None
        self._model.new_game()
        self._view.draw_board(self._model.get_game())
        self._status.reset()
        
    def restart(self):
        """
        Starts functions for when a game is restarted.
        Keeps same pokemon locations.
        """
        self._model.restart()
        self._view.draw_board(self._model.get_game())
        self._status.reset()
       
    def quit(self):
        """
        Displays a message asking if the user wants to quit.
        If yes, the app is closed.
        Otherwise, nothing happens.
        """
        ans = messagebox.askokcancel('Quit', 'Are you sure you want to quit?')
        if ans:
            self._master.destroy()

    def save_file(self):
        """
        Handles saving the current file.
        If no filename is set, asks for a new filename.

        Saves the necessary information into the file.
        This includes the number and location of pokemon, the game string and the timer.
        """
        if self._filename is None:
            filename = filedialog.asksaveasfilename()
            if filename:
                self._filename = filename
        if self._filename:
            fd = open(self._filename, 'w')
            #Puts information into special format for when the file is loaded
            info = [str(self._model.get_num_pokemons()), "#",
                    str(self._model.get_pokemon_locations()), "$",
                    self._model.get_game(), str(self._status.get_info())]
            fd.writelines(info)
            fd.close()
            
    def load_file(self):
        """
        Handles loading a specified file.
        Asks for a file to open.

        Attempts to open and read the file information.

        If successful, splits the file into number of pokemon, locations of
        pokemon, game string and timer information, running the appropriate
        functions with this information.

        Also checks if the loaded file game string has the same grid size as the
        current file. If not, stops the load action and displays appropriate message.

        If this process is unsuccessful, displays an error message.
        """
        filename = filedialog.askopenfilename()
        if filename:
            try:
                self._filename = filename
                fd = open(filename, 'r')
                f= fd.read()
                loop = True
                #Loop used to decode the format of the saved file.
                #Special symbols are used as breaks between classes of information.
                #f1 is number of pokemon, f2 is pokemon locations, f3 is board model and f4 is timer
                for i, c in enumerate(f):
                    if c == "#":
                        for m, n in enumerate(f):
                            if n == "$":
                                for x, y in enumerate(f):
                                    if y == "|":
                                        f1 = f[:i]
                                        f2 = f[i + 1:m]
                                        f3 = f[m + 1:x]
                                        f4 = f[x + 1:]
                                        break
                if len(f3) > self._grid_size *self._grid_size:
                    messagebox.showerror('Error',
                                         'The load file has a grid size different to your current game.')
                    self._filename = None
                else:
                    self._model.set_game(f1.strip(), f2.strip(), f3.strip())
                    self._model.check_catches()
                    self._view.draw_board(self._model.get_game())
                    self._status.reset()
                    self._status.load_time(f4)
                    self._status.draw_images()
            except:
                messagebox.showerror('Error 1', 'The load file format is invalid.')
            fd.close()


class BoardView (tk.Canvas):
    """
    Class for Boardview canvas. Used to handle the view side of the game.
    """

    def __init__ (self, master, grid_size, board_width=600, *args, **kwargs):
        """
        Initialises the BoardView Class

        Sets the title and size of the window. If the board size is below a set
        point, it defaults it to a large size to allow for the red pokemon title banner.

        Packs itself and sets important variables
        
        Parameters:
            master (tk.Widget): Widget within which to place the Boardview
            grid_size (int): The grid size of the game.
            board_width(int): Size of the board
        """
        super().__init__(master, bg='white')
        self._master = master
        self._master.title("Pokemon: Got 2 Find Them All!")
        self._board_width = board_width
        #Allows full length of title banner, as shown in assignment brief
        if board_width < 600:
            self._master.geometry(("600x"+str(board_width + 40)))
        else:
            self._master.geometry((str(board_width)+"x"+str(int(board_width+ 40))))
        self.pack(expand=1, fill=tk.BOTH)
        self._grid_size = grid_size
        self._tile_size = self._board_width/self._grid_size

    def draw_board(self, board):
        """
        Takes the game string and displays the visuals for the game.
        Creates the different coloured rectangles for the tiles,
        adding text and changing colour when necessary.
        
        Parameters:
            board (str): The game string for the board
        """
        self.ids = []
        self._board = board
        self.delete(tk.ALL)
        #If under certain size, can't use board width to determine locations
        if self._board_width < 600:
            width = 600
            text_loct=300
        else:
            width = self._board_width
            text_loct=self._board_width/2
        self.create_rectangle(0, 0, width, 40, outline ='red', fill='red')
        self.create_text(text_loct, 20 ,fill="white",font="Times 20 italic bold",
                         text="Pokemon: Got 2 Find Them All!")
        row = 0
        col = 0
        #Goes through list that converts row and column to pixel and bbox to place tiles
        for i, c in enumerate (self._board):
            pix = self.position_to_pixel((col, row))
            box = self.get_bbox(pix)
            if c == '~':
                self.ids.append(self.create_rectangle(box, outline ='black', fill='darkgreen'))
            elif c == 'F':
                self.ids.append(self.create_rectangle(box, outline ='black', fill='red'))
            elif c == 'P':
                self.ids.append(self.create_rectangle(box, outline ='black', fill='yellow'))
            else:
                self.ids.append(self.create_rectangle(box, outline ='black', fill='lightgreen'))
                self.ids.append(self.create_text(self.text_pos(pix) ,fill="black",
                                                 font="Times 12 italic bold", text=c))
            row += 1
            if row == self._grid_size:
                row = 0
                col += 1   

    def get_bbox(self, pixel):
        """
        Takes in a pixel x and y position and returns the bounding box for that location.
        This is the four corners and is determined by the tile size (board width/grid size)

        Parameters:
            pixel (tuple): A tuple containg the x and y pixel position on the board

        Returns:
            (tuple): Tuple containg the corners of the box
        """
        x1 = pixel[0] - (self._tile_size/2) 
        y1 = pixel[1] - (self._tile_size/2)+ 40
        x2 = pixel[0] + (self._tile_size/2)
        y2 = pixel[1] + (self._tile_size/2)+ 40
        bound = (x1, y1, x2, y2)
        return bound

    def text_pos(self,pixel):
        """
        Takes in a pixel position and returns the relevenat text location for that position.

        Parameters:
            pixel (tuple): A tuple containg the x and y pixel position on the board

        Returns:
            (tuple): Tuple containg x and y coordinate of the text
        """
        x, y = pixel
        y1 = y - (self._tile_size/2)+ 40
        y2 = y + (self._tile_size/2)+ 40
        return (x, ((y1 + y2)/2))

    def position_to_pixel(self, position):
        """
        Takes in a x and y grid position and converts it into a x and y pixel position.

        Parameters:
            position (tuple): A tuple containg the x and y grid positions

        Returns:
            (tuple): A tuple containg the x and y pixel position on the board
        """
        y = position[0] + 1
        x = position[1] + 1
        xpix = (x*self._tile_size) - self._tile_size/2
        ypix = (y*self._tile_size) - self._tile_size/2
        pix = (xpix, ypix)
        return pix

    def pixel_to_position(self, pixel):
        """
        Takes in a x and y pixel position and converts it into a x and y grid position.

        Parameters:
            pixel (tuple): A tuple containg the x and y pixel position on the board
            
        Returns:
            (tuple): A tuple containg the x and y grid positions
        """
        x = pixel[0] 
        y = pixel[1] - 40
        pos = (x//self._tile_size) + ((y//self._tile_size) * self._grid_size)
        if x >= self._board_width or y >= self._board_width:
            return None
        else:
            return (int(pos))

    def end (self, win):
        """
        Takes in the win variable that determines end message.
        If win is true, displays the winning message.
        Otherwise the game is lost and the lossing message is displayed.

        Parameters:
            win (boolean): True/false depending on win/loss state
        """
        if win == True:
            messagebox.showinfo("Game Over", "You won! :D")
        else:
            messagebox.showinfo("Game Over", "You lost! :(")



class ImageBoardView (BoardView):
    """
    Class for ImageBoardview, child of BoardView. Handles the view side of the game in task 2.
    """
    
    def __init__ (self, master, grid_size, board_width=600, *args, **kwargs):
        """
        Initialises the ImageBoardView Class for task 2

        Sets the title and size of the window. If the board size is below a set point, it
        defaults it to a large size to allow for the red pokemon title banner and status bar.

        Packs itself and sets important variables
        
        Parameters:
            master (tk.Widget): Widget within which to place the Boardview
            grid_size (int): The grid size of the game.
            board_width(int): Size of the board
        """
        super().__init__(master, grid_size, board_width, bg='white')
        self._master = master
        self._con = False
        self._tile_size = self._board_width//self._grid_size
        #Discrepancies were found if board size was below a certain level
        #This accounts for that by adjusting the board size
        if board_width < 230:
            self._master.geometry(("600x"+str(board_width + 120)))
        #Allows full length of title banner, as shown in assignment brief
        elif board_width < 600:
            self._master.geometry(("600x"+str(board_width + 100)))
        else:
            self._master.geometry((str(board_width)+"x"+str(int(board_width+ 100))))
        self.pack(expand=1,side=tk.TOP)
        self._grid_size = grid_size
        self.set_images()

        
    def set_images (self):
        """
        Sets the image files into two dictionaries, one for revealed tiles and one for pokemon.
        """
        self._tall_grass_img = get_image("images/unrevealed", self._tile_size)
        self._pokeball_img = get_image("images/pokeball", self._tile_size)
        self._revealed = {
            0 : get_image("images/zero_adjacent", self._tile_size),
            1 : get_image("images/one_adjacent", self._tile_size),
            2 : get_image("images/two_adjacent", self._tile_size),
            3 : get_image("images/three_adjacent", self._tile_size),
            4 : get_image("images/four_adjacent", self._tile_size),
            5 : get_image("images/five_adjacent", self._tile_size),
            6 : get_image("images/six_adjacent", self._tile_size),
            7 : get_image("images/seven_adjacent", self._tile_size),
            8 : get_image("images/eight_adjacent", self._tile_size)

        }
        self._pokemon_imgs = {
            0 : get_image("images/pokemon_sprites/charizard", self._tile_size),
            1 : get_image("images/pokemon_sprites/cyndaquil", self._tile_size),
            2 : get_image("images/pokemon_sprites/pikachu", self._tile_size),
            3 : get_image("images/pokemon_sprites/psyduck", self._tile_size),
            4 : get_image("images/pokemon_sprites/togepi", self._tile_size),
            5 : get_image("images/pokemon_sprites/umbreon", self._tile_size)
        }
        

    def draw_board(self, board):
        """
        Takes the game string and displays the visuals for the game.
        Places the images on the board with the bbox function.
        Randomly chooses pokemon images when the board is revealed.
        Only runs if the game is still playing (self._con == False)
        
        Parameters:
            board (str): The game string for the board
        """
        if self._con == False:
            self._board = board
            self.delete(tk.ALL)
            #If under certain size, can't use board width to determine locations
            if self._board_width < 600:
                width = 600
                text_loct=300
            else:
                width = self._board_width
                text_loct=self._board_width/2
            self.create_rectangle(0, 0, width, 40, outline ='red', fill='red')
            self.create_text(text_loct, 20 ,fill="white",font="Times 20 italic bold",
                             text="Pokemon: Got 2 Find Them All!")
            row = 0
            col = 0
            #Goes through list that converts row and column to pixel and bbox to place images
            for i, c in enumerate (self._board):
                pix = self.position_to_pixel((col, row))
                box = self.get_bbox(pix)
                if c == '~':
                    self.draw_img(box, self._tall_grass_img)
                elif c == 'F':
                    self.draw_img(box, self._pokeball_img)
                elif c == 'P':
                    #Randint is used to randomly select the pokemon images
                    value = randint(0, 5)
                    self.draw_img(box, self._pokemon_imgs[value])
                else:
                    for p in self._revealed:
                        try:
                            if p == int(c):
                                self.draw_img(box, self._revealed[p])
                        except:
                            pass
                row += 1
                if row == self._grid_size:
                    row = 0
                    col += 1 


    def draw_img (self, box, img):
        """
        Creates the appropriate image for the game board.

        Parameters:
            box (tuple): A tuple containg the x and y position of the image (NW corner)
            img (image): The image that is being created.
        """
        self.create_image(box, anchor=NW, image=img)
            
    def get_bbox(self, pixel):
        """
        Takes in a pixel x and y position and returns the bounding box for that location.
        This is the NW corner and is determined by the tile size (board width/grid size)

        Parameters:
            pixel (tuple): A tuple containg the x and y pixel position on the board

        Returns:
            (tuple): Tuple containg the NW corner of the image
        """
        x1 = pixel[0] - (self._tile_size/2) 
        y1 = pixel[1] - (self._tile_size/2)+40
        bound = (x1, y1)
        return bound
       
    def end (self, loss):
        """
        Sets the game condition variable to the input boolean. Used to stop other functions from running.

        Parameters:
            loss (boolean) :Represents the current loss state
        """
        self._con = loss


class StatusBar (tk.Frame):
    """
    Class for StatusBar frame. Used to handle the status bar UI
    """
    def __init__ (self, master, model, control, bg):
        """
        Initialises the StatusBar Class for task 2

        Creates the status bar frame and the various subframes for the required info.
        Creates the pokemon catches, timer and new/restart game buttons.

        Packs itself and sets important variables
        
        Parameters:
            master (tk.Widget): Widget within which to place the Boardview
            model (Class): The BoardModel class
            control(Class): The PokemonGame class
        """
        super().__init__(master, bg=bg, height =100)
        self._model = model
        self._control = control
        self._master = master
        self.pack(side=tk.BOTTOM,fill=tk.X)
        self._pokeframe = tk.Frame(self, bg= 'white', padx=40 )
        self._pokeframe.pack(side=tk.LEFT)
        self._timeframe = tk.Frame(self, bg= 'white', height = 400 )
        self._timeframe.pack(side=tk.LEFT)
        self._buttonframe = tk.Frame(self, bg= 'white', padx=40 )
        self._buttonframe.pack(side=tk.RIGHT)
        self.set_buttons()
        self.set_images()
        self.set_timer()
        self._end = False
        self._t = 0
        self._m = 0
        self.run_timer()

    def set_images (self):
        """
        Sets the status bar visuals at the start of the game.
        Puts the pokemon catches components into the pokeframe and the timer in the timeframe
        """
        pokeball = get_image_norm("images/full_pokeball")
        self._pokeball = tk.Label(self._pokeframe, image=pokeball, bg='white')
        self._pokeball.pokeball = pokeball
        clock = get_image_norm("images/clock")
        self._clock = tk.Label(self._timeframe, image=clock, bg='white')
        self._clock.clock = clock        
        self._pokeball.pack(side=tk.LEFT)
        self._attempts = tk.Label(self._pokeframe, text = str(self._model.get_num_attempted_catches())
                                  + " attempted catches", bg= 'white' )
        self._catches_left = tk.Label(self._pokeframe, text = str(
            self._model.get_num_pokemons() - self._model.get_num_attempted_catches())
                                      + " pokeballs left", bg= 'white')
        self._attempts.pack()
        self._catches_left.pack()

    def draw_images (self):
        """
        Updates the catches visuals.
        Destroys the old versions and creates new, updated text.
        """
        self._attempts.destroy()
        self._catches_left.destroy()
        self._attempts = tk.Label(self._pokeframe, text = str(self._model.get_num_attempted_catches())
                                  + " attempted catches", bg= 'white')
        self._catches_left = tk.Label(self._pokeframe, text = str(
            self._model.get_num_pokemons() - self._model.get_num_attempted_catches())
                                      + " pokeballs left", bg= 'white')
        self._attempts.pack()
        self._catches_left.pack()

    def set_buttons (self):
        """
        Sets the new game and restart button into the status bar frame and links to commands in PokemonGame
        """
        tk.Button(self, text="New Game", command=self._control.new_game, bg='white').pack(side=tk.TOP)
        tk.Button(self, text="Restart Game", command=self._control.restart, bg='white').pack(side=tk.BOTTOM)

    def set_timer (self):
        """
        Sets the timer visuals.
        Packs the images and text into the timer frame.
        """
        self._clock.pack(side=tk.LEFT)
        self._time_text = tk.Label(self._timeframe, text = "Time elapsed", bg= 'white')
        self._time_text.pack(side=tk.TOP)
        self._time = tk.Label(self._timeframe, text = str(0) + "m " + str (0) + "s", bg= 'white')
        self._time.pack(side=tk.TOP)
      
    def run_timer (self):
        """
        Timer function. If game is running, continues to count and update visuals.
        """
        if self._end == False:
            #Destroys self to refresh info
            self._time.destroy()
            self._time = tk.Label(self._timeframe, text = str(self._m) + "m " +
                                  str (self._t) + "s", bg= 'white')
            self._time.pack(side=tk.TOP)
            self._t += 1
            if self._t == 59:
                #Checks if the seconds are at a minute
                self._t = 1
                self._m+= 1
            self.after(1000, self.run_timer)
            #.after is used to call the function every second
            
    def stop_time (self):
        """
        Variable and function that controls the timer. Called when timer should stop.
        """
        self._end = True


    def reset (self):
        """
        Resets the timer when a new game is start or the game is restarted.
        Updates the visuals at the same time.
        """
        self._t  = 0
        self._m = 0
        if self._end == True:
            #Used in cases where the game was lost before it was restarted
            #Self._end has to be set before run timer is started
            self._end = False
            self.run_timer()
        self._end = False
        #Incase if statement isn't triggered, self._end also has to be set after
        self.draw_images()

    def load_time (self, time):
        """
        Sets the timer after loading new info from a different file.
        
        Parameters:
            time (str) :Info on time state from file
        """
        time= time.strip('|')
        for i, c in enumerate(time):
                if c == "/":
                    self._m = int(time[:i])
                    self._t = int(time[i+1:])
                    break

    def get_info (self):
        """
        Puts the timer variables into an appropriate format for when a file is saved.
        Symbols are used for when the info is deconstructed in the load function.
        
        Returns:
            (str) : Timer information for when a file is saved.
        """
        info = "|" + str(self._m) + "/" + str(self._t)
        return info


def get_image(image_name, tile_size):
    """(tk.PhotoImage) Get a image file based on capability.

    If a .png doesn't work, default to the .gif image.

    Added code to resize the image

    Parameters:
            image_name (str) :Name of image to return
            tile_size (int) : Size to resize to

    Returns:
            (image): The image for the current tile
    """
    try:
        file=image_name + ".png"
        pilImage = PIL.Image.open(file)
        size = (int(tile_size), int(tile_size))
        pilImage = pilImage.resize(size)
        image = ImageTk.PhotoImage(pilImage)
    except tk.TclError:
        file=image_name + ".gif"
        pilImage = PIL.Image.open(file)
        size = (int(tile_size), int(tile_size))
        pilImage = pilImage.resize(size)
        image = ImageTk.PhotoImage(pilImage)
    return image


def get_image_norm(image_name):
    """(tk.PhotoImage) Get a image file based on capability.

    If a .png doesn't work, default to the .gif image.

        Parameters:
            image_name (str) :Name of image to return

    Returns:
            (image): The image for the input image name
    """
    try:
        file=image_name + ".png"
        pilImage = PIL.Image.open(file)
        image = ImageTk.PhotoImage(pilImage)
    except tk.TclError:
        file=image_name + ".gif"
        pilImage = PIL.Image.open(file)
        image = ImageTk.PhotoImage(pilImage)
    return image


def main():
    """
    Function to start the game.
    Sets the root as tk to start game with and gets it to update and loop.
    """
    root = tk.Tk()
    PokemonGame(root)
    root.update()
    root.mainloop()


if __name__ == "__main__":
    main()
