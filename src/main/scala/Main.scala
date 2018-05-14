import java.net.URI

import akka.actor.Props
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer

case class StartScraping(url: URI)
case class ScrapeRequest(url: URI)
case class IndexRequest(content: PageContent)
case class ScrapeResponse(url:URI,list: List[URI])
case class PageContent(url:URI,content:List[String],links:List[URI])
case class SearchRequest(search:List[String])



object Searcher {

    def main(args: Array[String]) {

      implicit val system = ActorSystem("akka-search")
      implicit val materializer = ActorMaterializer()
      // needed for the future flatMap/onComplete in the end
      implicit val executionContext = system.dispatcher

      val handler = system.actorOf(Props(new Handler(system)))
    }
}