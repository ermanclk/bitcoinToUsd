BitCoin to Usd Scheduler Application

Application  has a service that polls a remote Http Url to get latest Bitcoin-Usd rates. 
Used Spring's @Scheduler annotation to have basic Scheduler. On Each Schedule, Application saves Usd rate into Database,
and uses database to get historical usd rates. 

Please check the image "app-basic-diagram.png" for a basic representation.

Application provides requested REST API's, 

1. /v1/bitcoin/USD : for getting latest usd rate 

* This api implementation tries to get latest rate from remote service, uses a Circuit Breaker pattern 
here to provide failure tolerance, on any Exception, it gets latest rate from database. 

2. /v1/bitcoin/USD/USD/byDateRange : for getting historical rates in given time periods 

After a long term usage with small periods, application may have huge data to be stored,because of that,
decided to use a NoSql database, MongoDb, just because Nosql databases scale and perform 
better on high loads. I checked your tech stack and see DynamoDb as Nosql, but as I did not used it before, 
I decided to use MongoDb for faster implementation as I have some experience on it. If using a tech from tech radar is mandatory,
please notify me to change, as it should not be hard to switch that.

* Application checks the currency exchange rate from Bitcoin to US-Dollar with a given configurable period. 

* I used and http endpoint for getting rates, check period is configurable in milliseconds format.

Notes: 
* Used Remote server(api.coindesk.com) provides updates on 1 minute periods, so configuring this application to periods
smaller than 1 minute can cause unnecessary remote api calls, so unnecessary resource usage. To prevent that, 
I used a default minimum period value,1 minute, if provided configuration is less than 1 minute, application
will use 1 minute as period. 

* There are some missing concerns to be production ready, cannot complete all because of lack of time:

-- Integration tests can be added, application has some unit tests, but not for all classes because of lack of time.
-- Logs can be improved
-- Application can be containerized.
-- Monitoring endpoints can be provided.
