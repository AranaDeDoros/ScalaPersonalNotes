

object Chapter4 extends App{

	//**************4.1****************//
	//in this example the constructor params are mutable
	//this means Scala will generate both its accessor(getter)
	//and mutator (setter)
	class Person(var firstName: String, var lastName: String) {

		def this(firstName: String) = {this(firstName, "foo")}

		println("the constructor begins")

		//some class fields
		private val HOME = System.getProperty("user.home")
		var age  = 0

		//some methods
		override def toString = s"$firstName $lastName is $age years old"
		def printHome = {println(s"HOME =$HOME")}
		def printFullName = {println(this)}

		printHome
		printFullName
		println("still in the constructor")

	}

	val p = new Person("Adam", "Meyer")
	p.firstName = "Scott"
	p.lastName = "Jones"
	p.age_$eq = 100 //param_$eq actual names of mutators
	println(s"${p.firstName} ${p.lastName} ${p.age}")
	val newPerson = new Person("Me")
	println(newPerson.firstName+""+ newPerson.lastName)

	//**************4.2****************//
	
	//when using val, only the accesor (getter) is generated
	//if neither val or var are used, Scala doesn't generate anything
	//both var and field can be modified with private access keyword
	//this prevents getters and setters from being generated
	class PersonVar(var name: String)
	val p2 = new PersonVar("Alvin y las ardillas")
	println(p2.name)
	p2.name = "Name changed"
	println(p2.name)

	//has accessor/getter but not mutator/setter
	class PersonVal(val name: String)
	val p3 = new PersonVal("Literally me")
	println(p3.name)
	//p3.name = "Name changed"
	//println(p3.name)	

	//neither val or var
	class PersonN(name: String)
	val p4 = new PersonN("Me once again")
	//println(p.name4)	

	//adding private fields only accessed within members of the class
	class PersonPrivate(private var name: String) { def getName: Unit = {println{name}}}
	val p5 = new PersonPrivate("Guess what me")
	//p5.name
	//getName works as it has access to name
	p5.getName

	//case class params are val by default hence have a getter
	case class PersonCC(name: String)
	val pcc = new PersonCC("Dave")
	println(pcc.name) 

	//**************4.3****************//
	//auxiliary constructors must call another inside
	class Pizza (var crustSize: Int, var crustType: String){
		
		//one arg aux constructor
		def this(crustSize: Int) = {
			this(crustSize, Pizza.DEFAULT_CRUST_TYPE)
		}

		def this(crustType: String) ={
			this(Pizza.DEFAULT_CRUST_SIZE, crustType)
		}

		def this() = {
			this(Pizza.DEFAULT_CRUST_SIZE, Pizza.DEFAULT_CRUST_TYPE)
		}

		override def toString = s"A $crustSize inch pizza with a $crustType crust"
	}

	object Pizza {
		val DEFAULT_CRUST_SIZE = 12
		val DEFAULT_CRUST_TYPE = "THIN"
	}

	val pizza1 = new Pizza(Pizza.DEFAULT_CRUST_SIZE, Pizza.DEFAULT_CRUST_TYPE)
	val pizza2 = new Pizza(Pizza.DEFAULT_CRUST_SIZE)
	val pizza3 = new Pizza(Pizza.DEFAULT_CRUST_TYPE)
	val pizza4 = new Pizza

	/*In the example shown, all of the auxiliary constructors call the primary constructor, but
	this  isn’t  necessary;  an  auxiliary  constructor  just  needs  to  call  one  of  the  previously
	defined constructors. */
	/*
	def this (crustType: String) {
		this(Pizza.DEFAULT_CRUST_SIZE)
		this.crustType = Pizza.DEFAULT_CRUST_TYPE
	}*/

	//auxiliary constructors for case classes are actually apply methods
	//in the companion object of the class (object with the same name as a class)
	
	case class PersonCCC (var name: String, var age: Int)

	//adding compaion object of the Person case class
	object PersonCCC {
		def apply() = new PersonCCC("Nanashi", 0)
		def apply(name: String) = new PersonCCC(name, 0)
	}

	val pccc = PersonCCC("John", 30) //PersonCCC.apply(...)

	val ap = PersonCCC()
	val bp = PersonCCC("Pam")
	val cp = PersonCCC("Willy", 85)
	println(ap, bp, cp)
	ap.name = "EPN"
	ap.age = 200
	println(ap)
	//**************4.4****************//
	//private constructor, singleton pattern
	class Order private (id: Int)
	//val order = new Order(1020)

