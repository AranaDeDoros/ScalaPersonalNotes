println("hello world")

/*val test = 15032123
val mult = 1.3

var res = test * mult

val typed : Integer = 100
println(res)
println(typed + 20)
*/
/*def sayHi(name: String) : String = "Hello "+name
def sayBye(name: String) : String = {"Bye adsads "+name}

val greet = sayBye("me")
println(greet)

//4.0f 2L  true..

///unit main only executes code
def main(args: Array[String]): Unit = {
	println("hello world from main")
}
//ignore =, scala will assume current func is unit

//singleton
object Person{
	val day = 10
	def sayHi(name: String):Unit = println("Hello "+name + " "+day)
	def sayBye(name: String): String = {"Bye adsads "+name}
}



Person.sayHi("(YOU)")

main(Array())

val an = 10

if(an > 5){
	println(s"$an mayor que 5")
}else{
	println(s"$an menor que 5")
}

val month = 2
*/
//match returns an expression can be used as a param
/*println(month  match {
	case 1 => "Jan"
	case 2 => true
	case 3 => 3
	case 4 => 4.0
	case 5 => "May"
	case 6 => "Jun"
	case 7 => "Jul"
	case 8 => "Aug"
	case 9 => "Sept"
	case 10 => "Oct"
	case 11 => "Nov"
	case 12 => "Dec"
	case _ =>  ""
})


var k = 0

do{
	println(s"k $k next ${k}")
	k+= 1;
}
while(k < 20)

/*
while(k < 20){
	println(s"k $k next ${k}")
	k+= 1;
}

*/


//ANY
//Anyval represents a value
//AnyRef  objectRef, class, instance

//Null Anyref sin valor
//Option
//Nothing -> functions without return or infinite i.g. infinite loops

*/
///collections
///Inmutable
//scala.collection.inmutable.
//import scala.collection.mutable.{Set,ListMap}
///Mutable

//Traversable ops to work with cols
//Iterable
//Set unique, Map k,v unique, Seq (list, vect, range)
								//IndexsedSeq LineaSeq

//array indexed seq
val frutas= Array("Pear", "Naranja")
val frutaz:Array[String]= new Array[String](2)
frutaz(0) ="Pear"
frutaz(1) = "Naranja"
var mixed = Array(1,"OK", true) ///Any

//apply -> can be called without name)
frutas.apply(1)
frutas(1)
frutas.length //if func doesnt have params, can ommit ()
frutas.isEmpty
frutas.nonEmpty

//println(frutas.indexOf("Pear"))
frutas indexOf "Pear"

val fl = frutas.length
var i = 0
while(i < fl){
//	println(frutas(i))
	i+= 1
}

import scala.math.pow

//flatten, map , y flatmap
val nums = List(1, 2, 3 ,4)
val strs = List("Omae", "wo", "mou", "shindeiru")
/*print(nums.map{ n=> pow(n,2)})
println(strs.map{ _.length})*/


val chingodenums = List(List(1,2), List(3,4,5))

/*println(chingodenums.flatten)

println(chingodenums.map{ (n:List[Int]) => n.map{n => n*2}}.flatten)

println(chingodenums.flatMap{ (n:List[Int]) => n.map{n => n*2}})
*/
val more = List(1,3,4,5,89)
//println(more.flatMap{ n => List(n, pow(n, 30))})

import scala.collection.immutable.Set
val languages = Set("ruby", "python", "scala", "haskell")
/*println(languages("scala")) //apply contains
println(languages + "go")
println(languages ++ Set("java", "javascript"))
println(languages - "python")
println(languages -- Set("ruby", "scala"))
println(languages intersect Set("ruby", "elang"))
println(languages diff Set("js","ruby"))*/
//% dif | union  &~ dif entre conjuntos

import scala.collection.mutable

val mset = mutable.Set(1,2,39, 4)
println(mset + 8) //new
//println(mset += 2) //same
//println(mset ++= Set(4,5))
//println(mset -= 2)
println(mset - 2) //no toca original new
println(mset.retain{ n => n % 2 == 0})
println(mset.filter{ n => n % 2 == 0}) //copy

//sorted Set
import scala.collection.immutable
val zet = Set(1,112,242,32,2)
var sset = immutable.SortedSet(1,34,2,4,11,8)
println(sset + 400) //orden default
val mayorAMenor = Ordering.fromLessThan[Int]({(m,n) => m > n})
val rzet = immutable.SortedSet.empty(mayorAMenor) ++ zet
println(rzet)
println()



