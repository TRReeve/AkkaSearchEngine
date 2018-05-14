import java.net.URI
import akka.actor.{Actor,ActorRef}

class Indexer (supervisor: ActorRef) extends Actor {

  var store = scala.collection.mutable.Map.empty[String, List[URI]]

  //receives scraping content and inverts it
  def receive: Receive = {

    case IndexRequest(content) =>
      val pageurl = content.url

      for (record <- content.content) {

        if (store contains (record)) {

          store(record) = pageurl :: store(record)
        }
        else {
          store += (record -> List(pageurl))

        }
      }

    //makes a search on the inverted index store
    case SearchRequest(searchstring) =>

      println(searchstring.size)

      val fnlistget = (i:String) => {store.get(i.toLowerCase)}

      //flatten twice for multiple keywords
      val listlists = searchstring.map(fnlistget).distinct.flatten.flatten


      val summary = listlists.groupBy(i=>i).mapValues(_.size)

      //receive search request object and retrieve from store

      val indexsize = store.size

      println(s"searched $indexsize index entries searched returned for ${searchstring.size} keywords ")
      println(summary)

  }

}