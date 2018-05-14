import java.net.URI
import akka.actor.{Actor, ActorRef}
import org.jsoup.Jsoup
import org.apache.commons.validator.routines.UrlValidator
import scala.collection.JavaConverters._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.language.postfixOps

class Scraper (supervisor: ActorRef, indexer: ActorRef) extends Actor {

  val urlValidator = new UrlValidator()

  def receive: Receive = {

    case ScrapeRequest(url) =>

       val tick = context.system.scheduler.schedule(0 millis, 1000 millis, self,"processing")

      val pagecontent = scrapecontent(url)

      sender() ! ScrapeResponse(url,pagecontent.links)

      indexer ! IndexRequest(pagecontent)

      context.stop(self)
  }

  def scrapecontent(url:URI): PageContent = {

    val link = url.toString
    val connect = Jsoup.connect(link).execute()
    val contentType:String = connect.contentType

    if (contentType.startsWith("text/html")) {

      val content = connect.parse()
      val pageContent = content.text().toLowerCase.split(" ").toList

      // parse hyperlinks into java URL objects to send back to Handler
      val links: List[URI] = content.getElementsByTag("a").
        asScala.map(e => e.attr("href")).
        filter(s => urlValidator.isValid(s)).
        map(link => new URI(link)).toList

      //Return Page content class
      PageContent(url, pageContent, links)

    } else {

      PageContent(url,List(),List())}



  }


}
