object Ch19 extends App {

	//skipping 15 through 18

	//19 Types
	/*Array[T] Invariant
	Seq[+A] C Covariant
	Foo[-A] C Contravariant
	Used when elements in the container are mutable*/

	class Grandparent
	class Parent extends Grandparent
	class Child	extends	Parent

	class InvariantClass[A]
	class CovariantClass[+A]
	class ContravariantClass[-A]

	class VarianceExample{

		def invarMethod(x:InvariantClass[Parent]) {}
		def covarMethod(x:CovariantClass[Parent]) {}
		def contraMethod(x:ContravariantClass[Parent]) {}
		invarMethod(new InvariantClass[Child]) // ERROR - won't compile
		invarMethod(new InvariantClass[Parent]) // success
		invarMethod(new InvariantClass[	Grandparent]) // ERROR - won't compile
		covarMethod(new CovariantClass[Child]) // success
		covarMethod(new CovariantClass[Parent]) // success
		covarMethod(new CovariantClass[Grandparent]) // ERROR - won't compile
		contraMethod(new ContravariantClass[Child])  // ERROR - won't compile
		contraMethod(new ContravariantClass[Parent]) // success
		contraMethod(new ContravariantClass[Grandparent]) // success

	}

	//Bounds
	/*A <: B upper bound A must be a subtype of B
	A >: B lower bound A must be a supertype of B
	A <: Upper >: lower and upper bounds*/

	//Type  Constraints
	//A =:= B
	// A must be equal to B
	//A <:< B
	// A must be a subtype of B
	//A <%< B
	// A must be viewable as B

	//**************19.1****************//

	//Creating classes that use generic types
	class LinkedList[A]{
		private class Node[A](elem:A){
			var next:Node[A] = _
			overrid def toString = elem.toString
		}

		private var head: Node[A] = _

		def add(elem:A){
			val n = new Node(elem)
			n.next = head
			head = n
		}

		private def printNodes(n: Node[A]){
			if(n != null){
				println(n)
				printNodes(n.next)
			}
		}

		def printAll(){ printNodes(head)}
	}

	val ints = new LinkedList[Int]()
	ints.add(1)
	ints.add(2)

	val strings = new LinkedList[String]()
	strings.add("Nacho")
	strings.add("Libre")
	strings.printAll()

	trait Animal

	class Dog extends Animal{
	override def toString = "Dog"
	}

	class SuperDog extends Dog{
	override def toString= "SuperDog"
	}

	class FunnyDog extends Dog{
	override def toString =	 "FunnyDog"
	 }

	val dogs = new LinkedList[Dog]


	val fido = new Dog
	val wonderDog = new SuperDog
	val  scooby = new FunnyDog
	dogs.add(fido)
	dogs.add(wonderDog)
	dogs.add(scooby)

	def printDogTypes(dogs: LinkedList[Dog]) {
		dogs.printAll()
	}

	val	superDogs = new LinkedList[SuperDog]
	superDogs.add(wonderDog) // error: this line won't compile
	printDogTypes(superDogs)

	/*The Scala standard is that simple types should be
	declared as A, the next with B, and so on.*/

	//**************19.2****************//

	//Creating a method that takes a simple generic type
	def randomName(names:Seq[String]):	String = {
		val randomNum = util.Random.nextInt(names.length)
		names(randomNum)
	}


	val names =	Seq	("Aleka", "Christina", "Tyler", "Molly")
	val	winner = randomName(names)

	def	randomElement[A](seq:Seq[A]): A	 = 	{
		val randomNum = util.Random.nextInt(seq.length)
		  seq(randomNum)
	}

	randomElement(Seq("Aleka", "Christina", "Tyler", "Molly"))
	randomElement(List(1,2,3))
	randomElement(List(1.0,2.0,3.0))
	randomElement(Vector.range('a', 'z'))

	//**************19.3****************//

	//Using duck typing (Structural types)

	/*Scala’s version of “Duck Typing” is known as using a structural type.
	As an example of this approach, the following code shows how a callSpeak method
	can require that its obj  type parameter have a speak() method:*/

	def callSpeak[A <:{ def speak () : Unit } ](obj : A ) {
		// code here ...
	   	obj.speak()
	 }

	 /*An instance of any class that has a speak() method that takes no
	 parameters and returns nothing can be passed as a parameter to callSpeak.*/

	class Dog{
		def	speak() { println("woof")}
	}

	class Klingon {
		def speak() { println("Qapla!") }
	}

	object DuckTyping extends App {

		def callSpeak[A <: { def speak(): Unit }](obj: A) {
			obj.speak()
		}
		callSpeak(new Dog)
		callSpeak(new Klingon)

	}

	 /*The structural type syntax is necessary in this example because the callSpeak method
	 invokes a	speak method on the object that’s passed in. In a statically typed language,
	 there must be some guarantee that the object that’s passed in will have this method,
	 and this recipe shows the syntax for that situation. Had the method been written as follows,
	 it wouldn’t compile, because the compiler can’t guarantee that the type  A has a  speak  method:*/

