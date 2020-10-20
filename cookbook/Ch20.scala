object Ch20 extends App {

	//20 idioms
	//don't compile

	//**************20.1****************//
	//Create Methods with No Side Effects (Pure Functions)

	/*An  expression  is  referentially	transparent (RT) if it can be replaced
	by its resulting value without changing the behavior of the program.
	This must be true regardless of where the expression is used in	the program.
	For  instance, assume that x and y are immutable variables within some scope of
	an application, and within that scope they’re used to form this expression:
	x + y You can assign this expression to a third variable, like this: val z = x + y
	*/

	//A pure function is given one or more input parameters.
	//Its result is based solely off of those parameters and its algorithm.
	//The algorithm will not be based on any hidden state in the class
	//or object it’s contained in. It won’t mutate the parameters it’s given.
	//It won’t mutate the state of its class or object.It doesn’t perform any I/O operations,
	//such as reading from disk, writing to disk, prompting for input, or reading input

	//a poorly written class
	class Stock(var symbol:String, var company:String,var price:BigDecimal, var volume:Long) {
		var html:String = _
		def buildUrl(stockSymbol:String):String = { ??? }
		def getUrlContent(url:String):String = { ??? }
		def setPriceFromHtml(html:String) {
			this.price= ??? }
		def setVolumeFromHtml(html:String) {
			this.volume= ??? }
		def setHighFromHtml(html:String) {
			this.high = ??? }
		def setLowFromHtml(html:String) {
			this.low = ??? }

		// some dao-like functionality
		private val _history:ArrayBuffer[Stock] = { ??? }
		val getHistory =_history
	}

	//fixing it
	case class Stock(symbol:String, company:String)

	case class StockInstance(symbol:String,datetime:String,price:BigDecimal,volume:Long)

	object NetworkUtils {
	def getUrlContent(url:String):String = { ??? }
	}

	object StockUtils {
		def buildUrl(stockSymbol:String):String = { ??? }
		def getPrice(symbol:String, html:String):String = { ??? }
		def getVolume(symbol:String, html:String):String = { ??? }
		def getHigh(symbol:String, html:String):String = { ??? }
		def getLow(symbol:String, html:String):String = { ??? }
	}

	object DateUtils {
		def currentDate:String = { ??? }
		def currentTime:String = { ??? }
	}

	val stock = Stock("AAPL", "Apple")
	val url = StockUtils.buildUrl(stock.symbol)
	val html = NetworkUtils.getUrlContent(url)

	val price = StockUtils.getPrice(html)
	val volume = StockUtils.getVolume(html)
	val high = StockUtils.getHigh(html)
	val low = StockUtils.getLow(html)
	val date = DateUtils.currentDate
	val stockInstance = StockInstance(symbol, date, price, volume, high, low)

	object StockDao {
		def getStockInstances(symbol:String):Vector[StockInstance] = { ??? }
	// other code ...
	}

	//**************20.2****************//
	//Prefer immutable objects

	/*even though the List parameter taken by the
	trustMeMuHaHa method is marked as final,
	the method can still mutate the collection

	//java
	class EvilMutator	 {
		// trust me ... mu ha ha (evil laughter)
		public static void trustMeMuHaHa(final List<Person> people) {
		    people.clear();
		}
	}
	*/

	/*
	still present
	def evilMutator(people:ArrayBuffer[Person]) {
	  people.clear()
	}

	switching to an immutable col will prevent any mutation
	def evilMutator(people:Vector[Person]) {
		// ERROR - won't compile
	  	people.clear()
	}

	*/

	/*Using val + mutable, and var + immutable
	//some developers like to use these combinations:
	A mutable collection field declared as a val is typically made
	private to its class (ormethod).
	An immutable collection field declared as a var in a class is more often
	made publicly visible, that is, it’s made available to other classes.*/

	//**************20.3****************//

	//Think “Expression-Oriented Programming”

	/*Statements  do  not  return  results  and
	are  executed  solely  for  their  side  effects,
	while expressions always return a result and
	often do not have side effects at all.*/

	//an intentionally bad example
	class Stock (var symbol:String,var company:String,
		         var price:String,var volume:String,
		         var high:String,var low:String) {
		var html:String = _
		def buildUrl(stockSymbol:String):String = { ??? }
		def getUrlContent(url:String):String = { ??? }
		def setPriceUsingHtml() { this.price = ??? }
		def setVolumeUsingHtml() { this.volume = ??? }
		def setHighUsingHtml() { this.high = ??? }
		def setLowUsingHtml() { this.low = ??? }
	}

	val stock = new Stock("GOOG", "Google", "", "", "", "")
	val url = buildUrl(stock.symbol)
	stock.html = stock.getUrlContent(url)
	//a series of calls on an object ('statements')
	stock.setPriceUsingHtml
	stock.setVolumeUsingHtml
	stock.setHighUsingHtml
	stock.setLowUsingHtml

	// a series of expressions
	val url = StockUtils.buildUrl(symbol)
	val html = NetworkUtils.getUrlContent(url)
	val price = StockUtils.getPrice(html)
	val volume = StockUtils.getVolume(html)
	val high = StockUtils.getHigh(html)
	val low = StockUtils.getLow(html)
	val date = DateUtils.getDate
	val stockInstance = StockInstance(symbol, date, price, volume, high, low)



	//**************20.4****************//

	//Use Match Expressions and Pattern Matching
	val month = i match {
		case 1  => "January"
		case 2  => "February"// more months here ...
		case 11 => "November"
		case 12 => "December"
		case _ => "Invalid month"
		//the default, catch-all
	}
	i match {
		case
		 1 | 3 | 5 | 7 | 9 => println("odd")
		case
		 2 | 4 | 6 | 8 | 10 => println("even")
	}

	def readTextFile(filename:String):Option[List[String]] = {
		try {
			Some(Source.fromFile(filename).getLines.toList)
		  } catch {
			case e:Exception => None
		  }
	}

	def readTextFile(filename:String):Option[List[String]] = {
		try {
			Some(Source.fromFile(filename).getLines.toList)
		}
		catch{
			case ioe:IOException =>logger.error(ioe); None
			case fnf:FileNotFoundException =>logger.error(fnf); None
		}
	}

	def getClassAsString(x:Any):String = x match {
		case s:String => "String"
		case i:Int => "Int"
		case l:List[_] => "List"
		case p:Person => "Person"
		case Dog() => "That was a Dog"
		case Parrot(name) => s"That was a Parrot, name = $name"
		case _ => "Unknown"
	}

	val divide:PartialFunction[Int, Int] = {
		case d:Int if d!= 0 => 42 / d
	} //apply

	//use with Option/Some/None
	def toInt(s:String):Option[Int] = {
		try {
			Some(s.toInt)
  		} catch {
  			case e:Exception => None  }
  	}

  	toInt(aString) match {
  		case Some(i) => println(i)
  		case None=> println("Error: Could not convert String to Int.")
	}



	//**************20.5****************//

	//Eliminate null Values from Your Code
	/*David Pollak, author of the book
	Beginning Scala, offers a wonderfully simple rule about
	null values: “Ban null from any of your code. Period.”

	There are several common situations where you may be tempted
	to	use	null values, so this recipe demonstrates how not
	to use null values in those situations:
	When a var field in a class or method doesn’t have
	an initial default value, initialize it with Option instead of null

	When a method doesn’t produce the intended result,
	you may be tempted to return
	null. Use an Option or Try instead.

	If you’re working with a Java library that returns null,
	convert it to an Option, or something else.*/

	//Initialize var fields with Option, not null
	case class Address(city:String, state:String, zip:String)
	class User(email:String, password:String){
		var firstName: String = _
		var lastName: String = _
		var address: String = _
	}

	//better define these fields as Option
	case class Address(city:String, state:String, zip:String)
	class User(email:String, password:String){
		var firstName = None: Option[String]
		var lastName = None: Option[String]
		var address = None: Option[Address]
	}

	val u = new User("al@example.com", "secret")
	u.firstName = Some("Al")
	u.lastName = Some("Alexander")
	u.address = Some(Address("Talkeetna", "AK", "99676"))

	println(firstName.getOrElse("<not assigned>"))
	u.address.foreach{a =>
		println(a.city)
		println(a.state)
		println(a.zip)
	}

	//you should also use an Option in a constructor when a field is optional
	case class Stock(id: Long,
	                 var symbol: String,
	             	 var company: Option[String])

	//don't return null from methods
	//if you need to know about an error that may have occurred in the method,
	// use Try instead of Option
	def readTextFile(filename:String):Option[List[String]] = {
		try {
			Some(io.Source.fromFile(filename).getLines.toList)
		  }
		catch {
			case e:Exception => None
		  }
	}

	import scala.util.{Try, Success, Failure}
	object Test extends App {
		def readTextFile(filename:String): Try[List[String]] = {
			Try(io.Source.fromFile(filename).getLines.toList)
		}
		val filename = "/etc/passwd"
		readTextFile(filename) match {
			case Success(lines) => lines.foreach(println)
			case Failure(f) => println(f)
		}
	}

	/*As a word of caution (and balance), the Twitter
	Effective Scala page recommends not overusing
	Option, and using the Null Object Pattern
 	where it makes sense. */

 	trait Animal {
  	  def makeSound()
	}

	class Dog extends Animal {
	  def makeSound() { println("woof") }
	}

	class NullAnimal extends Animal {
	  def makeSound() {}
	}

	/*
	The makeSound method in the NullAnimal class has a neutral,
  	“do nothing” behavior. Using this approach, a  method defined
  	to return an Animal can return NullAnimal rather than null
    */

    /*
    //converting a null into an Option, or something else
    For instance, the following getName method converts a result from a Java method that
	may be null and returns an Option[String] instead:
    */
   	def getName:Option[String] = {
   		var name = javaPerson.getName
   		if (name == null) None else Some(name)
	}

	/*
	Adding an Option[T]	return type declaration to a method is a terrific way
	to indicate	that something is happening in the method such that the caller
	may receive a None instead of a Some[T]. This is a much better approach
	than returning null from a method that is expected to return an object.
	 */

	//**************20.6****************//
	//using the Option/Some/None pattern
	/*
	Returning an Option from a method
	Getting the value from an Option
	Using Option with collections
	Using Option with frameworks
	Using  Try/Success/Failure
	when  you  need  the  error  message  (Scala  2.10  and
	newer)
	Using Either/Left/Right
	when you need the error message (pre-Scala 2.10
    */

	//returning an Option from a method

	def toInt(s:String):Option[Int] = {
		try {
			Some(Integer.parseInt(s.trim))
		}
		catch {
			case e:Exception => None
		}
	}

	val x = toInt("1").getOrElse(0)
	toInt("1").foreach{ i =>  println(s"Got an int: $i")}

	toInt("1") match {
		case Some(i) => println(i)
		case None => println("That didn't work.")
	}

	//using optioms with scala cols
	val bag = List("1","2","foo","3","bar")
	bag.map{toInt}
	bag.map{toInt}.flatten
	bag.flatMap{toInt}
	bag.map{toInt}.collect{case Some(i) => i}

	/*
	That  example  works  because  the
	collect method  takes  a  partial  function,  and  the
	anonymous function that’s passed in is only defined for
	Some values; it ignores the None values
	*/

	//using option with other frameworks
	def getAll() :List[Stock] = {
		DB.withConnection { implicit connection=>sqlQuery().collect {
		// the 'company' field has a value
		case Row(id:Int, symbol:String, Some(company:String)) => Stock(id, symbol, Some(company))
		// the 'company' field does not have a value
		case Row(id:Int, symbol:String, None) => Stock(id, symbol, None)}.toList
		}
	}


	verifying("If age is given, it must be greater than zero",
			  model=>  model.age match {
			  	case Some(age) => age < 0 case None=>true
			 })

	import scala.util.control.Exception._
	def readTextFile(f: String): Option[List[String]] =  allCatch.opt(Source.fromFile(f).getLines.toList)

	//using Try, Success, and Failure
	/*
	The result of a computation wrapped in a
	Try will be one of its subclasses: Success
	or Failure. If the computation succeeds, a
	Success instance is returned; if an exception
	was thrown, a Failure will be returned, and the
	Failure will hold information about what failed.
	*/
	import scala.util.{Try,Success,Failure}
	def divideXByY(x:Int, y:Int):Try[Int] = {
		Try{(x / y)}
	}


	/*
	This method returns a successful result as long
	as y is not zero. hen y is zero, an ArithmeticException happens.
	However, the exception isn’t thrown out of the method;
	it’s caught by the Try, and a Failure object
	is returned from the method.
	*/

	divideXByY(1,1)
	divideXByY(1,0)

	/*As with an Option, you can access the Try result usinggetOrElse,
	a foreach method, or a match expression. If you don’t care about the error message
	and just want a result,	use getOrElse
	*/

	val x = divideXByY(1, 1).getOrElse(0)
	val y = divideXByY(1, 0).getOrElse(0)
	divideXByY(1, 1).foreach(println)
	divideXByY(1, 0).foreach(println)
	divideXByY(1, 1) match {
		case Success(i) => println(s"Success, value is: $i")
		case Failure(s) => println(s"Failed, message is: $s")
	}

	//see if Failure was returned
	if (x.isFailure) x.toString

	//can chain
	val z = for {
	  a<-Try(x.toInt)
	  b<-Try(y.toInt)
	} yield a * b

	val answer = z.getOrElse(0) * 2

	def readTextFile(filename:String):Try[List[String]]= {
		Try(Source.fromFile(filename).getLines.toList)
	}

	// if an exception happens while trying to open and read the file,
	// the Failure line in the match expression prints the error,

	//using Either, Left, and Right
	//Either is analogous to Try, Right is similar to Success,
	//and Left is similar to Failure
	def divideXByY(x:Int, y:Int):Either[String,Int] = {
		if (y == 0) Left("Dude, can't divide by 0") else Right(x / y)
	}

	/*The Right type is the type your
	method  returns  when  it  runs
	successfully  (an  Int  in  this  case),
	and  the  Left type  is typically a String,
	because that’s how the error message is returned */
	val x = divideXByY(1, 1).right.getOrElse(0)
	// returns 1
	val x = divideXByY(1, 0).right.getOrElse(0)
	// returns 0
	// prints "Answer: Dude, can't divide by 0"
	divideXByY(1, 0) match {
		case Left(s) => println("Answer: " + s)
		case Right(i) => println("Answer: " + i)
	}

	val x = divideXByY(1, 0)
	x.isLeft
	x.left

	/*a weakness of using Option, it’s that it doesn’t tell
	you why something failed; you just get a None instead of
	a Some. If you need to know why something failed, use Try
	instead of Option*/

	//Don’t use the get method with Option
	val x = toInt("5").get
	val x = toInt("foo").get
	val x = if (toInt("foo").isDefined) toInt("foo") else 0
	/*use getOrElse, a match expression, or foreach.
	(As with null values, I just imagine thatget doesn’t exist.) */

}