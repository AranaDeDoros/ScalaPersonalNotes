object Ch11 extends App {


	//11 List, Array, Map, Set

	//**************11.1****************//
	
	//different ways to create and populate a collection

	val xl = List.fill(3)("foo")
	val xll = List.tabulate(5)(n => n * n)

	//**************11.6****************//
	
	//using stream a lazy version of a list
	val stream = 1 #:: 2 #:: 3 #:: Stream.empty
	val stream2 = (1 to 12345678).toLazyList
	println(stream2.head)
	println(stream2.tail.tail.tail.head)
	println(stream2.filter(_ > 200))

	//**************11.7****************//
	
	//different ways to create and update an array
	val ax = Array.fill(3)("foo")
	val axn = Array.tabulate(5)(n => n * n)

	//**************11.10****************//
	
	//sorting an array
	val fruits = Array("cherry", "apple", "banana")
	println(scala.util.Sorting.quickSort(fruits))
	//Notice that quickSort sorts the Array in place;
	//there’s no need to assign the result to a new variable.
	/*
	If the type an Array is holding doesn’t have an implicit Ordering, you can either modify
	it to mix in the Ordered trait (which gives it an implicit Ordering), or sort it using the
	sorted, sortWith, or sortBy methods
	 */

	//**************11.11****************//

	 //multidimensional arrays
	 val adim = Array.ofDim[String](10, 5)
	 val an = Array( Array("a", "b", "c"), Array("d", "e", "f") )
	 println(adim)
	 println(an)

	//**************11.13****************//

	 /*
	 If you’re looking for a basic map class, 
	 where sorting or insertion order doesn’t matter,
	you can either choose the default, immutable Map, 
	or import the mutable Map
	 */

	import scala.collection.SortedMap

	val grades = SortedMap("Kim" -> 90,
	                        "Al" -> 85,
	                    	"Melissa" -> 95)
	 /*
	 If you want a map that remembers the insertion order of its elements,
	 use a LinkedHashMap or ListMap. Scala only has a mutable LinkedHashMap,
	 and it returns its elements in the order you inserted them
	  */

	import scala.collection.mutable.LinkedHashMap
	var states = LinkedHashMap("IL" -> "Illinois")
	states += ("KY" -> "Kentucky")
	states += ("TX" -> "Texas")

	println(states)

	import scala.collection.mutable.ListMap
	var stattesz = scala.collection.immutable.ListMap("IL" -> "Illinois")
	stattesz += ("KY" -> "Kentucky")

	//**************11.14****************//

	//mutable map ops
	println(stattesz.put("CO", "Colorado"))
	println(stattesz.retain{(k,v) => k == "AL"})
	println(stattesz.remove("CO"))

	//**************11.16****************//
	
	//accesing map values
	val statess = Map("AL" -> "Alabama", "AK" -> "Alaska", "AZ" -> "Arizona").withDefaultValue("NA")
	println(statess("foo"))
	val s = statess.getOrElse("FOO", "No such state")

	//**************11.18****************//
	
	//getting they keys or values from a map
	println(statess.keySet)
	println(statess.keys)
	println(statess.keysIterator)
	println(statess.values)
	println(statess.valuesIterator)

	//**************11.19****************//
	
	//reversing keys and values

	val products = Map("Breadsticks" -> "$5", "Pizza" -> "$10", "Wings" -> "$5")
	val reverseMap = for((k,v) <- products) yield (v,k)

	/*As shown, the $5 wings were lost when the values became the keys, 
	because both the breadsticks and the wings had the String value $5*/

	//**************11.20****************//

	//testing for the existence of a key or value in a map
	
	if(products.contains("Pizza")) println("found") else println("not found")
		println(products.valuesIterator.exists{_.contains("izza")})
		println(products.valuesIterator.exists{_.contains("izza")})
		println(states.valuesIterator)

		//11.21 filtering a map
		var xc = collection.mutable.Map(1 -> "a", 2 -> "b", 3 -> "c")
		println(xc.transform((k,v) => v.toUpperCase))

		//mutable and immutable
		val y = xc.filterKeys{_ > 2}
		println(y)

	//**************11.22****************//
	
	//sorting an existing map by key or value
	//sort map by key, from low to high
	println(ListMap(grades.toSeq.sortBy(_._1): _*))
	//sort keys in asc or desc using sortWith
	println(ListMap(grades.toSeq.sortWith(_._1 < _._1): _*))
	println(ListMap(grades.toSeq.sortWith(_._1 > _._1): _*))
	//sort map by value
	ListMap(grades.toSeq.sortBy(_._2): _*)
	//asc, desc
	println(ListMap(grades.toSeq.sortWith(_._2 < _._2): _*))
	println(LinkedHashMap(grades.toSeq.sortWith(_._2 > _._2): _*))

	/*
	_* is used to convert the data so it will be passed as 
	multiple parameters to the ListMap or LinkedHashMap.
	You can’t directly construct a ListMap with a sequence of tuples, 
	but because the apply method in the ListMap companion object accepts a Tuple2 varargs parameter,
	you can adapt x to work with it, i.e., giving it what it wants:
	ListMap(x: _*)
	*/


	def printAll(strings: String*) = {
		strings.foreach(println)
	}

	val fts = List("apple", "banan", "cherry")
	printAll(fts: _*)

	//**************11.23****************//
	//finding largest key or value in a map
	println(grades.max)
	println(grades.keysIterator.max)
	println(grades.keysIterator.reduceLeft((x,y) => if(x > y) x else y ))
	println(grades.keysIterator.reduceLeft((x,y) => if(x.length > y.length) x else y ))
	println(grades.valuesIterator.max)
	println(grades.valuesIterator.reduceLeft(_ max _))
	println(grades.valuesIterator.reduceLeft((x, y) => if(x > y) x else y))

	//**************11.26****************//
	
	//SortedSet
	//only immutable

	/*
	To retrieve values from a set in sorted order, use a SortedSet. 
	To retrieve elements from a set in the order in which elements were inserted, 
	use a LinkedHashSet
	 */
	
	val ss = scala.collection.immutable.SortedSet(10, 4, 8, 2)
	val lhs = scala.collection.mutable.LinkedHashSet(10, 4, 8, 2)
	println(ss)
	println(lhs)

	/*
	The SortedSet is available only in an immutable version. 
	If you need a mutable version, use the java.util.TreeSet. 
	The LinkedHashSet is available only as a mutable collection
	examples shown in the Solution work because the types used
	in the sets have an implicit Ordering
	*/	

	class PersonC (var name: String) extends Ordered [PersonC] {
	 	override def toString = name
	 	// return 0 if the same, negative if this < that, positive if this > that
		 def compare (that: PersonC) = {
			 if (this.name == that.name){
                0
			} else if (this.name > that.name){
                1
			}
			 else {
               -1			 	
			 }
		}
	}

	val aleka = new PersonC("Aleka")
	val christina = new PersonC("Christina")
	val molly = new PersonC("Molly")
	val tyler = new PersonC("Tyler")
	val css = scala.collection.immutable.SortedSet(aleka, christina, molly, tyler)
	println(css)

	//**************11.27****************//
	
	//using a queue
	import scala.collection.mutable.Queue
	var q = new Queue[String]
	q += "apple"
	q ++= Seq("kiwi", "banana")
	q ++= Seq("cherry", "coconut")
	q.enqueue("pineapple")
	println(q)
	println(q.dequeue())
	println(q.dequeue())
	println(q)
	q.dequeueFirst(_.startsWith("b"))
	q.dequeueAll(_.length > 6)


	//**************11.28****************//
	
	//using a stack

	import scala.collection.mutable.Stack
	val ints = Stack(1,2,3)
	var stfruits = Stack[String]()
	stfruits.push("apple")
	stfruits.push("banana")
	stfruits.pushAll(List("coconut", "orange", "pineapple"))

	println(stfruits.pop())
	println(stfruits.top)

	/*
	There’s also an ArrayStack class, and according to the Scala documentation, 
	“It provides fast indexing and is generally slightly more efficient for most operations
	than a normal mutable stack.”
	Although I haven’t used an immutable Stack, 
	I’ve seen several people recommend using
	a List instead of an immutable Stack for this use case. 
	A List has at least one less layer
	of code, and you can push elements onto the List with ::
	and access the first element with the head method.
	 */


	//**************11.29****************// 
	
	//using a range

	println(1 to 10)
	println(1 to 10 by 2)
	println(1 until 10)
	println(1 until 20 by 5)

	println(List.tabulate(10)(_ * 3))


}