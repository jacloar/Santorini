```
tournament manager       ||                 player
---------------------------------------------------- connection phase
        |                ||                   + player starts
        |                ||                   |
        |<------------------------------------| tcp connect
        |                ||                   |
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| sign-up name
        |                ||                   |
---------------------------------------------------- starting tournament
        |                ||                   |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| recieve internal name
        |                ||                   |
---------------------------------------------------- running game
        |                ||                   |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive start game
        |                ||                   |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive placement-prompt
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| send check-placement
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receieve valid-move
        |                ||                   |
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| send placement-request
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive worker-id
        |                ||                   |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive placement-prompt
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| send placement-request
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive worker-id
        |                ||                   |
        |                ||                   |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive turn-prompt
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| send check-turn
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receieve valid-move
        |                ||                   |
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| send turn-request
        |                ||                   |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive turn-prompt
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| send turn-request
        |                ||                   | ...
        |                ||                   |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive game-result
        |                ||                   |
----------------------------------------------------- ending tournament
        |                ||                   |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive tournament-result
        |                ||                   |
        
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
| valid move                 | boolean representing if the request is valid |
| placement-request          | [number, number]                   |
|                            | where the first number is the row  |
|                            | and the second number is the column|
| turn-prompt                | ["turn", Board]                    |
| check-turn                 | ["check-turn", turn-request]       |
| turn-request               | [worker-id, Direction] or [worker-id, Direction, Direction] |
| Direction                  | [EastWest, NorthSouth]             |
| EastWest                   | One of: "EAST", "PUT", "WEST"      |
| NorthSouth                 | One of: "NORTH", "PUT", SOUTH"     |
| game-result                | One of: "WINNER", "LOSER", "CHEATER" |
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