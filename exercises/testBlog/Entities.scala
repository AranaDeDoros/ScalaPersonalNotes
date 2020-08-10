package blog.entities{


  import scala.collection.mutable.{ListBuffer => ListBuffer}
  import blog.media._
  
  //main
  class Author(
      val name: String,
      val nickName: String,
      private val nFollowers: Int
  ) {

    override def toString: String =
      s"Hello my name is $name but you can call me $nickName. Become one of the $nFollowers followers!"

    def signAs: String = toString

  }

  class Blog(val title: String, val nSuscribers: Int, val desc: String)

	//categories
  case class Category(val postId: Int, val name: String, val relevance: Int)

  //tags
  case class Tag(val postId: Int, val description: String)

  //post
  trait Sticky {

    def stickToFirstPage(postId: Int): Unit = println("sticking to home")

  }

  case class Post(
      val title: String,
      val date: String,
      val body: ListBuffer[String],
      val tags: ListBuffer[Tag],
      val category: ListBuffer[Category],
      val media: ListBuffer[Media],
      val slug: String,
      val postedAt: String
  ) {

    def length = {
      val postBody = body.toList.flatten
      postBody.length
    }

  }

  //comments
  case class Comment(
      val postId: Int,
      val author: String,
      val message: String,
      val postedAt: String
  )

}