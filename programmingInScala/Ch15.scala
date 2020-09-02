object Ch15 extends App {

	//15 Pattern matching

	//15.1
	/*abstract class Expr
	case class Var(name: String) extends Expr
	case class Number(num: Double) extends Expr
	case class UnOp(operator: String, arg: Expr) extends Expr
	case class BinOp(operator: String,
	left: Expr, right: Expr) extends Expr*/

 	/*val ${1:pattern} = "$2".r .r
 	import scala.collection.parallel.CollectionConverters._
 	s$ s"${$1}" imppar*/

 	//15.2 kinds of patterns

 	/*
 	
 	case _ wildcard
 	case 5 constant
 	case something variable 
 	case x:Int typed
 	case Person("anne", 20) constructor
 	case List(0, _, _) sequence
 	case (a,b) tuple

 	 */
 	
 	/*Scala uses a simple lexical rule for disambiguation: a simple
	name starting with a lowercase letter is taken to be a pattern variable; 
	all other references are taken to be constants*/

	//isInstanceOf typeof
	//asInstanceOf cast

	//type erasure
	def isIntIntMap(x: Any): Boolean = {
		x match {
			case m:Map[Int, Int] => true
			case _ => false
		}
	}

	println(isIntIntMap(Map(1 -> 1)))
	println(isIntIntMap(Map("abc" -> "abc")))

	/*Scala uses the Java model of generics, so no information about the values is mantained at runtime
	no way to determine whether it was instantiated with a specific type rather than with
	arguments of different type*/


	//Arrays however, do mantain info abut its type. So they're exempt from this:
	def isStringArray(x: Any): Boolean = {
		x match {
			case a: Array[String] => true
			case _ => false
		}
	}

	val as = Array("abc")
	println(isStringArray(as))
	val ai = Array(1,2,3)
	println(isStringArray(ai))

	//variable binding

	/*expr match {
		case UnOp("abs", e @ UnOp("abs", _)) => e
		case _ =>
	}*/

	//15.3 pattern guards
	/*def simplifyAdd(e: Expr) = e match {
		case BinOp("+", x, x) => BinOp("*", x, Number(2))
		case _ => e
	}*/

	//This fails because Scala restricts patterns to be linear: 
	//a pattern variable may only appear once in a pattern.

	def simplifyAdd(e: Expr) = e match {
		case BinOp("+", x, y) if x == y =>
		 BinOp("*", x, Number(2))
		case _ => e
	}

	//15.4 pattern overlaps 
	def simplifyAll(expr: Expr): Expr = expr match {
		case UnOp("-", UnOp("-", e)) =>
		 simplifyAll(e) // `-' is its own inverse
		case BinOp("+", e, Number(0)) =>
		 simplifyAll(e) // `0' is a neutral element for `+'
		case BinOp("*", e, Number(1)) =>
		 simplifyAll(e) // `1' is a neutral element for `*'
		case UnOp(op, e) =>
		 UnOp(op, simplifyAll(e))
		case BinOp(op, l, r) =>
		 BinOp(op, simplifyAll(l), simplifyAll(r))
		case _ => expr
 	}

 	//15.5 sealed classes

 	//by adding sealed to the superclass, all its children will be match-exhaustive
 	sealed abstract class Expr 
	case class Var(name: String) extends Expr
	case class Number(num: Double) extends Expr
	case class UnOp(operator: String, arg: Expr) extends Expr
	case class BinOp(operator: String,
	left: Expr, right: Expr) extends Expr

	def describe(exp: Expr): String = {
		(exp: @unchecked) match { //the unchekced annotation makes the warning go away
			case Number(_) => "number"
			case Var(_) => "variable"
			//case _ => throw RunTimeException //not ideal
		}
	}


	//15.6 option type

	//Scala For the Impatient alternatives listed for this topic are pretty good
	val capitals = Map("France" -> "Paris", "Japan"-> "Tokyo")
	println(capitals.get("France"))
	println(capitals.get("USA"))

	def show(x: Option[String]) = x match {
		case Some(s) =>  s
		case None => "?"
	}

	println(show(capitals.get("France")))
	println(show(capitals.get("USA")))


	//15.7 patterns everywhere

	val myTuple = ("abc",1)
	val (string, number) = myTuple
	println(myTuple)
	println(s"$string , $number")

	val exp = new BinOp("*", Number(5), Number(1))
	val BinOp(op, left, right) = exp
	println(exp)
	println(s"$op, $left, $right")

	//case sequences as partial functions

	//a sequence of cases in curly braces can be used anywhere a function literal is valid
	//a case sequence is a function literal, only more general
	//basically it's a partial function because it works only for a finite set of inputs

	val withDefault: Option[Int] => Int = {
		case Some(x) => x
		case None => 0
	}

	println(withDefault(Some(10)))
	println(withDefault(None))

	//not exhaustive, missing case for Nil
	val second: List[Int] => Int = {
		case x :: y :: _ => y
	}

	println(second(List(5,6,7)))
	//println(second(List()))

	val secondd: PartialFunction[List[Int],Int] = {
	 case x :: y :: _ => y
	}

	println(secondd.isDefinedAt(List(5,6,7)))
	println(secondd.isDefinedAt(List()))	

	/*such an expression gets translated by the Scala compiler to a partial function
	by translating the patterns twice—once for the implementation of the real function, 
	and once to test whether the function is defined or not.*/

	//{ case x :: y :: _ => y }
	/*new PartialFunction[List[Int], Int] {
		 def apply(xs: List[Int]) = xs match {
		 	case x :: y :: _ => y
		 }
		 def isDefinedAt(xs: List[Int]) = xs match {
			 case x :: y :: _ => true
			 case _ => false
		 }

	In general, you should try to work with complete functions whenever possible, 
	because using partial	functions allows for runtime errors that the compiler cannot help you with. 
	Sometimes partial functions	are really helpful though. 
	You might be sure that an unhandled value will never be supplied. 
	}*/

	//patterns in for expressions

	for((country, city) <- capitals) println(s"the capital of $country is $city")

	val results = List(Some("apple"), None, Some("orange"))
	for(Some(fruit) <- results) 
		println(fruit)

	//15.8 skipping this for now
}