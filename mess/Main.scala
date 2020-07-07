import  scala.io.StdIn.{readInt}
import scala.collection.mutable

object Main extends App {

  runSetsExample()


  def runSetsExample(): Unit = {
    //by specifying the type it won't throw an error 
    //if you add an element to this empty set
    val first_set = mutable.Set[Int]()  
    val second_set = mutable.Set[Int]()

    println("How many values")
    val length = readInt()
    var i = 0

    
    println("\nEnter values for the first set")
    for(  i <- 0 until length){
      println("\nEnter a number")
      var element = readInt()
      println(s"\nAdding $element to set 1")
      first_set += element
    }

    println("\nEnter values for the second set")
    for(  i <- 0 until length){
      println("\nEnter a number")
      var element = readInt()
      println(s"\nAdding $element to set 2")
      second_set += element
    }  


    println(s"\nFirst Set: $first_set")
    println(s"\nSecond Set: $second_set")

    val union = first_set ++ second_set

    println(s"\nUnion: $union")
  }


}