	class Brain private {
		override def toString = "This is the brain."
	}

	object Brain {
		val brain = new Brain
		def getInstance = brain
	}

	//val brain = new Brain
	val brain = Brain.getInstance
	println(brain)

	//any method declared inside a companion object
	//will appear to be static
	object FileUtils {

		def readFile(filename: String) = {
			"okay"
		}

		def writeToFile(filename: String, contents: String): Unit = {
			//code
		}

	}

	val contents = FileUtils.readFile("test.txt")
	FileUtils.writeToFile("text2.txt", contents)
 
	//**************4.5****************//
	/*
	class Socket (val timeout: Int = 1000)
	val s = new Socket
	println(s.timeout)

	val ss = new Socket(5555)
	println(ss.timeout)

	val sss = new Socket(timeout=1000)
	println(s.timeout)
	*/

	//equivalent of two constructors
	/*class Socket (val timeout: Int = 1000)
	class Socket(val timeout: Int) {
		def this() = this(1000)
		override def toString = s"timeout: $timeout"
	}*/

	//multiple parameters
	class Socket(val timeout: Int = 1, val linger: Int = 2) {
		override def toString = s"timeout: $timeout, linger: $linger"
	}

	//named parameters
	println(new Socket(timeout=3000, linger=4000))
	println(new Socket(linger=4000, timeout=3000))
	println(new Socket(timeout=3000))
	println(new Socket(linger=4000))
	
	//**************4.6****************//

	//override default accessor and mutator
	class PersonAM(private var _name: String) {
		def name = _name //accesor
		def name_= (aName: String):Unit = { _name = aName} //mutator
	}

	val pam = new PersonAM("John")
	pam.name = "OKOKO"
	println(pam.name)

	//**************4.7****************//

	//preventing getter and setter from being generated
	class Stock {
		//getter and setters are generated
		var deleayedPrice: Double = _
		//not for this filed though
		private var currentPrice: Double = _ //default value
	}

	class StockD {
		//a private field can be seen by any StockD instance
		private var price: Double = _
		def setPrice(p:Double) :Unit = { price = p}
		def isHigher(that: StockD): Boolean = this.price > that.price
	}

	object Driver  {
		var s1 = new StockD
		s1.setPrice(20)

		val s2 = new StockD
		s2.setPrice(100)

		println(s1.isHigher(s1))

	}

/*	class StockSP {
		//a private[this] var is object-private and can only
		//be seen by the current instance
		private[this] var price: Double = _
		def setPrice(p:Double) {price = p}
		def isHigher(that:Stock): Boolean = this.price > that.price
	}*/



	//**************4.8****************//	

	//assigning a field to a block or function
	class Foo {
		//set text equal to the result of the block of code
		val text = {
			var lines = ""
			try {
				lines = io.Source.fromFile("/etc/passwd").getLines().mkString
			} catch {
				case e: Exception => lines = "Error happened"
			}
			lines
		}
	}

	object Test {
		val f = new Foo()
	}

	val f = new Foo()

	/*class Fooo {
		import scala.xml.XML
		//assign the xml field to the result
		val xml = XML.load("http://exmaple.com/foo.xml")
	}*/


	class Foooo {
		val text = 
			io.Source.fromFile("/etc/passwd").getLines().foreach(println)
	}

	object Testt {
		val f = new Foooo
	}

	class FooLazy {
		lazy val text = 
			io.Source.fromFile("/etc/passwd").getLines().foreach(println)
	}

	object TestLazy {
		val f = new FooLazy
	}


	//**************4.9****************//	

	//setting uninitialized var field types
	case class User(var username: String, var password: String){
		var age = 0
		var firstName = ""
		var lastName = ""
		var address:Option[Address] = None
	}

	case class Address(city: String, state: String, zip: String)

	val newUser = User("literally me", "1234")
	newUser.address = Some(Address("ASBC", "OK", "9392"))
	newUser.address.foreach{ a => {
		println(a.city)
		println(a.state)
		println(a.zip)
	}}

	//**************4.10****************//	

	//handling constructor params when extending a class
	class People(var name: String, var address: Address){
		override def toString = if (address == null) name else s"$name @ $address" 
	}

	//name and adress are common to People, but age is new so add var
	class Employee(name: String, address: Address, var age: Int)
	extends People (name, address){
		//println(s"$name $address, $age")
	}

	val teresa = new Employee("Teresa", Address("Louisville", "KY", "7989"), 25)
	println(teresa.toString)

