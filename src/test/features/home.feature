Feature: Calling homepage

Scenario: client makes call to homepage with no login
  When the client pings the server
  Then the client receives status code of 403