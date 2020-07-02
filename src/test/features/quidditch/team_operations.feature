Feature: Team Operations

Scenario: When retrieving all teams for quidditch
  Given The following teams exist
  | name       | teamType  |
  | Gryffindor | QUIDDITCH |
  | Slytherin  | QUIDDITCH |
  | Hufflepuff | QUIDDITCH |
  | Ravenclaw  | QUIDDITCH |
  When the client pings the server
  Then the client receives status code of 403