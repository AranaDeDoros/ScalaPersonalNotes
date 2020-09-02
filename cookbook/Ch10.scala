object Ch10 extends App {

	import scala.collection.immutable._
	import scala.collection.mutable.{Map => Map }
	import java.io._
	//import scala.collection.parallel.CollectionConverters._
	//a predicate is simply a method or function
	//that takes some or more params and returns a boolean
	//filter, map have "implied loops"

	//**************10.1****************//

	val v = Vector(1, 2, 3)
	println(v.sum)
	println(v.filter(_ > 1))
	println(v.map(_ * 2))

	/*
	Travesable -> Iterable ->  {
		Seq,              Set, Map
	IndexedSeq, LinearSeq
	}

	 */	
	
	/*
	The Traversable trait lets you traverse an entire collection, and its Scaladoc states that
	it “implements the behavior common to all collections in terms of a 
	foreach method,”

	The Iterable  trait defines an iterator, which lets you loop through a collection’s ele‐
	ments one at a time, but when using an iterator, the collection can be traversed only
	once, because each element is consumed during the iteration process.
	 */
	
	 //IndexedSeq indicates random access of elements is efficient
	 //by default specifying IndexedSeq when intializing a variable yields a vectors
	 val x = IndexedSeq(1,2,3)
	 println(x)
	 //LinearSeq implies that the collection is split into head and tail
	 //good for recursion, not so good for accessing elements randomly
	 //yields a List by default
	 val seq = scala.collection.immutable.LinearSeq(1,2,3)
	 println(seq)
	 //maps
	 //Map {
	 //HashMap, WeakHashMap, SortedMap, TreeMap, LinkedHashMap, ListMap}

	 val m = Map(1 -> "a", 2 -> "b")
	 val mm = scala.collection.mutable.Map(1 -> "a", 2 -> "b")
	 println(m)
	 println(mm)

	 //sets
	 //Set{ BitSet, HashSet, ListSet, SortedSet -> TreeSet}
	 val set = Set(1,2,3)
	 val mset = scala.collection.mutable.Set(1,2,3)
	 println(set)
	 println(mset)
	 //Stream, Queue, Stack, Range, etc


	 //**************10.2****************//

	 /* General purpose collections, Immutable, Mutable
	 Indexed -> Vector, ArrayBuffer
	 Linear (LinkedLists) -> List, ListBuffer
	  */
	 
	//**************10.3****************// 
	
	//filtering
	/*collect, diff, distinct,drop, dropWhile,  
	filter,  filterNot, find, foldLeft, foldRight,
	head,headOption, init, intersect, last, 
	lastOption, reduceLeft, reduceRight,remove, slice, 
	tail, take, takeWhile, and union*/

	//transformer methods
	/*
	+, ++, −, −−, diff,distinct, 
	collect, flatMap, map, reverse, 
	sortWith, takeWhile, zip, andzipWithIndex
	 */

	//grouping methods
	//groupBy,  partition, sliding,  
	//span,splitAt, and unzip

	//information and mathematical methods
	/*
	canEqual,contains, containsSlice, count, endsWith,  
	exists, find,  forAll, has-DefiniteSize, indexOf,   
	indexOfSlice, indexWhere,  isDefinedAt,  isEmpty,
	lastIndexOf, lastIndexOfSlice, lastIndexWhere, max, 
	min, nonEmpty, product,segmentLength,  size,  
	startsWith,  sumfoldLeft, foldRight,reduceLeft, and reduceRight
	 */

	//others
	//par, view, flatten,foreach, and mkString

	//**************10.4****************// 
	
	//performance
	//collections performance characteristics
	var v2 = Vector[Int]()
	for(i <- 1 to 50000) 
		v2 = v2 :+ 1

	println(v2)

	//map and set peformance characteristics

	//**************10.5****************// 
	
	//declaring a type when creating a collection
	val xx = List(1, 2.0, 33D, 400L)
	val xxx = List[Number](1, 2.0, 33D, 400L)
	val xxxx = List[AnyVal](1, 2.0, 33D, 400L)

	trait Animal
	trait FurryAnimal extends Animal
	case class Dog(name: String) extends Animal
	case class Cat(name: String) extends Animal

	val xz = Array(Dog("Fido"), Cat("Felix"))
	val xzz = Array[Animal](Dog("Fido"), Cat("Felix"))

	class AnimalKingdom {
		def animals = Array(Dog("Fido"), Cat("Felix"))
	}

	//def animals: Array[Product with Serializable with Animal]
	//if def animals: Array[Animal] is wanted

	def animalss = Array[Animal](Dog("Fido"), Cat("Felix"))

	println(xx)
	println(xxx)
	println(xxxx)

	//**************10.6****************// 
	
	var sisters = Vector("Melinda")
	sisters = sisters :+ "Melissa"
	sisters = sisters :+ "Marisa"
	sisters.foreach(println)

	//though var is just being reassigned

	//**************10.7****************// 
	
	//make vector your "go to" immutable sequence
	//indexed, sequential, where as List is linear and sequential
	val v3 = Vector("a", "b", "c")
	println(v3(0))
	val a = Vector(1,2,3)
	val b = a ++ Vector(4,5)
	val c = b.updated(0, "x") //replacing one element
	val aa = Vector(1,2,3,4,5)
	val bb = aa.take(2)
	val cc = aa.filter(_ > 2)

	/*
	Vector is a collection type (introduced in Scala 2.8) 
	that addresses the inefficiency forrandom  access  on  lists.  
	Vectors allow  accessing  any  element  of  the  list  in  ‘effectively’constant time. 
	Because vectors strike a good balance between fast random selectionsand  fast  random functional updates,  
	they  are  currently the  default  implementation  ofimmutable indexed sequences.
	 */

	val iseq = IndexedSeq(1,2,3)

	//**************10.8****************// 
	
	//make ArrayBufer  you "go to" mutable sequence
	import scala.collection.mutable.ArrayBuffer
	var fruits = ArrayBuffer[String]()
	var ints = ArrayBuffer[Int]()
	var nums = ArrayBuffer(1,2,3)
	nums += 4
	nums ++= List(5,6) //varargs param
	nums ++= List(7,8)
	nums -= 9
	nums --= List(7,8)

	val abf = ArrayBuffer(1,2,3)
	abf.append(4)
	abf.appendAll(Seq(5,6))
	abf.appendAll(Seq(7,8))
	abf.clear()

	val abff = ArrayBuffer(9,10)
	abff.insert(0,8)
	abff.insert(0,6)
	abff.insert(0,7)
	abff.insertAll(0, Vector(4,5))
	abff.prepend(3)
	abff.prependAll(Vector(1,2))
	abff.prependAll(Array(0))

	val abfff = ArrayBuffer.range('a', 'h')
	abfff.remove(0)
	abfff.remove(2,3)

	val abffff = ArrayBuffer.range('a', 'h')
	abffff.trimStart(2)
	abffff.trimEnd(2)

	println(abf)
	println(abff)
	println(abfff)
	println(abffff)

	/*
	Append, update, and random access take constant time (amortized time).  
	Prepends and removes are linear in the buffer size.
	array buffers are useful for efficiently building up a 
	large collection whenever the newitems are always added to the end
	If you need a mutable sequential collection that works more like a List 
	(i.e., a linearsequence rather than an indexed sequence), 
	use ListBuffer instead of ArrayBuffer.The Scala documentation on the ListBuffer states, 
	“A ListBuffer is like an array bufferexcept that it uses a linked list internally instead of an array. 
	If you plan to convert thebuffer to a list once it is built up, 
	use a list buffer instead of an array buffer.”
	 */

	//**************10.9****************// 
	
	//looping over a collection with foreach
	//val m =Map("fname" -> "Tyler", "lname" -> "LeDude")m foreach (x => println(s"${x._1} -> ${x._2}"))
	/*
	movieRatings.foreach {case(movie, rating) => println(s"key: $movie, value: $rating")}
	 */

	//**************10.10****************// 
	
	//loping over a collection with a forl loop
	val fruitss = Iterable("apple", "banana", "orange")
	for((elem, count) <- fruitss.view.zipWithIndex){
		println(s" element $count is $elem")
	}

	for((elem, count) <- fruitss.zip(LazyList from 1)){
		println(s" element $count is $elem")
	}

	//**************10.11****************// 
	
	val days = Array("Sunday", "Monday", "Tuesday", "Wednesday","Thursday", "Friday", "Saturday")
	days.zipWithIndex.foreach{
		case(day, count) => println(s"$count is $day")
	}

	for((day,count) <- days.zipWithIndex){
		println(s"$count is $day")
	}

	for((day,count) <- days.zip(LazyList from 1)){
		println(s"$day, $count")
	}

	val list = List("a", "b", "c")
	val zwi = list.zipWithIndex
	val zwi2 = list.view.zipWithIndex //lazy

	days.zipWithIndex.foreach { 
		d =>  println(s"${d._2} is ${d._1}")
	}

	//**************10.12****************// 
	
	//using iterators
	val it = Iterator(1,2,3)
	//it.foreach(println)
	//it.foreach(println)
	it.toArray.foreach{println}


	//**************10.13****************// 

	//transforming one collection to another
	val xf = for (e <- fruits) yield {  
		 val s = e.toUpperCase
		 s
	}

	case class Person (name:String)
	val friends= Vector("Mark", "Regina", "Matt")
	val xff = for (e <- fruits if e.length < 6) yield e.toUpperCase
	println(xff)
	

	//**************10.14****************// 

	//transforming one collection to another with map
	val nieces = List("Aleka", "Christina", "Molly")
	//val elems = nieces.map(niece => <li>{niece}</li>)
	//val ul = <ul>{nieces.map(i => <li>{i}</li>)}</ul>

	println(nieces)
	//println(elems)
	//println(ul)
	

	//**************10.15****************// 

	//flattening lists
	val lol = List(List(1,2), List(3,4))
	val result = lol.flatten
	println(result)
	val xxzz = Vector(Some(1), None, Some(3), None)
	println(xxzz.flatten)

	//**************10.16****************//

	//flatMap
	val bag =List("1", "2", "three", "4", "one hundred seventy five")

	def toInt(in:String):Option[Int] = {
		try {
			Some(Integer.parseInt(in.trim))  
		} 
		catch {
			case e:Exception => None 
		}
	}

	println(bag.flatMap(toInt).sum)


	//**************10.17****************//

	//using filter
	//Unique characteristics of filter compared to 
	//these other methods include:

	/*
	filter walks through all of the elements in the collection; 
	some of the other meth‐ods stop before reaching the end of the collection.

	filter lets you supply a predicate (a function that returns true or false)
	to filter the elements 
	 */

	def getFileContentsWithoutBlanksComments(canonicalFilename:String):List[String] = {  
		io.Source.fromFile(canonicalFilename)
		.getLines()
		.toList
		.filter(_.trim != "")
		.filter(_.charAt(0) != '#')
	}

	//filter doesn’t modify the collection it’s invoked on

	//**************10.18****************//

	//extracting a sequence of elements
	val xd = (1 to 10).toArray
	val y = xd.drop(3)
	val yy = xd.dropRight(4)
	val peepz = List("John", "Mary", "Jane", "Fred")
	println(x)
	println(y)
	println(yy)
	println(peepz.slice(1,3))

	//**************10.19****************//

	//splitting sequences into subsets
	val xg = List(15, 10, 5, 8, 20, 12)
	xg.groupBy(_ > 10)
	xg.partition(_ > 10)
	xg.span(_ < 20)
	xg.splitAt(2)

	val groups = xg.groupBy(_ > 10)
	val trues = groups(true)
	val falses = groups(false)

	val numsz = (1 to 5).toArray
	numsz.sliding(2).toList
	numsz.sliding(2,2).toList
	numsz.sliding(2,3).toList

	val listOfTuple2s = List((1,2), ('a', 'b'))
	listOfTuple2s.unzip


	val couples = List(("Kim", "Al"), ("Julia", "Terry"))
	val (women, men) = couples.unzip
	val couplez = women zip men

	//**************10.20****************//
	//reduce and fold methods
	val arf = Array(12,6,15,2,20,9)
	arf.reduceLeft(_ + _)

	//returns the max of the two elements
	val findMax = (x: Int, y: Int) => {
		val winner = x max y
		println(s"compared to $x to $y, $winner was larger")
		winner
	}

	a.reduceLeft(findMax)

	val peeps = Vector("al", "hannah", "emily", "christina", "aleka")
	peeps.reduceLeft((x,y) => if(x.length > y.length) x else y)

	/*foldLeft works just like reduceLeft, but it lets you
	seed a value to be used for the first element*/

	val aarf = Array(1,2,3)
	println(aarf.reduceLeft(_ + _))
	println(aarf.foldLeft(20)(_ + _))
	println(aarf.foldLeft(100)(_ + _))

	//first list is the seed value, second is the binary operation

	val divide = (x:Double, y:Double) => {
		val result = x / y
		println(s"divided $x by $y to yield $result")
		result
	}

	val aaa = Array(1.0,2.0,3.0)
	println(aaa.reduceLeft(divide))
	println(aaa.reduceRight(divide))

	//scanLeft and scanRight

	/*similar to reduceX they return a sequence instead of a single value
	"produce a collection containing cumulative results of applying
	the operator going left to right*/

	val product = (x: Int, y: Int) => {
		val result = x * y
		println(s"multiplied $x by $y to yield $result")
		result
	}

	val ab = Array(1,2,3)
	ab.scanLeft(10)(product)

	val findMaxx = (x: Int, y: Int) => {
		Thread.sleep(10)
		val winner = x max y
		println(s"compared $x to $y, $winner was larger")
		winner
	}


	/*val abb = Array.range(0, 50)
	abb.par.reduce(findMaxx)*/

	//**************10.21****************//

	//extracting unique elements from a sentence

	val xv = Vector(1,1,2,3,3,4)
	val yd = xv.distinct //or just use .toSet
	println(yd)

	//with classes
	class PersonD(firstName: String, lastName: String){
		override def toString = s"$firstName $lastName"

		def canEqual(a:Any) = a.isInstanceOf[PersonD]

		override def equals(that:Any): Boolean = {
			that match {
				case that:Person => that.canEqual(this) && this.hashCode == that.hashCode
				case _ => false
			}
		}

		override def hashCode: Int = {
			val prime = 31
			var result = 1

			result = prime * result + lastName.hashCode
			result = prime * result + (if(firstName == null) 0 else firstName.hashCode)
			result
		}
	}

	object PersonD {
		def apply(firstName: String, lastName: String) = new PersonD(firstName, lastName)
	}

	val dale1 = new PersonD("Dale", "Cooper")
	val dale2 = new PersonD("Dale", "Cooper")
	val ed = new PersonD("Ed", "Hurley")
	val listz = List(dale1, dale2, ed)
	val uniques = listz.distinct

	//**************10.22****************//
	//merging sequential collections

	val am = Array.range(1,5)
	val amm = Array.range(4,8)

	println(am.intersect(b))
	println(am.concat(b))
	println(am.concat(b).distinct)
	println(am diff amm)
	println(amm diff am)

	//relative complement of two sets

	val ams = Array(1,2,3,11,4,12,4,5)
	val amss = Array(6,7,4,5)
	val df1 = ams.toSet diff amss.toSet
	val df2 = amss.toSet diff ams.toSet

	println(df1)
	println(df2)

	println(df1 ++ df2)

	//**************10.23****************//
	//merging two sequential collections with zip

	val womenz = List("Wilma", "Betty")
	val menz = List("Fred", "Barney")
	val couplezz = womenz zip menz

	for((wife, husband) <- couplezz){
		println(s"$wife is married to $husband")
	}

	println(couplezz.toMap) //convenient

	//**************10.24****************//

	//creating a lazy view on a collection
	//opposite of lazy is strict, memory is asigned from the beginning
	//when used with a transformer method, the values will be calculated as they're required
	//transformer method is that which transforms one collection into another, like map
	println(1 to 100)
	println((1 to 100).view) //SeqView
	println((1 to 100).view.force)

	(1 to 100).foreach{println}
	(1 to 100).view.foreach{println}

	println((1 to 100).map{_ * 2})
	println((1 to 100).view.map{_ * 2})

	val xt = (1 to 1000).view.map{ e=> {
		Thread.sleep(10)
		e * 2
	 }
	}

	/*
	use cases:
	peformance 
	treat a collection like a database view
	*/

	val arr =(1 to 10).toArray
	val view = arr.view.slice(2,5)
	arr(2) = 42
	//view is affected
	view.foreach{println}
	//change elements in the view
	/*view(0) = 10
	view(1) = 20
	view(2) = 30*/

	//array was affected
	println(arr)

	/*
	don’t confuse using a view with saving memory when creating
	a collection. Both of the following approaches will generate
	a “java.lang.OutOfMemoryError
	*/

	//populating a col with a range
	val lr = (1 to 10 by 2).toList
	println(lr)

	println(('a' to 'f').toList)	

	//nice
	val map = (1 to 5).map{e => (e,e)}.toMap

	println(map)

	//**************10.25****************//
	//skipping, nothing new

	//**************10.26****************//
	//creaing and using enums

	object Margin extends Enumeration{
		type Margin = Value
		val TOP, BOTTOM, LEFT, RIGHT = Value
	}

	val currentMargin = Margin.TOP
	if(currentMargin == Margin.TOP) println("working on Top")

	Margin.values.foreach(println)

	//**************10.27****************//
	//tuples for bag of things
	val debt = ("Debi", 95)

	case class Personn(name: String)
	val tup = (3, "Three",  Personn("Al"))
	//val (x, y, z) = (3, "Three", Personn("Al"))
	val (xtup, ytup, _) = tup
	println(s"$xtup $ytup")
	val tuple = "AL" -> "Alabama"
	val nmap = Map("AL" -> "Alabama")

	val itt = tuple.productIterator
	for(e <- itt) println(e)
	println(itt.toArray)

	//**************10.28****************//
	//sorting a collection
	/*The sorted method can sort collections with 
	type Double, Float, Int, and any other type that 
	has an implicit scala.math.Ordering*/

	val tbs = List(10, 5, 8, 1, 7).sorted

	/*
	the rich versions numeric classes like RichInt and StringOps 
	extends the Ordered trait
	 */

	println(List(10,5,8,1,7).sortWith(_ < _))

	def sortByLength(s1: String, s2: String) = {
	 println("comparing %s and %s".format(s1, s2))
	 s1.length > s2.length
	}

	 List("banana", "pear", "apple").sortWith(sortByLength)

	class AnotherPerson(var name: String) {
	 	override def toString = name
	 }

	val ty = new AnotherPerson("Tyler")
	val al = new AnotherPerson("Al")
	val paul = new AnotherPerson("Paul")
	val dudes = List(ty, al, paul)

	//dudes.sorted //no implicit Ordering defined

	val sortedDudes = dudes.sortWith(_.name < _.name)

	 //or mix it in
	class RandomPerson(var name:String) extends Ordered[RandomPerson]{
	 	override def toString = name
	 	def compare(that: RandomPerson) = {
	 		if(this.name == that.name)
	 			{0}
	 		else if(this.name > that.name)
	 			{1}
	 		else
	 			{-1}
	 	}
	 }

	val tyler = new RandomPerson("Tyler")
	val alt = new RandomPerson("Al")

	if (alt > tyler) println("Al") else println("Tyler")

	//**************10.29****************//

	//converting a collection to a String with mkString
	val amk = Array("apple", "banana", "cherry")	 
	println(amk.mkString("[", ",", "]"))	
		 





}