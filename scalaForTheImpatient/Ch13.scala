object Ch13 extends App {
	
	//Collections

	/*The key points of this chapter are:
	• All collections extend the Iterable trait.
	• The three major categories of collections are sequences, sets, and maps.
	• Scala has mutable and immutable versions of most collections.
	• A Scala list is either empty, or it has a head and a tail which is again a list.
	• Sets are unordered collections.
	• Use a LinkedHashSet to retain the insertion order or a SortedSet to iterate in sorted
	order.
	• + adds an element to an unordered collection; +: and :+ prepend or append
	to a sequence; ++ concatenates two collections; - and -- remove elements.
	• The Iterable and Seq traits have dozens of useful methods for common
	operations. Check them out before writing tedious loops.
	• Mapping, folding, and zipping are useful tech*/

	//13.1
	/*Each Scala collection trait or class has a companion object with an apply method
	for constructing an instance of the collection. For example,*/
	Iterable(0xFF, 0xFF00, 0xFF0000)
	Set(Color.RED, Color.GREEN, Color.BLUE)
	Map(Color.RED -> 0xFF0000, Color.GREEN -> 0xFF00, Color.BLUE -> 0xFF)
	SortedSet("Hello", "World")
	/*This is called the “uniform creation principle.”
	There are methods toSeq, toSet, toMap, and so on, as well as a generic to[C] method,
	that you can use to translate between collection types.*/
	val coll = Seq(1, 1, 2, 3, 5, 8, 13)
	val set = coll.toSet
	val buffer = coll.to[ArrayBuffer]

	//13.3
	/*A Vector is the immutable equivalent of an ArrayBuffer: an indexed sequence with
	fast random access. Vectors are implemented as trees where each node has up
	to 32 children. For a vector with one million elements, one needs four layers of
	nodes. (Since 103 ª 210, 106 ª 324
	.) Accessing an element in such a list will take
	4 hops, whereas in a linked list it would take an average of 500,000.
	A Range represents an integer sequence, such as 0,1,2,3,4,5,6,7,8,9 or 10,20,30. Of
	course a Range object doesn’t store all sequence values but only the start, end, and
	increment. */

	//13.4
	//:: operator in the second pattern. It “destructures” the list into headand tail.

	//13.5
	/*Unlike lists, sets do not retain the order in which elements are inserted. By default,
	sets are implemented as hash sets in which elements are organized by the value
	of the hashCode method. (In Scala, as in Java, every object has a hashCode method.)*/

	/*
	You may wonder why sets don’t retain the element order. It turns out that you
	can find elements much faster if you allow sets to reorder their elements. Finding
	an element in a hash set is much faster than in an array or list.
	A linked hash set remembers the order in which elements were inserted. It keeps
	a linked list for this purpose. For example,
	val weekdays = scala.collection.mutable.LinkedHashSet("Mo", "Tu", "We", "Th", "Fr")
	If you want to iterate over elements in sorted order, use a sorted set:
	val numbers = scala.collection.mutable.SortedSet(1, 2, 3, 4, 5)
	A bit set is an implementation of a set of non-negative integers as a sequence of
	bits. The ith bit is 1 if i is present in the set. This is an efficient implementation as
	long as the maximum element is not too large. Scala provides both mutable and
	immutable BitSet classes
	 */
	
	val digits = Set(1, 7, 2, 9)
	digits contains 0 // false
	Set(1, 2) subsetOf digits // true

	/*The union, intersect, and diff methods carry out the usual set operations.
	If you	prefer, you can write them as |, &, and &~. You can also write union as ++ 
	and	difference as --. For example, if we have the set*/
	val primes = Set(2, 3, 5, 7)
	/*then digits union primes is Set(1, 2, 3, 5, 7, 9), digits & primes is Set(2, 7), 
	and	digits -- primes is Set(1, 9)*/

	//13.6
	//coll :+ elem elem +: coll
	//set | set2 set & set2 set &~ set2 union, intersect difference

	//13.7
	//iterable
	//slice(from, to), view(from, to)
	//filter(pred), filterNot(pred), partition(pred)
	//Returns all elements fulfilling or not fulfilling the predicate; the pair of both
	//takeWhile(pred), dropWhile(pred), span(pred) Returns the first elements fulfilling pred;
	//all but those elements; the pair of both.
	//grouped(n), sliding(n)
	/*Returns iterators of subcollections of length n;
	grouped yields elements with index 0 until n, then
	with index n until 2 * n, and so on; sliding yields
	elements with index 0 until n, then with index 1
	until n + 1, and so on*/
	/*groupBy(k)
	Yields a map whose keys are k(x) for all elements
	x. The value for each key is the collection of
	elements with that key*/
	/*mkString(before, between, after),
	addString(sb, before, between, after)
	Makes a string of all elements, adding the given
	strings before the first, between each, and after
	the last element. The second method appends that
	string to a string builder*/

	//seq
	//padTo(n, fill)
	//sorted, sortWith(less), sortBy(f)
	/*The sequence sorted using the element ordering, the
	binary less function, or a function f that maps each
	element to an ordered type*/

	//13.8
	/*
	val buffer = ArrayBuffer("Peter", "Paul", "Mary")
	buffer.transform(_.toUpperCase)
	
	The collect method works with partial functions—functions that may not be defined
	for all inputs. It yields a collection of all function values of the arguments on
	which it is defined. For example,
	 */
	"-3+4".collect { case '+' => 1 ; case '-' => -1 } // Vector(-1, 1)

	/*The groupBy method yields a map whose keys are the function values, and whose
	values are the collections of elements whose function value is the given key. For
	example*/

	val words = ...
	val map = words.groupBy(_.substring(0, 1).toUpper)

	//13.9
	//reducing, folding, and scanning
	List(1, 7, 2, 9).reduceLeft(_ - _)
	//((1 - 7) - 2) - 9 = 1 - 7 - 2 - 9 = -17

	//The reduceRight method does the same, but it starts at the end of the collection
	List(1, 7, 2, 9).reduceRight(_ - _)
	//1 - (7 - (2 - 9)) = 1 - 7 + 2 - 9 = -13

	List(1, 7, 2, 9).foldLeft(0)(_ - _)
	//0 - 1 - 7 - 2 - 9 = -19
	(0 /: List(1, 7, 2, 9))(_ - _)

	val freq = scala.collection.mutable.Map[Char, Int]()
	for (c <- "Mississippi") freq(c) = freq.getOrElse(c, 0) + 1
	// Now freq is Map('i' -> 4, 'M' -> 1, 's' -> 4, 'p' -> 2)

	//found this more readable
	(Map[Char, Int]() /: "Mississippi") {
	 (m, c) => m + (c -> (m.getOrElse(c, 0) + 1))
	}

	/*
	Finally, the scanLeft and scanRight methods combine folding and mapping. You
	get a collection of all intermediate results. For example, 
	*/
	(1 to 10).scanLeft(0)(_ + _)
	//Vector(0, 1, 3, 6, 10, 15, 21, 28, 36, 45, 55)

	//13.10 zipping
	val prices = List(5.0, 20.0, 9.95)
	val quantities = List(10, 2, 1)

	prices zip quantities

	(prices zip quantities) map { p => p._1 * p._2 }

	((prices zip quantities) map { p => p._1 * p._2 }) sum

	//The zipAll method lets you specify defaults for the shorter list:
	List(5.0, 20.0, 9.95).zipAll(List(10, 2), 0.0, 1)
	
	"Scala".zipWithIndex
	//Vector(('S', 0), ('c', 1), ('a', 2), ('l', 3), ('a', 4))

	"Scala".zipWithIndex.max._2 //accessing a tuple element

	//13.11
	//Iterators

	/*
	For example, Source.fromFile yields an iterator because it might not be efficient to
	read an entire file into memory. There are a few Iterable methods that yield an
	iterator, such as grouped or sliding
	 */
	
	/*
	while (iter.hasNext)
	do something with iter.next()

	for (elem <- iter)
	do something with elem
	 */
	
	/*Sometimes, you want to be able to look at the next element before deciding
	whether to consume it. In that case, use the buffered method to turn an Iterator
	into a BufferedIterator. The head method yields the next element without advancing
	the iterator*/

	val iter = scala.io.Source.fromFile(filename).buffered
	while (iter.hasNext && !iter.head.isWhitespace) iter.next
 	// Now iter points to the first non-whitespace character

 	/*
 	Iterator class defines a number of methods that work identically to the
	methods on collections. In particular, all Iterable methods listed in
	“Common Methods” 
	Neither head, headOption, last, lastOption, tail, init, takeRight, and dropRight
	After calling a method such as map,	filter, count, sum, or even length, 
	the iterator is at the end of the collection, and you can’t use it again

	If you find it too tedious to work with an iterator, you can use a method such as
	toArray, toIterable, toSeq, toSet, or toMap to copy the values into a collection
	*/

	//13.12
	//Streams NOW LazyList which is fully lazy, Stream was only tail-lazy
	/*
	In the preceding sections, you saw that an iterator is a “lazy” alternative to a
	collection. You get the elements as you need them. If you don’t need any more
	elements, you don’t pay for the expense of computing the remaining ones.
	However, iterators are fragile. Each call to next mutates the iterator. Streams offer
	an immutable alternative. A stream is an immutable list in which the tail is
	computed lazily—that is, only when you ask for it.
	 */
	
	def numsFrom(n: BigInt): Stream[BigInt] = n #:: numsFrom(n + 1)

	//The #:: operator is like the :: operator for lists, but it constructs a stream.

	val tenOrMore = numsFrom(10)
	//Stream(10, ?)
	tenOrMore.tail.tail.tail
	//Stream(13, ?)

	val squares = numsFrom(1).map(x => x * x)

	/*If you want to get more than one answer, you can invoke take followed by force,
	which forces evaluation of all values. For example,*/
	squares.take(5).force
	//produces Stream(1, 4, 9, 16, 25).

	squares.force // No!
	//would attempt to evaluate all members of an infinite stream, causing an OutOfMemoryError.

	/*You can construct a stream from an iterator. For example, the Source.getLines
	method returns an Iterator[String]. With that iterator, you can only visit the lines
	once. A stream caches the visited lines so you can revisit them:*/

	val words = Source.fromFile("/usr/share/dict/words").getLines.toStream
	words // Stream(A, ?)
	words(5) // Aachen
	words // Stream(A, A's, AOL, AOL's, Aachen, ?)

	//13.3
	//lazy views
	/*In the preceding section, you saw that stream methods are computed lazily, 
	delivering results only when they are needed. 
	You can get a similar effect with other collections by applying the view method. 
	This method yields a collection	on which methods are applied lazily. For example,*/

	val palindromicSquares = (1 to 1000000).view
 		.map(x => x * x)
 		.filter(x => x.toString == x.toString.reverse)


 	/*
 	yields a collection that is unevaluated. (Unlike a stream, not even the first element
	is evaluated.) When you call */

	palindromicSquares.take(10).mkString(",")

	/*then enough squares are generated until ten palindromes have been found,
	and then the computation stops. Unlike streams, views do not cache any values.
	If you call palindromicSquares.take(10).mkString(",") again, the computation starts over

	As with streams, use the force method to force evaluation of a lazy view. 
	You get	back a collection of the same type as the original.

	***The apply method forces evaluation of the entire view. 
	Instead of	calling lazyView(i), call lazyView.take(i).last***

	When you obtain a view into a slice of a mutable collection, any mutations affect
	the original collection. For example,
	ArrayBuffer buffer = ...
	buffer.view(10, 20).transform(x => 0)
	clears the given slice and leaves the other elements unchanged.
	*/

	//13.14 Interoperability with Java Collections
	//The JavaConversions object provides a set of conversions between Scala and Java collections.

	import scala.collection.JavaConversions._
	val props: scala.collection.mutable.Map[String, String] = System.getProperties()

	import scala.collection.JavaConversions.propertiesAsScalaMap

	/*Note that the conversions yield wrappers that let you use the target interface 
	to access the original type. For example, if you use*/

	val props: scala.collection.mutable.Map[String, String] = System.getProperties()

	/*then props is a wrapper whose methods call the methods of the underlying Java
	object. If you call*/

	props("com.horstmann.scala") = "impatient"

	/*
	then the wrapper calls put("com.horstmann.scala", "impatient") on the underlying
	Properties object.
	 */

	//table 13-4, 13-5


	//13.5
	//parallel views - pretty rad

	/*
	***Parallel collections have been moved in Scala 2.13 to separate module scala/scala-parallel-collection***
	libraryDependencies += "org.scala-lang.modules" %% "scala-parallel-collections" % "0.2.0"
	import scala.collection.parallel.CollectionConverters._

	It is hard to write correct concurrent programs, yet concurrency is often required
	nowadays to keep all processors of a computer busy. Scala offers a particularly
	attractive solution for manipulating large collections. Such tasks often parallelize
	naturally. For example, to compute the sum of all elements, multiple threads can
	concurrently compute the sums of different sections; in the end, these partial results are summed up. 
	Of course it is troublesome to schedule these concurrent
	activities—but with Scala, you don’t have to. If coll is a large collection, then

	coll.par.sum
	computes the sum concurrently. The par method produces a parallel implementation
	of the collection. That implementation parallelizes the collection methods
	whenever possible. For example,	coll.par.count(_ % 2 == 0)
	counts the even numbers in coll by evaluating the predicate on subcollections 
	in parallel and combining the results

	You can parallelize a for loop by applying .par to the collection over which you
	iterate, like this:
	for (i <- (0 until 100000).par) print(s" $i")
	import scala.collection.parallel.CollectionConverters._

	val ps = (for (i <- (0 until 100000).par) yield i)

	The parallel collections returned by the par method belong to types that extend
	the ParSeq, ParSet, or ParMap traits. These are not subtypes of Seq, Set, or Map, 
	and you	cannot pass a parallel collection to a method that expects a sequential collection.
	You can convert a parallel collection back to a sequential one with the seq method.
	
	val result = coll.par.filter(p).seq

	Not all methods can be parallelized. For example, reduceLeft and reduceRight 
	require that each operator is applied in sequence. There is an alternate method,
	reduce, that operates on parts of the collection and combines the results
	the operator must be associative—it must fulfill (a op b) op c = a op (b op c).
	For example, addition is associative but subtraction is not: (a – b) – c π a – (b – c).
	To solve this problem, there is an even more general aggregate that applies an
	operator to parts of the collection, and then uses another operator to combine
	the results. For example, str.par.aggregate(Set[Char]())(_ + _, _ ++ _) is the equivalent
	of str.foldLeft(Set[Char]())(_ + _), forming a set of all distinct characters in str.
	*/

}