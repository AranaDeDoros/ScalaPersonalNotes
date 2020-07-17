import scala.io.StdIn._
import scala.collection.mutable.Set
import scala.language.postfixOps
import scala.math._
import scala.util.control.Breaks._
import scala.util.Random

object Main {
def main(args: Array[String]): Unit = {

	
	
	val bigIntList:List[BigInt] = List.range(1,10)

	println(s"${bigIntList.min} ** ${bigIntList.max} =")
	val bigIntPow = pow(bigIntList.min toDouble , bigIntList.max toDouble)

	println("Permutations")
	println(bigIntList.permutations.mkString("\n"))
	println("Combinations")
	println(bigIntList.combinations(3).mkString("\n"))



	def toNextLetter(s:String): String = s.map{c => (c+1) toChar}

	val mapEx = toNextLetter("String")
	println(mapEx)
	
	



	
	def receiveValues(length:Int) : Set[String] = {

		val firstSet : Set[String] = Set[String]()
		val secondSet : Set[String] = Set[String]()

		println("-------------------------------")
		println("Enter values for the first Set")

		for( i <- 1 to length ){

		  println("\nEnter a string for the first set")
	      val firstEl = readLine()
	      println(s"\nAdding $firstEl to set 1")
	      firstSet += firstEl

	      println("\nEnter a string for the second set")
	      val secondEl = readLine()
	      println(s"\nAdding $secondEl to set 2")
	      secondSet += secondEl

		}	

		println("-------------------------------")
		println("Select an Operation")
		println("1.- Union 2.- Difference ")

		val option = readInt()
		val result = option match {
			case 1 => firstSet ++ secondSet
			case 2 => firstSet -- secondSet //either immutable or .diff
			case _ => Set[String]()
		}
			
	    result

	}


	def runMutableSetsExample(): Set[String] = {

		println("-------------------------------------------")
		println("--------------Sets Demo--------------------")
		println("How many values for each set? (String)")

		val setLength = readInt()

		if( setLength < 1) Set[String]() else receiveValues(setLength)
			
	}

		val setEx = runMutableSetsExample()
		println(setEx)


	 }

}