
object Main extends App {

  /*

  //**************3.1****************//
  var fruits = Array("apple", "banana", "grapes")
  for(fruit <- fruits) println(fruit)
  /*for(fruit <- fruits) {
  	println(fruit)
  }*/

  var newFruits = for(fruit <- fruits) yield fruit.toUpperCase
   newFruits = for(fruit <- fruits) yield {
	println(fruit.toUpperCase)   	
  	fruit.toUpperCase
  }
  

  for(i <- 0 until fruits.length){
  	println(s"$i is a ${fruits(i)}")
  }

  for((e, count) <- fruits.zipWithIndex){
  	println(s"$count is a $e")
  }

  //scala.collection.immutable.Range.Inclusive
  var range = 1 to 10
  for(i <- range) println(i)

  for( i <- range if i%2==0) println(i)

  val names = Map("fname" -> "Robert", "lname"->"Goren")
  for((k,v) <- names) println(s"key: $k, value: $v")
  names.foreach(println)

  fruits.foreach(f => println(e.capitalize))
  */

  //**************3.2****************//
  for(i <- 1 to 10; j <- 5 to 10) println(s"i = $i, j = $j")
  val marr = Array.ofDim[Int](2,3)
  marr(0)(0) = 0
  marr(0)(1) = 1
  marr(0)(2) = 4
  marr(1)(0) = 2
  marr(1)(1) = 3
  marr(1)(2) = 4
  for{
  	 i <- 0 until 2 //rows
  	 j <- 0 until 3 //cols
  	
  } println(s"($i)($j) = ${marr(i)(j)}")



  //**************3.3****************//
  for( i <- 1 to 10 if i % 2 ==0) println(i)
  for{
  	i <- 1 to 10
  	if i % 2 == 0
  } println(i)

  for{
  	i <- 1 to 10
  	if i > 3
  	if i < 6
  	if i % 2 == 0
  } println(i)

  //**************3.4****************//
  val names = Array("chris", "ed", "maurice")
  val capNames = for ( e <- names) yield e.capitalize
  val lengths = for ( e <- names) yield {
  	e.length
  }
  names.foreach(println)
  capNames.foreach(println)
  lengths.foreach(println)

  //**************3.1****************//
  
  //**************3.1****************//
  
  //**************3.1****************//
  
  //**************3.1****************//
  
  //**************3.1****************//
  
  //**************3.1****************//
  
  //**************3.1****************//
  
  //**************3.1****************//
  
  //**************3.1****************//
  
  //**************3.1****************//
  
  //**************3.1****************//
  
  //**************3.1****************//
  
  //**************3.1****************//
  
  //**************3.1****************//
  
  //**************3.1****************//
  
  //**************3.1****************//
  
  //**************3.1****************//

}

