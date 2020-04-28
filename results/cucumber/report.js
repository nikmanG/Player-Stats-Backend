$(document).ready(function() {var formatter = new CucumberHTML.DOMFormatter($('.cucumber-report'));formatter.uri("src/test/features/home.feature");
formatter.feature({
  "name": "the version can be retrieved",
  "description": "",
  "keyword": "Feature"
});
formatter.scenario({
  "name": "client makes call to homepage",
  "description": "",
  "keyword": "Scenario"
});
formatter.step({
  "name": "the client pings the server",
  "keyword": "When "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "the client receives status code of 200",
  "keyword": "Then "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
formatter.step({
  "name": "the client receives the message \u0027This is the API backend. API calls ony.\u0027",
  "keyword": "And "
});
formatter.match({});
formatter.result({
  "status": "undefined"
});
});