object Ch15 extends App{
	///15
	//15.1 Creating JSON String from Scala Object
	/*name := "Basic Lift-JSON Demo"
	version := "1.0"
	scalaVersion := "2.10.0"
	libraryDependencies += "net.liftweb" %% "lift-json" % "2.5+"

	Lift-JSON solution*/
	import scala.collection.mutable._
	import net.liftweb.json._
	import net.liftweb.json.Serialization.write
	case class Person(name: String, address: Address)
	case class Address(city: String, state: String)
	object LiftJsonTest extends App {
		val p = Person("Alvin Alexander", Address("Talkeetna", "AK"))
		// create a JSON string from the Person, then print it
		implicit val formats = DefaultFormats
		val jsonString = write(p)
		println(jsonString)
	}


	//Gson
	import com.google.gson.Gson
	case class Person(name: String, address: Address)
	case class Address(city: String, state: String)
	object GsonTest extends App {
		val p = Person("Alvin Alexander", Address("Talkeetna", "AK"))
		// create a JSON string from the Person, then print it
		val gson = new Gson
		val jsonString = gson.toJson(p)
		println(jsonString)
	}

	import net.liftweb.json.JsonAST
	import net.liftweb.json.JsonDSL._
	import net.liftweb.json.Printer.{compact,pretty}
	object LiftJsonWithCollections extends App {
		val json = List(1, 2, 3)
		println(compact(JsonAST.render(json)))
		val map = Map("fname" -> "Alvin", "lname" -> "Alexander")
		println(compact(JsonAST.render(map)))
	}
	println(pretty(JsonAST.render(map)))

	/*The Lift-JSON examples in this recipe work well for either objects or collections, but
	when you have an object that contains collections, such as a Person class that has a list
	of friends defined as List[Person], it’s best to use the Lift-JSON DSL.*/

	//15.2. Creating a JSON String from Classes That Have Collections
	import net.liftweb.json._
	import net.liftweb.json.JsonDSL._
	case class Person(name: String, address: Address) {
	    var friends = List[Person]()
	}
	case class Address(city: String, state: String)
	object LiftJsonListsVersion1 extends App {
	 	//import net.liftweb.json.JsonParser._
		implicit val formats = DefaultFormats
		val merc = Person("Mercedes", Address("Somewhere", "KY"))
		val mel = Person("Mel", Address("Lake Zurich", "IL"))
		val friends = List(merc, mel)
		val p = Person("Alvin Alexander", Address("Talkeetna", "AK"))
		p.friends = friends
		 // define the json output
		val json =
		("person" ->
		("name" -> p.name) ~
		("address" ->
		("city" -> p.address.city) ~
		("state" -> p.address.state)) ~
		("friends" ->
		friends.map { f =>
		("name" -> f.name) ~
		("address" ->
		("city" -> f.address.city) ~
		("state" -> f.address.state))
		})
		)
		println(pretty(render(json)))
	 }
	/*

 The first thing to know is that any Tuple2 generates a JSON field, so a code snippet like
 ("name" -> p.name) produces this output:
 "name":"Alvin Alexander"
 The other important thing to know is that the ~ operator lets you join fields. You can
 see from the example code and output how it works
 The following code achieves the same result as the previous example,
 but I’ve broken the JSONgenerating code down into small methods that are easier to maintain and reuse:*/
	import net.liftweb.json._
	import net.liftweb.json.JsonDSL._
	object LiftJsonListsVersion2 extends App {
	val merc = Person("Mercedes", Address("Somewhere", "KY"))
	val mel = Person("Mel", Address("Lake Zurich", "IL"))
	val friends = List(merc, mel)
	val p = Person("Alvin Alexander", Address("Talkeetna", "AK"))
	p.friends = friends
	val json =
		("person" ->
		("name" -> p.name) ~
		getAddress(p.address) ~
		getFriends(p)
		)
		println(pretty(render(json)))

	def getFriends(p: Person) = {
	("friends" ->
	 p.friends.map { f =>
	 ("name" -> f.name) ~
	 getAddress(f.address)
	 })
	 }

