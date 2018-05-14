# AkkaSearchEngine
An concurrent website crawler and content indexer and search engine concept application using Akka actor models. 


To Run 

1. Download package 

2. cd into the package directory and execute sbt run 

3. Follow instructions on the page 


Todos

- Implement the search function through an akka http web service and return html document with search results

- Store the inverted index to a Hadoop or cassandra cluster and crawl every major news website. 

- Return the sentences containing the sentences in which search terms appear on page. 

- Connect web front end to the akka service.