	//assuming employee doesn't need new params
	/*class Employee(var name: String, var address: Address)
	extends People (name, address)*/
	//or
	/*class Employee(name: String, address: Address)
	extends People (name, address)*/

	//same for val

	//**************4.11****************//	

	//calling a superclass constructor
	/*
	you can control the superclass constructor that’s
    called  by  the  primary  constructor  in  a  subclass,
    but  you  can’t  control  the  superclass constructor
     that’s called by an auxiliary constructor in the subclass
	 */
	
	//1 primary constructor
	class Animal (var name: String, var age: Int){
		//2 aux const
		def this(name: String) = {
			this(name,0)
		}

		override def toString = s"$name is $age years old"
	}

	//call Animal's one-arg const
	class Dog (name: String) extends Animal(name){
		println("Dog constructor called")
	}
	
	/*class Dog (name: String) extends Animal(name, 0){
		println("Dog constructor called")	
	}*/

	val doggo = new Dog("cheems")


	case class Role(role: String)
	class Persona (var name: String, var address: Address){
		def this(name: String) = {
			this(name, null)
			address = null
		}

		override def toString = if (address == null) name else s"$name @ $address"
	}

	class EmployeeN (name: String, role: Role, address: Address)
	extends Persona (name, address){
		def this (name: String) = {
			this(name, null, null)
		}

		def this(name:String, role:Role) = {
			this(name, role, null)
		}

		def this(name: String, address: Address) = {
			this(name, null, address)
		}

	//**************4.12****************//	

	//when to use an abstract class
	//you want to create a base class that requires constructor arguments
	//code will be called from Java code
	//trait Animal(name: String) //no
	//whenever a behaviour must have construct parameters
	//abstract class Animall(name:String)
	//def speak

	/*abstract class BaseController(db: Database){
		def save {db.save}
		def update {db.save}
		def delete {db.save}

		def connect
		def getStatus : String //abstract returning String
		def setServerName(serverName: String)
	}*/


	//**************4.13****************//	

	//defining properties in abstract base class
	abstract class Pet (name: String) {
		
		val greeting: String
		var age: Int
		def sayHello: Unit = {println(greeting)}
		override def toString = s"I say $greeting, and I'm $age"

	}

	class Dogg(name: String) extends Pet(name){
		
		val greeting = "woof"
		var age = 2

	}

	class Catt(name: String) extends Pet(name){
		
		val greeting = "meow"
		var age = 5

	}

	val dog = new Dogg("Frido")
	val cat = new Catt("Morris")
	dog.sayHello
	cat.sayHello
	println(dog)
	println(cat)
	//verify that the age can be changed
	cat.age = 10
	println(cat)

	/*
	abstract class Pett(name: String){
		def greeting: String
	}	

	class Doggg(name:String) extends Pett(name){
		val greeting= "Woof"
	}

	val dogz = new Doggg("fido")
	println(dog.greeting)

	//when concrete, override
	abstract class Animal {
		val greeting = "hello" //provive initial
		def sayHello { println(greeting)}
		def run
	}

	class Dog extends Animal{
		override val greeting = "woof" //override the value
		def run {println("dog is running")}
	}

	//greeting is generated
	abstract class Animal{
		val greeting = { println("Animal"); "Hello"}
	}

	class Dog extends Animal{
		override val greeting = {println("dog") "woof"}
	}

	//prevent a val field in an asbract base class
	//from being overriden in a subclass
	//declare the field as final val
	abstract class  Animal{
		final val greeting = "hello"
	}

	class Dog extends Animal{
		val greeting = "woof"
	}

	//concrete var in abstract
	abstract class Animal {
		var greeting = "hello"
		var age = 0
		override def toString = s"I say $greeting, and I'm $age years old"
	}

	class Dog extends Animal{
		greeting = "woof"
		age = 2
	}

	//avoiding null
	trait Animal{
		var greeting: Option[Int] = None
		var age: Option[Int] = None
		override def toString = s"I say $greeting, and I'm $age years old"
	}

	class Dog extends Animal{
		var greeting = Some("Woof")
		age = Some(2)
	}

	val d = new Dog
	println(d)*/


	//**************4.14****************//
	
	//case class boilerplate
	//apply, accesor and/or mutator, toString, equals
	//hashCode, copy, unapply
	/*
	case class Person(name: String, relation: String)
	val emily = Person("emiliy", "niece")
	println(emily.name)
	emily.name = "fred"

	case class Company(var name:String)

	val c = Company("Mat-su Valley")
	println(c.name) //toString
	c.name = "valley programming"

	emily match { case Person(n, r) => println(n, r)}
	val hannah = Person("Hannah", "niece")
	println(emily == hannah)

	case class Employee(name: String, loc:String, role:String)
	val fred = Employee("Fred", "Anchorage", "Salesman")
	val joe = fred.copy(name="Joe"; role="Mechanic")

	*/
	//**************4.15****************//	

	//defining an equals method (object equality)
	/*
	class Person(name:String, age:Int){
		def canEqual(a:Any) = a.isInstanceOf[Person]

		override def equals(that: Any): Boolean =
			that match{
				case that: Person => that.canEqual(this) && this.hashCode == that.hashCode
				case _ => false
			}

		override def hashCode:Int = {
			val prime = 31
			val result = 1
			result = prime * result + age
			result = prime * result + (if (name == null) 0 else name.hashCode)
				result
		}	
	}

	//first two instances should be equal
	val nimoy = new Person("Leonard Nimoy", 82)
	val nimoy2 = new Person("Leonard Nimoy", 82)
	val shatner new Person("Wiliam Shatner", 82)
	val ed = new Person("Ed Chigliak", 20)

	//tests pass
	test("nimoy == nimoy") { assert(nimoy == nimoy)}
	test("nimoy == nimoy2") { assert(nimoy == nimoy2)}
	test("nimoy2 == nimoy") { assert(nimoy2 == nimoy)}
	test("nimoy != shatner") { assert(nimoy != shatner)}
	test("shatner != nimoy") { assert(shatner != nimoy)}
	test("nimoy != null") { assert(nimoy != null)}
	test("nimoy != String") { assert(nimoy != "Leonard")}
	test("nimoy != ed") { assert(nimoy != ed)}

	class Employee(name:String, age:Int, role:String)
	extends Person(name, age){
		override def canEqual(a:Any) = a.isInstanceOf[Employee]

		override def equals(that: Any): Boolean =
			that match{
				case that: Employee => that.canEqual(this) && this.hashCode == that.hashCode
				case _ => false
			}

		override def hashCode:Int = {
			val ourHash = if(role ==null) 0 else role.hashCode
			super.hashCode + ourHash
		}	
	}
	

	class EmployeeTests extends FunSuite with BeforeAndAfter {
	// these first two instance should be equal
	val eNimoy1 = new Employee("Leonard Nimoy", 82, "Actor")
	val eNimoy2 = new Employee("Leonard Nimoy", 82, "Actor")
	val pNimoy  = new Person("Leonard Nimoy", 82)
	val eShatner = new Employee("William Shatner", 82, "Actor")

  	test("eNimoy1 == eNimoy1") { assert(eNimoy1 == eNimoy1) }
  	test("eNimoy1 == eNimoy2") { assert(eNimoy1 == eNimoy2) }
  	test("eNimoy2 == eNimoy1") { assert(eNimoy2 == eNimoy1) }
  	test("eNimoy != pNimoy")  { assert(eNimoy1 != pNimoy) }
  	test("pNimoy != eNimoy")  { assert(pNimoy != eNimoy1) }

  	*/
	//**************4.16****************//	

	//creating inner class
	class PandoraBox {
		case class Thing(name: String)

		var things = new collection.mutable.ArrayBuffer[Thing]()
		things += Thing("evil thing #1")
		things += Thing("evil thing #2")

		def addThings(name:String):Unit = { things += new Thing(name)}
	}

	val pandora = new PandoraBox
	pandora.things.foreach(println)
	pandora.addThings("evil thing #3")
	pandora.things.foreach(println)


	class OuterClass{
		class InnerClass{
			var x =1
		}
	}
	// inner classes are bound to the outer object
	val oc1 = new OuterClass 
	val oc2 = new OuterClass
	val ic1 = new  oc1.InnerClass
	val ic2 = new oc2.InnerClass
	ic1.x = 10
	ic2.x = 20
	println(s"ic1.x = ${ic1.x}")
	println(s"ic2.x = ${ic2.x}")

	/*
	object OuterObject{
		class InnerClass{
			var x = 1
		}
	}

	class OuterClass{
		object InnerObject{
			val y = 2
		}
	}

	//object InnerClassDemo2 extends App {
		//class inside object
		println(new OuterObject.InnerClass().x)
		println(new OuterClass().InnerObject.y)
	//}
	
	*/


	}


}


