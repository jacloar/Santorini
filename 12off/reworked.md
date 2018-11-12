## Reworked File

In our code, we mostly worked on improving the StayAliveStrategy
and adding tests/documentation for existing pieces of code.

- Removed unused worker ID field from player
  - Commit id: `dad7b630e22fac76370882f2ac2ae25f5c73396f`
- Extracted the initial height of buildings as a constant
  - Commit id: `40cef62e1c78be52ff7e554ecdac0d5fa635e6bc`
- Changed `getMaxRows` and `getMaxColumns` methods to return default
rows/columns constants
  - Commit id: `7afc8e27a8b9fbe395c9e751e6bfca4734b8740a`
- Made `isMoveLegal` method public
  - Commit id: `00904e87277f5365de33feec6ffbf72a67d545a7`
- Changed `calculateAllMoves` to `calculateLegalMoves` to improve performance
  - Commit id: `9db7e1b810af6d04f87b1c16f90fdae7665c328e`
- Updated strategy test to reflect changes made to strategy
  - Commit id: `602b448c87c4b24362b996418cff5eab8f53b35d`
- Modified `ITurnStrategy` interface so that `score` and `getLegalTurns`
are no longer contained, and it now has the `getTurn` method
  - Commit id: `7c99cc2322fb7a9d33814250de270163fb6b4594`
- Fixed our test that checks if there are cheaters
  - Commit id: `073a9460cfcbd79d28cf31bd64c3056b651bb456`
- Modified the strategies so that `StayAliveStrategy` takes a depth
rather than `Strategy`
  - Commit id: `4add65375bc57e6073e4c744432ad23debc52e20`
- Updated our `xobserve` class so that it works with our changes
and saw that the players choose moves much faster!
  - Commit id: `2c815c1a16464f3ccf80f38940bd9a4ec9748ce7`
- Added more test cases to rules test class
  - Commit id: `09d4bb0de675d79dff66c7e15526f9657eaced38`
- Fixed a bug where it was considered valid to build on the square
a worked just moved to
  - Commit id: `24c9fb18f27107b5ae3f7b4b7e4bf420dd00a48d`
- Added more tests to rules tests to reflect bug fix
  - Commit id: `ee17560f78656caa631d88147aa9c0747510fbd8`
- Fixed bad test case with incorrect player names
  - Commit id: `f0d67687d1bb7da543ab31a405d95e15aefbffc6`
- Added new test cases to TESTME
  - Commit id: `1eb21403a3a2b8739d7f9261336cb65f93286140`