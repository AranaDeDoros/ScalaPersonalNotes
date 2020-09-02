object Ch14 extends App {

	//pretty good book so far, very straight-forward

	//14 pattern matching

	//14.2 guards
	//Patterns are always matched top-to-bottom. If the pattern with the guard clause
	//doesn’t match, the case '+' pattern is attempted next

	//14.4 type Patterns

	/*obj match {
	 case x: Int => x
	 case s: String => Integer.parseInt(s)
	 case _: BigInt => Int.MaxValue
	 case _ => 0
	}

	When you match against a type, you must supply a variable name.
	Otherwise, you match the object:
	obj match {
	 case _: BigInt => Int.MaxValue // Matches any object of type BigInt
	 case BigInt => -1 // Matches the BigInt object of type Class

	Matches occur at runtime, and generic types are erased in the
	Java virtual machine. For that reason, you cannot make a type match for a
	specific Map type.
	case m: Map[String, Int] => ... // Don’t
	You can match a generic map:
	case m: Map[_, _] => ... // OK
	However, arrays are not erased.You can match an Array[Int].
	}*/

	//14.5 matching arrays, lists, and tuples
	arr match {
	 case Array(0) => "0"
	 case Array(x, y) => s"$x $y"
	 case Array(0, _*) => "0 ..."
	 case _ => "something else"
	}

	/*If you want to bind a variable argument match _* to a variable, use the @ notation like this:
	case Array(x, rest @ _*) => rest.min*/

	/*
	note how the variables are bound to parts of the list or tuple. Since these
	bindings give you easy access to parts of a complex structure, this operation is
	called destructuring.
	The variable names that you use in the pattern must
	start with a lowercase letter. In a match against case Array(X, Y), X and Y are
	deemed constants, not variables.

	If a pattern has alternatives, you cannot use variables other than an
	underscore. For example,
	pair match {
	 case (_, 0) | (0, _) => ... // OK, matches if one component is zero
	 case (x, 0) | (0, x) => ... // Error—cannot bind with alternatives
	}
	*/

	//14.6 Extractors

	/*extractors—objects with an unapply or
	unapplySeq method that extract values from an object..
	The unapply method is provided to extract a fixed number of objects, 
	while unapplySeq extracts a sequence whose length can vary*/

	arr match {
	 case Array(x, 0) => x
	 case Array(x, rest @ _*) => rest.min
	 ...
	}

	/*
	The Array companion object is an extractor—it defines an unapplySeq method. That
	method is called with the expression that is being matched, not with what appear to
	be the parameters in the pattern. The call Array.unapplySeq(arr), when successful,
	results in a sequence of values, namely the two values in the array. In the first
	case, the match succeeds if the array has length 2 and the second element is zero.
	In that case, the initial array element is assigned to x.
	 */

	/*Regular expressions provide another good use of extractors. When a regular expression has groups, 
	you can match each group with an extractor pattern. For example:*/

	val pattern = "([0-9]+) ([a-z]+)".r
	"99 bottles" match {
	 case pattern(num, item) => ...
	 // Sets num to "99", item to "bottles"
	}

	/*The call pattern.unapplySeq("99 bottles") yields a sequence of strings that match the groups. 
	These are assigned to the variables num and item. 
	Note that here the extractor isn’t a companion object but a regular expression object*/

	//14.7 patterns in variable declarations
	
	val (x, y) = (1, 2)
	val (q, r) = BigInt(10) %/ 3)
	val Array(first, second, rest @ _*) = arr

	/*assigns the first and second element of the array arr to the variables first and
	second and rest to a Seq of the remaining elements.*/

	/*
	val p(x1, ..., xn) = e
	is, by definition, exactly the same as
	val $result = e match { case p(x1, ..., xn) => (x1, ..., xn) }
	val x1 = $result._1
	...
	val xn = $result._n
	 */
	
	//14.8 patterns in for expressions

	import scala.collection.JavaConversions.propertiesAsScalaMap
	 // Converts Java Properties to a Scala map—just to get an interesting example
	for ((k, v) <- System.getProperties())
	 println(s"$k -> $v")

	//prints all keys with empty value, skipping over all others:
	for ((k, "") <- System.getProperties())
	 println(k)


	for ((k, v) <- System.getProperties() if v == "")
 	println(k)

 	//14.9 case classes
 	//: Use () with case class instances, no parentheses with case objects

 	//14.10 the copy method and named parameters
 	
 	abstract class Amount
	case class Dollar(value: Double) extends Amount
	case class Currency(value: Double, unit: String) extends Amount

 	val amt = Currency(29.95, "EUR")
	val price = amt.copy()

	//you can use named parameters to modify some of the properties:
	val price = amt.copy(value = 19.95) // Currency(19.95, "EUR")

	//14.11 infix notation in case clauses

	/*When an unapply method yields a pair, you can use infix notation in the case clause.
	In particular, you can use infix notation with a case class that has two parameters.

	The feature is meant for matching sequences.
	For example, every List object is either Nil or an object of the case class ::,
	defined as
	*/

	lst match { case h :: t => ... }
 	// Same as case ::(h, t), which calls ::.unapply(lst)

 	/* the ~ case class for combining pairs of parse
	results. It is also intended for use as an infix expression in case clauses:*/
 	result match { case p ~ q => ... } // Same as case ~(p, q)

 	/*
 	These infix expressions are easier to read when you have more than one. 
 	For	example, result match { case p ~ q ~ r => ... }
	is nicer than ~(~(p, q), r).
	If the operator ends in a colon, it associates right-to-left. For example,
	case first :: second :: rest means case ::(first, ::(second, rest))
 	 */
 	
 	/*
 	Infix notation works with any unapply method that returns a pair. 
 	Here is an example:
	case object +: {
	 def unapply[T](input: List[T]) =
	 if (input.isEmpty) None else Some((input.head, input.tail))
	}
	Now you can destructure lists using +:.
	1 +: 7 +: 2 +: 9 +: Nil match {
	 case first +: second +: rest => first + second + rest.length
	}
 	 */
 	
 	//14.12 matching nested structures

 	abstract class Item
	case class Article(description: String, price: Double) extends Item
	case class Bundle(description: String, discount: Double, items: Item*) extends Item

	Bundle("Father's day special", 20.0,
	 Article("Scala for the Impatient", 39.95),
	 Bundle("Anchor Distillery Sampler", 10.0,
	 Article("Old Potrero Straight Rye Whiskey", 79.95),
	 Article("Junípero Gin", 32.95)))

	def price(it: Item): Double = it match {
	 case Article(_, p) => p
	 case Bundle(_, disc, its @ _*) => its.map(price _).sum - disc
	}

	//14.13
	/*Case classes work well for structures whose makeup doesn’t change. For example,
	the Scala List is implemented with case classes. Simplifying things a bit, a list is
	essentially

	For certain kinds of classes, case classes give you exactly the right semantics.
	Some people call them value classes. For example, consider the Currency class:
	case class Currency(value: Double, unit: String)


	Case classes with variable fields are somewhat suspect, at least with respect to
	the hash code. With mutable classes, one should always derive the hash code
	from fields that are never mutated, such as an ID.

	The toString, equals, hashCode, and copy methods are not generated
	for case classes that extend other case classes.You get a compiler warning
	if one case class inherits from another. A future version of Scala may outlaw
	such inheritance altogether. If you need multiple levels of inheritance to factor
	out common behavior of case classes, make only the leaves of the inheritance
	tree into case classes
	*/

	//14.14 sealed classes

	/*When you use pattern matching with case classes, you would like the compiler
	to check that you exhausted all alternatives. To achieve this, declare the common
	superclass as sealed:
	sealed abstract class Amount
	case class Dollar(value: Double) extends Amount
	case class Currency(value: Double, unit: String) extends Amount*/

	//this is better explained in Programming In Scala, by a large margin


	//14.15 simulating enums
	/*sealed abstract class TrafficLightColor
	case object Red extends TrafficLightColor
	case object Yellow extends TrafficLightColor
	case object Green extends TrafficLightColor
	color match {
	 case Red => "stop"
	 case Yellow => "hurry up"
	 case Green => "go"
	}*/

	//14.16 option type

	//You can use pattern matching to analyze such a value.
	val alicesScore = scores.get("Alice")
	alicesScore match {
	 case Some(score) => println(score)
	 case None => println("No score")
	}
	//But frankly, that is tedious. Alternatively, you can use the isEmpty and get:
	if (alicesScore.isEmpty) println("No score")
	else println(alicesScore.get)
	//That’s tedious too. It is better to use the getOrElse method:
	println(alicesScore.getOrElse("No score"))

	/*A more powerful way of working with options is to consider them as collections
	that have zero or one element. You can visit the element with a for loop:*/
	for (score <- alicesScore) println(score)

	//14.17 partial functions

	/*A set of case clauses enclosed in braces is a partial function—a function which may
	not be defined for all inputs. It is an instance of a class PartialFunction[A, B]. 
	(A is the parameter type, B the return type.) That class has two methods: apply, 
	which computes the function value from the matching pattern, and isDefinedAt,
	which returns true if the input matches at least one of the patterns*/

	val f: PartialFunction[Char, Int] = { case '+' => 1 ; case '-' => -1 }
	f('-') // Calls f.apply('-'), returns -1
	f.isDefinedAt('0') // false
	f('0') // Throws MatchError

	"-3+4".collect { case '+' => 1 ; case '-' => -1 } // Vector(-1, 1)

	/*A Seq[A] is a PartialFunction[Int, A], and a Map[K, V] is a PartialFunction[K, V]. For
	example, you can pass a map to collect:*/

	val names = Array("Alice", "Bob", "Carmen")
	val scores = Map("Alice" -> 10, "Carmen" -> 7)
	names.collect(scores) // Yields Array(10, 7)

	/*The lift method turns a PartialFunction[T, R] into a regular function with return
	type Option[R].*/

	val f: PartialFunction[Char, Int] = { case '+' => 1 ; case '-' => -1 }
	val g = f.lift // A function with type Char => Option[Int]
	//Now g('-') is Some(-1) and g('*') is None.

	//Regex.replaceSomeIn method requires a function String => Option[String] for the replacement.

	val varPattern = """\{([0-9]+)\}""".r
	val message = "At {1}, there was {2} on {0}"
	val vars = Map("{0}" -> "planet 7", "{1}" -> "12:30 pm",
	 "{2}" -> "a disturbance of the force.")
	val result = varPattern.replaceSomeIn(message, m => vars.lift(m.matched))
	
	/*Conversely, you can turn a function returning Option[R] into a partial function by
	calling Function.unlift.*/


	/*The catch clause of the try statement is a partial function.You can
	even use a variable holding a function:
	def tryCatch[T](b: => T, catcher: PartialFunction[Throwable, T]) =
	 try { b } catch catcher
	Then you can supply a custom catch clause like this:
	val result = tryCatch(str.toInt,
	 { case _: NumberFormatException => -1 })*/
	 
}