import scala.util.control.Breaks._
import scala.annotation.switch
import scala.io._
import scala.annotation.tailrec
object Main extends App {


  //**************3.1****************//
  

  var fruits = Array("apple", "banana", "grapes")
  for(fruit <- fruits) println(fruit)
  for(fruit <- fruits) {
    println(fruit)
  }

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

  fruits.foreach(f => println(f.capitalize))
  

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

  val names2 = Array("chris", "ed", "maurice")
  val capNames = for ( e <- names2) yield e.capitalize
  val lengths = for ( e <- names2) yield {
    e.length
  }
  names.foreach(println)
  capNames.foreach(println)
  lengths.foreach(println)

  //**************3.5****************//

  //was inside an object
  //breakable catches the exception coming from break
  //then the code afterwards is run
  //break and breakable ARE METHODS not keywords

  
    breakable{
      for( i <- 1 to 10){
        println(i)
        if (i>4) break() //break out of the loop
      }
    }
  
  println("continue example")
  val searchMe = "peter picked a peck of pickled peppers"
  var numPs = 0
  for(i <- 0 until searchMe.length){
    breakable{
      if(searchMe.charAt(i) != 'p'){
        break()
      }else{
        numPs += 1
      }
    }
  }
 
  println("found " + numPs + " p's in the string")
  val countS = searchMe.count( _ == 'p')
  println(countS)

  //LabeledBreakDemo
  import scala.util.control._
  val Inner = new Breaks
  val Outer = new Breaks

  Outer.breakable{
    for ( i <- 1 to 5) {
      Inner.breakable{
        for(j <- 'a' to 'e'){
          //println(s"printing $i $j")
          if(i == 1 && j == 'c') Inner.break()  else println(s"i $i j $j")
          if(i == 2 && j == 'b') Outer.break()
        }
      }
    }
  }

  val Exit = new Breaks
  Exit.breakable{
    for(j <- 'a' to 'e'){
      if(j == 'c') Exit.break() else println(s"j $j")
    }
  }

  /*
  var barrelIsFull = false
  for(monkey <- monkeyCollection if !barrelIsFull){
  addMonkeyToBarrel(monkey)
  barrelIsFull = checkIfBarrelIsFull
  }

   */
  
  def sumToMax(arr: Array[Int], limit: Int) : Int = {
    var sum = 0
    for(i <- arr){
      sum+= i
      if(sum > limit) return limit
    } 
    sum
  }

  val astm = Array.range(0,10)
  println(sumToMax(astm, 10))


  /*def factorial(n:Int) : Int = {
    if(n==1) 1
    else n * factorial(n -1 )
  }*/


  import scala.annotation.tailrec
  def factorialTR(n: Int): Int = {
    @tailrec def factorialAcc(acc: Int, n:Int): Int ={
      if(n<=1) acc
      else factorialAcc(n * acc, n -1)
    }
    factorialAcc(1, n)
  }

  println(factorialTR(3))

  
  
  /*private val breakException = BreakControl
  def break(): Nothing = { throw breakException}

  def breakable(op: => Unit): Unit = {
    try{
      //op
    }catch{
      case ex: BreakControl => 
        if (ex ne breakException) throw ex
    }
  }*/

  
  //**************3.6****************//
  
  //ternary operator is just scala's regular if/else
  
  val vl = -2
  val absValue = if (vl>0) -vl else vl
  println(absValue)
  


  //**************3.7****************//

  //add annotation if the match is simple like this
  //it'll be turned into a table lookup with better performance

  @switch
  val month = 2 match {
    case 1 => "Monday"
    case 2 => "Tuesday"
    case 3 => "Wednesday"
    case 4 => "Thursday"
    case 5 => "Friday"
    case 6 => "Saturday"
    case 7 => "Sunday"
    case _ => "Invalid month"
  }

  println(month)

  val xswtch = 2
  val xv = (xswtch: @switch) match {
    case 1 => "One"
    case 2 => 2
    case _ => None
  }

  println(xv)

