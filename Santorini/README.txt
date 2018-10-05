Files in this directory:

- Design/: Directory for design documents
- Design/plan.pdf: Our initial design plans for the Santorini game
- Design/board.java: Our interface for the Board. Allows players to move, place, and build with workers.

- Common/: Directory for everything that is common knowledge on the board
- Common/IBoard.java: The interface we implement for Board. Enables placing and moving workers and building.
- Common/Board.java: The Board class that implements IBoard
- Common/Worker.java: Represents a worker on the Board
- Common/Building.java: Represents a building on the Board
- Common/IRules.java: Interface to check if proposed moves are valid
- Common/Rules.java: Implementation of IRules. This class enforces valid moves, builds, and placements
- Common/IPlayer.java: Interface for an administrator to take the player through the phases of the game
- Common/Posn.java: Class that represents a Position. A position has a row and a column

- Player/: Directory for everything relating to the player
- Player/IPlayer: Interface for the player class. Allows the player to perform moves.
- Player/Player: Implementation of the player class.

- Admin/: Directory for everything related to the administrator

- Lib/: Directory for any library-like code