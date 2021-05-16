object Ch19 extends App{

	//19.1 Singleton Types
	//Given any reference v, you can form the type v.type, which has two values: v and null. T
	class Document {
	 def setTitle(title: String) = { ...; this }
	 def setAuthor(author: String) = { ...; this }
	 ...
	}

	article.setTitle("Whatever Floats Your Boat").setAuthor("Cay Horstmann")


	class Book extends Document {
	 def addChapter(chapter: String) = { ...; this }
	 ...
	}
	val book = new Book()
	book.setTitle("Scala for the Impatient").addChapter(chapter1) // Error
	def setTitle(title: String): this.type = { ...; this }

	//19.2 Type projections
	//19.3 Paths
	/*Consider a type such as	com.horstmann.impatient.chatter.Member
	or, if we nest Member inside the companion object, com.horstmann.impatient.Network.Member
	Such an expression is called a path. Each component of the path before the final type must be “stable,”
	that is, it must specify a single, definite scope. Each such component is one of the following:
	• A package
	• An object
	• A val
	• this, super, super[S], C.this, C.super, or C.super[S]
	A path component can’t be a class because, as you have seen, a nested class isn’t
	a single type, but it gives rise to a separate type for each instance.
	Moreover, a path element can’t be a var. For example,*/
	var chatter = new Network
	...
	val fred = new chatter.Member // Error—chatter is not stable
	/*Since you might assign a different value to chatter, the compiler can’t assign a
	definite meaning to the type chatter.Member

	NOTE: Internally, the compiler translates all nested type expressions a.b.c.T
	to type projections a.b.c.type#T. For example, chatter.Member becomes
	chatter.type#Member—any Member inside the singleton chatter.type. That is not
	something you generally need to worry about. However, sometimes you will
	see an error message with a type of the form a.b.c.type#T. Just translate it
	back to a.b.c.T*/


	//19.4 Type aliases
	class Book {
	 import scala.collection.mutable._
	 type Index = HashMap[String, (Int, Int)]
	 ..
	}

	 /*Then you can refer to Book.Index instead of the cumbersome type scala.collection.
	 mutable.HashMap[String, (Int, Int)].
	 A type alias must be nested inside a class or object. It cannot appear at the top
	 level of a Scala file. However, in the REPL, you can declare a type at the top level,
	 since everything in the REPL is implicitly contained in a top-level object.
	 */


	 //19.5 Structural Types (similar to Duck Typing)
	 /*A “structural type” is a specification of abstract methods, fields,
	 and types that  a conforming type should possess. For example,
	 this method has a structural type parameter:*/

	 def appendLines(target: { def append(str: String): Any },
	  lines: Iterable[String]) {
	  for (l <- lines) { target.append(l); target.append("\n") }
	 }

	 /*You can call the appendLines method with an instance of any class that has an append
	 method. This is more flexible than defining a Appendable trait, because you might
	 not always be able to add that trait to the classes you are using.
	 Under the hood, Scala uses reflection to make the calls to target.append(...).
	 Structural typing gives you a safe and convenient way of making such calls.
	 However, a reflective call is much more expensive than a regular method call.
	 For that reason, you should only use structural typing when you model common
	 behavior from classes that cannot share a trait.*/


	 //19.6 Compound Types
	 /*A compound type has the form T1 with T2 with T3 ...
	 where T1, T2, T3, and so on are types. In order to belong to the compound type,
	 a value must belong to all of the individual types. Therefore, such a type is also
	 called an intersection type.
	 You can use a compound type to manipulate values that must provide multiple
	 traits. For example,*/
	 val image = new ArrayBuffer[java.awt.Shape with java.io.Serializable]
	 //You can draw the image object as
	 for (s <- image) graphics.draw(s).
	 //You can serialize the image object because you know that all elements are serializable*/

	 //Of course, you can only add elements that are both shapes and serializable objects:
	 val rect = new Rectangle(5, 10, 20, 30)
	 image += rect // OK—Rectangle is Serializable
	 image += new Area(rect) // Error—Area is a Shape but not Serializable


	 /*You can add a structural type declaration to a simple or compound type.
	 For example, */
	 Shape with Serializable { def contains(p: Point): Boolean }
	 /*An instance of this type must be a subtype of Shape and Serializable,
	 and it must have a contains method with a Point parameter.*/
	 /*Technically, the structural type { def append(str: String): Any }
	 is an abbreviation for AnyRef { def append(str: String): Any }
	 and the compound type Shape with Serializable is a shortcut for
	 Shape with Serializable {}*/

	 //19.7 Infix Types
	 /*An infix type is a type with two type parameters, written in “infix” syntax,
	 with the type name between the type parameters. For example, you can write
	 String Map Int	 instead of	 Map[String, Int]
	 The infix notation is common in mathematics.
	 For example, A × B = { (a, b) | a Œ A,	 b Œ B } is the set of pairs
	 with components of types A and B. In Scala, this type
	 is written as (A, B). If you prefer the mathematical notation,
	 you can define	 type ×[A, B] = (A, B)
	 Then you can write String × Int instead of (String, Int).
	 All infix type operators have the same precedence.
	 As with regular operators, they are left-associative unless their names end in :.*/

	 //19.8
	 /*Existential types were added to Scala for compatibility with Java wildcards.
	 An existential type is a type expression followed by forSome { ... },
	 where the braces contain type and val declarations. For example,*/
	 Array[T] forSome { type T <: JComponent }
	 //This is the same as the wildcard type
	 Array[_ <: JComponent]
	 //that you saw in Chapter 18.
	 //Scala wildcards are syntactic sugar for existential types. For example,
	 Array[_]
	 //is the same as
	 Array[T] forSome { type T }
	 //and
	 Map[_, _]
	 //is the same as
	 Map[T, U] forSome { type T; type U }
	 /*The forSome notation allows for more complex relationships than wildcards can
	 express, for example:*/
	 Map[T, U] forSome { type T; type U <: T }
	 /*You can use val declarations in the forSome block because a val can have its own
	 nested types (see Section 19.2, “Type Projections,” on page 281). Here is an
	 example:*/
	 n.Member forSome { val n: Network }
	 /*By itself, that’s not so interesting—you could just use a type projection
	 Network#Member. But consider*/
	 def process[M <: n.Member forSome { val n: Network }](m1: M, m2: M) = (m1, m2)

	 /*This method will accept members from the same network, but reject members
	 from different ones:*/
	 val chatter = new Network
	 val myFace = new Network
	 val fred = chatter.join("Fred")
	 val wilma = chatter.join("Wilma")
	 val barney = myFace.join("Barney")
	 process(fred, wilma) // OK
	 process(fred, barney) // Error

	 //19.9 Scala Type System
	 //19.10 Self Types

	 /*In Chapter 10, you saw how a trait can require that it is mixed into a class that
	 extends another type. You define the trait with a self type declaration:*/
	 this: Type =>
	 /*Such a trait can only be mixed into a subclass of the given type. In the following
	 example, the LoggedException trait can only be mixed into a class that extends
	 Exception:*/
	 trait Logged {
	  def log(msg: String)
	 }
	 trait LoggedException extends Logged {
	 this: Exception =>
	  def log() { log(getMessage()) }
	  // OK to call getMessage because this is an Exception
	 }
	 /*If you try to mix the trait into a class that doesn’t conform to the self type, an error
	 occurs:*/
	 val f = new JFrame with LoggedException
	  // Error: JFrame isn’t a subtype of Exception, the self type of LoggedException

	//*Self types do not automatically inherit. If you define
	trait ManagedException extends LoggedException { ... }
	/*you get an error that ManagedException doesn’t supply Exception. In this
	situation, you need to repeat the self type:*/
	trait ManagedException extends LoggedException {
	 this: Exception =>
	 ...
	}


	 //19.11 Dependency Injection



	 //19.12 Abstract Types

	trait Reader {
	  type Contents
	  def read(fileName: String): Contents
	}

	class StringReader extends Reader {
	  type Contents = String
	  def read(fileName: String) = Source.fromFile(fileName, "UTF-8").mkString
	}

	class ImageReader extends Reader {
	 type Contents = BufferedImage
	 def read(fileName: String) = ImageIO.read(new File(fileName))
	}


	trait Reader[C] {
	 def read(fileName: String): C
	}

	class StringReader extends Reader[String] {
	 def read(fileName: String) = Source.fromFile(fileName, "UTF-8").mkString
	}

	class ImageReader extends Reader[BufferedImage] {
	 def read(fileName: String) = ImageIO.read(new File(fileName))
	}

	/*

	Use type parameters when the types are supplied as the class is instantiated.
	For example, when you construct a HashMap[String, Int], you want control over
	the types.

	Use abstract types when the types are expected to be supplied in a subclass.
	That is the case in our Reader example.

	*/


	 //19.13 Family Polymorphism

	 //19.14 Higher-kinded Types





}