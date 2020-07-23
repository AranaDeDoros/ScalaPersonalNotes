object Ch6 extends App {
	
	//**************6.1****************//
	//cast instance of a class to another
	//val recognizer = cm.lookup("recognizer").asInstanceOf[Recognizer]
	//val ctx = new ClassPathXmlApplicationContext("applicationContext.xml")

	//instantiate our dog and cat
	/*
	val dog = ctx.getBean("dog").asInstanceOf[Animal]
	val cat = ctx.getBean("cat").asInstanceOf[Animal]
	val yaml = new Yaml(new Constructor(classOf[EmailAccount]))
	val emailAccount = yaml.load(text).asInstanceOf[EmailAccount]
	val cm = new ConfigurationManager("config.xml")
	//instance of recognizer
	val recognizer = cm.lookup("recognizer").asInstanceOf[Recognizer]
	val microphone = cm.lookup("microphone").asInstanceOf[Microphone]
	*/

	val a = 10
	val b = a.asInstanceOf[Long]
	val c = a.asInstanceOf[Byte]
	println(a,b,c)

	//when interacting with java
	/*
	 val objects = Array("a", 1)
	val arrayOfObject = objects.asInstanceOf[Array[Object]]
	AJavaClass.sendObjects(arrayOfObject)

	//can lead to ClassCastException
	import java.net.{URL, HttpUrlConnection}
	val connection = (new URL(url)).openConnection.asInstanceOf[HttpUrlConnection]
	*/

	//**************6.2****************//

	//equivalent of Java's .class
	//val info = new DataLine.Info(classOf[TargetDataLine], null)
	val stringClass = classOf[String]
	println(stringClass.getMethods)

	//**************6.3****************//
	
	//determing class of an object
	def printAll(numbers: Int*) ={
		println(s"class : ${numbers.getClass}")
	}

	printAll(1,2,3)
	printAll()

	/*
	val hello = <p>Hello, world</p>
	hello.child.foreach(e=> println(e.getClass))
	*/

	def printClass(c: Any) = { println(c.getClass)}
	printClass(1)
	println("yo")

	//**************6.4****************//

	//launching app with an object
	/*  
	object Test extends App {
		
	}

	def main(args: Array[String]): Unit = {
	  
	}

	object Hello extends App{
	*/
		if(args.length == 1)
			println(s"hello, ${args(0)}")
		else
			println("I didn't get your name")	
	//}


	//**************6.5****************//

	//creating singletons with object
	object CashRegister{
		def open = {println("Opened")}
		def close = {println("Closed")}
	}

	CashRegister.open
	CashRegister.close

	import java.util.Calendar
	import java.text.SimpleDateFormat

	object DateUtils{
		//as Thursday, November 29
		def getCurrentDate: String = getCurrentDateTime("EEEE, MMM, d")
		//as 6:20 p.m.
		def getCurrentTime: String = getCurrentDateTime("K:m aa")
		//common function used by other date/time functions
		private def getCurrentDateTime(dateTimeFormat: String): String = {
			val dateFormat = new SimpleDateFormat(dateTimeFormat)
			val cal = Calendar.getInstance()
			dateFormat.format(cal.getTime())
		}
	}

	println(DateUtils.getCurrentTime)
	println(DateUtils.getCurrentDate)

	//useful for messages


	//**************6.6****************//

	//pizza class
	class Pizza(var crustType: String){
		override def toString = s"Crust type is $crustType"
	}

	object Pizza{
		val CRUST_TYPE_THIN = "thin"
		val CRUST_TYPE_THICK = "thick"
		def getFoo = "Foo"
	}

	println(Pizza.CRUST_TYPE_THIN)
	println(Pizza.getFoo)

	var pizza = new Pizza(Pizza.CRUST_TYPE_THICK)
	println(pizza)


	//accessing private members
	class Foo{
		private val secret = 2
	}

	object Foo{
		//access the private class field secret
		def double(foo: Foo) = foo.secret * 2
	}

	val f = new Foo
	println(Foo.double(f))


	class Fooo{
		//access the private field object field obj
		def printObj = {println(s"I can see ${Fooo.obj}")}
	}

	object Fooo {
		private val obj = "Foo's object"
	}

	val fooo = new Fooo
	fooo.printObj







	//**************6.7****************//
	/*
	package com.alvinalexander.myapp
	package object model{
		val MAGIC_NUM = 42
		def echo(a: Any) {println(a)}

		//enum
		object Margin extends Enumeration{
			type Margin = Value
			val TOP, BOTTOM, LEFT, RIGHT = Value
		}

		//type definition
		type MutableMap[K,V] = scala.collection.mutable.Map[K,V]
		val MutableMap = scala.collection.mutable.Map
	}

	package com.alvinalexander.myapp.model
	object MainDriver extends App{
		//access 
		echo("hello")
		echo(MAGIC_NUM)
		echo(Margin.LEFT)

		val mm = MutableMap("name" -> "all")
		mm += ("password" -> 123)
		for((k,v) <-mm) printf("key $s value %sn", k, v)
	}
	*/

	//**************6.8****************//

	/*
	class Person{
		var name: String = _
	}

	object Person{
		def apply(name: String): Person = {

			var p = new Person
			p.name = name
			p
		}
	}

	val dawn = Person("Dawn")
	val people = Array(Person("Dan"), Person("Elijah"))

	case class Person(var name: String)
	...
	*/

	class Person{
		var name = ""
		var age = 0
	}

	object Person{
		//a one-arg constructor
		def apply(name: String): Person = {
			var p = new Person
			p.name = name
			p
		}

		def apply(name: String, age: Int): Person = {
			var p = new Person
			p.name = name
			p.age = age
			p
		}
	}

	val fred = Person("Fred")
	val john = Person("John", 42)

	//multiple constructors for cc
	
	//want accessor and mutator methods for the namge and fields
	/*
	case class Person(var name: String, var age: Int)

	object Person{

		def apply() = new Person("<no name>", 0)
		def apply(name: String) = new Person(name, 0)
	}

	object Test extends App{

		val a = Person()
		val b = Person("All")
		val c = Person("William", 82)
		println(a, b, c)
		a.name = "leonard"
		a.age = 192
		println(a)
	}*/

	
	//**************6.9****************//

	//factory method

	trait Animal{
		def speak: Unit
	}

	object Animal{
		
		private class Dog extends Animal{
			override def speak = { println("woof")}
		}

		private class Cat extends Animal{
			override def speak = { println("meow")}
		}

		def apply(s: String): Animal = {
			if(s == "dog") new Dog
			else new Cat
		}

		/*
		def getAnimal(s: String): Animal{
			if(s == "dog") return new Dog
			else return new Cat
		 */
		}

	val cat = Animal("cat")
	cat.speak
	val dog = Animal("dog")
	dog.speak

}	