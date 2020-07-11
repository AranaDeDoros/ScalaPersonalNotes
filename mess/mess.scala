lazy val service = "World"
println(service)
var a = 0
var rango = 1 to 10 by 2
//'a'  to 'z by 3' -> range.toList
//for( a <- rango) println(a)
rango.toList.foreach(println)

var tpl = Tuple3("ok", "bk", 1.0)
println(tpl._1)

val varios : List[Any] = List(1,'2',"three", true, ()=>"test")
println(varios)

println("Step 1: How to use Option and Some - a basic example")
val glazedDonutTaste: Option[String] = Some("Very Tasty")
println(s"Glazed Donut taste = ${glazedDonutTaste.get}")

println("\nStep 2: How to use Option and None - a basic example")
val glazedDonutName: Option[String] = None
println(s"Glazed Donut name = ${glazedDonutName.getOrElse("Glazed Donut")}")

println("\nStep 3: How to use Pattern Matching with Option")
glazedDonutName match {
  case Some(name) => println(s"Received donut name = $name")
  case None       => println(s"No donut name was found!")
}


class Person(val name:String, val age:Int){
	def sayHi():Unit = println(s" $name says hi")
	def age():Unit =  age+=1

}

var newp = new Person("me", 10)
newp.sayHi
newp.age

