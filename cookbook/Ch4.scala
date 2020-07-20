

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
	//whenever a behavaiour must have construct parameters
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



	}


}


