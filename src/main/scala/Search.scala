import akka.actor.{Actor, ActorRef, ActorSystem}

class Search(supervisor:ActorRef,index:ActorRef) extends Actor{

  def receive: Receive = {

    case SearchRequest(searchstring) =>

      search(searchstring)
      supervisor ! "newsearch"
  }

  def search(search:List[String]):Unit = {

      println(s"Searching for $search")
      val result = index ! SearchRequest(search)


  }


}
