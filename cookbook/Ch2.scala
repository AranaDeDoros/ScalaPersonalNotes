
import scala.language.postfixOps
import java.util.{Currency, Locale}

object Main extends App {
//**************2.1****************//
//nscala-time https://github.com/nscala-time/nscala-time
//println("100".toInt)

println(10.toFloat)
var bd = BigDecimal("232422411")

println(bd)
Integer.parseInt("70", 8)

/*
implicit class StringToInt(s: String){
	def toInt(radix: Int) = Integer.parseInt(s, radix)
}
*/
//println("10".toInt(2))


//not required to declare throws NumberFormatException
//it'll just use this one instead
//def toInt(s: String) = s.toInt

//using annotations in case of working with java
//@throws(classOf[NumberFormatException])
//def toInt(s: String) = s.toInt

//option/some/none approach
def toInt(s: String): Option[Int] = {
	try{
		Some(s.toInt)
	}catch{
		case e: NumberFormatException => None
	}
}

println(toInt("1").getOrElse(0))
println(toInt("a")) //none

//using match
toInt("10") match{
	case Some(n) => println(n)
	case None => println("NaN")
}
 
//Alternatives to Option -> 20.6
//Try, Success, Failure
//Either, Left, and Right

//**************2.2****************//
//casting

val fltToDbl = 10f.toDouble
val lngToShrt = 100L.toShort

println(f" $fltToDbl%.1f, ${lngToShrt}%d")

val a = 100L
println(a.isValidByte)
//check RichDouble


//**************2.3****************//
//type ascription -> an upcasting
val entero = 1 : Byte
val long = 100L 
val float: Float = 3
val hexn = 0x20
println(s"$entero $hexn")


class Foo{
	var a: Short = 0
	var b: Short = _ //wildcard, sets its value as the default value of its type
}

//_ only works when creating variables and with numeric types isn't an issue but
//now this approach works with other types in other circumstances
var name = null.asInstanceOf[String]
//we should avoid nulls as much as possible...


//**************2.4****************//
//replacements for ++ and --
var aa = 10
aa += 1
println(aa)
aa -= 1
println(aa)
aa *= 2
println(aa)
aa /= 5
println(aa)
//these are methods not operators, only available on vars

//**************2.5****************//

def ~==(x: Double, y:Double, precision: Double) = {
	if ((x - y).abs < precision) true else false
}

val ab = 0.44
val bb = 0.124
val res: Boolean = ~==(aa, bb, 0.001)
println(res)

val fltOne: Double = 0.0101
val fltTwo: Double = 0.0022

object CompareDoubles{
	def approximatelyEqual(x:Double, y:Double, precision:Double) : Boolean = {
		if((x-y).abs < precision) true else false
	}
}

println(CompareDoubles.approximatelyEqual(fltOne,fltTwo, 0.0001)) 

//**************2.6****************//
var bigNum = BigInt(1234567890)
var bigDec = BigDecimal(123456.789)

println(bigNum*2)

println(Double.PositiveInfinity)


//**************2.7****************//
//random numbers
val r = scala.util.Random
r.nextInt
r.nextInt(100) //limiting 0 100
r.nextFloat
r.nextDouble
//seed
val rr = new scala.util.Random(100)
rr.setSeed(1000L)
rr.nextPrintableChar
var range = 0 to rr.nextInt(10) by 2


val yieldres = for( i <- range ) yield i * 20

println(yieldres)

val randomChars = for( i <- range) yield rr.nextPrintableChar
//be careful
println(randomChars)
//sequence of known length, random numbers
val fixedLength = for( i <- 1 to 5 ) yield r.nextInt(100)
println(fixedLength)


//**************2.8****************//
val incRange = 1 to 100 by 10 // n until to n too
//scala.collection.immutable.Range.Inclusive = Range
println(incRange)
//for( i <- incRange) println(i)
//incRange.foreach(i => println(i))
val rangeToArr = 1 to 10 toArray
val rangeToLst = 1 to 10 toList

rangeToLst.foreach(e => println(e))
rangeToArr.foreach(e => println(e))


val xx = (1 to 10).toList
val yy = (1 to 10).toArray

var rangg = 0 to scala.util.Random.nextInt(10)
var rngToVctr = for( i <- 1 to 5) yield i * 2
println(rangg)
println(rngToVctr)

//**************2.9****************//

val pi = scala.math.Pi
println(f"$pi%1.5f")
println(f"$pi%06.2f")
//"%06.2f".format(pi)

var formatter = java.text.NumberFormat.getIntegerInstance
println(formatter.format(10000))
println(formatter.format(10000000))

val locale = new java.util.Locale("de", "DE")
val formatterf = java.text.NumberFormat.getIntegerInstance(locale)
println(formatterf.format(1020055))

//floating point
val ff = java.text.NumberFormat.getInstance
println(ff.format(100000.43))

//currency
val cf = java.text.NumberFormat.getCurrencyInstance
println(cf.format(123456.789))

//international currency
val de = Currency.getInstance(new Locale("de", "DE"))
cf.setCurrency(de)
println(cf.format(123456.789))

}