//K,V
//import scala.collections.immutable

val hm = Map(1 -> "Juan", 2 -> "Alex")

println(hm)
println(hm.keySet)
println(hm.values)
println(hm.get(2)) //option to avoid null needs validation
println(hm(2)) //apply
println(hm.getOrElse(9, "No existe"))
//optoin nothing some
println(hm.contains(9))


//adding
println(hm + (3 -> "JOJO"))
println(hm - 2)
println(hm ++ Map(5->"AS", 6->"Mmee"))

import scala.collection.mutable

val mhm = mutable.Map(1->"BMW", 2->"Renault")
println(mhm += (88 -> "Volks"))
println(mhm.put(1, "AMLO"))
println(mhm(1) = "EPN")
println(mhm ++= mutable.Map(9->"vovo"))
println(mhm -= (88))
println(mhm.clear)

/////FOR FOREACH
val lenguajes = List("Java", "Scala", "Clojure")
val ops = List("shit", "verbose", "best")
//:unit
lenguajes.foreach( l => println(s"$l nice"))
lenguajes.foreach( l => ops.foreach(o =>println(s"$l $o")))
for (l <- lenguajes if l.endsWith("a"); o <- ops if o.endsWith("t")){println(s"$l $o")}