	 // won't compile
	 def callSpeak[A](obj:A) {
	 	   obj.speak()
	 }

	 /*This is one of the great benefits of type safety in Scala.
	 It may help to break down the structural type syntax. First, here’s the entire method:*/

	def callSpeak[ A<:{ def speak(): Unit } ] (obj:A){ obj.speak() }
	class Stack[ A <: Animal ] (val elem:A)

	  /*A must be a subtype of a type that has a
	 speak method. The speak method (or function) can’t
	 take any parameters and must not return anything.
	 As a word of warning, this technique uses reflection,
	 so you may not want to use it when
	 performance is a concern.*/

	//**************19.4****************//

	//Make mutable collections invariant
	/*When creating a collection of elements that can be changed (mutated),
	its generic type	parameter should be declared as [A], making it invariant

	Declaring a type as invariant has several effects.
	First, the container can hold both the
	specified types as well as its subtypes.*/

	trait Animal {
		def speak
	}

	class Dog(var name:String) extends Animal{
		def speak { prinln("woof") }
		override def toString = name
	}

	class SuperDog(name:String) extends Dog(name){
		def useSuperPower { println("using my superpower") }
	}

	val fido =	new	Dog	("Fido")
	val wonderDog	=	new	SuperDog("Wonder Dog")
	val shaggy	=	new	SuperDog("Shaggy")
	val dogs 	=	ArrayBuffer[Dog]()
	dogs += fido
	dogs += wonderDog

	/*The second effect of declaring an invariant type is
	the primary purpose of this recipe.	Given the same code,
	you can define a method as follows to accept an	ArrayBuffer[Dog],
	and then have each Dog speak:*/

	 import collection.mutable.ArrayBuffer
	 def makeDogsSpeak(dogs	: ArrayBuffer[Dog]){
	   dogs.foreach(_.speak)
	 }

	 val dogs = ArrayBuffer[Dog]()
	 dogs += fido
	 makeDogsSpeak(dogs)
	 val superDogs = ArrayBuffer[SuperDog]()
	 superDogs += shaggy
	 superDogs += wonderDog
	 makeDogsSpeak(superDogs)
	 // ERROR: won't compile

	 /*This code won’t compile because of the conflict built up in this situation:
	 •Elements in an ArrayBuffer  can be mutated.
	 •makeDogsSpeak is defined to accept a parameter of type ArrayBuffer[Dog].
	 •You’re attempting to pass in superDogs, whose type is ArrayBuffer[SuperDog]
	 •If the compiler allowed this, makeDogsSpeak could replace SuperDog elements in	 superDogs
	  with plain old Dog elements. This can’t be allowed. If you want to write a method to make all
	  Dog types and  subtypes speak, define it to accept a collection of immutable elements, such as a
	  List, Seq, or Vector*/

	  //mutable -> invariant
	  class Array[T]
	  class ArrayBuffer[A]
	  class ListBuffer[A]

	  //immuable -> covariant
	  class List[+T]
	  class Vector[+A]
	  trait Seq[+A]

	//**************19.5****************//

	//make immuable collections covariant
		class List[+T]
		class Vector[+A]
		trait Seq[+A]

		trait Animal{
			def speak
		}

		class Dog(var name: String) extends Animal{
			def speak = println("dog says woof")
		}

		class SuperDog(name: String) extends Animal(name){
			override def speak = println("i'm a superdog")
		}

		def makeDogsSpeak(dogs:Seq[Dog]) {
			dogs.foreach(_.speak)
		}

		// this works
		val dogs = Seq(new Dog("Fido"), new Dog("Tanner"))
		makeDogsSpeak(dogs)

		// this works too
		val superDogs	= Seq(new	SuperDog("Wonder Dog"), 	new	SuperDog("Scooby"))
		makeDogsSpeak(superDogs)

		class	Container[+A] (val elem	:	A)

		def makeDogsSpeak(dogHouse	:	Container	[	Dog	]) {
			dogHouse.elem.speak()
		}

		val dogHouse =	new	Container	(new Dog("Tanner"))
		makeDogsSpeak(dogHouse)
		val	superDogHouse	=	new	Container(new SuperDog("Wonder Dog"))
		makeDogsSpeak(superDogHouse)

		/*defining an immutable collection to take a covariant generic type parameter
		makes the collection more flexible and useful throughout your	code*/


	//**************19.6****************//
	//Create a Collection Whose Elements Are All of Some Base Type
	trait	CrewMember
	class	Officer	extends	CrewMember
	class	RedShirt extends	CrewMember
	trait	Captain
	trait	FirstOfficer
	trait	ShipsDoctor
	trait	StarfleetTrained

	val kirk 	=	new	Officer	with Captain
	val spock	=	new	Officer	with FirstOfficer
	val bones	=	new	Officer	with ShipsDoctor
	val officers = new Crew[Officer]()
	officers += kirk
	officers += spock
	officers += bones

	val redShirt = new RedShirt
	officers += redShirt // ERROR: this won't compile

	class Crew[A<:CrewMember] extends ArrayBuffer[A]