	def getAddress(a: Address) = {
		 ("address" ->
		 ("city" -> a.city) ~
		 ("state" -> a.state))
	 }
	}

	case class Person(name: String, address: Address) {
		var friends = List[Person]()
	}
	case class Address(city: String, state: String)


	 //15.3. Creating a Simple Scala Object from a JSON String

	 import net.liftweb.json._
	 // a case class to represent a mail server
	 case class MailServer(url: String, username: String, password: String)
	 object JsonParsingExample extends App {
	  implicit val formats = DefaultFormats
	  // simulate a json string
	  val jsonString = """
	 {
	 "url": "imap.yahoo.com",
	 "username": "myusername",
	 "password": "mypassword"
	 }
	 """
	  // convert a String to a JValue object
	  val jValue = parse(jsonString)
	  // create a MailServer object from the string
	  val mailServer = jValue.extract[MailServer]
	  println(mailServer.url)
	  println(mailServer.username)
	  println(mailServer.password)
	 }

	 //15.4. Parsing JSON Data into an Array of Objects
	 import net.liftweb.json.DefaultFormats
	 import net.liftweb.json._
	 // a case class to match the JSON data
	 case class EmailAccount(
	  accountName: String,
	  url: String,
	  username: String,
	  password: String,
	  minutesBetweenChecks: Int,
	  usersOfInterest: List[String]
	 )
	 object ParseJsonArray extends App {
	  implicit val formats = DefaultFormats
	  // a JSON string that represents a list of EmailAccount instances
	  val jsonString ="""
	 {
	 "accounts": [
	 { "emailAccount": {
	 "accountName": "YMail",
	 "username": "USERNAME",
	 "password": "PASSWORD",
	 "url": "imap.yahoo.com",
	 "minutesBetweenChecks": 1,
	 "usersOfInterest": ["barney", "betty", "wilma"]
	 }},
	 { "emailAccount": {
	 "accountName": "Gmail",
	 "username": "USER",
	 "password": "PASS",
	 "url": "imap.gmail.com",
	 "minutesBetweenChecks": 1,
	 "usersOfInterest": ["pebbles", "bam-bam"]
	 }}
	 ]
	 }
	 """
	  // json is a JValue instance
	  val json = parse(jsonString)
	  val elements = (json \\ "emailAccount").children
	  for (acct <- elements) {
	  val m = acct.extract[EmailAccount]
	  println(s"Account: ${m.url}, ${m.username}, ${m.password}")
	  println(" Users: " + m.usersOfInterest.mkString(","))
	  }
	 }

	 //15.5. Creating Web Services with Scalatra
	 /*g8 scalatra/scalatra-sbt
	 > container:start
	 ~ ;copy-resources;aux-compile*/
	 package com.alvinalexander.app
	 import org.scalatra._
	 import scalate.ScalateSupport
	 class MyScalatraServlet extends MyScalatraWebAppStack {
	  get("/") {
	  <html>
	  <body>
	  <h1>Hello, world!</h1>
	  Say <a href="hello-scalate">hello to Scalate</a>.
	  </body>
	   </html>
	   }
	  }

	  //Add a new service
	  get("/hello") {
	   <p>Hello, world!</p>
	  }

	//15.6. Replacing XML Servlet Mappings with Scalatra Mounts
	/*Simply create a boilerplate web.xml file like
	this in the src/main/webapp/WEB-INF directory:*/
	// <?xml version="1.0" encoding="UTF-8"?>
	// <web-app xmlns="http://java.sun.com/xml/ns/javaee"
	//  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
	//  xsi:schemaLocation="http://java.sun.com/xml/ns/javaee ↵
	// htmltp://java.sun.com/xml/ns/javaee/web-app_3_0.xsd"
	//  version="3.0">
	//  <listener>
	//  <listener-class>org.scalatra.servlet.ScalatraListener</listener-class>
	//  </listener>
	//  <servlet-mapping>
	//  <servlet-name>default</servlet-name>
	//  <url-pattern>/img/*</url-pattern>
	//  <url-pattern>/css/*</url-pattern>
	//  <url-pattern>/js/*</url-pattern>
	//  <url-pattern>/assets/*</url-pattern>
	//  </servlet-mapping>
	// </web-app>