  def getClassAsString(x:Any): String = x match {
    case s : String => s + " is a string"
    case i : Int => s"$i is an int" // deprecated i + " is an Int"
    case f : Float => s"$f is a floating number"
    case l : List[_] => s"$l  is a list"
    case default => "fug"
    //case p : Person => p + " is a person"
  }

  println(getClassAsString(Set(1,2)))

  val monthNumberToName = Map(
    1 -> "Jan",
    2 -> "Feb",
    3 -> "Mar",
    4 -> "Apr",
    5 -> "May",
    6 -> "Jun",
    7 -> "Jul",
    8 -> "Aug",
    9 -> "Set",
    10 -> "Oct",
    11 -> "Nov",
    12 -> "Dec"

  )

  val monthName = monthNumberToName(3)
  println(monthName)

  

  //**************3.8****************//

  val imc = 5
  imc match {
    case 1 | 3 | 5 | 7 | 9 => println("odd")
    case 2 | 4 | 6 | 8 | 10 => println("odd")
  }

  val cmd = "stop"
  cmd match {
    case "start" => println("starting")
    case "stop" => println("stopping")
    case _ => println("do nothing")
  }
  


  //**************3.9****************//

  val shortMethodVal = 2
  def isTrue(a: Any) = a match{
    case 0 | "" => false
    case _ => true
  }

  println(isTrue(shortMethodVal))
  
  //**************3.10****************//

  //throws MatchError if default case isn't handled
  val defaultVal = 1
  defaultVal match {
    case 0 => println("1")
    case 1 => println("2")
    case default => println(s"default $default")
  }


  //**************3.11****************//

  def echoWhatYouGaveMe(x: Any): String = x match {
  // constant patterns
  case 0 => "zero"
  case true => "true"
  case "hello" => "you said 'hello'"
  case Nil => "an empty List"
  // sequence patterns
  case List(0, _, _) => "a three-element list with 0 as the first element"
  case List(1, _*) => "a list beginning with 1, having any number of elements"
  case Vector(1, _*) => "a vector starting with 1, having any number of elements"
  // tuples
  case (a, b) => s"got $a and $b"
  case (a, b, c) => s"got $a, $b, and $c"
  // constructor patterns
  case Person(first, "Alexander") => s"found an Alexander, first name = $first"
  case Dog("Suka") => "found a dog named Suka"
  // typed patterns
  case s: String => s"you gave me this string: $s"
  case i: Int => s"thanks for the int: $i"
  case f: Float => s"thanks for the float: $f"
  case a: Array[Int] => s"an array of int: ${a.mkString(",")}"
  case as: Array[String] => s"an array of strings: ${as.mkString(",")}"
  case d: Dog => s"dog: ${d.name}"
  case list: List[_] => s"thanks for the List: $list"
  case m: Map[_, _] => m.toString
  // the default wildcard pattern
  case _ => "Unknown"
}

  case class Person(firstName: String, lastName: String)
  case class Dog(name: String)
  // trigger the constant patterns
  println(echoWhatYouGaveMe(0))
  println(echoWhatYouGaveMe(true))
  println(echoWhatYouGaveMe("hello"))
  println(echoWhatYouGaveMe(Nil))
  // trigger the sequence patterns
  println(echoWhatYouGaveMe(List(0,1,2)))
  println(echoWhatYouGaveMe(List(1,2)))
  println(echoWhatYouGaveMe(List(1,2,3)))
  println(echoWhatYouGaveMe(Vector(1,2,3)))// trigger the tuple patterns
  println(echoWhatYouGaveMe((1,2))) // two element tuple
  println(echoWhatYouGaveMe((1,2,3))) // three element tuple
                                      // trigger the constructor patterns
  println(echoWhatYouGaveMe(Person("Melissa", "Alexander")))
  println(echoWhatYouGaveMe(Dog("Suka")))
  // trigger the typed patterns
  println(echoWhatYouGaveMe("Hello, world"))
  println(echoWhatYouGaveMe(42))
  println(echoWhatYouGaveMe(42F))
  println(echoWhatYouGaveMe(Array(1,2,3)))
  println(echoWhatYouGaveMe(Array("coffee", "apple pie")))
  println(echoWhatYouGaveMe(Dog("Fido")))
  println(echoWhatYouGaveMe(List("apple", "banana")))
  println(echoWhatYouGaveMe(Map(1->"Al", 2->"Alexander")))
  // trigger the wildcard pattern
  println(echoWhatYouGaveMe("33d"))
  
