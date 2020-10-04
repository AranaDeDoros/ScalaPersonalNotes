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
	val s:(Int, Int) => Int = (a:Int)


	val t: (Double, Double) => Double = (a: Double, b: Double) => a + b

	def method(i: Double, j: Double, f: (Double, Double) => Double) = {
		f(i, j)
	}

	method(10, 20.2, t)


	val pf = f(1,_:Int)
	pf(2)

	def somethingElse(a:Int)(b:(Int) => String) = a.toString + b

	def somethingElse(a:Int)(b:(Int) => String) = a.toString + b(a)
	val b:(Int) => String = (n:Int) => n.toString * n
	somethingElse(3)(b)


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
	st.getId()
	st.test
	st.toString


	def meth(arg: Int): String = {

		def first: Int = arg + 1

		s"from meth ${first}"
	}

	meth(2)

	val t = 10
	t
	def anothertest(n:Int) = t + 10
	val tt = anothertest(t)
	tt

	val l1 = List(1,2,3)
	val l2= List(1,2,3)

	val l1 = List(1,2,3)
	val l2= List(1,2,3)

	l1.+:(0)
	l2.:+(4)

	l1.:\(List.empty[Int]){(n,acc)=> (n+2):: acc}
	l1.reduceRight((a,b) => a+b)
	l1.reduceLeft((a,b) => a+b)
	l1./:(List.empty[Int]){(n,acc)=> (n+2):: acc}

	l1.zipWithIndex(0)
	(l1.view.zipWithIndex.map{case (elem, idx) => (elem, (idx+1))}).force
	(l1.view.zipWithIndex.map{case (elem, idx) => (elem, (idx+1))}).toIndexedSeq

	sys.error("error message")

	import scala.sys.process._
	"ls".!
	"ls".!!
	("ls" #| "grep 'sbt'").!


	object Status extends Enumeration{
		type Status = Value
		val ACCEPTED = Value(1,"ok")
		val REJECTED = Value(0,"denied")
		val ERROR = Value(-1,"err")
	}


	object Statuss extends Enumeration{
		val ACCEPTED, REJECTED, ERROR = Value
	}

	object Statuz extends Enumeration{
		type Status = String
		val ACCEPTED = Value("ok")
		val REJECTED = Value(0,"denied")
		val ERROR = Value(-1,"err")
	}

	Status.ACCEPTED.id
	Statuss.ERROR.id
	val enumTest = Statuz.ACCEPTED
	enumTest //Statuz.Value
	s"${enumTest.toString}  ${enumTest.getClass().getSimpleName()}"

	import scala.io.Source
	val url = new Source.FromURL("https://3v4l.org/", "UTF-8")
	url.mkString
	println(url)

	import scala.sys.process._
	"curl https://3v4l.org/".!

	lazy val v = 10


	val parList = List[Int](1,2,3)
	val view = parList.view

	view.map(_ * 2)

	class Person(val age:Int) extends Ordered[Person]{

		def compare(that:Person) = {

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

	val people = Vector[Person](new Person(10), new Person(88), new Person(4), new Person(100))
	people(0) > people(2)
	people.sortWith(_ > _).foreach{p => println(p.age)} //since Person extends Ordered, we can simply use sortWith
	people.sortWith(_.age > _.age) //fine too, not the best example I guess

	sealed case class Thing(amount:Int, name:String)

	val t1 = Thing(10,"asdafa")

	val res = t1 match {
		case Thing(10,_) => "ok"
	}

	val ares = t1 match {
		case Thing(_,_) => 1
		case _ => 0
	}

	ares

	res

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


}