	import org.scalatra._
	import javax.servlet.ServletContext
	import com.alvinalexander.app._
	class ScalatraBootstrap extends LifeCycle {
	 override def init(context: ServletContext) {
	 // created by default
	 context.mount(new MyScalatraServlet, "/*")
	 // new
	 context.mount(new StockServlet, "/stocks/*")
	 context.mount(new BondServlet, "/bonds/*")
		}
	}

	/*The two new context.mount lines shown tell Scalatra that a class named StockServlet
	should handle all URI requests that begin with /stocks/, and another class named
	BondServlet should handle all URI requests that begin with /bonds/.
	Next, create a file named src/main/scala/com/alvinalexander/app/OtherServlets.scala to
	define the StockServlet and BondServlet classes:*/

	package com.alvinalexander.app
	import org.scalatra._
	import scalate.ScalateSupport
	class StockServlet extends MyScalatraWebAppStack {
	 get("/") {
	 <p>Hello from StockServlet</p>
	 }
	}
	class BondServlet extends MyScalatraWebAppStack {
	 get("/") {
	 <p>Hello from BondServlet</p>
	 }
	}


	//15.7. Accessing Scalatra Web Service GET Parameters

	/*If you want to let parameters be passed into your Scalatra servlet with a URI that uses
	traditional ? and & characters to separate data elements, like this:
	http://localhost:8080/saveName?fname=Alvin&lname=Alexander
	you can access them through the implicit params variable in a get method:*/

	/**
	* The URL
	* http://localhost:8080/saveName?fname=Alvin&lname=Alexander
	* prints: Some(Alvin), Some(Alexander)
	*/
	get("/saveName") {
	 val firstName = params.get("fname")
	 val lastName = params.get("lname")
	 <p>{firstName}, {lastName}</p>
	}

	/*However, Scalatra also lets you use a “named parameters” approach, which can be more
	convenient, and also documents the parameters your method expects to receive. Using
	this approach, callers can access a URL like this:
	http://localhost:8080/hello/Alvin/Alexander
	You can handle these parameters in a get method like this:*/
	get("/hello/:fname/:lname") {
	 <p>Hello, {params("fname")}, {params("lname")}</p>
	}

	/*With this approach, you can use wildcard characters for other needs, such as when a
	client needs to pass in a filename path, where you won’t know the depth of the path
	beforehand:*/

	get("/getFilename/*.*") {
	 val data = multiParams("splat")
	 <p>{data.mkString("[", ", ", "]")}</p>
	}

	//15.8. Accessing POST Request Data with Scalatra

	/*To handle a POST request, write a post method in your Scalatra servlet, specifying the
	URI the method should listen at:*/

	post("/saveJsonStock") {
	 val jsonString = request.body
	 // deserialize the JSON ...
	}

	package com.alvinalexander.app
	import org.scalatra._
	import scalate.ScalateSupport
	import net.liftweb.json._
	class StockServlet extends MyScalatraWebAppStack {
	    /**
		* Expects an incoming JSON string like this:
		* {"symbol":"GOOG","price":"600.00"}
		*/
		 post("/saveJsonStock") {
		 // get the POST request data
		 val jsonString = request.body
		 // needed for Lift-JSON
		 implicit val formats = DefaultFormats
		 // convert the JSON string to a JValue object
		 val jValue = parse(jsonString)
		 // deserialize the string into a Stock object
		 val stock = jValue.extract[Stock]
		 // for debugging
		 println(stock)
		 // you can send information back to the client
		 // in the response header
		 response.addHeader("ACK", "GOT IT")

		 }
	}

	// a simple Stock class
	class Stock (var symbol: String, var price: Double) {
	 override def toString = symbol + ", " + price
	}

	/*The last step to get this working is to add the Lift-JSON dependency to your project.
	Assuming that you created your project as an SBT project as shown in Recipe 15.1, add
	this dependency to the libraryDependencies declared in the project/build.scala file in
	your project:*/

	"net.liftweb" %% "lift-json" % "2.5+"