  //constant pattern
  //can olny match itself. Any literal may be used as constant
  /*case 0 => "zero"
  case true => "true"

  //variable pattern
  case _ => s"Something..."
  case foo => s"$foo"

  //construct pattern
  case Dog("Suka") => "dog Suka"

  //sequence patterns
  case List(0, _*) => "a list begining with 1 and zero or more elemnts after"

  //tuple patterns
  //works similar to sequence pattern
  case (a, b, c) => s"3-eleme tuple, with values $a $b $c"

  //type patterns
  //str:String is a typed pattern, and str is a pattern variable
  case str:String => s"this string is $str"

  //in this case the value being provided will be evaluated against match {
  //if it matches any case, the value provided will be able to be used in the right side
  //of the case

  //adding variable to patterns
  //you may want to add a variable to a pattern

  case list: List[_] => s"thanks for the list $list"
  case list: List(1, _*) => s"thanks for the list $list"
  //error
  case list @ List(1, _*) => s"$list"
  //compiles and you can access to the List on the right side
  */


	def matchType(x:Any):String = x match{
		case x @ List(1,_*) => s"$x"
		case x @ Some(_)=> s"$x"
		case p @ Person(first, "Doe") => s"$p"
	}

	println(matchType(List(1,2,3)))
	println(matchType(Some("foo")))
	println(matchType(Person("John", "Doe")))

	def toInt(s:String): Option[Int] = {
	  	try{
	  		Some(Integer.parseInt(s.trim))
	  	}catch{
	  		case e: Exception => None
	  	}
	  }

	toInt("42") match{
	  	case Some(i) => println(i)
	  	case None => println("that wasn't an Int")
	  }

  
  //**************3.12****************//

  trait Animal
  case class Doggo(name: String) extends Animal
  case class Cat(name: String) extends Animal
  case object Woodpecker extends Animal

  def determineType(x: Animal) :String = x match {
  	case Doggo(moniker) => s"Got a dog  called $moniker"
  	case _:Cat => "Got a cat (ignoring name)" //any instance of cat
  	case Woodpecker => "That was a woodpecker" 
  	case _ => "Something else" 
  }


  val doggo = new Doggo("Rocky")
  
  println(determineType(doggo))
  println(determineType(new Cat("Rusty the cat")))
  println(determineType(Woodpecker))
  
  
  //**************3.13****************//

  //adding guards to case statements
  val valueForGuard = 12

  valueForGuard match {
  	case a  if 0 to 9 contains a => println("0-9 range: "+a)
  	case b  if 10 to 19 contains b => println("10-19 range: "+b)
  	case c  if 20 to 29 contains c => println("20-29 range: "+c)
  	case _ => println("hmmm...") 
  }
  
  valueForGuard match {
  	case x if x == 1 => println("one, a lonely number")
  	case x if (x == 2 || x == 3) => println(x)
  	case _  => println("some other value")
  }

  //referencing fields
  /*
  stock match {
  	case x if (x.symbol === "XYZ" && x.price < 20) => buy(x)
  	case x if (x.symbol === "XYZ" && x.price > 50) => buy(x)
  	case _ => None
  }
  */
  //extracting fields and use them in guards
  val newPerson = new Person("Fred", "ASDF")

  def speak(p: Person) = p match {
  	case Person(name, _) if name == "Fred" => "yubba dubba doo"
  	case Person(name, lastname) if name == "Bam Bam" => "Bam Bam!"
  	case _ => "Watch the Flintstones!"
  }

  println(speak(newPerson))

  //guards can be on the right side but they're harder to read tbh
  /*case Person(name) =>
  	 if (name === "Fred") "yubba dubba doo"
  	 else if (name === "Bam Bam") "Bam Bam!"*/




  
  //**************3.14****************//

  ///if(x.isInstanceOf[Person])
  
  
  	def isPerson(x:Any): Boolean = x match {
  		case p:Person => true 
  		case _ => false
  	}

