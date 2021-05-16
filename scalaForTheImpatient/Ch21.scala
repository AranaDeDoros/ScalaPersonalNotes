object Ch21	extends App{

	//21 Implicits

		//21.1 Implicit conversions
		/*An implicit conversion function is a function with a single parameter that is declared
		 with the implicit keyword. As the name suggests, such a function is automatically
		 applied to convert values from one type to another.
		 Consider the Fraction class from Section 11.2, “Infix Operators,” on page 143 with
		 a method * for multiplying a fraction with another. We want to convert integers
		 n to fractions n / 1.*/
		 implicit def int2Fraction(n: Int) = Fraction(n, 1)
		 //Now we can evaluate
		 val result = 3 * Fraction(4, 5) // Calls int2Fraction(3)

		//21.2 Using Implicits for Enriching Existing Classes

		//In Scala, you can define an enriched class that provides what you want:
		 class RichFile(val from: File) {
		  def read = Source.fromFile(from.getPath).mkString
		 }
		 implicit def file2RichFile(from: File) = new RichFile(from)

		/*Now it is possible to call read on a File object. It is implicitly converted to a RichFile.
		 Instead of providing a conversion function, you can declare RichFile as an implicit
		 class:*/
		 implicit class RichFile(val from: File) { ... }

		/*An implicit class must have a primary constructor with exactly one argument.
		 That constructor becomes the implicit conversion function.
		 It is a good idea to declare the enriched class as a value class:*/
		 implicit class RichFile(val from: File) extends AnyVal { ... }
		/*In that case, no RichFile objects are created. A call file.read is directly compiled
		 into a static method call RichFile$.read$extension(file).*/

		//21.3
		 /*Scala will consider the following implicit conversion functions:
		 1. Implicit functions or classes in the companion object of the source or target
		 type
		 2. Implicit functions or classes that are in scope

		 For an implicit conversion to be in scope, it must be imported without a prefix.
		 For example, if you import com.horstmann.impatient.FractionConversions or com.horstmann,
		 then the int2Fraction method is in scope as FractionConversions.int2Fraction or
		 impatient.FractionConversions.int2Fraction, to anyone who wants to call it explicitly.
		 But if the function is not available as int2Fraction, without a prefix, the compiler
		 won’t use it implicitly.

		 You can localize the import to minimize unintended conversions. For example,*/
		 object Main extends App {
		  import com.horstmann.impatient.FractionConversions._
		  val result = 3 * Fraction(4, 5) // Uses imported conversion
		  println(result)
		 }

		/*You can even select the specific conversions that you want. Suppose you have a
		 second conversion*/
		 object FractionConversions {
		  ...
		  implicit def fraction2Double(f: Fraction) = f.num * 1.0 / f.den
		}

		 //If you prefer this conversion over int2Fraction, you can import it:
		import com.horstmann.impatient.FractionConversions.fraction2Double
		 val result = 3 * Fraction(4, 5) // result is 2.4
		 //You can also exclude a specific conversion if it causes you trouble:
		import com.horstmann.impatient.FractionConversions.{fraction2Double => _, _}
		  // Imports everything but fraction2Double

		//21.4 Rules for implicit conversions
		/*Implicit conversions are considered in three distinct situations:
		• If the type of an expression differs from the expected type:
		3 * Fraction(4, 5) // Calls fraction2Double
		The Int class doesn’t have a method *(Fraction), but it has a method *(Double).
		• If an object accesses a nonexistent member:
		3.den // Calls int2Fraction
		The Int class doesn’t have a den member but the Fraction class does.
		• If an object invokes a method whose parameters don’t match the given
		arguments:
		Fraction(4, 5) * 3
		// Calls int2Fraction
		The * method of Fraction doesn’t accept an Int but it accepts a Fraction.
		On the other hand, there are three situations when an implicit conversion is not
		attempted:
		• No implicit conversion is used if the code compiles without it. For example,
		if a * b compiles, the compiler won’t try a * convert(b) or convert(a) * b.
		• The compiler will never attempt multiple conversions, such as convert1(
		convert2(a)) * b.
		• Ambiguous conversions are an error. For example, if both convert1(a) * b and
		convert2(a) * b are valid, the compiler will report an error.

		If you want to find out which implicit conversion the compiler uses, compile your program as
		scalac -Xprint:typer MyProg.scala*/

		//21.5 Implicit parameters
		case class Delimiters(left: String, right: String)
		def quote(what: String)(implicit delims: Delimiters) =
		 	delims.left + what + delims.right

		object FrenchPunctuation {
		 implicit val quoteDelimiters = Delimiters("«", "»")
		 ...
		}

		import FrenchPunctuation.quoteDelimiters
		quote("Bonjour le monde")


		//21.6 implicit conversion with implicit parameters
		def smaller[T](a: T, b: T) = if (a < b) a else b // Not quite


		def smaller[T](a: T, b: T)(implicit order: T => Ordered[T])
		 = if (order(a) < b) a else bz


		//21.7 Context Bounds
		/*A type parameter can have a context bound of the form T : M, where M is another
		generic type. It requires that there is an implicit value of type M[T] in scope.*/
		class Pair[T : Ordering](val first: T, val second: T) {
		def smaller(implicit ord: Ordering[T]) =
			if (ord.compare(first, second) < 0) first else second
		}

		/*If we form a new Pair(40, 2), then the compiler infers that we want a Pair[Int]. Since
		there is an implicit value of type Ordering[Int] in the Ordering companion object, Int
		fulfills the context bound. That ordering becomes a field of the class, and it is
		passed to the methods that need it.
		If you prefer, you can retrieve the ordering with the implicitly method in the Predef
		class:*/
		class Pair[T : Ordering](val first: T, val second: T) {
		def smaller =
			if (implicitly[Ordering[T]].compare(first, second) < 0) first else second
		}

		//The implicitly function is defined as follows in Predef.scala:
		def implicitly[T](implicit e: T) = e

		/*Alternatively, you can take advantage of the fact that the Ordered trait defines an
		implicit conversion from Ordering to Ordered. If you import that conversion, you
		can use relational operators:*/
		class Pair[T : Ordering](val first: T, val second: T) {
		def smaller = {
		import Ordered._;
			if (first < second) first else second
		}
		}

		/*These are just minor variations; the important point is that you can instantiate
		Pair[T] whenever there is an implicit value of type Ordering[T]. For example, if you
		want a Pair[Point], arrange for an implicit Ordering[Point] value:*/
		implicit object PointOrdering extends Ordering[Point] {
			def compare(a: Point, b: Point) = ...
		}

		//21.8 Type Classes
		/*A trait such as Ordering is called a type class. A type class defines some behavior,
		and a type can join the class by providing that behavior. (The term comes from
		Haskell, and “class” is not used in the same way as in object-oriented programming. Think of “class” as in a “class action”—types banding together for a
		common purpose.)*/

		trait NumberLike[T] {
		 def plus(x: T, y: T): T
		 def divideBy(x: T, n: Int): T
		}

		object NumberLike {
		 implicit object NumberLikeDouble extends NumberLike[Double] {
		 def plus(x: Double, y: Double) = x + y
		 def divideBy(x: Double, n: Int) = x / n
		 }
		 implicit object NumberLikeBigDecimal extends NumberLike[BigDecimal] {
		 def plus(x: BigDecimal, y: BigDecimal) = x + y
		 def divideBy(x: BigDecimal, n: Int) = x / n
		 }
		}

		/*Here, we’ll just compute the average of two values. The general case is left as an
		exercise. There are two ways in which we can supply the type class instance: as
		an implicit parameter, or with a context bound. Here is the first approach:*/
		def average[T](x: T, y: T)(implicit ev: NumberLike[T]) =
		 ev.divideBy(ev.plus(x, y), 2)
		/*The parameter name ev is shorthand for “evidence”—see the next section.
		When using a context bound, we retrieve the implicit object from the “nether
		world.”*/
		def average[T : NumberLike](x: T, y: T) = {
		 val ev = implicitly[NumberLike[T]]
		 ev.divideBy(ev.plus(x, y), 2)
		}

		/*It must provide an implicit object, just like the NumberLikeDouble and
		NumberLikeBigDecimal objects that we provided out of the gate. Here is how Point
		can join:*/
		class Point(val x: Double, val y: Double) {
		 ...
		}
		object Point {
		 def apply(x: Double, y: Double) = new Point(x, y)
		 implicit object NumberLikePoint extends NumberLike[Point] {
		 def plus(p: Point, q: Point) = Point(p.x + q.x, p.y + q.y)
		 def divideBy(p: Point, n: Int) = Point(p.x * 1.0 / n, p.y * 1.0 / n)
		 }
		}

		/*Here we added the implicit object to the companion object of Point. If you can’t
		modify the Point class, you can put the implicit object elsewhere and import it as
		needed.*/

		//21.9 Evidence
		//In Chapter 18, you saw the type constraints
		T =:= U
		T <:< U
		T => U

		/*The constraints test whether T equals U, is a subtype of U, or is convertible to U. To
		use such a type constraint, you supply an implicit parameter, such as*/
		def firstLast[A, C](it: C)(implicit ev: C <:< Iterable[A]) =
		 (it.head, it.last)

		/*The =:= and <:< are classes with implicit values, defined in the Predef object. For
		example, <:< is essentially*/
		abstract class <:<[-From, +To] extends Function1[From, To]
		object <:< {
		 implicit def conforms[A] = new (A <:< A) { def apply(x: A) = x }
		}

		/*Suppose the compiler processes a constraint implicit ev: String <:< AnyRef. It looks
		in the companion object for an implicit object of type String <:< AnyRef. Note that
		<:< is contravariant in From and covariant in To. Therefore the object
		<:<.conforms[String]
		is usable as a String <:< AnyRef instance. (The <:<.conforms[AnyRef] object is also usable,
		but it is less specific and therefore not considered.)
		We call ev an “evidence object”—its existence is evidence of the fact that, in this
		case, String is a subtype of AnyRef.
		Here, the evidence object is the identity function. To see why the identity function
		is required, have a closer look at*/
		def firstLast[A, C](it: C)(implicit ev: C <:< Iterable[A]) =
		 (it.head, it.last)
		/*The compiler doesn’t actually know that C is an Iterable[A]—recall that <:< is not
		a feature of the language, but just a class. So, the calls it.head and it.last are not
		valid. But ev is a function with one parameter, and therefore an implicit conversion
		from C to Iterable[A]. The compiler applies it, computing ev(it).head and ev(it).last.*/


		/*//21.10 The @implicitNotFound Annotation
		The @implicitNotFound annotation raises an error message when the compiler cannot
		construct an implicit parameter of the annotated type. The intent is to give a
		useful error message to the programmer. For example, the <:< class is annotated as*/
		@implicitNotFound(msg = "Cannot prove that ${From} <:< ${To}.")
		/*abstract class <:<[-From, +To] extends Function1[From, To]
		For example, if you call
		firstLast[String, List[Int]](List(1, 2, 3))
		then the error message is
		 Cannot prove that List[Int] <:< Iterable[String]
		That is more likely to give the programmer a hint than the default
		Could not find implicit value for parameter ev: <:<[List[Int],Iterable[String]]
		Note that ${From} and ${To} in the error message are replaced with the type
		parameters From and To of the annotated class.*/

		//21.11 CanBuildFrom Demystified
		//


}