import java.net.URL

import akka.actor.{ActorSystem, Props}
import akka.actor.ActorSystem
import akka.http.javadsl.server.Route
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.Uri.Query
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer

import scala.io.StdIn


case class StartScraping(url: URL)
case class ScrapeRequest(url: URL)
case class IndexRequest(content: PageContent)
case class ScrapeResponse(url:URL,list: List[URL])
case class PageContent(url:URL,content:List[String],links:List[URL])
case class SearchRequest(search:List[String])



object WebServer {
  def main(args: Array[String]) {

    implicit val system = ActorSystem("akka-search")
    implicit val materializer = ActorMaterializer()
    // needed for the future flatMap/onComplete in the end
    implicit val executionContext = system.dispatcher

    val handler = system.actorOf(Props(new Handler(system)))
    val url = new URL("https://www.nytimes.com/?WT.z_jog=1&hF=t&vS=undefined")

    //initiate actor system
    handler ! StartScraping(url)

    val route =
      parameters('query) { (query) =>
        complete(s"The query is '$query")
      }

    val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    StdIn.readLine() // let it run until user presses return
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ => system.terminate()) // and shutdown when done
  }
}