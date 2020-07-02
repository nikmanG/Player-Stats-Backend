Feature: teams

Scenario: Retrieving leagues for given type
  Given the following leagues exist
  | leagueName | teamType   |
  | NBA        | QUIDDITCH  |
  | NFL        | QUIDDITCH  |
  | MMA        | DUEL       |
  When getting all available leagues for type 'QUIDDITCH'
  Then the following leagues are returned
  | name |
  | NBA  |
  | NFL  |

