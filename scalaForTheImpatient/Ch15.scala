object Ch15 extends App {

	//15 Annotations
	//don't compile

	/*You can annotate classes, methods, fields, local variables, parameters,
	expressions, type parameters, and types.
	• With expressions and types, the annotation follows the annotated item.
	• Annotations have the form @Annotation, @Annotation(value), or @Annotation(name1 =
	value1, ???).
	• @volatile, @transient, @strictfp, and @native generate the equivalent Java modifiers.
	• Use @throws to generate Java-compatible throws specifications.
	• The @tailrec annotation lets you verify that a recursive function uses tail call
	optimization.
	• The assert function takes advantage of the @elidable annotation. You can
	optionally remove assertions from your Scala programs.
	• Use the @deprecated annotation to mark deprecated features*/

	//15.2
	//what can be annotated?

	/*@Entity class Credentials
	@Test def testSomeFeature() {}
	@BeanProperty var username = _
	def doSomething(@NotNull message: String) {}*/


	class Credentials @Inject() (var username: String, var password: String)
	//(myMap.get(key): @unchecked) match { ??? }
	class MyContainer[@specialized T]
	//Annotations on an actual type are placed after the type, like this:
	def country: String @Localized

	//15.3
	//annotation arguments

	@Test(timeout = 100, expected = classOf[IOException])
	@Named("creds") var credentials: Credentials = _

	//Most annotation arguments have defaults.
	/*Numeric literals
	Strings
	Class literals
	Java enumerations
	Other annotations
	Arrays of the above (but not arrays of arrays)*/


	//15.4 annotation implementations

	/*
	An annotation must extend the Annotation trait. For example, the unchecked annotation
	is defined as follows:*/
	class unchecked extends annotation.Annotation
	//A type annotation must extend the TypeAnnotation trait:
	class Localized extends StaticAnnotation with TypeConstraint

	//field definitions in Scala can give rise to multiple features in Java, all of which can potentially be annotated.
	class Credentials(@NotNull @BeanProperty var username: String)
	/*
	@param, @field, @getter, @setter, @beanGetter, and @beanSetter cause an annotation to be
	attached elsewhere. For example, the @deprecated annotation is defined as:
	 */

	@getter @setter @beanGetter @beanSetter
	class deprecated(message: String = "", since: String = "") extends annotation.StaticAnnotation

	@Entity class Credentials {
	 @(Id @beanGetter) @BeanProperty var id = 0
	 	???
	}

	//15.5 annotation for Java features

	//15.5.1 java modifiers
	//Scala uses annotations instead of modifier keywords for some of the less commonly used Java features.
	//The @volatile annotation marks a field as volatile:
	@volatile var done = false // Becomes a volatile field in the JVM
	//A volatile field can be updated in multiple threads.
	//The @transient annotation marks a field as transient:
	@transient var recentLookups = new HashMap[String, String]
	 // Becomes a transient field in the JVM
	/*/A transient field is not serialized. This makes sense for cache data that need not
	be saved, or data that can easily be recomputed.
	The @strictfp annotation is the analog of the Java strictfp modifier:*/
	@strictfp def calculate(x: Double) = ???
	/*This method does its floating-point calculations with IEEE double values, not using
	the 80 bit extended precision (which Intel processors use by default). The result
	is slower and less precise but more portable.
	The @native annotation marks methods that are implemented in C or C++ code.
	It is the analog of the native modifier in Java.*/
	@native def win32RegKeys(root: Int, path: String): Array[String]


	//15.5.2 marker interfaces
	/*Scala uses annotations @cloneable and @remote instead of the Cloneable and
	java.rmi.Remote marker interfaces for cloneable and remote objects.*/
	@cloneable class Employee
	/*With serializable classes, you can use the @SerialVersionUID annotation to specify
	the serial version:*/
	@SerialVersionUID(6157032470129070425L)
	class Employee extends Person with Serializable



	//15.5.3 checked exceptions

	/*Unlike Scala, the Java compiler tracks checked exceptions. If you call a Scala
	method from Java code, its signature should include the checked exceptions that
	can be thrown. */
	class Book {
	@throws(classOf[IOException]) def read(filename: String) { ??? }
	 	???
	}
	//The Java signature is
	//void read(String filename) throws IOException
	/*Without the @throws annotation, the Java code would not be able to catch the
	exception.*/
	try { // This is Java
	 	book.read("war-and-peace.txt");
	} catch (IOException ex) {
	 	ex
	}


	//15.5.4 variable arguments
	//The @varargs annotation lets you call a Scala variable-argument method from Java.
	def process(args: String*)
	//the Scala compiler translates the variable argument into a sequence
	def process(args: Seq[String])
	//That is cumbersome to use in Java. If you add @varargs,
	@varargs def process(args: String*)
	//then a Java method
	//void process(String??? args) // Java bridge method
	//is generated that wraps the args array into a Seq and calls the Scala method


	//15.5.5 beans
	class Person {
	 	@BeanProperty var name : String = _
	}
	/*generates methods
	getName() : String
	setName(newValue : String) : Unit
	The @BooleanBeanProperty annotation generates a getter with an is prefix for a Boolean
	method.*/


	//15.6 annotations for optimizations
	//15.6.1 tail recursion
	/*A recursive call can sometimes be turned into a loop, which conserves stack
	space. This is important in functional programming where it is common to write
	recursive methods for traversing collections.*/
	object Util {
		def sum(xs: Seq[Int]): BigInt =
		if (xs.isEmpty) 0 else xs.head + sum(xs.tail)
		???
	}

	/*This method cannot be optimized because the last step of the computation is
	addition, not the recursive call. But a slight transformation can be optimized:*/
	def sum2(xs: Seq[Int], partial: BigInt): BigInt =
	 	if (xs.isEmpty) partial else sum2(xs.tail, xs.head + partial)


	/*If you rely on the compiler to remove the recursion,
	you should annotate your method with @tailrec.
	Then, if the compiler cannot apply the optimization, it will report an error.*/

	class Util {
	@tailrec def sum2(xs: Seq[Int], partial: BigInt): BigInt =
	 	if (xs.isEmpty) partial else sum2(xs.tail, xs.head + partial)
	}
	/*Now the program fails with an error message "could not optimize @tailrec annotated
	method sum2: it is neither private nor final so can be overridden". In this situation, you
	can move the method into an object, or you can declare it as private or final.*/


	/*A more general mechanism for recursion elimination is “trampolining”.
	A trampoline implementation runs a loop that keeps calling functions. Each
	function returns the next function to be called.Tail recursion is a special case
	where each function returns itself. The more general mechanism allows for
	mutual calls—see the example that follows.
	Scala has a utility object called TailCalls that makes it easy to implement a
	trampoline. The mutually recursive functions have return type TailRec[A] and
	return either done(result) or tailcall(fun) where fun is the next function to be
	called.This needs to be a parameterless function that also returns a TailRec[A].
	Here is a simple example:
	*/
	import scala.util.control.TailCalls._
	def evenLength(xs: Seq[Int]): TailRec[Boolean] =
	 	if (xs.isEmpty) done(true) else tailcall(oddLength(xs.tail))
	def oddLength(xs: Seq[Int]): TailRec[Boolean] =
	 	if (xs.isEmpty) done(false) else tailcall(evenLength(xs.tail))
		//To obtain the final result from the TailRec object, use the result method:
		evenLength(1 to 1000000).result


	//15.6.2 jump table generation and inlining
	/*In C++ or Java, a switch statement can often be compiled into a jump table, which
	is more efficient than a sequence of if/else expressions
	The @switch annotation lets you check
	whether a Scala match clause is indeed compiled into one.*/
	(2: @switch) match {
		 case 0 => "Zero"
		 case 1 => "One"
		 case _ => "?"
	}

	/*A common optimization is method inlining—replacing a method call with the
	method body. You can tag methods with @inline to suggest inlining, or @noinline
	to suggest not to inline.*/


	//15.6.3 eliding methods
	/*The @elidable annotation flags methods that can be removed in production code.
	For example,*/
	@elidable(500) def dump(props: Map[String, String]) { ??? }
	/*If you compile with
	scalac -Xelide-below 800 myprog.scala
	then the method code will not be generated. The elidable object defines the
	following numerical constants:


	MAXIMUM or OFF = Int.MaxValue
	• ASSERTION = 2000
	• SEVERE = 1000
	• WARNING = 900
	• INFO = 800
	• CONFIG = 700
	• FINE = 500
	• FINER = 400
	• FINEST = 300
	• MINIMUM or ALL = Int.MinValue
	You can use one of these constants in the annotation:*/

	import scala.annotation.elidable._
	@elidable(FINE) def dump(props: Map[String, String]) { ??? }
	//You can also use these names in the command line:
	scalac -Xelide-below INFO myprog.scala
	/*If you don’t specify the -Xelide-below flag, annotated methods with values below
	1000 are elided, leaving SEVERE methods and assertions, but removing warnings.


	The levels ALL and OFF are potentially confusing. The annotation
	@elide(ALL) means that the method is always elided, and @elide(OFF) means
	that it is never elided. But -Xelide-below OFF means to elide everything, and
	-Xelide-below ALL means to elide nothing.That’s why MAXIMUM and MINIMUM have
	been added.*/

	//The Predef object defines an elidable assert method. For example,
	def makeMap(keys: Seq[String], values: Seq[String]) = {
		 assert(keys.length == values.length, "lengths don't match")
	 	???
	}
	/*If the method is called with mismatched arguments, the assert method throws
	an AssertionError with message assertion failed: lengths don't match.
	To disable assertions, compile with -Xelide-below 2001 or -Xelide-below MAXIMUM. Note
	that by default assertions are not disabled. This is a welcome improvement over
	Java assertions

	Calls to elided methods are replaced with Unit objects. If you use
	the return value of an elided method, a ClassCastException is thrown. It is best
	to use the @elidable annotation only with methods that don’t return a value.
	*/


	//15.6.4 specialization for Primitive types
	/*It is inefficient to wrap and unwrap primitive type values—but in generic code,
	this often happens. Consider, for example,*/
	def allDifferent[T](x: T, y: T, z: T) = x != y && x != z && y != z
	/*If you call allDifferent(3, 4, 5), each integer is wrapped into a java.lang.Integer
	before the method is called. Of course, one can manually supply an overloaded
	version*/
	def allDifferent(x: Int, y: Int, z: Int) = ???
	/*as well as seven more methods for the other primitive types.
	You can generate these methods automatically by annotating the type parameter
	with @specialized:*/
	def allDifferent[@specialized T](x: T, y: T, z: T) = ???
	//You can restrict specialization to a subset of types:
	def allDifferent[@specialized(Long, Double) T](x: T, y: T, z: T) = ???

	/*In the annotation constructor, you can provide any subset of Unit, Boolean, Byte,
	Short, Char, Int, Long, Float, Double.*/


	//15.7 Annotations for Errors and Warnings
	/*If you mark a feature with the @deprecated annotation, the compiler generates a
	warning whenever the feature is used. The annotation has two optional
	arguments, message and since.*/
	@deprecated(message = "Use factorial(n: BigInt) instead")
	def factorial(n: Int): Int = ???
	/*The @deprecatedName is applied to a parameter, and it specifies a former name for
	the parameter.*/
	def draw(@deprecatedName('sz) size: Int, style: Int = NORMAL)
	//You can still call draw(sz = 12) but you will get a deprecation warning

	/*The @deprecatedInheritance and @deprecatedOverriding annotations generate warnings
	that inheriting from a class or overriding a method is now deprecated.
	The @implicitNotFound and @implicitAmbiguous annotations generates meaningful error
	messages when an implicit value is not available or ambiguous.
	for details about implicits.*/

	/*The @uncheckedVariance annotation suppresses a variance error message. For example,
	it would make sense for java.util.Comparator to be contravariant. If Student is a
	subtype of Person, then a Comparator[Person] can be used when a Comparator[Student]
	is required. However, Java generics have no variance. We can fix this with the
	@uncheckedVariance annotation:*/

	trait Comparator[-T] extends{
	 java.lang.Comparator[T @uncheckedVariance]
	}

	//16 -> XML, skipping
}