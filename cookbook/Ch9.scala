object Ch9 extends App {
	
	//**************9.1****************//
	
	//function literals
	val x = List.range(1, 10)
	//val evens = x.filter((i: Int) => 1 % 2 == 0)
	//aka anonymous function
	//val evens = x.filter(i => i % 2 == 0)
	val evens = x.filter(_ % 2 == 0)
	//only one param
	//x.foreach(i: Int) => println(i)
	//x.foreach((i) => println(i))
	//x.foreach(i => println(i))
	//x.foreach(println(_))
	x.foreach(println)

	//**************9.2****************//
	
	val double = (i :Int) => { i * 2 }
	println(double(2))
	println(double(3))
	val list = List.range(1, 5)
	println(list.map(double))

	val f = (i: Int) => { i % 2 == 0 }
	//val f: Int => Boolean = i => { i % 2 == 0 }
	//val f: Int => Boolean = i =>  i % 2 == 0 
	//val f: Int => Boolean = _ % 2 == 0 

	//implicit approach
	//val add = (x: Int, y: Int) => { x + y }
	//val add = (x: Int, y: Int) => x + y

	//explicit approach
	//val add: (Int, Int) => Int = (x, y) => { x + y }
	val add: (Int, Int) => Int = (x,y) => x + y
	val addThenDouble: (Int, Int) => Int = (x, y) => {
		val a = x + y
		2 * a
	}

	//using a method like an anonymous function
	def modMethod(i: Int) = i % 2 == 0
	//def modMethod(i: Int) = { i % 2 == 0 }
	//def modMethod(i: Int): Boolean = i % 2 == 0
	//def modMethod(i: Int): Boolean = { i % 2 == 0 }

	val alist = List.range(1, 10)
	println(alist.filter{modMethod})

	val modFunction = (i: Int) => i % 2 == 0
	println(alist.filter{modMethod})
	
	//assigning an existing function/method to a function variable
	val c = scala.math.cos _
	//val c = scala.math.cos(_)
	println(c(0))
	val p = scala.math.pow(_ , _)
	p(scala.math.E, 2)
	
	//**************9.3****************//

	//defining a method that accepts a simple function
	def executeFunction(callback:() => Unit) = {
		callback()
	}

	val sayHello = () => println("hello")
	println(executeFunction(sayHello))

	/*def excecuteFUnction(f:() => Unit) = {
		f()
	}

	f:() => Unit*/

	//defining a function as a method parameter
	//parameterName: (parameterType(s)) => returnType

	/*executeFunction(f:() => Unit)
	executeFunction(f:String => Int)
	executeFunction(f:(String) => Int)*/


	//**************9.4****************//
	
	//more complex functions

	def exec(callback: Int => Unit) = {
		callback(1)
	}

	val plusOne = (i: Int) => { println(i+1) }
	exec(plusOne)
	val plusTen = (i: Int) => { println(i+10) }
	exec(plusTen)

	/*executeFunction(f: (String) => Int)
	executeFunction(f:(Int, Int) => Boolean)*/
	//exec(f:(String, Int, Double) => Seq[String])

	//exec(f:(Int) => Unit)
	//exec(f:Int => Unit)

	//passing in a function with other parameters
	//val sayHello = () => println("hello")

	def executeXTimes(callback:() => Unit, numTimes: Int) = {
		for( i <- 1 to numTimes) callback()
	}

	executeXTimes(sayHello, 3)

	def executeAndPrint(f:(Int, Int) => Int, x: Int, y: Int) = {
		val result = f(x, y)
		println(result)
	}

	val sum = (x: Int, y: Int) => x + y
	val multiply = (x: Int, y: Int) => x * y

	executeAndPrint(sum, 2, 9)
	executeAndPrint(multiply, 3, 9)

	/*// 1 - define the method
	def exec(callback: (Any, Any) => Unit, x: Any, y: Any) = {
	  callback(x, y)
	}

	// 2 - define a function to pass in
	val printTwoThings =(a:Any, b:Any) => {  
		println(a)  
		println(b)
	}
	
	// 3 - pass the function and some parameters to the method
	case class Person(name: String) 
	exec(printTwoThings, "Hello", Person("Dave"))

	
	// 2a - define a method to pass in
	def printTwoThings (a:Any, b:Any) {
	  println(a)
	  println(b)
	 }

	 // 3a - pass the printTwoThings method to the exec method
	case class Person(name: String)
	exec(printTwoThings, "Hello", Person("Dave"))*/


	//**************9.5****************//

	//using closures
	/*package otherscope {
  	
  	class Foo {
	    // a method that takes a function and a string, and passes the string into
	    // the function, and then executes the function
	    def exec(f:(String) => Unit, name: String) {
	      f(name)
		}
	 }
	}

   object ClosureExample {
	  var hello = "Hello"
	  def sayHello(name: String) = { println(s"$hello, $name") }
	  // execute sayHello from the exec method foo
	  val foo = new otherscope.Foo
	  foo.exec(sayHello, "Al")
	  // change the local variable 'hello', then execute sayHello from
	  // the exec method of foo, and see what happens
	  hello = "Hola"
	  foo.exec(sayHello, "Lorenzo")
	}
	*/

	//second example

	/*val isOfVotingAge = (age: Int) => age >= 18
	println(isOfVotingAge(16))
	println(isOfVotingAge(20))*/

	var votingAge = 18
	val isOfVotingAge = (age :Int) => age >= votingAge

	println(isOfVotingAge(16))
	println(isOfVotingAge(20))

	def printResult(f: Int => Boolean, x: Int) = {
		println(f(x))
	}

	printResult(isOfVotingAge, 20)

	votingAge = 21
	printResult(isOfVotingAge, 20)

	//using closures with other data types

	import scala.collection.mutable.ArrayBuffer
	val fruits = ArrayBuffer("apple")

	//the function addToBasket has a reference to fruits
	val addToBasket = (s: String) => {
		fruits += s
		println(fruits.mkString(", "))
	}

	def buyStuff(f: String => Unit, s: String) = {
		f(s)
	}

	buyStuff(addToBasket, "cherries")
	buyStuff(addToBasket, "grapes")

	//**************9.6****************//


	//partially applied functions
	val summ = (a: Int, b: Int, c: Int) => a + b + c
	val ff = summ(1, 2, _:Int)
	println(ff(3))

	def wrap(prefix: String, html: String, suffix: String) = {
		prefix + html + suffix
	}

	val wrapWithDiv = wrap("<div", _: String, "</div>")
	println(wrapWithDiv("<p>Hello, world</p>"))
	println(wrapWithDiv("<img src=\"/images/foo.png\"/>"))
	wrap("<pref>", "val x = 1", "</pre>")

	//**************9.7****************//

	//creating a function that returns a function
	//(s: String) => { prefix + " " + s}
	def saySomething(prefix: String) = (s: String) => {
		prefix + " " + s
	}

	val sayHelloo = saySomething("hello")
	println(sayHelloo("Al"))

	def greeting(language: String) = (name: String) => {
		language match {
			case "english" => "hello" + name
			case "spanish" => "buenos dias" + name
		}
	}

	def greetingg(language: String) = (name: String) => {
		val english = () => "hello" + name
		val spanish = () => "buenos dias" + name
		language match {
			case "english" => {
								  println("returning english function")
								  english()
							  }
			case "spanish" => {
							  	 println("returning spanish function")
							  	 spanish
							  }
		}
	}

	val helloo = greeting("english")
	val buenosDias = greeting("spanish")

	helloo("Al")
	buenosDias("Lorenzo")


	//**************9.8****************//
	
	//val divide = (x: Int) => 42 / x
	//println(divide(0))

	val divide = new PartialFunction[Int, Int]{
		def apply(x: Int) = 42 / x
		def isDefinedAt(x: Int) = x != 0
	}

	println(divide.isDefinedAt(1))
	if (divide.isDefinedAt(1)) divide(1)
	println(divide.isDefinedAt(0))

	val divide2: PartialFunction[Int, Int] = {
		case d: Int if d != 0 => 42 / d
	}

	println(divide2.isDefinedAt(0))
	println(divide2.isDefinedAt(1))

	//The signature of the PartialFunction trait looks like this:
	//trait PartialFunction[-A, +B] extends (A) => B
	/*As discussed in other recipes, the => symbol can be thought of 
	as a transformer, and in this case, the (A) => B can be interpreted 
	as a function that transforms a type A into a resulting type B.*/

	val convertToLowNumToString = new PartialFunction[Int, String]{
		val nums = Array("one", "two", "three", "four", "five")
		def apply(i: Int) = nums(i-1)
		def isDefinedAt(i: Int) = i > 0 && i < 6
	}

	//orElse and andThen
	// converts 1 to "one", etc., up to 5
	val convert1to5=new PartialFunction[Int, String] {
	val nums =Array("one", "two", "three", "four", "five")
	def apply(i:Int) = nums(i-1)
	def isDefinedAt(i:Int) = i > 0 && i < 6
	}

	// converts 6 to "six", etc., up to 10
	val convert6to10=new PartialFunction[Int, String] {
	val nums =Array("six", "seven", "eight", "nine", "ten")
	def apply(i:Int) = nums(i-6)
	def isDefinedAt(i:Int) = i > 5 && i < 11
	}

	val handle1to10 = convert1to5.orElse(convert6to10)
	println(handle1to10(3))
	println(handle1to10(8))

	val divide22:PartialFunction[Int, Int] = 	{
		case d:Int if d != 0 => 42 / d
	}

	println(List(0,1,2).collect { divide22 })
	
	println(List(42, "cat").collect { case i: Int => i + 1 })

	val sample = 1 to 5


	val isEven: PartialFunction[Int, String] = {
		case x if x % 2 == 0 => s"$x  is even"
	}

	val evenNumbers = sample.collect(isEven)

	println(evenNumbers)

	val isOdd: PartialFunction[Int, String] = {
		case x if x % 2 == 1 => s"$x is odd"
	}

	val numbers = sample.map(isEven.orElse(isOdd))

	println(numbers)

}