	import net.liftweb.json._
	import net.liftweb.json.Serialization.write
	import org.apache.http.client.methods.HttpPost
	import org.apache.http.entity.StringEntity
	import org.apache.http.impl.client.DefaultHttpClient
	object PostTester extends App {
	 // create a Stock and convert it to a JSON string
	 val stock = new Stock("AAPL", 500.00)
	 implicit val formats = DefaultFormats
	 val stockAsJsonString = write(stock)
	 // add the JSON string as a StringEntity to a POST request
	 val post = new HttpPost("http://localhost:8080/stocks/saveJsonStock")
	 post.setHeader("Content-type", "application/json")
	 post.setEntity(new StringEntity(stockAsJsonString))
	 // send the POST request
	 val response = (new DefaultHttpClient).execute(post)
	 // print the response
	 println("--- HEADERS ---")
	 response.getAllHeaders.foreach(arg => println(arg))
	}

	class Stock (var symbol: String, var price: Double)

	//15.9. Creating a Simple GET Request Client
	/*• A simple use of the scala.io.Source.fromURL method
	• Adding a timeout wrapper around scala.io.Source.fromURL to make it more
	robust
	• Using the Apache HttpClient library*/

	/**
	* Returns the text (content) from a URL as a String.
	* Warning: This method does not time out when the service is non-responsive.
	*/
	@throws(classOf[java.io.IOException])
	def get(url: String) = scala.io.Source.fromURL(url).mkString



	/**
	* Returns the text (content) from a REST URL as a String.
	* Inspired by http://matthewkwong.blogspot.com/2009/09/↵
	scala-scalaiosource-fromurl-blockshangs.html
	* and http://alvinalexander.com/blog/post/java/how-open-url-↵
	read-contents-httpurl-connection-java
	*
	* The `connectTimeout` and `readTimeout` comes from the Java URLConnection
	* class Javadoc.
	* @param url The full URL to connect to.
	* @param connectTimeout Sets a specified timeout value, in milliseconds,
	* to be used when opening a communications link to the resource referenced
	* by this URLConnection. If the timeout expires before the connection can
	* be established, a java.net.SocketTimeoutException
	* is raised. A timeout of zero is interpreted as an infinite timeout.
	* Defaults to 5000 ms.
	* @param readTimeout If the timeout expires before there is data available
	* for read, a java.net.SocketTimeoutException is raised. A timeout of zero
	* is interpreted as an infinite timeout. Defaults to 5000 ms.
	* @param requestMethod Defaults to "GET". (Other methods have not been tested.)
	*
	* @example get("http://www.example.com/getInfo")
	* @example get("http://www.example.com/getInfo", 5000)
	* @example get("http://www.example.com/getInfo", 5000, 5000)
	*/
	@throws(classOf[java.io.IOException])
	@throws(classOf[java.net.SocketTimeoutException])
	def get(url: String,
	 connectTimeout:Int =5000,
	 readTimeout:Int =5000,
	 requestMethod: String = "GET") = {
	 import java.net.{URL, HttpURLConnection}
	 val connection = (new URL(url)).openConnection.asInstanceOf[HttpURLConnection]
	 connection.setConnectTimeout(connectTimeout)
	 connection.setReadTimeout(readTimeout)


	 connection.setRequestMethod(requestMethod)
	  val inputStream = connection.getInputStream
	  val content = io.Source.fromInputStream(inputStream).mkString
	  if (inputStream != null) inputStream.close
	  content
	 }
	 As the Scaladoc shows, this method can be called in a variety of ways, including this:
	 try {
	  val content = get("http://localhost:8080/waitForever")
	  println(content)
	 } catch {
	  case ioe: java.io.IOException => // handle this
	  case ste: java.net.SocketTimeoutException => // handle this
	 }

