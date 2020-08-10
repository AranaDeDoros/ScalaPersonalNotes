object Ch12 extends App {
	
	//**************12.1****************//

	//how traits work
	trait Philosophical {
		def philosophize():Unit = {
			println("I consume memory, therefore I am!")
		}
	}

	/*class Frog extends Philosophical {
		override def toString = "green"
	}

	val frog = new Frog
	frog.philosophize()
	*/
	
	class Animal
	trait HasLegs
	
	/*class Frog extends Animal with Philosophical with HasLegs {
		override def toString = "green"
	}*/

	//overriding
	class Frog extends Animal with Philosophical {
		override def toString = "green"
		override def philosophize():Unit = {
			println("It ain't easy being "+toString+"!")
		}
	}

	val phrog: Philosophical = new Frog
	phrog.philosophize()

	
	//**************12.2****************//

	//thin vs rich interfaces

	/*trait CharSequence {
		def chartAt(index: Int): Char
		def length: Int
		def subSequence(start: Int, end: Int): CharSequence
		def toString(): String
	}*/


	//**************12.3****************//

	//example, rectangular objects
	//without traits
	class Point(val x:Int, val y: Int)

	/*class Rectangle(val topLeft: Point, val bottomRight: Point){
		def left = topLeft.x
		def right = bottomRight.x
		def width = right - left
	}*/

	/*abstract class Component {
		def topLeft: Point
		def bottomRight: Point
		def left = topLeft.x
		def right = bottomRight.x
		def width = right - left
	}*/

	//with traits
	trait Rectangular {
		def topLeft: Point
		def bottomRight: Point
		def left = topLeft.x
		def right = bottomRight.x
		def width = right - left
	}	

/*	abstract class Component extends Rectangular{
		def topLeft: Point
		def bottomRight: Point
		def left = topLeft.x
		def right = bottomRight.x
		def width = right - left
	}*/

	class Rectangle(val topLeft: Point, val bottomRight: Point) extends Rectangular{
		override def left = topLeft.x + 5
		override def right = bottomRight.x + 8
		override def width = (right - left) * 2
	}
	

	val rect = new Rectangle(new Point(1,1), new Point(10, 10))
	println(rect.left, rect.right, rect.width)

	//**************12.4****************//

	//ordered trait
	/*class Rational(n: Int, d: Int) {
		def < (that: Rational) = this.numer * that.denom > that.numer * this.denom
		def > (that: Rational) = that < this
		def <= (that: Rational) = (this < that) || (this == that)
		def >= (that: Rational) = (this > that) || (this == that)
	}*/

	//to use it we must implement the compare method
	// then < > <= >= will be defined

	//typed parameter
	/*class Rational(n: Int, d: Int) extends Ordered[Rational]{
		def compare(that: Rational) = (this.numer * that.denom) - (that.numer * this.denom)
	}*/

	//the compare method must compare the receiver with another object
	//it should return an intenger that is zero if the objects are the same
	//negative if receiver is less than the argument
	//positive if the receiver is greater than the arguments
		
	
	/*val half  = new Rational(1, 2)	
	val third  = new Rational(1, 3)

	println(half < third)	
	println(half > third)	
	*/
	
	//it doesn't define equals though
	

	//**************12.5****************//

	//stackable modifications
	abstract class IntQueue {
		def get(): Int
		def put(x: Int)
	}

	import scala.collection.mutable.ArrayBuffer

	class BasicIntQueue extends IntQueue{
		private val buf = new ArrayBuffer[Int]
		def get() = buf.remove(0)
		def put(x: Int) { buf += x}
	}

	val queue = new BasicIntQueue
	println(queue.put(10))
	println(queue.put(20))
	println(queue.get())
	println(queue.get())

	trait Doubling extends IntQueue {
		abstract override def put(x: Int) { super.put(2*x)}
	}

	class MyQueue extends BasicIntQueue with Doubling

	val queue2 = new MyQueue
	println(queue2.put(10))
	println(queue2.get())

	val queue3 = new BasicIntQueue with Doubling
	println(queue3.put(10))
	println(queue3.get())

	trait Incrementing extends IntQueue{
		abstract override def put(x: Int) {super.put(x+1)}
	}

	trait Filtering extends IntQueue{
		abstract override def put(x: Int){
			if(x>=0) super.put(x)
		}
	}

	val queue4 = new BasicIntQueue with Incrementing with Filtering
	println(queue4.put(-1))
	println(queue4.put(0))
	println(queue4.put(1))
	println(queue4.get())
	println(queue4.get())

	/*
	roughly speaking, traits further to the right take effect first.  
	When you call a method on a class with mixins,  
	the method in the trait furthest to the right is called first. 
	If that method calls super, it invokes the method in the next trait to its left, and so on.  
	In the previous example, Filtering’s put is invoked first the Incrementing’s putis invoked second,
	 */


	//**************12.6****************//

	/*
	
	Linearilization

	As hinted previously, the answer is linearization. When you instantiate a
	class with new, Scala takes the class and all of its inherited classes and traits
	and puts them in a single, linear order. Then, whenever you call super inside
	one of those classes, the invoked method is the next one up the chain.  
	If all of the methods but the last call	super, the net result is stackable behavior.
	The precise order of the linearization is described in the language specification.  
	It is a little bit complicated, but the main thing you need to know	is that, 
	in any linearization, a class is always linearized before all of its superclasses and mixed in traits.   
	Thus,  when you write a method that callsnsuper, that method is definitely modifying 
	the behavior of the superclasses and mixed in traits, not the other way around.

	clas Animal
	trait Furry extends Animal
	trait HasLegs extends Animal
	trait FourLegged extends HasLegs
	class Cat extends Animal with Furry with FourLegged


	 */


	//**************12.7****************//

	/*When to use traits

	If the behavior will not be reused, then make it a concrete class. 
	It is not reusable behavior after all.

	If it might be reused in multiple, unrelated classes, make it a trait.  
	Only traits can be mixed into different parts of the class hierarchy.

	If you want to inherit from it in Java code, use an abstract class. 


	If  you  plan  to  distribute  it  in  compiled form and  you  expect  outside
	groups to write classes inheriting from it, you might lean towards using an
	abstract class.  The issue is that when a trait gains or loses a member, any
	classes that inherit from it must be recompiled, even if they have not changed.
	If outside clients will only call into the behavior, instead of inheriting from	 it then a trait is fine


	If  efficiency  is  very  important,  lean  towards  using  a  class.

	If you still do not know, after considering the above, then start by making it as a trait. 
	You can always change it later, and in general using a trait keeps	more options open.


	*/

}