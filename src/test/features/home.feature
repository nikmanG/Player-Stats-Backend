Feature: Calling homepage

Scenario: client makes call to homepage
  When the client pings the server
  Then the client receives status code of 200
  And the client receives the message 'This is the API backend. API calls only.'