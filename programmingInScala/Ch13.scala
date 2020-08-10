object Ch13 extends App {

	//**************13.2****************//
	package bobsrockets {
		package navigation{
			class Navigator
		}

		package tests{
			class NavigatorSuite
		}
	}
	//multiple pacakges in the same file

	package bobsrockets{
		package navigation{
			class Navigator{
				val map = new StarMap
			}

			class StarMap
		}

		class Ship{
			val nav = new navigation.Navigator
		}

		package fleets{
			class Fleet{
				def addShip(){ new Ship }
			}
		}
	}
	//concise access to classes and packages

	package bobsrockets {
		class Ship
	}

	package bobsrockets.fleets{
		class Fleet{
			//Ship isn't in scope
			def addShip() { new Ship}
		}
	}
	//symbols in enclosing packages not available

	package launch {
		class Booster3
	}

	package bobsrockets {
		package navigation {
			package launch {
				class Booster1
			}

			class MissionControl {
				val booster1 = new launch.Booster1
				val booster2 = new bobsrockets.launch.Booster2
				val booster3 = new _root_.launch.Booster3
			}
		}

		package launch {
			class Booster2
		}
	}
	//accessing hidden package names

	//**************13.3****************//
	import bobsdelights.Fruit
	import bobsdelights._
	import bobsdelights.Fruits._

	package bobsdelights
	abstract class Fruit {
		val name: String,
		val color: String
	}

	object Fruits {
		object Apple extends Fruit("apple", "red")
		object Orange extends Fruit("orange", "orange")
		object Pear extends Fruit("pear", "yellowish")
		val menu = List(Apple, Orange, Pear)
	}

	//arbitrary values
	def showFruit(fruit: Fruit){
		import fruit._
		println(s"$name s are $color")
	}
	//fruit.name fruit.color

	///scala's imports:
	//may appear anywhere
	//may refer to obejcts (singleton or regular) in addition to packages
	//let you rename and hide some of the imported members

	//import selector clause
	//hide members
	import Fruits.{Apple, Orange}
	//renaming Apple
	import Fruits.{Apple => McIntosh, Orange}
	//importing everything Fruits but renames to McIntosh
	import Fruits.{Apple => McIntosh, _}
	//imports all members of Fruits except Pear
	import Fruits.{Pear => _ , _}	

	import Notebooks._
	import Fruits.{Apple => _ , _}



	//**************13.4****************//
	//implicit imports
	import java.lang._ //the java.lang package
	import scala._ 	   //scala standard library
	import Predef._    //the Predef object

	//.NET implementation
	import system

	//**************13.5****************//
	
	//access modifiers
	//private members
	class Outer {
		class Inner {
			private def f() { println("f")}
			class InnerMost {
				f(); //ok
			}
		}

		(new Inner).f() //f not accesible
	}

	//protected 
	//slightly more restrictive
	//only accesible from subclasses of the class
	//in which the member is defined
	package p {
		class Super {
			protected def f() { println("f")}
		}
		class Sub extends Super{
			f()
		}

		class Other {
			(new Super).f() //f isn't accesible
		}
	}

	//public
	//every member not labeled private or protected is public
	

	//scope of protection
	//allows you define visibility at a very fine granularity
	package bobsrockets{
		package navigation{ 
			private[bobsrockets] class Navigator{
				protected[navigation] def useStarChart(){}
				class LegOfJourney{
					private[Navigator] val distance = 100
				}

				private[this] var speed = 200
			}
		}

		package launch {
			import navigation._
			object Vehicle {
				private[launch] val guide = new Navigator
			}
		}
	}
	//private[X] access is private up to X
	//where X designates some enclosing package, class or object
	//private[bobsrockets] access within outer package
	//private[navigation] same as package visibility in java
	//private[Navigator] same as private in Java
	//private[LegOfJourney] same as private in Scala
	//private[this] access only from same object - object/private

	class Rocket {
		import Rocket.fuel
		private def canGoHomeAgain = fuel > 20
	}

	object Rocket{
		private def fuel = 10
		def chooseStrategy(rocket: Rocket) = {
			if (rocket.canGoHomeAgain) else pickAStar()
		}

		def goHome(){}
		def pickAStar(){}
	}


	//**************13.6****************//

	//package objects
	//each package is allowed to have one package object
	//any definition placed in a package object
	//are considered package members
	

}
