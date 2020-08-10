object Ch10 extends App {
	
	//**************10.2****************//
	
	/*abstract class Element {
		def contents : Array[String]
		def height: Int = contents.length  //empty paren methods
		def width: Int = if(height == 0) 0 else contents(0).length
	}*/

	//**************10.3****************//
	/*
	the uniform access principle
	which says that client code should not be affected by 
	a decision to iplement an attribute as a field or method
	 */
	
	/*
	abstract class Element {
		def contents : Array[String]
		val height: Int = contents.length  //empty paren methods
		val width: Int = if(height == 0) 0 else contents(0).length
	}*/

	//**************10.4****************//
	
	class ArrayElement(const: Array[String]) extends Element {
		def contents: Array[String] = const
	}

	val ae = new ArrayElement(Array("hello", "world"))
	println(ae.width)

	val e: Element = new ArrayElement(Array("hello"))


	//**************10.5****************//

	//fields and methods belong to the same namespace
	//so fields can overridie parameterless method
	/*
	class ArrayElement(conts: Array[String]) extends Eelement{
		val contents: Array[String] = conts;
	}*/

	//doesn't work
	/*class WontCompile{
		private var f = 1
		def f = 1
	}*/

	//scala has two namespaces
	//values (fields, methods, packages, and single objects)
	//types (class and trait names)


	//**************10.6****************//

	//parametric fields
	//combining parameter and field

	/*
	class ArrayElement(
		val contents: Array[String]
	) extends Element

	class ArrayElement(
		x123: Array[String]
	) extends Element{
		val contents: Array[String] = x123
	}
	*/

	class Cat {
		val dangerous = false
	}

	class Tiger(
		override val dangerous: Boolean,
		private var age: Int
	) extends Cat


	//same as above
	/*
	class Tiger(param1: Boolean, param2: Int) extends Cat{
		override val dangerous= param1
		private var age = param2
	}	
	*/
	
	//**************10.7****************//
	
	//invoking superclass constructors
	//Inheritance: a class may inherit - use by default 
	//-the fields and methods of its superclass. 
	//Inheritance is transitive, so a class may inherit from
	//another class which inherits from another class, and so on, 
	//up to a base class (typically Object, possibly implicit/absent). 
	// Subclasses may override some methods
	//and/or fields to alter the default behavior.

	//Composition: when a field’s type is a class, the field will hold 
	//a reference to another object, thus creating an association relationship between them. 
	//let’s intuitively define composition as when the class uses another object
	// to provide some or all of its functionality.
	/*class LineElement(s: String) extends ArrayElement(Array(s)){
		override def width = s.length
		override def height = 1
	}*/

	//**************10.9****************//

	//dynamical binding
	/*class UniformElement(
		ch: Char,
		override val width: Int,
		override val height: Int
	) extends Element{
		private val line = ch.toString * width
		def contents = Array.fill(height)(line)
	}

	val e1: Element = new ArrayElement(Array("hello", "world"))
	val aee: Element = new LineElement("hello")
	val e2: Element = aee
	val e3: Element = new UniformElement('x', 2, 3)*/

	/*dynamically bound
	actual method implementation invoked is
	determined at run time based on the class
	of the object, not the type of the var or exp
	*/

	/*
	abstract class Element{
		def demo(){
			println("element's implementation invoked")
		}
	}

	class ArrayElement extends Element{
		override def demo(){
			println("ArrayElement's implementation invoked")
		}
	}

	class LineElement extends ArrayElement{
		override def demo(){
			println("LineElement's implementation invoked")
		}
	}

	class UniformElement extends Element

	def invokedDemo(e: Element){
		e.demo()
	}


	*/

	//**************10.10****************//

	/*
	class ArrayElement extends Element{
		final override def demo(){
			println("ArrayElement's implementaion invoked"))
		}
	}

	final class ArrayElement extends Element{
		override def demo(){
			println("ArrayElement's implementaion invoked"))
		}
	}
	*/

	
	
	//**************10.11****************//

	//using composition and inheritance
	/*class LIneElement(s: String) extends Element{
		val contents = Array(s)
		override def width = s.length
		override def height = 1
	}*/

	//**************10.12****************//

	//implementing above, beside and toString
	/*def above(that: Element): Element =
		new ArrayElement(this.contents ++ that.contents)

	def beside(that: Element): Element = {
		val contents = new Array[String](this.contents.length)
		new ArrayElement(
			for(
			(line1, line2) <- this.contents zip that.contents
			)
		)
	}

	override def toSrring = contents mkString "\n"*/

	//**************10.13****************//

	//defining a factory object
	/*abstract class Element{

		def contents: Array[String]
		
		def width: Int = 
			if(height == 0) 0 else contents(0).length

		def height: Int = contents.length

		def above(that: Element): Element =
			new ArrayElement(this.contents ++ that.contents)

		def beside(that: Element): Element =
			new ArrayElement(
				for(
					(line1, line2) <- this.contents zip that.contents
				)
			)

		override def toString = contents mkString "\n"

	}
	
	object Element{

		def elem(contents: Array[String]): Element =
			new ArrayElement(contents)

		def elem(chr: Char, width: Int, height: Int): Element =
			new UniformElement(chr, width, height)

		def elem(line: String): Element =
			new LineElement(line)
	}
	*/
	//**************10.14****************//

	import Element.elem

	//heighten and widen
	object Element {

		private class ArrayElement(
			val contents: Array[String]
		) extends Element
		
		private class LineElement(s: String) extends Element{
			val contents = Array(s)
			override def width = s.length
			override def height = 1
		}

		private class UniformElement(
			ch: Char,
			override val width: Int,
			override val height: Int
		) extends Element{
			private val line = ch.toString * width
			def contents = Array.fill(height)(line)
		}

		def elem(contents: Array[String]): Element =
			new ArrayElement(contents)

		def elem(chr: Char, width: Int, height: Int): Element =
			new UniformElement(chr, width, height)

		def elem(line: String): Element =
			new LineElement(line)
	}


	abstract class Element{

		def contents: Array[String]
		
		def width: Int = 
			if(height == 0) 0 else contents(0).length

		def height: Int = contents.length

		def above(that: Element): Element =
			elem(this.contents ++ that.contents)

		def beside(that: Element): Element = {
			elem(
				for(
					(line1, line2) <- this.contents zip that.contents
				) yield line1 + line2
			)
		}

		def widen(w: Int): Element =
			if(w <= width) this
			else {
				val left = elem(' ', (w - width) / 2, height)
				val right = elem(' ', w - width - left.width, height)
				left beside this beside right
			}

		def heighten(h: Int): Element = {
			if(h <= height) this
			else{
				val top = elem(' ', width, (h-height)/2)
				val bot = elem(' ', width, h-height - top.height)
				top above this above bot
			}
		}

		override def toString = contents mkString "\n"

	}

	

	//**************10.15****************//

	//wrapping up everything
	//import Element.elem
	object Spiral {
		val space = elem(" ")
		val corner = elem("+")

		def spiral(nEdges: Int, direction: Int): Element = {
			if(nEdges == 1)
			  elem("+")
			else{
				val sp = spiral(nEdges - 1, (direction + 3) % 4)
				def verticalBar = elem('|', 1, sp.height)
				def horizontalBar = elem('-', sp.width, 1)
				if(direction == 0)
					(corner beside horizontalBar) above (sp beside space)
				else if(direction == 1)
					(sp above space) beside (corner above verticalBar)
				else if(direction == 2)
					(space beside sp) above (horizontalBar beside corner)
				else
					(verticalBar above corner) beside (space above sp)
			}
		}
	}

	println(Spiral.spiral(10, 0))


	//val filter: (String|Int) => Boolean =

}