	 /*Using the Apache HttpClient
	 Another approach you can take is to use the Apache HttpClient library. Before I learned
	 about the previous approaches, I wrote a getRestContent method using this library
	 like this:*/
	 import java.io._
	 import org.apache.http.{HttpEntity, HttpResponse}
	 import org.apache.http.client._
	 import org.apache.http.client.methods.HttpGet
	 import org.apache.http.impl.client.DefaultHttpClient
	 import scala.collection.mutable.StringBuilder
	 import scala.xml.XML
	 import org.apache.http.params.HttpConnectionParams
	 import org.apache.http.params.HttpParams
	 /**
	 * Returns the text (content) from a REST URL as a String.
	 * Returns a blank String if there was a problem.
	 * This function will also throw exceptions if there are problems trying
	 * to connect to the url.
	 *
	 * @param url A complete URL, such as "http://foo.com/bar"
	 * @param connectionTimeout The connection timeout, in ms.
	 * @param socketTimeout The socket timeout, in ms.
	 */
	 def getRestContent(url: String,
	  connectionTimeout: Int,
	  socketTimeout: Int): String = {
	  val httpClient = buildHttpClient(connectionTimeout, socketTimeout)
	  val httpResponse = httpClient.execute(new HttpGet(url))
	  val entity = httpResponse.getEntity
	  var content = ""

	  if (entity != null) {
	  val inputStream = entity.getContent
	  content = io.Source.fromInputStream(inputStream).getLines.mkString
	  inputStream.close
	  }
	  httpClient.getConnectionManager.shutdown
	  content
	 }
	 private def buildHttpClient(connectionTimeout: Int, socketTimeout: Int):
	 DefaultHttpClient = {
	  val httpClient = new DefaultHttpClient
	  val httpParams = httpClient.getParams
	  HttpConnectionParams.setConnectionTimeout(httpParams, connectionTimeout)
	  HttpConnectionParams.setSoTimeout(httpParams, socketTimeout)
	  httpClient.setParams(httpParams)
	  httpClient
	 }

	//15.10. Sending JSON Data to a POST URL
	 import java.io._
	 import org.apache.commons._
	 import org.apache.http._
	 import org.apache.http.client._
	 import org.apache.http.client.methods.HttpPost
	 import org.apache.http.impl.client.DefaultHttpClient
	 import java.util.ArrayList
	 import org.apache.http.message.BasicNameValuePair
	 import org.apache.http.client.entity.UrlEncodedFormEntity
	 import com.google.gson.Gson
	 case class Person(firstName: String, lastName: String, age: Int)
		 object HttpJsonPostTest extends App {
		  // create our object as a json string
		  val spock = new Person("Leonard", "Nimoy", 82)
		  val spockAsJson = new Gson().toJson(spock)
		  // add name value pairs to a post object
		  val post = new HttpPost("http://localhost:8080/posttest")
		  val nameValuePairs = new ArrayList[NameValuePair]()
		  nameValuePairs.add(new BasicNameValuePair("JSON", spockAsJson))
		  post.setEntity(new UrlEncodedFormEntity(nameValuePairs))
		  // send the post request
		  val client = new DefaultHttpClient
		  val response = client.execute(post)
		  println("--- HEADERS ---")
		  response.getAllHeaders.foreach(arg => println(arg))


		  //15.11. Getting URL Headers
		  import org.apache.http.client.methods.HttpGet
		  import org.apache.http.impl.client.DefaultHttpClient
		  object FetchUrlHeaders extends App {
		   val get = new HttpGet("http://alvinalexander.com/")
		   val client = new DefaultHttpClient
		   val response = client.execute(get)
		   response.getAllHeaders.foreach(header => println(header))
		  }

		  //15.12. Setting URL Headers When Sending a Request
		  import org.apache.http.client.methods.HttpGet
		  import org.apache.http.impl.client.DefaultHttpClient
		  object SetUrlHeaders extends App {
		   val url = "http://localhost:9001/baz"
		   val httpGet = new HttpGet(url)
		   // set the desired header values
		   httpGet.setHeader("KEY1", "VALUE1")
		   httpGet.setHeader("KEY2", "VALUE2")
		   // execute the request
		   val client = new DefaultHttpClient
		   client.execute(httpGet)
		    client.getConnectionManager.shutdown
		   }

