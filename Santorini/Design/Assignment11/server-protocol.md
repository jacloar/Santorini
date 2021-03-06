```
  tournament manager     ||                 player                                 observer
------------------------------------------------------------------------------------------------------ connection phase
        |                ||                   + player starts                         + observer starts
        |                ||                   |                                       |
        |<------------------------------------| tcp connect                           |
        |<----------------------------------------------------------------------------| tcp connect
        |                ||                   |                                       |
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| sign-up name                          |
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| sign-up name
        |                ||                   |                                       |
------------------------------------------------------------------------------------------------------ starting tournament
        |                ||                   |                                       |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| recieve internal name                 |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive internal name
        |                ||                   |                                       |
------------------------------------------------------------------------------------------------------ running game
        |                ||                   |                                       |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive start game                    |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive Board
        |                ||                   |                                       |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive placement-prompt              |
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| send check-placement                  |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receieve valid-move                   |
        |                ||                   |                                       |
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| send placement-request                |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive worker-id                     |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive Board
        |                ||                   |                                       |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive placement-prompt              |
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| send placement-request                |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive worker-id                     |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive Board
        |                ||                   |                                       |
        |                ||                   |                                       |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive turn-prompt                   |
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| send check-turn                       |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receieve valid-move                   |
        |                ||                   |                                       |
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| send turn-request                     |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive turn-request
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive Board
        |                ||                   |                                       |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive turn-prompt                   |
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| send turn-request                     |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive turn-request
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive Board
        |                ||                   | ...                                   |
        |                ||                   |                                       |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive game-result                   |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive win-player
        |                ||                   |                                       |
--------------------------------------------------------------------------------------------------- ending tournament
        |                ||                   |                                       |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive tournament-result             |
        |                ||                   |                                       |
        
```
The "running game" section repeats as many times as necessary (thrice for every other player in the tournament).

The message formats are as follows:

|  message                   | format                             |
| -------------------------- | ---------------------------------- |
| sign-up name               | string of lowercase letters        |
| internal name              | string of lowercase letters (in case of duplicate) |
| start game                 | string of lowercase letters        |
|                            | where string is the opponent name  |
| placement-prompt           | ["place", Board]                   |
| Board                      | [[Cell, ...], ...]                 |
| Cell                       | one of Height or BuildingWorker    |
| Height                     | one of 0, 1, 2, 3, 4 (indicating the height of a building) |
| BuildingWorker             | a string that starts with a single digit (representing a Height) followed by a worker-id |
| worker-id                  | a string that starts with the internal name of a player followed by a 1 or 2 |
| check-placement            | ["check-placement", placement-request] |
| valid-move                 | boolean representing if the request is valid |
| placement-request          | [number, number]                   |
|                            | where the first number is the row  |
|                            | and the second number is the column|
| turn-prompt                | ["turn", Board] or ["turn", "none"] |
| check-turn                 | ["check-turn", turn-request]       |
| turn-request               | [worker-id, Direction] or [worker-id, Direction, Direction] |
| Direction                  | [EastWest, NorthSouth]             |
| EastWest                   | One of: "EAST", "PUT", "WEST"      |
| NorthSouth                 | One of: "NORTH", "PUT", SOUTH"     |
| game-result                | One of: "WINNER", "LOSER", "CHEATER" |
| win-player                 | ["win", string of lowercase letters] |
|                            | where string is the name of the winning player |
| tournament-result          | [Cheaters, [Game, ...]]            |
| Cheaters                   | [string, ...] where each string is the name of a player that cheated|
| Game                       | [string, string]                   |
|                            | where the first string is the name of the winning player |
|                            | and the second string is the name of the losing player |

All JSON values are well formed and valid. 
If a player sends an invalid or malformed request, they will be booted from the tournament.
If a player sends a move that is considered invalid, they will be booted from the tournament.

A request must be made within 5 seconds of receiving a prompt.
If more than 10 check-placements or check-turns are made within 1 second, the player will be booted
for attempting to overload the server.

Check requests (ie, check-placement and check-turn) are optional. The player does not need to send one, 
but it can inform the player if the move they are about to make is illegal

If a player cheats or times out, an observer receives an "Error" where "Error" is a ["error", string] where string 
contains an error message, followed by a win-player.

If a player has no valid moves, they can send a ["turn", "none"] request to indicate they give up.
In this case, the observer receives a give-up-player which is ["give up", string of lowercase letters] where
the string represents the name of the player giving up, followed by a win-player