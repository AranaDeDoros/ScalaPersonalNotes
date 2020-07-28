object Ch5 extends App {
	
	//***************5.1*****************//	
	//object-private private package package-spec public
	/*
	//object private
	class Foo{
		private[this] def isFoo = true

		def doFoo(other: Foo){
			if(other.isFoo) {} //won't compile
		}
	}

	//private
	class Fooo{
		private def isFoo = true
		def doFoo(other:Foo){
			if(other.isFoo){}
		}
	}

	class Animal{
		private def heartBeat {}
	}

	class Dog extends Animal{
		heartBeat //won't compile
	}

	//protected scope available to subclasses
	//but not necessarily same package
	class AnimalP{
		protected def breathe {}
	}

	class DogP extends AnimalP{
		breathe
	}

	package world{
		class Animal{
			protected def breathe{}
		}

		class Jungle{
			val a = new Animal
			a.breahe //error
		}
	}

	//package scope
	//available to all members of same package
	package com.acme.coolapp.model{
		class Foo{
			private[model] def doX {}
			private def doY {}
		}

		class Bar{
			val f = new Foo
			f.doX
			f.doY //won't compile
		}
	}

	package com.acme.coolapp.model{
		class Foo{
			private[model] def doX {}
			private[coolapp] def doY {}
			private[acme] def doZ {}
		}
	}

	import com.acme.coolapp.model._
	package com.acme.coolapp.view{
		class Bar{
			val f = new Foo
			f.doX //won't compile
			f.doY
			f.doZ
		}
	}

	package com.acme.common{
		class Bar{
			val f = new Foo
			f.doX
			f.doY
			f.doZ
		}
	}

	//public
	package com.acme.coolapp.model{
		class Foo{
			def doX{}
		}
	}

	package org.xyz.bar{
		class Bar{
			val f = new com.acme.coolapp.model.Foo
			f.doX
		}
	}
	*/

	//***************5.2*****************//	

	//calling method on a superclass
	/*
	class WelcomeAcivity extends Activity{
		override def onCreate(bundle: Bundle){
			super.onCreate(bundle)
		}
	}*/

	class FourLeggedAnimal{
		def walk {println("I'm walking")}
		def run {println("I'm running")}
	}

	class Dog extends FourLeggedAnimal{
		def WalkTheRun{
			super.walk
			super.run
		}
	}


	//controlling which trait you call a method from
	trait Human{
		def hello = "the human trait"
	}

	trait Mother extends Human{
		override def hello = "mother"
	}

	trait Father extends Human{
		override def hello = "father"
	}

	class Child extends Human with Mother with Father{
		def printSuper = super.hello
		def printMother = super[Mother].hello
		def printFather = super[Father].hello
		def printHuman = super[Human].hello
	}

	val child = new Child
	println(s"child.printSuper = ${child.printSuper}")
	println(s"child.printMother = ${child.printMother}")
	println(s"child.printFather = ${child.printFather}")
	println(s"child.printHuman = ${child.printHuman}")

	//***************5.3*****************//	

	//setting default values for method parameters
	class Connection{
		def makeConnection(timeout: Int = 4000, protocol:String = "http"){
			println("timeout = %d, protocol = %s".format(timeout, protocol))
		}
	}

	val c = new Connection
	c.makeConnection() //must use empty when the method signature takes more than one param
	c.makeConnection(2000)
	c.makeConnection(3000, "https")

	c.makeConnection(timeout =10000)
	c.makeConnection(protocol ="https")
	c.makeConnection(timeout=1000, protocol="https")

	//if method mixes default value params and regular
	//put them last
	
	/*class Connection{
		//intentional
		def makeConnection(timeout: Int= 4000, protocol:String){
			println(f"timeout = %d, protocol = %s", timeout, protocol)
		}
	}*/

	class Connectionn{
		def makeConnection(timeout: Int, protocol:String = "http"){
			println("timeout = %d, protocol = %s".format(timeout, protocol))
		}
	}

	val cc = new Connectionn
	cc.makeConnection(1000)
	cc.makeConnection(1000, "https")


	//***************5.4*****************//	

	//using param names when calling a method

	class Pizza{

		var crustSize = 12
		var crustType = "Thin"

		def update(crustSize: Int, crustType:String){
			this.crustSize = crustSize
			this.crustType = crustType
		}

		override def toString = {
			"A %d inch %s crust pizza".format(crustSize, crustType)
		}
	}

	val pizza = new Pizza
	pizza.update(crustSize=16, crustType="Thick")
	pizza.update(crustType="Pan", crustSize=14)
	println(pizza)

	//***************5.5*****************//	

	//defining a method that returns multiple items(tuples)
	def getStockInfo = {
		//other code here...
		("NFLX", 100.00, 101.00) //tuple3
	}

	val (symbol, currentPrice, bidPrice) = getStockInfo
	println(symbol, currentPrice, bidPrice)


	val result = getStockInfo
	println(result._1, result._2)

	//***************5.6*****************//		

	//forcing callers to leave parentheses off accesor methods

	class Pizzaa{
		//no parentheses after crustSize
		def crustSize = 12
	}

	val pizzaa = new Pizzaa
	//p.crustSize() //error
	p.crustSize
	//leave parentheses off when calling methods with no side effects
	//IO mutating state throwing Exception
	// calling other functions with side effects	


	//***************5.7*****************//		

	//creating methods that take variable-argument
	//spread js
	def printAll(string: String*){
		string.foreach(println)
	}

	printAll()
	printAll("foo")
	printAll("foo", "bar")
	printAll("foo", "bar", "baz")

	//adapt a a sequence
	val fruits = List("apple", "banana", "cherry")
	printAll(fruits: _*)

	def printAllInt(numbers: Int*){
		println(numbers.getClass)
	}

	printAllInt(1,2,3)
	printAllInt()

	//***************5.8*****************//		

	//declaring that method can throw an exception
	//the annotation is useful when interoperating with Java
	@throws(classOf[Exception])
	override def play{
		//exception throwing code here...
	}

	/*
	@throws(classOf[IOException])
	@throws(classOf[LineUnavailableException])
	@throws(classOf[UnsupportedAudioFileException])
	def playSoundFileWithJavaAudio{

	}
	*/

	//***************5.9*****************//		

	//fluent style, chaining methods
	//if your class can be extended
	//specify this.type as the return type of fluent methods

	//if you're sure that your class won't be exnteded
	//you can optionally return this from your fluent style methods


	class Person{
		
		protected var fname = ""
		protected var lname = ""

		def setFirstName(firstName: String): this.type = {
			fname = firstName
			this
		}

		def setLastName(lastName: String): this.type = {
			lname = lastName
			this
		}
	}

	class Employee extends Person{

		protected var role = ""

		def setRole(role: String): this.type = {
			this.role = role
			this
		}

		override def toString = {
			"%s, %s, %s".format(fname, lname, role)
		}
	}

	val employee = new Employee
	//use the fluent methods
	employee.setFirstName("Al")
			.setLastName("Alexander")
			.setRole("Developer")

	println(employee)

	//if you're sure your class won't be extended
	//specifying this.type as the return type of
	//set* methods isn't necessary, you can just return this

	final class PizzaN {
		
		import scala.collection.mutable.ArrayBuffer
		private val toppings = ArrayBuffer[String]()
		private var crustSize = 0
		private var crustType = ""

		def addTopping(topping: String) = {
			toppings += topping
			this
		}

		def setCrustSize(crustSize: Int) = {
			this.crustSize = crustSize
			this
		}

		def setCrustType(crustType: String) = {
			this.crustType = crustType
			this
		}

		def print(){
			println(s"crust size: $crustSize")
			println(s"crust type: $crustType")
			println(s"crust toppings: $toppings")
		}
	}

	val pn = new Pizza
	pn.setCrustSize(14)
	  .setCrustType("thin")
	  .addTopping("cheese")
	  .addTopping("green olives")
	  .print()

	







}