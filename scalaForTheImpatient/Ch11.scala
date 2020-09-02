object Ch11 extends App {

	//Operators
	
	//11.8 Extractors
	var Fraction(a, b) = Fraction(3, 4) * Fraction(2, 5)
 	// a, b are initialized with the numerator and denominator of the result or a pattern match
	case Fraction(a, b) => ... // a, b are bound to the numerator and denominator

	object Fraction {
	 def unapply(input: Fraction) =
	 if (input.den == 0) None else Some((input.num, input.den))
	}

	val Fraction(a, b) = f;

	val tupleOption = Fraction.unapply(f)
	if (tupleOption == None) throw new MatchError
	// tupleOption is Some((t1, t2))
	val a = t1
	val b = t1

	/*Note that in the declaration
	val Fraction(a, b) = f;	neither the Fraction.apply method nor the Fraction constructor are called. 
	Instead, the statement means:“Initialize a and b so that if they would be passed
	to Fraction.apply, the result would be f.”*/

	val author = "Cay Horstmann"
	val Name(first, last) = author // Calls Name.unapply(author)

	object Name {
	 def unapply(input: String) = {
		 val pos = input.indexOf(" ")
		 if (pos == -1) None
		 else Some((input.substring(0, pos), input.substring(pos + 1)))
	 }
	}

	//11.9 Extractors with One or No Arguments 
	object Number {
	 def unapply(input: String): Option[Int] =
		 try {
		 Some(input.trim.toInt)
		 } catch {
		 case ex: NumberFormatException => None
		 }
	}

	val Number(n) = "1729"

	object IsCompound {
	 def unapply(input: String) = input.contains(" ")
	}
 
 	author match {
	 case Name(first, IsCompound()) => println(first, "IsCompound()")
	 // Matches if the last name is compound, such as van der Linden
	 case Name(first, last) => println(first, last)
	}

	//11.10 The unapplySeq Method
	object Name {
	 def unapplySeq(input: String): Option[Seq[String]] =
	 	if (input.trim == "") None else Some(input.trim.split("\\s+"))
	}

	author match {
	 case Name(first, last) => s"$first $last"
	 case Name(first, middle, last) => s"$first $middle $last"
	 case Name(first, "van", "der", last) => s"$first var der $last"
	 case _ => println("")
	}

	//Do not supply both an unapply and an unapplySeq methods with the same argument types
}