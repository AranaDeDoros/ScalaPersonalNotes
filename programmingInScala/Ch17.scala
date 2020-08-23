import scala.language.postfixOps
import scala.collection.immutable._
object Ch17 extends App {
	

	//**************17.1****************//

	//sequences
	val colors = List("red", "blue", "green")
	colors.head
	colors.tail

	val fiveInts = new Array[Int](5)
	val fiveToOne = Array(5 ,4,3 ,2, 1)
	fiveInts(0) = fiveToOne(4)

	println(fiveInts.toList)

	import scala.collection.mutable.ListBuffer

	val buf = new ListBuffer[Int]
	buf += 1
	buf += 2
	println(buf)

	3+=: buf
	println(buf.toList)


	//arraybuffers

	import scala.collection.mutable.ArrayBuffer

	val abuf = new ArrayBuffer[Int]()

	abuf += 12
	abuf += 15
	println(abuf.toList)
	abuf.length
	buf(0)

	//StringOps

	def hasUpperCase(s: String) = s.exists(_.isUpper)
	hasUpperCase("Robert Frost")
	hasUpperCase("ee cummings")

	//**************17.2****************//


	//sets and maps
	val text = "See Spot run. Run, Spot. Run!"

	val wordsArray = text.split("[ !,.]+")
	println(wordsArray)

	val words = scala.collection.mutable.Set.empty[String]
	println(words)

	for(word <- wordsArray) words += word.toLowerCase
	println(words)

	//common ops + - -- ++ size contains += -= ++= --=
	val map = scala.collection.mutable.Map.empty[String, Int]
	map("hello") = 1
	map("there") = 2
	println(map)

	println(map("hello"))

	def countWords(text: String) = {
		val counts = scala.collection.mutable.Map.empty[String, Int]
		for(rawWord <- text.split("[ ,!.]+")){
			val word = rawWord.toLowerCase
			val oldCount = if(counts.contains(word)) counts(word) else 0
			counts += (word -> (oldCount +1 ))
		}
		counts
	}

	println(countWords("See Spot run! Run, Spot. Run!"))

	//common ops, same as sets except for .keys keySet values

	//if a mutable Map or Set has  1 < elem < 5
	//it'll use a SetN/MapN class, EmptySet/EmptyMap,
	//conversely, if it has >=5 elements
	//it'll use a HashSet or HashMap depending on the collection

	//immutables use the apply method of their companion object

	//sorted sets and maps
	//scala has both SortedSet and SortedMap traits
	//TreeSet implements both of them while mixin in the Orderbale trait
	import scala.collection.immutable.TreeSet
	val ts = TreeSet(9, 3, 1, 8, 0 , 2, 7, 4 ,6 ,5)
	val cs = TreeSet('f', 'u', 'n')

	val tsTuple = (ts, cs)
	println(tsTuple)

	var tm = TreeMap(3 -> 'x', 1 -> 'x, 4-> 'x')
	tm += (2 -> 'x')
	println(tm)


	//**************17.3****************//

	//mutable vs immutable
	//switch val with var
	var people = Set("Nancy", "Jane")
	people += "Bob"
	println(people)

	people -= "Jane"
	people ++= List("Tom", "Harry")
	println(people)

	var capital = Map("US"-> "Washington","France"-> "Paris")
	capital += ("Japan"-> "Tokyo")
	println(capital("France"))

	var roughlyPi = 3.0
	roughlyPi += 0.1
	roughlyPi += 0.04
	println(roughlyPi)


	//**************17.4****************//


	//initializating collections
	List(1,2,3)
	Set('a', 'b', 'c')
	import scala.collection.mutable
	mutable.Map("hi"-> 2, "there"->5)
	Array(1.0, 2.0, 3.0)


	val stuff = mutable.Set[Any](42, "adsad")
	println(stuff)

	val colorss = List("blue", "yellow", "red", "green")
	import scala.collection.immutable.TreeSet

	val treeSet = TreeSet[String]() ++ colorss
	println(treeSet)

	//converting to or list
	val convertions = (treeSet.toList, treeSet.toArray)
	println(convertions)

	//converting between mutable and immutable sets and maps
	import scala.collection.mutable
	val mutaSet = mutable.Set.empty ++= treeSet
	val immutaSet = Set.empty ++ mutaSet
	val muta = mutable.Map("i" -> 1 , "ii" ->2)
	val immu = Map.empty ++ muta
	val mutas = (mutaSet, immutaSet, muta, immu)
	println(mutas)


	//**************17.5****************//


	//tuples
	println((1, "hello", Console))
	def longestWord(words: Array[String]) = {
		var word = words(0)
		var idx = 0
		for(i <- 1 until words.length)
			if(words(i).length > word.length){
				word = words(i)
				idx = 1
			}
		(word, idx)
	}

	val longest = longestWord("The quick brown fox".split(" "))
	println(longest)


	//used in pattern-matching
	val (word, idx) = longest
	println((word, idx))

	}
