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
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive place-worker-prompt
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| send place-worker-request
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive worker-id
        |                ||                   |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive place-worker-prompt
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| send place-worker-request
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive worker-id
        |                ||                   |
        |                ||                   |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive make-turn-prompt
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| send turn-request
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive make-turn-prompt
        |<~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~| send turn-request
        |                ||                   | ...
        |                ||                   |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive game-result
        |                ||                   |
----------------------------------------------------- ending tournament
        |                ||                   |
        |~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~>| receive tournament result
        |                ||                   |
        
```
The "running game" section repeats as many times as necessary (thrice for every other player in the tournament).

The message formats are as follows:

|  message                   | format                             |
| -------------------------- | ---------------------------------- |
| sign-up name               | string of lowercase letters        |
| internal name              | string of lowercase letters (in case of duplicate) |
|                            |                                    |
| start game                 | string of lowercase letters        |
|                            | where string is the opponent name  |
|                            |                                    |
| place-worker-prompt        | ["place", Board]                   |
| Board                      | [[Cell, ...], ...]                 |
| Cell                       | one of Height or BuildingWorker    |
| Height                     | one of 0, 1, 2, 3, 4 (indicating the height of a building) |
| BuildingWorker             | a string that starts with a single digit (representing a Height) followed by a worker-id |
| worker-id                  | a string that starts with the internal name of a player followed by a 1 or 2 |
|                            |                                    |
| place-worker-request       | [number, number]                   |
|                            | where the first number is the row  |
|                            | and the second number is the column|
|                            |                                    |
| make-turn-prompt           | ["turn", Board]                    |
| make-turn-request          | [worker-id, Direction, Direction]  |
