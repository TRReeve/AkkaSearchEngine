import akka.actor.{Actor, ActorPath, ActorSystem, PoisonPill, Props}
import scala.collection.mutable.ListBuffer
import java.net.URI
import scala.io.StdIn.readLine
import org.apache.commons.validator.routines.UrlValidator


class Handler (system:ActorSystem) extends Actor {

  val indexer = context actorOf Props(new Indexer(self))

  var wasscraped = Set.empty[URI]
  var indexstore = Map.empty[URI,List[String]]
  var actorslist = new ListBuffer[ActorPath]
  val urlValidator = new UrlValidator

  //start crawl
  self ! "newdomain"




  def receive: Receive = {

    case "newdomain" =>

      val page = readLine("What page do you want to start your scrape at?: ")

      if (urlValidator.isValid(page)){

        //create url
        val starturl = new URI(page)

        //start craawler
        scrapecontent(starturl)
        //add  start page to list of already scraped pages
        //initiate first keyword search
        self ! "newsearch"
      } else {

        println(s"$page is not a valid url, try again")
        self ! "newdomain"
      }



    case ScrapeResponse(url, links) =>

      wasscraped += url

      //takes list of returned links and filters down to the links on the same host
      val toscrape = links.filter(_.getHost == url.getHost)

      toscrape.map(x => if (!wasscraped.contains(x)) scrapecontent(x))

      sender() ! PoisonPill

      val toremove = sender().path

      actorslist -= toremove

    case "newsearch" =>

      //init new search actor
      val searchactor = context actorOf Props(new Search(self,indexer))

      var searchquery = readLine("what keywords are you looking for?, or type 'newsite' to scrape a new Domain: ")

      if (searchquery == "newsite") {
        self ! "newdomain"
        searchactor ! PoisonPill
      } else {
        val searchstring = searchquery.split(" ").toList

        //send request to actor
        val result = searchactor ! SearchRequest(searchstring)
      }



  }

  def scrapecontent(url: URI): Unit = {

    if (actorslist.size < 10) {

      val scraper = system.actorOf(Props (new Scraper(self,indexer)))

      actorslist += scraper.path

      scraper ! ScrapeRequest(url)
      }

  }

  def initsearchactor(): Unit = {

    val searchactor = context actorOf Props(new Search(self,indexer))

  }


}