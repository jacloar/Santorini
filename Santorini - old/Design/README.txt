Santorini Board:
The Santorini Board class will have the following instance variables:
	- 2D array of Buildings
	- List of workers

Board will have the following methods with the given inputs/outputs:
	- createGrid
		o input: an integer representing the x/y dimensions of the new board (should be positive)
		o output: a 2D array of Buildings (see instance variable above)
		o This method simply creates a board of the given size.
	- workerMove
		o input: a worker who is to make a move, a delta x (integer), and a delta y (integer)
		o moves the worker to a neighboring building
	- workerBuild
		o input: Worker to conduct the building, a delta x (integer), and a delta y (integer)
		o allows the given worker to add height to a neighboring building
	- placeWorker
		o input: Worker to be placed, an x location on the board (integer), a y location on the board (integer)
		o adds a Worker to the board at the specified location
	- getGrid
		o output: returns the 2D array of Buildings representing the current state of the board
		o allows clients to see the current state of the board
	- getWorkers
		o output: returns a list of the current Workers
		o allows a client of the board to see where the current workers are.

The worker class will have 2 fields, int x and int y to represent the location on the board.
The building class will have 1 field, int height.