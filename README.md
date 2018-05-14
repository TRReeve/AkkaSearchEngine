# AkkaSearchEngine
An concurrent website crawler and content indexer and search engine concept application using Akka actor models to enable both concurrent tasking of scraping and indexing and search but ideally concurrent scalable useage as well. Currently the Actor System is limited to generating 10 Scrape actors at a time but there is potential to scale this massively. 


To Run 

1. Download package 

2. cd into the package directory and execute sbt run (you can run in docker too but akka logging hasn't been disabled yet

3. Follow instructions on the page 


Todos

- Implement the search function through an akka http web service and return html document with search results instead of data map.

- Store the inverted index to a Hadoop or cassandra cluster and crawl every major news website. 

- Return the sentences containing the sentences in which search terms appear on page. 

- Connect web front end to the akka service.

- Handle dead-letter issues in akka actors on shutdown. 

- more sophisticated ranking than number of hits of keywords
