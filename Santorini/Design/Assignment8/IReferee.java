/**
 * An IReferee interface represents the knowledge of how to coordinate and organize IPlayers in a
 * game of Santorini. It is a component that is meant to be used by a larger entity
 * we're referring to as the Administrator. The Administrator is the component that
 * will be responsible for handling game configurations and setting up the technical
 * aspects of the game such as connecting to IPlayers, and then it will use this
 * IReferee interface to control the IPlayers during the game.
 */
public interface IReferee {
    // Set up the game according to informtaion gathered from the IRulesEngine
    // return true if set up is successful, and false if something went wrong
    // for Standard Santorini, a starting common.board is a common.board of all 0's with two workers per player
    boolean setUpGame();

    // Execute a turn in the game. The IReferee offers this an as atomic action
    // to give some control over the game process to the Administrator, which might
    // need to change the order of things or how many times they happen. The standard way to use this
    // method would be to call it via a while loop until it returns false, indicating
    // the game is over. Once this happens, the IReferee will be know who won and who
    // lost and will be able to notify them. If an error occurs or an IPlayer cheats
    // it will know this as well, and be able to report it to the Administrator if requested.
    //
    // returns whether the game is still ongoing
    boolean executeTurn();

    // Send the resuls of the game to the players, and return whether the messages
    // were conveyed successfully. In a normal game this means telling the IPlayers
    // whether they won or lost, but could also be used to communicate when an IPlayer
    // cheats or when an error has occured
    boolean notifyPlayersOfResults();
}