object Ch8 extends App  {
	
	//**************8.1****************//

	//using trait as an interface
	trait BaseSoundPlayer {
		def play: Unit
		def close: Unit
		def pause: Unit
		def stop: Unit
		def resume: Unit
	}

/*	trait Dog = {
		def speak(whatToSay: String)
		def wagTail(enabled: Boolean)
	}*/

	//class Mp3SoundPlayer extends BaseSoundPlayer = {}

	//extends for the class  and traits for subsequent traits
	/*class Foo extends BaseClass with Trait1 with Trait2{

	}

	//extending multiple traits
	class Foo extends Trait1 with Trait2....{

	}*/

	//must implement all methods
	class Mp3SoundPlayer extends BaseSoundPlayer  {
		def play =  { println("something") }
		def close = { println("something") }
		def pause = { println("something") }
		def stop = { println("something") }
		def resume = { println("something") }
	}

	//if not implemented, the class must be abstract
	/*abstract class SimpleSoundPlayerA extends BaseSoundPlayer = {
		def play = { }
		def close = { }
	}*/

	//one trait extending another trait
/*	trait Mp3SoundPlayer extends BaseSoundPlayer = {
		def getBasicPlayer: BasicPlayer
		def getBasicController: BasicController
		def setGain(volume: Double)
	}
*/

	//using fields
	abstract class Animal  {
		def speak: Unit
	}

	trait WaggingTail  {
		def startTail = { println("tail started") }
		def stopTail = { println("tail stopped") }
	}

	trait FourLeggedAnimal  {
		def walk: Unit
		def run: Unit
	}

	class Dog extends Animal with WaggingTail with FourLeggedAnimal {
		//implementation code here...
		def speak = { println("dog says woof") }
		def walk = { println("dog is walking") }
		def run = { println("dog is running") }
	}

	//**************8.2****************//

	//using abstract and concrete fields
	trait PizzaTrait{
		var numToppings: Int
		var size = 14
		val maxNumToppings = 10
	}

	class Pizza extends PizzaTrait{
		var numToppings = 0 //override not needed
		size = 16 //var and override not needed
	}


	trait PizzaTraitt{
		val maxNumToppings: Int
	}

	class Pizzza extends PizzaTraitt{
		override val maxNumToppings = 10 //override is required
	}

	
	//**************8.3****************//

	//using a trait like an abstract class
	trait Pet{
		def speak = { println("yo") }
		def comeToMaster: Unit
	}

	class Doggo extends Pet {
		def comeToMaster = { println("I'm coming!")}
	}

	class Kot extends Pet {
		override def speak = { println("mewo") }
		def comeToMaster = { println("that's not gonna happen")}
	}

	//since comeToMaster isn't implemented, it must be defined as abstract
	abstract class FlyingPet extends Pet {
		def fly = { println("I'm flying") }
	}

	/*
	Although Scala has abstract classes, it’s much more common to use traits than abstract
	classes to implement base behavior. A class can extend only one abstract class, but it can
	implement multiple traits, so using traits is more flexible
	 */

	 /*
	  Use an abstract class (a) when you want
	to define a base behavior, and that behavior requires a constructor with parameters,
	and (b) in some situations when you need to interact with Java
	  */


	//**************8.4****************//

	//using traits as simple mixins
	trait Tail {
		def wagTail = { println("tail is wagging") }
		def stopTail = { println("tail is stopped") }
	}

	abstract class Pett(var name: String) {
		def speak: Unit
		def ownerIsHome = { println("excited") }
		def jumpForJoy = { println("jumping for joy") }
	}

	class Doge(name: String) extends Pett(name) with Tail{
		def speak = { println("woof") }
		override def ownerIsHome = {
			wagTail
			speak
		}
	}

	val zeus = new Doge("Zeus")
	zeus.ownerIsHome
	zeus.jumpForJoy

	//check stackable trait pattern
	
	//**************8.5****************//

	//limiting which classes can use a trait by inheritance
	//trait [TraitName] extends [Type]

	/*class StarFleetComponent
	trait StarFleetWarpCore extends StarFleetComponent
	class Starship extends StarFleetComponent with StarFleetWarpCore
	*/

	/*class StarFleetComponent
	trait StarFleetWarpCore extends StarFleetComponent
	class RomulanStuff
	*/
	//won't compile
	//class Warbid extends RomulanStuff with StarFleetWarpCore

	abstract class Employee
	class CorporateEmployee extends Employee
	class StoreEmployee extends Employee

	trait DeliversFood extends StoreEmployee

	class DeliveryPerson extends StoreEmployee with DeliversFood

	//won't compile
	//class Receptionist extends CorporateEmployee with DeliversFood


	//**************8.6****************//

 	//marking traits so they can only be used by subclasses of a certain type
	/*trait MyTrait {
		this: BaseType =>
	}*/


	class Starship
	
	trait StarFleetWarpCore {
		this: Starship => 
	}
	
	//class Enterprise extends Starship with StarFleetWarpCore

	class RomulanShip

	//class Warbid extends RomulanShip with StarFleetWarpCore

	//this approach is referred to as a self type

	//any concrete class that mixes in the trait must ensure that its type conforms to the trait's self type


	//class Starship
	trait WarpCoreEjector
	trait FireExtinguisher
	trait WarpCore {
		this: Starship with WarpCoreEjector with FireExtinguisher =>
	}

	//this works
	class Enterprise extends Starship 
					 with WarpCore
					 with WarpCoreEjector
					 with FireExtinguisher



	//**************8.7****************//

	//ensuring a trait can only be added to a type that has a specific method
	trait WarpCoree{
		this: { def ejectWarpCore(password: String): Boolean
				def startWarpCore: Unit } =>
	}

	class Enterprisee extends Starship with WarpCoree {
		def ejectWarpCore(password: String): Boolean = {
			if(password == "password"){
				println("core ejected")
				true
			} else {
				false
			}
		}

		def startWarpCore = { println("core started") }
	}

	//structural type

	
	//**************8.8****************//

	//ading a trait to an object instance
	class DavidBanner

	trait Angry{
		println("you won't like me...")
	}

	val hulk = new DavidBanner with Angry

	trait Debugger{
		def log(message: String) = {

		}
	}

	class Child
	class ProblemChild

	val child = new Child
	val problemChild = new ProblemChild with Debugger

	//**************8.9****************//

	//extending a java interface like a trait

	/*public interface Animal{
		public void speak();
	}

	public interface Wagging{
		public void wag();
	}

	public interface Running{
		public void run();
	}*/

	trait Wagging {
		def wag: Unit
	}

	trait Running {
		def run: Unit
	}

	class AnotherDog extends Animal with Wagging with Running{
		def speak = { println("woof") }
		def wag = { println("tail is wagging") }
		def run = { println("I'm running") }
	}


}