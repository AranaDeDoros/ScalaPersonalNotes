object Ch10 extends App {

	//10.5
	trait TimestampLogger extends ConsoleLogger {
	 override def log(msg: String) {
	 super.log(s"${java.time.Instant.now()} $msg")}
	}

	//10.11

	val acct = new SavingsAccount with FileLogger {
	 val filename = "myapp.log" // Does not work
	}

	val acct = new { // Early definition block after new
	 val filename = "myapp.log"
	} with SavingsAccount with FileLogger

	//If you need to do the same in a class, the syntax looks like this:
	class SavingsAccount extends { // Early definition block after extends
	 val filename = "savings.log"
	} with Account with FileLogger {
	 //... // SavingsAccount implementation
	}

	//Another alternative is to use a lazy value in the FileLogger constructor, like this:
	trait FileLogger extends Logger {
	 val filename: String
	lazy val out = new PrintStream(filename)
	 def log(msg: String) { out.println(msg) } // No override needed
	}

	/*Then the out field is initialized when it is first used. At that time, the filename field
	will have been set. However, lazy values are somewhat inefficient because they
	are checked for initialization before every use.*/

	//11
	//11.1
	//The identifiers @ # : = _ => <- <: <% >: fi ¨ are reserved in the specification, and you cannot redefine them.
	
	//11.3
	val result = 42 toString
	println(result)
	/*yields the error message “too many arguments for method toString”. Since
	parsing precedes type inference and overload resolution, the compiler does
	not yet know that toString is a unary method. Instead, the code is parsed as
	val result = 42.toString(println(result)).
	 */
	
	//11.7
	/*
	 If f is not a function or method, then this expression is equivalent to the call f.apply(arg1, arg2, ...)
	unless it occurs to the left of an assignment. The expression f(arg1, arg2, ...) = value corresponds to the call
	f.update(arg1, arg2, ..., value)
	This mechanism is used in arrays and maps. For example,	val scores = new scala.collection.mutable.HashMap[String, Int]
	scores("Bob") = 100 // Calls scores.update("Bob", 100)	val bobsScore = scores("Bob") // Calls scores.apply("Bob")
	The apply method is also commonly used in companion objects to construct objects without calling new. For example, consider a Fraction class
	 */
	
	//11.8
	/*An unapply method retrieves the numerator and denominator. You can use it in a variable definition*/
	var Fraction(a, b) = Fraction(3, 4) * Fraction(2, 5)
	 // a, b are initialized with the numerator and denominator of the result or a pattern match
	case Fraction(a, b) => ... // a, b are bound to the numerator and denominator
	//(See Chapter 14 for more information about pattern matching.)
	/*In general, a pattern match can fail. Therefore, the unapply method returns an
	Option. It contains a tuple with one value for each matched variable. In our case,
	we return an Option[(Int, Int)].*/
	object Fraction {
	 def unapply(input: Fraction) =
	 if (input.den == 0) None else Some((input.num, input.den))
	}

	//11.9
	object Number {
	 def unapply(input: String): Option[Int] =
	 try {
	 Some(input.trim.toInt)
	 } catch {
	 case ex: NumberFormatException => None
	 }
	}

	val Number(n) = "1729"

	//11.10
	object Name {
	 def unapplySeq(input: String): Option[Seq[String]] =
	 if (input.trim == "") None else Some(input.trim.split("\\s+").toIndexedSeq)
	}


	val author = "Illya von Eiznbern"

	author match {
	 case Name(first, last) => println(first)
	 case Name(first, middle, last) => println(middle)
	 case Name(first, "van der", last) => println("van der")
	}


	//11.11
	//dynamic invocation
	/*
	Dynamic types are an “exotic” feature, and the compiler wants your
	explicit consent when you implement such a type.You do that by adding the
	import statement
	import scala.language.dynamics
	 */	

	/* 
	Consider obj.name, where obj belongs to a class that’s a subtype of Dynamic. 
	Here is what the Scala compiler does with it.
	1. If name is a known method or field of obj, it is processed in the usual way.
	2. If obj.name is followed by (arg1, arg2, ...),
	a. If none of the arguments are named (of the form name=arg), pass the arguments on to applyDynamic:
	obj.applyDynamic("name")(arg1, arg2, ...)
	b. If at least one of the arguments is named, pass the name/value pairs on to applyDynamicNamed:
	obj.applyDynamicNamed("name")((name1, arg1), (name2, arg2), ...)
	Here, name1, name2, and so on are strings with the argument names, or "" for unnamed arguments.
	3. If obj.name is to the left of an =, call	obj.updateDynamic("name")(rightHandSide)
	4. Otherwise call obj.selectDynamic("sel")
	*/

}