  trait SentientBeing
  trait Animall extends SentientBeing
  case class Wolf(name:String) extends Animall
  case class Me(name:String, age:Int) extends SentientBeing



  val wolf = new Wolf("Test")
  val newBeing = new Me("test", 20)

  def printInfo(x: SentientBeing) = x match {
  	case Me(name, age) => println(s"$name, $age")
  	case Wolf(name) => println(s"$name")
  }
	
  printInfo(wolf)
  printInfo(newBeing)

  //**************3.15****************//

  //list in a match
  val ylist = "Apples"::"Bananas"::"Oranges"::Nil //must always end in Nil

  def listToString(list: List[String]): String = list match{
    case s :: rest => s + " "+listToString(rest)
    case Nil => ""
  }

  println(listToString(ylist))

  def sum(list: List[Int]): Int = list match{
    case Nil => 1
    case n :: rest => n + sum(rest)
  }

  def multiply(list: List[Int]): Int = list match{
    case Nil => 1
    case n :: rest => n + multiply(rest)
  }

  val nums = List.range(1,5)
  println(sum(nums))
  println(multiply(nums))


  //**************3.16****************//

  val sfoo = "foo"
  try {
    val i = sfoo.toInt
  } catch {
    case e: Exception => e.printStackTrace
    //open and handle many...
  }

  //if the type isn't important and will use them maybe for logging
  try{
    val i = sfoo.toDouble
  }catch{
    case t: Throwable => t.printStackTrace
  }

  //catch tem and ignore all
  try{
    val i = sfoo.toFloat
  }catch{
    case _: Throwable => println("exception ignored")
  }

  //throwing from catch
  def toLong(s: String): Option[Long] =
    try{
      Some(s.toLong)
    }catch{
      case e: Exception => throw e
    }


    //using annotations
      @throws(classOf[NumberFormatException])
    def toDouble(s:String): Option[Double] =
      try {
        Some(s.toDouble)
      } catch {
        case e : NumberFormatException => throw e
      }
  

  //**************3.17****************//


  /*
  var in = None: Option[FileInputStream]
  var out = None: Option[FileOutputStream]

  try {
    in = Some(new FileInputStream("/tmp/Test.class"))
    in = Some(new FileInputStream("/tmp/Test.class.copy"))
    var c = 0
    while({c = in.get.read; c != -1}){
      out.get.write(c)
  } catch {
    case e: IOException => e.printStackTrace
  }finally{
    println("entered finally...")
  }
    if (in.isDefined) in.get.close
    if (out.isDefined) out.get.close
    
  }
  
  try{
    in = Some(new FileInputStream("tmp/Test.class"))
    out = Some(new FileOutputStream("tmp/Test.class.copy"))
    in.foreach{ outputStream =>
      var c = 0
      while({c = inputStream.read; c != -1}){
        outputStream.write(c)
      }
    }
  }

  var in = None:Option[FileInputStream]
  var out = None:Option[FileOutputStream]

  var store: Store = null
  var inbox: Folder = null

  try{
    store = session.getStore("imaps")
    inbox = getFolder(store, "INBOX")
    catch{
      case e: NoSuchProviderException => e.printStackTrace
      case me: MessagingException => me.printStackTrace
    } finally{
      if (inbox != null) inbox.close
      if (store != null) store.close
    }
  }
  
  */


  //**************3.18****************//
 
  /*
  def whilst(testCondition: => Boolean)(codeBlock: =>Unit){
    while(testCondition){
      codeBlock
    }
  }
  */


  @tailrec
  def whilst(testCondition: => Boolean)(codeBlock: =>Unit):Unit={
    if(testCondition){
      codeBlock
      whilst(testCondition)(codeBlock)
    }
  }

  var i = 0
  whilst ( i < 5){
    println(i)
    i+= 1
  }

  def doubleIf(test1: => Boolean)(test2: => Boolean)(codeBlock: => Unit):Unit={
    if(test1 && test2){
      codeBlock
    }
  }

  val age = 20 
  val numAccidents = 0
  doubleIf(age > 18)(numAccidents == 0) {println("Discount!")}

  

}

