object MixinsAndImplicits extends App {

	class Language[A]{
		def `type`(a:A) : String = a match {
			case c: RegularLanguage with Hispanic => a.toString + " is a Hispanic lang"
			case c: ProgrammingLanguage with ComputerLike => a.toString + " is used by computers"
			case c => c.toString
		}
	}

	trait Hispanic{
	  def isHispanic = true
	}

	trait HumanLike{
		def isHuman = true
	}

	trait ComputerLike{
		def isMachine = true
	}

	class Binary[A <: ProgrammingLanguage](lang:A){
		def alphabet = Map[Int,Int](0 -> 0 , 1 -> 1)
	}

	class HighLevelLanguage(name:String) extends ProgrammingLanguage(name)
	class LowLevelLanguage(name:String) extends ProgrammingLanguage(name)

	case class ProgrammingLanguage(name:String) extends Language{
		override def toString = name
		def program() = "programming in " + name
	}

	case class RegularLanguage(name:String, country:String) extends Language{
		override def toString = name
		def speak() = " speaking in "+ name
	}

	val java = new HighLevelLanguage("java")
	val asm = new LowLevelLanguage("asm")

	val bin = new Binary(java)
	println(bin.alphabet)


	val cobol = new ProgrammingLanguage("cobol") with ComputerLike
	println(cobol.program())
	println(cobol.isMachine)
	val celtic = new RegularLanguage("celtic", "some celtic country idk") with HumanLike
	println(celtic.speak())
	val spanish = new RegularLanguage("spanish", "mexico") with HumanLike with Hispanic
	println(spanish.speak())
	println(spanish.isHispanic)

	//val bin2 = new Binary(spanish) err

	val lang = new Language[ProgrammingLanguage]()
	println(lang.`type`(cobol))
	val langTwo = new Language[RegularLanguage]()
	println(langTwo.`type`(spanish))


	implicit val s = "!"
	def exclaim(s:String)(implicit mark: String) =  s+mark
	println(exclaim("hello world"))

	implicit class Shout(s: String) {
		def shoutIt = s.toUpperCase()+"!"
	}

	println("ok".shoutIt)

	implicit val f:(List[Double] => List[Double] ) = (l:List[Double]) => l.map{ _ * 10 }
	val lop = List(2.10,2.3,542.2,2.2)
	def printMoneyFormat(s:List[Double])(implicit f:(List[Double] => List[Double]) ): List[Double] = f(s)
	val after = printMoneyFormat(lop)
	val res = after

	import scala.language.implicitConversions
	class LOP (val s:List[Double])
	case class Total(p:LOP){
		val tax = 0.30
		val afterTax = "$" + ((p.s.reduce{(acc,n) => acc + n}) * tax)
	}

	implicit def list2Total(p: LOP): Total = Total(p)

	val prices = new LOP(List(2.10,2.3,5.2,2.2))
	println(prices.afterTax)

}


