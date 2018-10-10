package Admin;
import Player.IPlayer;

/**
 * Interface for the Referee class. The referee manages the game. The referee determines
 * which players turn it is, which player has won the game, validates player move requests
 * with the help of the Rule class, kicks malicious players, and executes valid moves.
 */
public interface IReferee {


  // The referee has the following:
  // - A Iboard
  // - A List of Iplayers


  /**
   * When two players are ready to play a game the referee will call setup game.
   * setupGame will prompt each player two times to place a worker on the board.
   * Once four workers have been placed setupGame will end.
   */
  void setupGame();


  /**
   * Once setupGame has completed playGame will be called.
   * playGame loops with a conditional. If the game is determined
   * to be over the loop will exit. Each player will be prompted
   * for turns following the opponents turn being validated and
   * executed. If a requested move from a player is invalid then
   * that player will be kicked from the game and his/her opponent
   * will be declared the winner.
   *
   * @return The player who won the game.
   */
  IPlayer playGame();


}
