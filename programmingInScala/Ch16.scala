object Ch16 extends App {
	
	//**************16.1****************//

	//list literals
	val fruit = List("apples", "oranges", "pears")
	val nums = List(1, 2, 3, 4)
	val diag3 = List(List(1,0,0), List(0,1,0), List(0,0,1))
	val empty = List()
	val nil = 'l'::'i'::'s'::'t'::Nil

	//lists have a recursive structure whereas arrays are flat

	//**************16.2****************//

	//the list type
	val fruit2:List[String] = List("apples", "oranges", "pears")
	val nums2:List[Int] = List(1, 2, 3, 4)
	val diag32:List[List[Int]] = List(List(1,0,0), List(0,1,0), List(0,0,1))
	val empty2:List[Nothing] = List()

	//List[T]
	//the list type is covariant
	//for each pair of type S and T
	//if S is a subtype of T,
	//then List[S] is a subtype of List[T]

	//hence List[Nothing] is a subtype of List[T]
	val xs: List[String] = List()



	//**************16.3****************//

	//:: cons
	//:: infix operator expressing list extension at the front
	//x:xs represents a list whose elment ix, followed by xs

	val fruitxs = "apples"::("oranges"::("pears"::Nil))
	val numsxs = 11::2::3::44::Nil
	val diag3xs = (1::(0::(0::Nil)))::
				  (0::(1::(0::Nil)))::
				  (0::(0::(1::Nil)))::Nil
	val emptyxs = Nil

	//**************16.4****************//

	//basic operations
	//println(Nil.head)

	def isort(xs: List[Int]): List[Int] = if(xs.isEmpty) Nil else insert(xs.head, isort(xs.tail))
	def insert(x: Int, xs: List[Int]): List[Int] = if(xs.isEmpty || x <= xs.head) x :: xs else xs.head :: insert(x, xs.tail)

	println(isort(numsxs))


	//**************16.5****************//

	//list patterns
	/*val List(a, b, c) = fruit
	println(a)
	println(b)
	println(c)
*/
	val a::b::rest = fruit
	println(a)
	println(b)
	println(rest)

	/*def isortt(xs: List[Int]): List[Int] = xs match {
		case List() => List()
		case x:xs1 => insert(x, isortt(xs1))
	}

	def insertt(x: Int, xs: List[Int]): List[Int] = xs match {
		case List() => List(x)
		case y:ys => if(x <= y) x::xs else y::insert(x, ys)
	}*/


	//**************16.6****************//
	
	//first-order methods on class List

	//a method is first order if it doesn't take any args
	List(1,2):::List(3,4,5)
	List():::List(1,2,3)
	List(1,2,3):::List(4)


	//xs:::ys::zs -> xs:::(ys::: zs)

	def append[T](xs: List[T], ys: List[T]): List[T] = xs match {
		case List() => ys
		case x::xs1 => x::append(xs1, ys)
	}

	//taking the length of a list

	List(1,2,3).length
	//length is a more lengthy op than isEmpty

	//accessing the end of a list: init and last
	
	val abcde = List('a', 'b', 'c', 'd', 'e')
	println(abcde.last)
	println(abcde.init) //all except last
	println(abcde.reverse)

	//::: reverse implementation
	def rev[T](xs:List[T]): List[T] = xs match {
		case List() => xs
		case x::xs1 => rev(xs1) ::: List(x)
	}

	//prefexies and suffixes drop, take and splitAt
	println(abcde.take (2))
	println(abcde.drop (2))
	println(abcde.splitAt (2))

	//element selection: apply and indices
	abcde.apply(2)
	abcde(2) //takes time proportional to the n of elements

	//flattening
	List(List(1,2),  List(3),List(),List(4,5)).flatten
	println(fruit.map(_.toCharArray).flatten)

	//zip and unzip
	println(abcde.indices zip abcde)
	val zipped = abcde zip List(1,2,3)
	println(abcde.zipWithIndex)

	println(zipped.unzip)

	//displaying
	println(abcde.toString)
	println(abcde.mkString("[", ",", "]"))
	println(abcde.mkString(""))
	println(abcde.mkString)
	println(abcde.mkString("List(", " , ", ")"))

	val buf = new StringBuilder
	abcde.addString(buf, "(", ";", ")")

	//converting lists
	val arr = abcde.toArray
	arr.toList

	val arr2 = new Array[Int](10)
	List(1,2,3).copyToArray(arr2, 3)
	println(arr2)

	val it = abcde.iterator
	println(it.next())

	def msort[T](less: (T, T) => Boolean)(xs:List[T]): List[T]= {
		def merge(xs:List[T], ys:List[T]):List[T]= (xs, ys) match{
			case (Nil, _) => ys
			case (_, Nil) => xs
			case (x :: xs1, y :: ys1) => if (less(x, y)) x :: merge(xs1, ys) else y :: merge(xs, ys1)
		}
			val n = xs.length / 2 
			if (n ==0) xs 
			else {
			val (ys, zs) = xs splitAt n
			merge(msort(less)(ys), msort(less)(zs))
		}
	}

	msort((x:Int , y:Int) => x < y)(List(5,7,1,3))
	val intSort = msort((x:Int , y:Int) => x < y) _

	val reverseIntSort = msort((x: Int, y: Int) => x > y) _

	val mixedInts = List(4,1,9,0,5,8,3,6,2,7)

	println(intSort(mixedInts))
	println(reverseIntSort(mixedInts))


	//**************16.7****************//

	//higher-order methods on class list
    //map, flatmap, foreach
    List(1,2,3).map{_+1}

    val words = List("the", "quick", "brown", "fox")
    println(words.map{_.length})
    println(words.map{_.toList.reverse.mkString})

    words.map{_.toList}
    words.flatMap(_.toList)

    List.range(1,5).flatMap{i => List.range(1, i).map{j => (i,j)}}
    for( i <- List.range(1,5); j <- List.range(1, i)) yield (i,j)

    var sum = 0
	List(1,2,3,4,5).foreach{sum += _}

	//filter, partition, find, takeWhile, dropWhile span
	val listf = List(1,2,3,-4,5)
	println(listf.filter{_% 2 == 0})
	println(words.filter{_.length == 3})
	println(listf.partition{_%2 == 0})
	println(listf.find(_ % 2 == 0))
	println(listf.find(_ <= 0))

	println(listf.takeWhile(_ > 0))
	println(List("test","ok").dropWhile(_.startsWith( "t")))

	println(listf.span(_ > 0))

	//forall and exists
	def hasZeroRow(m: List[List[Int]]) = m.exists(row => row.forall{_ ==0})

	//folding

	//sum(List(a,b,c)) equals 0+a+b+c
	def sum(xs:List[Int]):Int = (0/: xs) (_ + _)
	def product(xs: List[Int]): Int = (1 /: xs)(_*_)

	//fold left: (z/:xs)(op) start value, xs and op
	//(z /: List(a, b, c)) (op) equals op(op(op(z, a), b), c)
	println((""/:words)(_+" "+_))
	println((words.head/:words)(_+" "+_))

	def flattenLeft[T](xss:List[List[T]]) = (List[T]() /: xss) (_ ::: _)
	def flattenRight[T](xss:List[List[T]]) = (xss :\List[T]()) (_ ::: _)

	//list reversal using fold

	//def reverseLeft[T](xs:List[T]) = (startvalue/: xs)(operation
	def reverseLeft[T](xs:List[T]) = (List[T]() /: xs) {(ys, y) => y :: ys}

	//sortWith
	println(abcde.sortWith(_ < _))


	//**************16.8****************//

	//method of the list object

	List.apply(1,2,3)
	List.range(1,4)	
	println(List.fill(5)('a'))
	println(List.fill(3)("hello"))
	println(List.fill(2, 3)("hello"))


	//tabulating afunction
	//almost identical to fill, but the elements spawn from a function
	val squares = List.tabulate(5)(n=> n * n)
	val multiplication = List.tabulate(5,5)(_ * _)

	List.concat(abcde, abcde)


	//**************16.9****************//

	//processing multiple lists together
	println((List(10,20), List(3,4,5)).zipped.map(_ * _))
	println((List("abc","de"), List(3,2)).zipped.forall(_ .length == _))
	println((List("abc","de"), List(3,2)).zipped.exists(_ .length != _))
	




}