package blog

import scala.collection.mutable.{ListBuffer => ListBuffer}
import blog.util._
import blog.media._
import blog.seo._
import blog.newsletter._
import blog.logging._
import blog.entities._

object Blog extends App {


	val myBlog = new Blog("test blog", 200, "a test blog")
  println(myBlog.title)
	val me = new Author("David", "AranaDeDoros", 10)
	println(me.nickName)
  val bodyPost = ListBuffer[String]("this", "is", "a", "test")
	println(bodyPost)
  val tagList = ListBuffer[Tag](Tag(1, "test tag"), Tag(2, "another test tag"))
	println(tagList)
  val categoryList = ListBuffer[Category](Category(1, "test category", 10), 
											Category(2, "yet another test category", 20))
	println(categoryList)
  val postTitle = "A test post"
  println(postTitle)
	val date = "08/08/2020"
  println(date)
	val postedAt = "08/08/2020"
  println(postedAt)
	val slug = "a-test-post"
	println(slug)
	val media = ListBuffer[Media](new Picture("PCT-12", "path/image.jpg", ".jpg"), 
							new Video("VD-41", "path/video.mp4", ".mp4", "h264"))
  println(media.map{media=> media.path})
	val newPost = new Post(postTitle,
						   date,
						   bodyPost,
						   tagList,
						   categoryList, 
						   media,
						   slug,
						   postedAt)
						   with Sticky	

  println(newPost)

	val newComment =  Comment(1, "anon", "posting a comment", "09/08/2020")
  println(newComment)
	val visitor = Visitor("DEU", "08/08/20", 3, 1)
  println(visitor)
	val newsLetter = new NewsLetter
	println(newsLetter.mailList)
	val vipNewsLetter = new ExclusiveNewsLetter
	println(vipNewsLetter.mailList)

	//media
	val myLogger = new Logger("this is a message", "info")
	myLogger.registerToDatabase()

}
