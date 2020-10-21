object CSVTest extends App {

	import scala.collection.immutable.Stream;
	import scala.io.Source
	import scala.collection.mutable._

	val vg = ListBuffer[String]()

	//https://www.kaggle.com/heeraldedhia/groceries-dataset
	val bufferedSource = io.Source.fromFile("Groceries_dataset.csv")

	for (line <- bufferedSource.getLines()) {
		val cols = line.split(",").map(_.trim)
		if(cols(2).contains("vegetables") || cols(2).contains("meat")){
			val str = s"${cols(1)}|${cols(2)}"
			//println(str)
			vg += str
		}
 	}

 	bufferedSource.close

 	val lines = vg.toList
 	val meat = lines.filter{_.contains("meat")}
 	val vegetabales = lines.filter{_.contains("vegetables")}

 	println(s"""
 		- # of meat  ${meat.length}
 		- # of vegetabales ${vegetabales.length}
 		""".stripMargin('-'))

 	val vegetablesCategories = vegetabales
								.flatMap(e => e.split('|'))
								.map { e =>  {
									val r = "^[a-z]+".r
										r.findAllIn(e).filter{ e => e != "vegetables"}.toVector
									}
								}.flatten.distinct.toList
 	println(vegetablesCategories)

 	val meatCategories = meat
						.flatMap(e => e.split('|'))
						.map { e =>  {
							val r = "^[a-z]+".r
								r.findAllIn(e).filter{ e => e != "meat"}.toVector
							}
						}.flatten.distinct.toList
 	println(meatCategories)



}