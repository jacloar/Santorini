/**
 *
 */
public abstract class AStandardSantoriniReferee implements IReferee {
    // the list of IPlayers to know who is in the game
    List<IPlayer> players;
    // the set of rules that this Referee should enforce on the board and IPlayer Actions
    IRulesEngine rulesEngine;
    // the index of a player in "players" above that indicates whose turn it is
    int turn;
    // the IBoard that represents the single source of truth and state of the game
    IBoard board;

    // this constructor takes in information about the IPlayers in our game and our rule
    // set from the Administrator, and determines the starting player based on who the
    // "fooest" player is
    public AStandardSantoriniReferee(List<IPlayer> players, IRulesEngine rulesEngine) {
        this.players = players;
        this.rulesEngine = rulesEngine;
        IPlayer fooest = players.stream.max((p1, p2) => return p1.howFooAmI() - p2.howFooAmI());
        this.turn = players.indexOf(fooest);
        this.board = new Board();
    }

    /**
     * The interface methods are implemented by each concrete implementation
     */

    public abstract boolean setUpGame();

    public abstract boolean executeTurn();

    public abstract boolean notifyPlayersOfResults();
}