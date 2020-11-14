object Ch18 extends App{

	//Type Parameters , don't compile

	//T <: UpperBound, T >: LowerBound, T : ContextBound

	//Covariance  is  appropriate  for  parameters  that  denote  outputs,  such  as
	//elements in an immutable collection

	//Contravariance  is  appropriate  for  parameters  that  denote  inputs,  such  as
	//function arguments.

	//**************18.1****************//

	//Generic classes
	class Pair[T, S](val first:T , val second:S)

	val p = new Pair(42, "String")

	//**************18.2****************//

	//Generic functions
	def getMiddle[T](a: Array[T]) = a(a.length / 2)
	getMiddle(Array("Mary", "had", "a", "little", "lamb"))
	val f = getMiddle[String]_

	//**************18.3****************//

	//Bounds for Type Variables
	/*class Pair[T](val first: T, val second: T){
		def smaller = if (first.compareTo(second) < 0) first else second
	}*/

	//we don’t know if  first has a compareTo method.
	//To solve this, we can add an upper bound T <: Comparable[T]
	class Pair[T <: Comparable[T]](val first: T, val second: T){
		def smaller = if (first.compareTo(second) < 0) first else second
	}

	//t must be a subtype of Comparable[T]

	/*suppose we want to define a method that replaces
	the first component of a pair with another value.*/

	class Pair[T](val first: T, val second: T) {
	  def replaceFirst(newFirst: T) = new Pair[T](newFirst, second)
	}

	/*Suppose  we  have  a Pair[Student].
	It should be possible to replace the  first component with a Person.
	Of course, then the result	must be a Pair[Person].
	In general, the replacement type must be a supertype of the pair's component type*/

	def replaceFirst[R >: T](newFirst: R) = new Pair[R](newFirst, second)
	def replaceFirst[R >: T](newFirst: R) = new Pair(newFirst, second)

	//**************18.4****************//

	//View Bounds
	class Pair[T <: Comparable[T]]

	/*Unfortunately, if you try constructing a new Pair(4, 2), the compiler complains	that
	Int is not a subtype  of Comparable[Int]. Unlike  the java.lang.Integer  wrapper type,
	the Scala 	Int	 type does not implement Comparable. However, RichInt does implement Comparable[Int],
	and there is an implicit conversion from Int to RichInt	. A solution is to use a “view bound” like this:*/

	 class Pair[T <% Comparable[T]]

	 /*<% relation means that  T can be converted to a  Comparable[T]  through an implicit conversion.
	 If  you  compile  with  the -future flag,  you'll  get  a  warning  when  you  use  them.
	 You  can  replace  a  view	 bound with a  “type constraint”*/

	//**************18.5****************//

	//Context Bounds

	/*A view bound T <% V requires the existence of an implicit conversion from T  to V.
	A context bound	 has the form T : M	, where M is another generic type.
	It requires	that there is an  implicit value of type M[T]

	class Pair[T : Ordering] requires that there is an implicit value of type Ordering[T].
	That implicit value can then be used in the methods of the class. When you declare a method that uses
	the implicit value, you have to add an “implicit parameter.”*/

	class Pair[T : Ordering](val first: T, val second: T) {
		def smaller(implicit ord: Ordering[T]) =
			if (ord.compare(first,second) < 0 ) first else second
	}

	//**************18.6****************//

	//ClassTag Contex Bound

	/*To  instantiate  a  generic Array[T],  one  needs  a 	ClassTag[T] object.  This  is  required
	for primitive type arrays to work correctly. For example, if 	T	 is 	Int	, you want an
	int[]  array in the virtual machine. If you write a generic function that constructsa generic array,
	you need to help it out and pass that class tag object. Use a context bound, like this:*/

	import scala.reflect._
	def makePair[T : ClassTag](first: T, second: T) = { val r = new Array[T](2); r(0) = first; r(1) = second; r }

	/*If  you  call makePair(4,  9),  the  compiler  locates  the  implicit 	ClassTag[Int]
	and	actually calls	makePair(4, 9)(classTag). Then the 	new operator is translated to a call classTag.newArray,
	which in the case of a ClassTag[Int] constructs a primitive array int[2].
	Why all this complexity? In the virtual machine, generic types are erased.
	There is only a single makePair method that needs to work for all types	T.*/

	//**************18.7****************//

	//Multiple Bounds

	/*A type variable can have both an upper and a lower bound. The syntax is this:
	T >: Lower <: Upper

	You can't have multiple upper or lower bounds. However, you can still require that a type implements multiple traits, like this:
	T <: Comparable[T] with Serializable with Cloneable
	You can have more than one context bound:
	T : Ordering : ClassTag*/


	//**************18.8****************//

	//Type Constraints
	/*T =:= U
	T <:< U
	T => U

	These constraints test whether T equals	U, is a subtype of U, or is convertible to U. To use such a constraint, you add an 	“
	implicit evidence parameter	” like this:*/
	class Pair[T](val first: T, val second: T)(implicit ev: T <:< Comparable[T])

	//Type constraints let you supply a method in a generic class that can be used only under certain conditions. Here is an example:

	class Pair[T](val first: T, val second: T) {
	  def smaller(implicit ev: T <:< Ordered[T]) =
	    if (first < second) first else second
	}

	/*You  can  form  a 	Pair[URL]	,  even  though 	URL	  is  not  ordered.
	You  will  get  an  error	only if you invoke the 	smaller  method.
	Another example is the 	orNull	 method in the 	Option	 class:*/

	val friends = Map("Fred" -> "Barney", ...)
	val friendOpt = friends.get("Wilma") //
	val friendOrNull = friendOpt.orNull //


	/*The orNull method can be useful when working with Java code where it is
	common to encode missing values	as null. But it can't be applied to value types such as	Int
	that dont't have null as a valid value.	Because	orNull	is implemented using aconstraint Null <:< A,
	you can still instantiate	Option[Int], as long as you stay away from orNull for those instances

	Another use of type constraints is for improving type inference. Consider*/
	def firstLast[A, C <: Iterable[A]](it: C) = (it.head, it.last)
	//When you call
	firstLast(List(1, 2, 3))
	//you  get  a  message  that  the  inferred  type  arguments  [Nothing, List[Int]] don't conform to [A, C <: Iterable[A]].
	//Why 	 Nothing	 ? The type inferencer cannot 	 fi	 gure out what A is from looking at List(1, 2, 3), because it matches A
	//and C in a singlestep. To help it along, first match C and then A:
	def firstLast[A, C](it: C)(implicit ev: C <:< Iterable[A]) =   (it.head, it.last)

	//**************18.9****************//

	//Variance
	/*If Studentis a subclass of Person, can I call makeFriendwith a Pair[Student]?
	By default,	this is an error. Even though Studentis a subtype of Person	,
	there is norelationship	between Pair[Student]and Pair[Person]*/
	class Pair[+T](val first: T, val second: T)

	/*Since Student is a subtype of Person	, a Pair[Student]is now a subtype of Pair[Person]
	It is also possible to have variance in the other direction. Consider a generic type Friend[T]
	, which denotes someone who is willing to befriend anyone of type T.*/
	trait Friend[-T] {
 		def befriend(someone: T)
	}

	def makeFriendWith(s: Student, f: Friend[Student]) { f.befriend(s) }

	class Person extends Friend[Person] {  }
	class Student extends Person
	val susan = new Student
	val fred = new Person

	/*Note that the type varies in the opposite direction of the subtype relationship.
	Student	 is a subtype of Person	, but Friend[Student] is a supertype of	Friend[Person].
	In that case, you declare the type parameter to be 	contravariant:*/
	trait Friend[-T] {
	  def befriend(someone: T)
	}

	def friends(students: Array[Student], find: Function1[Student, Person]) =
	//You can write the second parameter as find: Student => Person
	  for (s <- students) yield find(s)

	 def findStudent(p: Person) : Student

	//**************18.10****************//

	//Co- and Contravariant Positions
	/*Generally, it makes sense to use contravariance for the values an object consumes,
	and covariance for the values it produces. If an object does both,
	then the type should be left invariant.
	This is generally thecase  for  mutable  data  structures. */

	val students = new Array[Student](length)
	val people: Array[Person] = students //
	//Not legal, but suppose it was
	people(0) = new Person("Fred") //
	//students(0)	 isn't a Student
	val people = new Array[Person](length)
	val students: Array[Student] = people //
	//Not legal, but suppose it was
	people(0) = new Person("Fred") //
	//students(0)	 isn't a Student

	//Suppose we tried to declare a covariant mutable pair.
  	class Pair[+T](var first: T, var second: T) //  Error

	 /*you get an error complaining that the covariant type  T
	 occurs in a contravariant 	position in the setter  first_=(value: T)
	 Parameters are contravariant positions, and return types are covariant*/

  	 /*However,  inside  a  function  parameter,  the  variance flips — its parameters are covariant.
  	 For example, look at the  foldLeft  method of Iterable[+A]:*/
  	 foldLeft[B](z: B)(op: (B, A) => B): B

	/*Note that A is now in a covariant position
	Consider the replaceFirst method from beore*/
	class Pair[+T](val first: T, val second: T) {
	  def replaceFirst(newFirst: T) = new Pair[T](newFirst, second) // Error
	}

	//The remedy is to come up with a second type parameter for the method, like this:
	def replaceFirst[R >: T](newFirst: R) = new Pair[R](newFirst, second)


	//**************18.11****************//

	//Objects can't be generic
	abstract class List[+T] {
	  def isEmpty: Boolean
	  def head: T
	  def tail: List[T]
	}
	class Node[T](val head: T, val tail: List[T]) extends List[T] {
	  def isEmpty = false
	}
	class Empty[T] extends List[T] {
	  def isEmpty = true
	  def head = throw new UnsupportedOperationException
	  def tail = throw new UnsupportedOperationException
	}

	//you can't simply turn it into an object:
	object Empty[T] extends List[T] //Error

	object Empty extends List[Nothing]
	val lst = new Node(42, Empty)

	//**************18.12****************//

    /*In Java, all generic types are invariant.
    However, you can vary the types where you use them,
    using wildcards. For example, a method void makeFriends(List<? extends Person> people)
    //This is Java  can be called with a  List<Student>.
    You can use wildcards in Scala too. They look like this:*/
    def process(people: java.util.List[_ <: Person]) = ???//
    //This is Scala  In Scala, you don''t need the wildcard for a covariant Pair
    class. But suppose  Pair  is invariant:
    class Pair[T](var first: T, var second: T)  //Then you can define
    def makeFriends(p: Pair[_ <: Person]) //OK to call with a     Pair[Student]
    //You can also use wildcards for contravariance:
    import java.util.Comparator
    def min[T](p: Pair[T])(comp: Comparator[_ >: T])

}