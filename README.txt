README
---------

Background/Requirement:
This project is to write a REST service (A) which can accept trade data in XML format and convert to JSON format and post to another WebService B.  The results from B are cached in REST Service A.

The REST Service has 2 methods 1. to saveTrade and 2. to getStatus of the saved trade.


How to build/run:
-----------------
1. Checkout/extract the project TradeAdapterRS to your local from github location: https://github.com/vijaiinuk/TradeAdapterRS
2. Run the maven goals clean, package.  This will create the war and standalone jar in "target" directory.
3. open a terminal or command prompt.
	- cd target
	- java -jar jaxrs-service-standalone.jar -httpPort=<appropriate port number>
	- default port will be 8080
4. Once the server is started, you can run the Integration test - com.tradeservice.restservice.TradeAdapterRSIT.java to verify the scenarios.

Other Details:
--------------
- Used Guava Collections for caching the responses.
- Statistics of number of requests/per minute will be logged in the application log file. (with file name TradeService.log where you run the standalone jar)


Any questions, please feel free to call/mail me.  :)