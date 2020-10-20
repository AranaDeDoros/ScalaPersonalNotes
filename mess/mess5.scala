object test extends App {

	class Test(val a: Array[Array[Int]]) {

	  def apply(i: Int) = a(i)(i)
	  def update(i: Int, j: Int) = a(i)(i) = j
	  def unapply(i:Int): Option[Vector[Int]] = if(i > 0 ) Some(a.flatten.toVector) else None

	}

	val arr = Array(Array(1, 2, 3), Array(5, 6, 7))
	val test = new Test(arr)
	test(0)
	test(0) = 2
	test(0)
	val(at) = test
	at.unapply(1)

	at match{
	  case at:Test if at.a.length > 1 => Some("It's test type")
	  case _ => None
	}

	val f:(Int, Int) => Int = (a:Int, b:Int) => a + b


	val t: (Double, Double) => Double = (a: Double, b: Double) => a + b

	def method(i: Double, j: Double, f: (Double, Double) => Double) = {
		f(i, j)
	}

	method(10, 20.2, t)


	val pf = f(1,_:Int)
	pf(2)

	//def somethingElze(a:Int)(b:(Int) => String) = a.toString + b

	def somethingElze(a:Int)(b:(Int) => String) = a.toString + b(a)
	val b:(Int) => String = (n:Int) => n.toString * n
	somethingElze(3)(b)


	val range = (1 to 10).toVector
	val isPrime = new PartialFunction[Int, Int] {
	  def apply(n: Int) = (-1) * n
	  def isDefinedAt(n: Int) = (n % 2 != 0)
	}
	isPrime.isDefinedAt(8)
	isPrime.isDefinedAt(1)

	val onlyPrimes = range.collect(isPrime)
	onlyPrimes


	/*
	val range = (1 to 10).toVector
	val isPrime: PartialFunction[Int, Int] =  {
    case n if (n % 2 != 0)  => (-1)*n

	}


	val onlyPrimes = range.collect(isPrime)
	onlyPrimes
	*/

	def optionExample(n:Any): Option[Any] = {
		if(!n.isInstanceOf[Int]) None else Some(n)
	}

	val isInt = optionExample(2)
  	val res = isInt match {
	    case Some(s) => s.asInstanceOf[Int]
	    case _ => "no" //because of this line, it'll return an Any, should be None
  	}

  	res

	optionExample("2")


	def intToDoubleOption(oi:Option[Int]): Option[Double] =  oi.map(s => s:Double)

	val oi = None:Option[Int]
	val oii = Some(1):Some[Int]
	oi
	oii
	val od = intToDoubleOption(oii)
	od

	for(i <- 0 until 5;
		j <- 0 until 10;
		if(i == j)) yield s"$i $j"


	class Person(val name:String){

		override def toString = s"${this.name}"

		def test = "test"

	}

	class Student(val id:Int, name:String) extends Person(name:String) {
		def getId = this.id
	}

	val st = new Student(123, "STudent")
	st.getId
	st.test
	st.toString


	def meth(arg: Int): String = {

		def first: Int = arg + 1

		s"from meth ${first}"
	}

	meth(2)

	val tz = 10
	tz
	def anothertest(n:Int) = tz + 10
	val tt = anothertest(tz)
	tt

	val l1 = List(1,-2,3)
	val l2= List(-1,-2,3)

	l1.+:(0)
	l2.:+(4)

	l1.:\(List.empty[Int]){(n,acc)=> (n+2)::acc}
	l1.reduceRight((a,b) => a+b)
	l1.reduceLeft((a,b) => a+b)
	//l1./:(List.empty[Int]){(n,acc)=> (n+2)::acc}

	l1.zipWithIndex(0)
	(l1.view.zipWithIndex.map{case (elem, idx) => (elem, (idx+1))}).force
	(l1.view.zipWithIndex.map{case (elem, idx) => (elem, (idx+1))}).toIndexedSeq

	//sys.error("error message")

	import scala.sys.process._
	/*"ls".!
	"ls".!!
	("ls" #| "grep 'sbt'").!*/


	object Status extends Enumeration{
		type Status = Value
		val ACCEPTED = Value(1,"ok")
		val REJECTED = Value(0,"denied")
		val ERROR = Value(-1,"err")
	}


	object Statuss extends Enumeration{
		val ACCEPTED, REJECTED, ERROR = Value
	}

	/*object Statuz extends Enumeration{
		type Statuz = String
		val ACCEPTED = Value("ok")
		val REJECTED = Value(1,"denied")
		val ERROR = Value(-1,"err")
	}*/

	Status.ACCEPTED.id
	Statuss.ERROR.id
	/*val enumTest = Statuz.ACCEPTED
	enumTest //Statuz.Value
	s"${enumTest.toString}  ${enumTest.getClass().getSimpleName()}"*/

	import scala.io.Source
	val url =  Source.fromURL("https://3v4l.org/", "UTF-8")
	url.mkString
	println(url)

	import scala.sys.process._
	"curl https://3v4l.org/".!

	lazy val v = 10


	val parList = List[Int](1,2,3)
	val view = parList.view

	view.map(_ * 2)

	class Human(val age:Int) extends Ordered[Human]{

		def compare(that:Human) = {

			val res = this.age - that.age
			if(res == 0){
				0
			} else if(res < 0){
				-1
			} else{
				1
			}

		}

	}

	val people = Vector[Human](new Human(10), new Human(88), new Human(4), new Human(100))
	people(0) > people(2)
	people.sortWith(_ > _).foreach{p => println(p.age)} //since Human extends Ordered, we can simply use sortWith
	people.sortWith(_.age > _.age) //fine too, not the best example I guess

	sealed case class Item(amount:Int, name:String)

	val t1 = Item(10,"asdafa")

	val anres = t1 match {
		case Item(10,_) => "ok"
	}

	val ares = t1 match {
		case Item(_,_) => 1
		case _ => 0
	}

	ares

	anres

	//this.type if the class'll be extended and want methods working for subclasses
	//using "this" in this case would've been enough tho
	class Rectangle(){
		var width:Int = 0
		var height:Int = 0

		def setWidth(w:Int): this.type = {
			this.width = w
			this
		}

		def setHeight(h:Int): this.type = {
			this.height = h
			this
		}

		def area = this.width * this.height
	}

	val rect = new Rectangle()
	val area = rect.setWidth(10)
			       .setHeight(5)
		           .area
	area


	//Random 5/6?

	/*package animals

	// Create Annotation `Mammal`
	class Mammal(indigenous:String) extends scala.annotation.StaticAnnotation

	// Annotate class Platypus as a `Mammal`
	@Mammal(indigenous = "North America")
	class Platypus{}
	new Platypus*/

	class Product(val serial:String)
	class Electronics(val name:String, val qty:Int,  serial:String) extends Product(serial)

	object OrderDispatcher{
	def apply() = {
		val order = scala.collection.mutable.ArrayBuffer[Electronics]()
		order += new Electronics("cd", 1, "112edada")
		order += new Electronics("dvd", 11, "9592ada")
		order += new Electronics("laptop", 2, "AVASA")
		order}
	}

	val pd = new Product( "112edada")
	pd.serial


	def listOfDuplicates[A](x: A, length: Int): List[A] = {
	  if (length < 1)
	    Nil
	  else
	    x :: listOfDuplicates(x, length - 1)
	}
	println(listOfDuplicates[Int](3, 4))  // List(3, 3, 3, 3)
	println(listOfDuplicates("La", 8))

	//annotations
	@deprecated
	def someOldMethod = "old"

	someOldMethod

	@volatile //vals cannot be volatile, which makes sense, since threads will change their value due to their mutability
	var k = 2
	k

	@transient
	val trnsnt = 3

	/*@inline
	@throws[NullPointerException]("Something may go wrong")
	@unchecked //to supress matching warnings about exhaustiveness*/

	import scala.beans._

	class aTest{
		@BeanProperty
		var s = "s"
	}

	val ate = new aTest()
	ate.getS()
	ate.setS("t")
	ate

	def callByValue(x:Int, y:Int)(s:Unit) = {
		println(s" $x $y")
		(x + y) * (scala.util.Random.between(x,y) + 2)
	  	s
	}

	def callByName(x: => Int, y: => Int)(s: => Unit) = {
		println(s" $x $y")
		(x + y) * (scala.util.Random.between(x,y) + 2)
	  	s
	}

	callByValue(2,10){
	  	println("hellooo vaaaal")
	}

	callByName(2,10){
	  	println("hellooo naaaame")
	}

	//bounds
	//case to case inheritance means you cannot use bounds
	// if both are case classes


	class Thing(n:Int){
		def say() = "thing"
	}

	class Another(n:Int) extends Thing(n){
		override def say() = "another thing"
	}
	class SomethingElse(n:Int) extends Another(n){
		override def say() = "something else"
	}

	class EvenMore(n:Int) extends SomethingElse(n){
		override def say() ="mixed"
	}

	def mixedBounds[A >:EvenMore <:SomethingElse ](p:A) = p

	mixedBounds(new EvenMore(2))

	def upperBoundTest[C<:Another](p:C) = p


	def lowerBoundTest[C>:Another](p:C) = p

	//upperBoundTest(new Thing(1)) //error
	lowerBoundTest(new Thing(2))


	case class Thing_(n:Int){
			def say() = "Thing_"
	}

	//assert(Thing_(2) != Thing_(3)	)

	val obj1 = Thing_(10)

	if(obj1.n > 0){
			assume(obj1.n >= 10)
			obj1.n-10
	}

	val divideByN: (Float, Float) => Float = (d:Float, n:Float) => n / d
	divideByN(2,10)
	divideByN(3,10)

	def divideByTwo(n:Float) = {
		require(n > 0)
		n / 2
	} ensuring(n < 10 && n > 0)

	divideByTwo(1) //ok
	//divideByTwo(10) //err
	//divideByTwo(0) //err

	///assert & assume can be removed at compile time with the command line option to scalac : -Xdisable-assertions

	import java.time._
	import scala.concurrent._
	import ExecutionContext.Implicits.global
	import scala.concurrent.duration._
	import scala.util.{Try, Success, Failure}

	Future {
	 Thread.sleep(10000)
	 val res = for(i <- 0 to 10000 by 2) yield s"[$i] = $i"
	 res

	}
	println("did it work out tho?")


	val f2 = Future {
	 	import scala.io._
		val url = Source.fromURL("https://printatestpage.com/", "iso-8859-1")
		val txt = url.mkString
		//if (txt.length > 100) throw new Exception("too much text")
		txt
	}



	val result = Await.result(f2, 10.seconds)
	result
	Await.ready(f2, 10.seconds)
	val Some(r) = f2.value
	r //remove exception to get an actual value


	//callback
	f2.onComplete{
		case Success(v) => println(s" The answer is $v")
		case Failure(ex) => println(ex.getMessage)
	}

}