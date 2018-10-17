# Santorini

This includes all the necessary source code for our version of [santorini](http://www.ccs.neu.edu/home/matthias/4500-f18/santorini.html). We have written it in Java 8 and have been utilizing `maven` for our package management and build system. 

## Project Structure

### `Player`

This is where all code related to Player classes belongs. This includes the IPlayer interface which details the behavior of a player, and
several "Strategy" classes. A Strategy is how an AIPlayer (a version of IPlayer where a computer makes the decisions) evaluates
it's moves and picks one.

### `Common`

As the name suggests, here is where all the common or `core` common.data definitions lie. This includes any dependant common.exceptions, our common.data model, common.board implementation, or rule implementation. Due to using `maven` we have set this up as it's own Module and thus can be pulled in via the other sibling modules in the project

### `Design`

All specific design documentation goes here. You will find our design planning session's end up here, as well as mocking out initial classes and interfaces for each weekly assignment.

### `Lib`

This is where all the libary code we may be dealing with such as specific test harnesses for each week, or our json parsing utility classes. This allows us to package are larger more involved items in one place without clutterly our core classes. 

## Testing

Currently each weekly project will now get a `testme` executable which will execute all of our test harness tests for that week at once. We are actively working on improving it, however currently the results are outputted via a `diff` and as such no output means everything passed. [Here](https://github.ccs.neu.edu/CS4500-F18/anon/blob/parse-multiline-sequences/7/testme) is our basic test script.
To run tests, simply go to the top level directory with the number of the assignment which contained the code you'd like to test and run `./testme`.