		  //15.13. Creating GET Request Web Service with the Play Framework
		  //play new WebServiceDemo
		  /*assume that you want to create a web service to return
		    an instance of a Stock when a client makes a GET request at the /getStock URI.
			To do this, first add this line to your	conf/routes file:*/
		  //GET /getStock controllers.Application.getStock
		  /*Next, create a method named getStock in the default Application controller
		  (apps/controllers/Application.scala), and have it return a JSON representation of a Stock
		  object:*/
		  package controllers
		  import play.api._
		  import play.api.mvc._
		  import play.api.libs.json._
		  import models.Stock
		  object Application extends Controller {
		   def index = Action {
		   Ok(views.html.index("Your new application is ready."))
		   }
		   def getStock = Action {
		   val stock = Stock("GOOG", 650.0)
		   Ok(Json.toJson(stock))
		   }
		  }
		  /*That code uses the Play Json.toJson method. Although the code looks like you can
		  create Stock as a simple case class, attempting to use only a case class will result in this
		  error when you access the /getStock URI:
		  No Json deserializer found for type models.Stock. Try to implement an implicit Writes
		  or Format for this type.
		  To get this controller code to work, you need to create an instance of a Format object to
		  convert between the Stock model object and its JSON representation. To do this, create
		  a model file named Stock.scala in the app/models directory of your project. (Create the
		  directory if it doesn’t exist.)


		  In that file, define the Stock case class, and then implement a
		  play.api.libs.json.Format object. In that object, define a reads method to convert
		  from a JSON string to a Stock object and a writes method to convert from a Stock
		  object to a JSON string
		  */

		 package models
		 case class Stock(symbol: String, price: Double)
		 object Stock {
		  import play.api.libs.json._
		  implicit object StockFormat extends Format[Stock] {
		  // convert from JSON string to a Stock object (de-serializing from JSON)
		  def reads(json: JsValue): JsResult[Stock] = {
		  val symbol = (json \ "symbol").as[String]
		  val price = (json \ "price").as[Double]
		  JsSuccess(Stock(symbol, price))
		  }
		  // convert from Stock object to JSON (serializing to JSON)
		  def writes(s: Stock): JsValue = {
		  // JsObject requires Seq[(String, play.api.libs.json.JsValue)]
		  val stockAsList = Seq("symbol" -> JsString(s.symbol),
		  "price" -> JsNumber(s.price))
		  JsObject(stockAsList)
		  }
		  }
		 }

		 //You should see this result in the browser:
		 //{"symbol":"GOOG","price":650.0}

		 //15.14 POSTing JSON Data to a Play Framework Web Service
		 /*Whereas the previous recipe used the writes method of the Format object in the model,
		 this recipe uses the reads method. When JSON data is received in a POST request, the
		 reads method is used to convert from the JSON string that’s received to a Stock object.
		 Here’s the code for the reads method:*/
		 import play.api._
		 import play.api.mvc._
		 object Application extends Controller {
		  import play.api.libs.json.Json
		  def saveStock = Action { request =>
		  val json = request.body.asJson.get
		  val stock = json.as[Stock]
		  println(stock)
		  Ok
		  }
		 }

		 /*The saveStock method gets the JSON data sent to it from the request object, and then
		 converts it with the json.as method. The println statement in the method is used for
		 debugging purposes, and prints to the Play command line (the Play console).
		 Finally, add a route that binds a POST request to the desired URI and the saveStock
		 method in the Application controller by adding this line to the conf/routes file:
		 POST /saveStock controllers.Application.saveStock
		 If you haven’t already done so, start the Play console from within the root directory of
		 your project, and issue the run command:

		 With the Play server running, use the following Unix curl command to POST a sample
		 JSON string to your saveStock web service:
		 curl \
		  --header "Content-type: application/json" \
		  --request POST \
		  --data '{"symbol":"GOOG", "price":900.00}' \
		  http://localhost:8080/saveStock
		 If everything works properly, you should see this output in your Play console window:
		 STOCK: Stock(GOOG,900.0)

		 A few notes about the code:
		 • The request object is a play.api.mvc.AnyContent object.
		 • The request.body is also a play.api.mvc.AnyContent object.
		 • The request.body.asJson returns an instance of the following:
		 Option[play.api.libs.json.JsValue].
		 • request.body.asJson.get returns a JsValue.*/


}
