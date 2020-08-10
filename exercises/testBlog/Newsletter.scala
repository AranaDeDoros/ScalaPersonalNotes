package blog.newsletter{

  import scala.collection.mutable.{ListBuffer => ListBuffer}
  
	//newsletter
  trait ExclusivePosts {
    this: NewsLetter =>
    def authorize(mailList: ListBuffer[String]): Unit =
      println("checking if they're exclusive")
  }

  class ExclusiveNewsLetter extends NewsLetter with ExclusivePosts {
    override def mailList = ListBuffer[String]("vip@vip.com", "vipok@vipok.com") 
  }

  class NewsLetter {
    def mailList = ListBuffer[String]("test@test.com", "ok@ok.com") 
  }
}