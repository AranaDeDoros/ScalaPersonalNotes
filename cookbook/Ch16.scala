object Ch16 extends App{

	//16 Databases and persistence

	//16.1 Connecting to MySQL with JDBC
	package tests
	import java.sql.{Connection,DriverManager}
	//object ScalaJdbcConnectSelect extends App {
	// connect to the database named "mysql" on port 8889 of localhost
		val url = "jdbc:mysql://localhost:8889/mysql"
		val driver = "com.mysql.jdbc.Driver"
		val username = "root"
		val password = "root"
		var connection:Connection = _
	try {
		Class.forName(driver)
		connection = DriverManager.getConnection(url, username, password)
		val statement = connection.createStatement
		val rs = statement.executeQuery("SELECT host, user FROM user")
		while (rs.next) {
		val host = rs.getString("host")
		val user = rs.getString("user")
		println("host = %s, user = %s".format(host,user))
	}
	} catch {
		case e: Exception => e.printStackTrace
	}
		connection.close
		//libraryDependencies += "mysql" % "mysql-connector-java" % "5.1.24"
	//}


	//16.2 Connecting to a Database with the Spring Framework
	/*To demonstrate this, create a basic Spring JDBC example. Start by creating a simple SBT
	project directory structure as demonstrated in Recipe 18.1, “Creating a Project Direc‐
	tory Structure for SBT”.
	Once the SBT directory structure is created, place this Spring applicationContext.xml
	file in the src/main/resources directory:
	<?xml version="1.0" encoding="UTF-8"?>
	<!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN"
	"http://www.springframework.org/dtd/spring-beans.dtd">
	<beans>
	 <bean id="testDao" class="springtests.TestDao">
	 <property name="dataSource" ref="basicDataSource"/>
	 </bean>
	 <bean id="basicDataSource" class="org.apache.commons.dbcp.BasicDataSource">
	 <property name="driverClassName" value="com.mysql.jdbc.Driver" />
	 <property name="url" value="jdbc:mysql://localhost/mysql" />
	 <property name="username" value="root" />
	 <property name="password" value="root" />
	 <property name="initialSize" value="1" />
	 <property name="maxActive" value="5" />
	 </bean>
	</beans>*/


	//*16.3
	//Connecting to MongoDB and inserting Database
	//Once it’s running, use the Casbah driver with your Scala application to interact with MongoDB.
	/**
	 name := "MongoDBDemo1"
	 version := "1.0"
	 scalaVersion := "2.10.0"
	 libraryDependencies ++= Seq(
	  "org.mongodb" %% "casbah" % "2.6.0",
	  "org.slf4j" % "slf4j-simple" % "1.6.4"
	 )
	 scalacOptions += "-deprecation"
	 */

		import com.mongodb.casbah.MongoCollection
		import com.mongodb.casbah.MongoConnection
		object MongoFactory {
			private val SERVER = "localhost"
			private val PORT = 27017
			private val DATABASE = "portfolio"
			private val COLLECTION = "stocks"
			val connection = MongoConnection(SERVER)
			val collection = connection(DATABASE)(COLLECTION)
		}

		import com.mongodb.casbah.Imports._
		case class Stock (symbol: String, price: Double)
		object Common {
			/**
			* Convert a Stock object into a BSON format that MongoDb can store.
			*/
			def buildMongoDbObject(stock: Stock): MongoDBObject = {
			val builder = MongoDBObject.newBuilder
			builder += "symbol" -> stock.symbol
			builder += "price" -> stock.price
			builder.result
 			}
		}

		import com.mongodb.casbah.Imports._
		import Common._
		object Insert extends App {
		// create some Stock instances
		val apple = Stock("AAPL", 600)
		val google = Stock("GOOG", 650)
		val netflix = Stock("NFLX", 60)
		// save them to the mongodb database
		saveStock(apple)
		saveStock(google)
		saveStock(netflix)
		// our 'save' method
		def saveStock(stock: Stock) {
				val mongoObj = buildMongoDbObject(stock)
				MongoFactory.collection.save(mongoObj)
	 	}
	}

	//16.4 Inserting, Documents into MongoDB with insert, save, or +=
	/*The third approach is a little different; it looks like what
  you’re doing is adding an object to a collection.
	In fact, you’re saving your object to the	database collection with each += call.
	Here’s what the += approach looks like in a complete program:*/
	import com.mongodb.casbah.Imports._
	import Common._
	object Insert2 extends App {
	 val collection = MongoFactory.collection
	 // create some Stock instances
	 val apple = Stock("AAPL", 600)
	 val google = Stock("GOOG", 650)
	 val netflix = Stock("NFLX", 60)
	 val amazon = Stock("AMZN", 220)
	 // add them to the collection (+= does the save)
	 collection += buildMongoDbObject(apple)
	 collection += buildMongoDbObject(google)
	 collection += buildMongoDbObject(netflix)
	 collection += buildMongoDbObject(amazon)
	}



	//16.5 Searching a MongoDB collection
	import com.mongodb.casbah.Imports._
	object Find extends App {
	 val collection = MongoFactory.collection
	 // (1) find all stocks with find()
	 // -------------------------------
	 println("\n___ all stocks ___")
	 var stocks = collection.find
	 stocks.foreach(println)
	 // (2) search for an individual stock
	 // ----------------------------------
	 println("\n___ .findOne(query) ___")
	 val query = MongoDBObject("symbol" -> "GOOG")
	 val result = collection.findOne(query) // Some
	 val stock = convertDbObjectToStock(result.get) // convert it to a Stock
	 println(stock)
	 // (3) find all stocks that meet a search criteria
	 // -----------------------------------------------
	 println("\n___ price $gt 500 ___")
	 stocks = collection.find("price" $gt 500)
	 stocks.foreach(println)
	 // (4) find all stocks that match a search pattern
	  // -----------------------------------------------
	  println("\n___ stocks that begin with 'A' ___")
	  stocks = collection.find(MongoDBObject("symbol" -> "A.*".r))
	  stocks.foreach(println)
	  // (5) find.limit(2)
	  // -------------------------------
	  println("\n___ find.limit(2) ___")
	  stocks = collection.find.limit(2)
	  stocks.foreach(println)
	  // warning: don't use the 'get' method in real-world code
	  def convertDbObjectToStock(obj: MongoDBObject): Stock = {
	  val symbol = obj.getAs[String]("symbol").get
	  val price = obj.getAs[Double]("price").get
	  Stock(symbol, price)
	  }
	 }

	 /*
	 In the first query, the find method returns all documents from the specified collection.
	 This method returns a MongoCursor, and the code iterates over the results using that
	 cursor.
	 In the second query, the findOne method is used to find one stock that matches the
	 search query. The query is built by creating a MongoDBObject with the desired attributes.
	 In this example, that’s a stock whose symbol is GOOG. The findOne method is called to
	 get the result, and it returns an instance of Some[MongoDBObject].
	 The convertDbObjectToStock method does the reverse of the buildMongoDbObject
	 method shown in the earlier recipes, and converts a MongoDBObject to a Stock instance.
	 The third query shows how to search for all stocks whose price is greater than 500:
	 stocks = collection.find("price" $gt 500)
	 This again returns a MongoCursor, and all matches are printed
  */
  //You can also query against multiple fields by joining tuples:
  val query: DBObject = ("price" $gt 50 $lte 100) ++ ("priceToBook" $gt 1)
  /*In the fourth query, a simple regular expression pattern is used to search for all stocks
  whose symbol begins with the letter A:*/
  stocks = collection.find(MongoDBObject("symbol" -> "A.*".r))
  /*The fifth query demonstrates how to use the limit method to limit the number of results
  that are returned:
  stocks = collection.find.limit(2)
  Because MongoDB is typically used to store a lot of data, you’ll want to use limit to
  control the amount of data you get back from a query

  The MongoCollection class also has a findByID method that you can use when you
  know the ID of your object. Additionally, there are findAndModify and findAndRemove
  methods, which are discussed in other recipes in this chapter*/

  //16.6. Updating Documents in a MongoDB Collection
  import com.mongodb.casbah.Imports._
  import Common._
  object Update extends App {
   val collection = MongoFactory.collection
   // findAndModify
   // -------------
   // create a new Stock object
   val google = Stock("GOOG", 500)
   // search for an existing document with this symbol
   var query = MongoDBObject("symbol" -> "GOOG")
   // replace the old document with one based on the 'google' object
   val res1 = collection.findAndModify(query, buildMongoDbObject(google))
   println("findAndModify: " + res1)
   // update
   // ------
   // create a new Stock
   var apple = Stock("AAPL", 1000)
   // search for a document with this symbol
   query = MongoDBObject("symbol" -> "AAPL")
   // replace the old document with the 'apple' instance
   val res2 = collection.update(query, buildMongoDbObject(apple))
   println("update: " + res2)
  }


  /*Whereas the findAndModify method returns the old document (the document that was
  replaced), the update method returns a WriteResult instance*/

	//16.7. Accessing the MongoDB Document ID Field
	import com.mongodb.casbah.Imports._
	import Common._
	object InsertAndGetId extends App {
	 val coll = MongoFactory.collection
	 // get the _id field after an insert
	 val amazon = Stock("AMZN", 220)
	 val amazonMongoObject = buildMongoDbObject(amazon)
	 coll.insert(amazonMongoObject)
	 println("ID: " + amazonMongoObject.get("_id"))
	}

	/*If you just need to get the ID field from a MongoDBObject after performing a query, the
	following complete example shows how to do that with a match expression:*/

	import com.mongodb.casbah.Imports._
	object GetId extends App {
	 val collection = MongoFactory.collection
	 val query = MongoDBObject("symbol" -> "GOOG")
	 collection.findOne(query) match {
	 	 case Some(result) => println("ID: " + result.get("_id"))
	 	 case None => println("Stock not found")
	 	 }
	 	}


	 	//16.8 Deleting Documents in a MongoDB Collection
	 	/*Use the findAndRemove method of the Casbah MongoCollection class to delete one
	 	document at a time, or use the remove method to delete one or more documents at a
	 	time*/

	 	val query = MongoDBObject("symbol" -> "AAPL")
	 	val result = collection.findAndRemove(query)
	 	println("result: " + result)

	 	/*When a document is deleted, the findAndRemove method returns the document that
	 	was deleted, wrapped in a Some:
	 	result: Some({ "_id" : { "$oid" : "50255d1d03644925d83b3d07"} ,
	 	 "symbol" : "AAPL" , "price" : 600.0})
	 	If nothing is deleted, such as when you try to delete a document that doesn’t exist, the
	 	result is None:
	 	result: None

	 	To delete multiple documents from the collection, specify your search criteria when
	 	using the remove method, such as deleting all documents whose price field is greater
	 	than 500:*/
	 	collection.remove("price" $gt 500)

	 	/*The following method is dangerous: it shows how to delete all documents in the current
	 	collection:*/
	 	// removes all documents
	 	def deleteAllObjectsFromCollection(coll: MongoCollection) {
	 	 coll.remove(MongoDBObject.newBuilder.result)
	 	}
	 	//(Be careful with that one.)


	 	import com.mongodb.casbah.Imports._
	 	object DeleteApple extends App {
	 	 var collection = MongoFactory.collection
	 	 // delete AAPL
	 	 val query = MongoDBObject("symbol" -> "AAPL")
	 	 val result = collection.findAndRemove(query)
	 	 println("result: " + result)
	 	}


	 	//The following complete code shows how to delete multiple documents:
	 	import com.mongodb.casbah.Imports._
	 	object DeleteMultiple extends App {
	 	 var collection = MongoFactory.collection
	 	 // delete all documents with price > 200
	 	 collection.remove("price" $gt 200)
	 	}


	 	//16.9 A Quick Look at Slick
	 	object Authors extends Table[(Int, String, String)]("AUTHORS") {
	 	 def id = column[Int]("ID", O.PrimaryKey)
	 	 def firstName = column[String]("FIRST_NAME")
	 	 def lastName = column[String]("LAST_NAME")
	 	 def * = id ~ firstName ~ lastName
	 	}
	 	object Books extends Table[(Int, String)]("BOOKS") {
	 	 def id = column[Int]("ID", O.PrimaryKey)
	 	 def title = column[String]("TITLE")
	 	 def * = id ~ title
	 	}
	 	object BookAuthors extends Table[(Int, Int, Int)]("BOOK_AUTHORS") {
	 	 def id = column[Int]("ID", O.PrimaryKey)
	 	 def bookId = column[Int]("BOOK_ID")
	 	 def authorId = column[Int]("AUTHOR_ID")
	 	 def bookFk = foreignKey("BOOK_FK", bookId, Books)(_.id)
	 	 def authorFk = foreignKey("AUTHOR_FK", authorId, Authors)(_.id)
	 	  def * = id ~ bookId ~ authorId
    }


		/*Having defined your tables in Scala code, you can refer to the fields in the tables in a
		type-safe manner. You can create your database tables using Scala code, like this:*/
		(Books.ddl ++ Authors.ddl ++ BookAuthors.ddl).create

		/*A simple query to retrieve all records from the resulting books database table looks like
		this:*/
		val q = Query(Books)
		q.list.foreach(println)

		//You can filter queries using a filter method:
		val q = Query(Books).filter(_.title.startsWith("Zen"))
		q.list.foreach(println)
		//You can write a join like this:
		val q = for {
		 b <- Books
		 a <- Authors
		 ba <- BookAuthors if b.id === ba.bookId && a.id === ba.authorId
		} yield (b.title, a.lastName)
		q.foreach(println)

		/*Insert, update, and delete expressions follow the same pattern. Because you declared
		the database design in Scala code, Slick makes working with a database feel like working
		with collections*/



}