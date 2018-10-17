Santorini common.board.Board Game README

Assignment 5: Design Task
common.board.java is the artifact for this assignment
It is the implementation of an interface for a Santorini game common.board and
contains the following interfaces and classes:

* common.board.IBoard
    * The common.board does not need to know the common.rules of the game and as such only
    presents methods that can manipulate the common.board and provide information. This is
    important since future implementations of Santorini may rely on different common.rules
    and as such will need to evaluate actions differently

* common.board.Board
    * A basic implementation of the common.data structure of our simple common.board

* IWorker
    * A piece in our modified version of Santorini, each piece is responsible for knowing
    where they are on a denoted common.board, as well as the player in which they belong to.
    this is important as it seperates the logic between a common.board, and a piece that may be placed on the common.board
* Worker
    * A basic implemenation of a worker that encapsulates the position of a worker as well as the player in which it belongs to. 
