object Main extends App {

val lpotencias:List[Int] = List.range(1,100000)

lpotencias.map{ x => scala.math.pow(x,x)}.filter{ x => x < 1E10}.foreach(println)

val parafor = List.range(1,10)

for( ltr <- parafor) println(ltr) //yield to return

val fc = for {
	ltr <- parafor
	if ltr > 0
} yield ltr*5

//println(fc)


def toLowerCase(c:Char):Char = c.toLower

val tlcres = "THIS IS A TEST".map{toLowerCase}
println(tlcres)

"Test123".drop(1).take(2)
"Test-1-2-3".split("-")

val nums = "[0-9]+".r
val str = "Test123"
//findAllIn.toArray
val mtch = nums.findFirstIn(str).foreach(println)
//Option[String] = Some(123)

import scala.util.matching.Regex
val numsr = new Regex("[0-9]+")
val mtchc = numsr.findAllIn(str)

println(mtch)
println(mtchc)

"test.scala" == "test.scala"

val ml= """"this is sealed trait ADT extends Product with Serializable
object ADT {	
}"""".stripMargin('#').replaceAll("\n", " ")
println(ml)
println(raw"foo\nbar")
//replaceFirst
//replaceAllIn


val pattern = "([0-9]+) ([A-Z]+) ([a-z]+)".r
//extracting groups
val pattern(number, upper, lower) = "1223 GASDAS ww"
println(number, upper, lower)


/*
scala> 
val match1 = numPattern.findFirstIn(address)
match1: Option[String] = Some(123)

The Option/Some/None pattern is discussed in detail in Recipe 20.6, but the simple way to think about an 
Option  is that it’s a container that holds either zero or one values. In the case of findFirstIn
, if it succeeds, it returns the string “ 123 ” as a Some(123), as shown in this example. However, if it fails to find the pattern in the string it’s searching,
it will return a  None, as shown here:



With the getOrElse approach, you attempt to “get” the result, while also specifying a
default value that should be used if the method failed:

scala> val result = numPattern.findFirstIn(address).getOrElse("no match")
result: String = 123

Because an Option is a collection of zero or one elements, an experienced Scala developer
will also use a foreach loop in this situation:

numPattern.findFirstIn(address).foreach { e =>
  // perform the next step in your algorithm,
  // operating on the value 'e'
}

A match expression also provides a very readable solution to the problem:
match1 match {
  case Some(s) => println(s"Found: $s")
  case None =>
}
 */

val z = "Another"
println(s"${z.(3)} ${z.apply(3)}")



implicit class StringImprovements(s:String) {
		def increment = s.map{ c => (c+1).toChar}
}


val nm = "NewMethod".increment
println(nm)

/*
In real-world code, this is just slightly more complicated. According to 
SIP-13, Implicit
Classes
,  “An  implicit  class  must  be  defined  in  a  scope  where  method  definitions  are
allowed (not at the top level).” This means that your implicit class must be defined inside
a class, object, or package object.
 */
}