	 /*This  states  that  any  instance  of  Crew can  only  ever  have  elements  that  are  of  type CrewMember.
	 In this example, this lets you define officers as a collection of Officer , like this*/

	val officers = new Crew[Officer]()	 // error: won't compile
	val officers = new Crew[String]()
	val redshirts = new Crew[RedShirt]()

	/*If you’re working with an implicit conversion, you’ll want to use a view bound instead of an upper bound.
	To do this, use the <% symbol instead of the <: symbol.*/

  class Crew[A<:CrewMember with StarfleetTrained] extendsArrayBuffer[A]

	val kirk = new Officer with Captain with StarfleetTrained
	val spock = new Officer with FirstOfficer	 with StarfleetTrained
	val bones = new Officer with ShipsDoctor with StarfleetTrained
  val officers = new Crew[Officer with StarfleetTrained]()
  officers += kirk
  officers += spock
  officers += bones

	class StarfleetOfficer extends  Officer with StarfleetTrained
	val kirk = new StarfleetOfficer with Captain

	//methods
	trait	CrewMember{	def	beamDown{ println	("beaming down")} }
	class	RedShirt extends CrewMember	 {def	 putOnRedShirt	 { println	("putting on my red shirt")} }

	def beamDown[A<:CrewMember](crewMember:	Crew[A]){ crewMember.foreach(_.beamDown) }

	def getReadyForDay[A<:RedShirt](redShirt:	Crew[A]) { redShirt.foreach(_.putOnRedShirt) }


	//**************19.7****************//

	/*Selectively Adding New Behavior to a Closed Model

	You have a closed model, and want to add new behavior to certain types within that
	model, while potentially excluding that behavior from being added to other types.*/

	//Implement your solution as a type class
	def add[A](x:A, y:A	)(implicit numeric:	Numeric[A]): A = numeric.plus(x, y)
	println(add(1, 1))
	println(add(1.0, 1.5))
	println(add(1, 1.5F))

	/*The process of creating a type class
	Usually you start with a need, such as having a closed model
	to which you want to	add new behavior.
	To add the new behavior, you define a type class.
	The typical approach is to create a base trait, and then  write
	specific  implementations  of  that  trait  using  implicit objects.
	Back in your main application, create a method that uses the type class
	to apply the behavior to the closed model, such as writing the add method in the previous example.*/

	package	typeclassdemo
	// an existing, closed model
	trait	Animal
	final	case	class	Dog	(name	:	String	)	extends	Animal
	final	case	class	Cat	(name	:	String	)	extends	Animal

	package typeclassdemo
	object Humanish {
		// the type class.
		// defines an abstract method named 'speak'.
		trait HumanLike[A] {
			def speak(speaker:A):	Unit
		}
		// companion object
		object HumanLike{
			// implement the behavior for each desired type. in this case,
			// only for a Dog.
			implicit object	DogIsHumanLike extends HumanLike[Dog] {
				def speak(dog:Dog) { println(s"I'm a Dog, my name is ${dog.name}")}
			}
		}
	}

	package typeclassdemo
	object TypeClassDemo extends App {
	 	 import Humanish.HumanLike
	 	// create a method to make an animal speak
	 	def makeHumanLikeThingSpeak[A](animal:A)(implicit humanLike:HumanLike[A]) {
	 		humanLike.speak(animal)
	}
	 	// because HumanLike implemented this for a Dog, it will work
	   makeHumanLikeThingSpeak(Dog("Rover"))	 // however, the method won't compile for a Cat (as desired)
	 	//makeHumanLikeThingSpeak(Cat("Morris"))
	 }

	/*The makeHumanLikeThingSpeak is similar to the add method in the first example.
	In the first example, the Numeric type class already existed, so you could just use it
	to create the add method. But when you’re starting from scratch, you need to create
	your own type class (the code in the HumanLike  trait).
	Because a speak method is defined in the DogIsHumanLike implicit object, which extends
	HumanLike[Dog], a Dog can be passed into the makeHumanLikeThingSpeak method.
	But because a similar implicit object has not been written for the Cat  class, it can’t be used*/

	//**************19.8****************//

	//Building Functionality with Types

	def timer[A](blockOfCode: => A)	=	 {
		val	startTime	=	System.nanoTime
		val result = blockOfCode
		val stopTime = System.nanoTime
		val	delta	=	stopTime	- startTime(result, delta	/1000000d)
	}

	val (result, time) = timer{ println("Hello") }

	// version 2
	sealed abstract class Attempt[A] {
		def getOrElse[ B >: A ](default: => B): B =
		if(isSuccess) get else default
		var isSuccess =	false
		def get:A
	}

	object Attempt{
		def apply[A](f: => A):Attempt[A] 	=
		try	 {
			val result = f
			Succeeded(result)
		}
		catch {
			case e: Exception => Failed(e)
		}
	}

	final case class Failed[A](
	val exception:Throwable) extends Attempt[A] {
		isSuccess = false
		def get:A = throw exception
	}

	final case class Succeeded[A](result:A) extends Attempt[A	] {
		isSuccess =	true
		def	 get 	=	 result
	}




}