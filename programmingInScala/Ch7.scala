object Ch7 {

	def main(args: Array[String]): Unit = {
		
		//var filename = if (!args.isEmpty).args[0] else "default.txt"
		var filesHere = (new java.io.File(".")).listFiles
		for( file <- filesHere 
			 if file.isFile
			 if file.getName.endsWith(".scala")
			){
			println(file)
		}

		def fileLines(file: java.io.File) =
			scala.io.Source.fromFile(file).getLines.toList

		def grep(pattern: String) =
			for( file <- filesHere
				if (file.getName.endsWith(".scala"));
				line <- fileLines(file)
				if (line.trim.matches(pattern))
				) println(s" file  ${line.trim}")

		grep(".*gcd.*")

		def scalaFiles =
			for {
				file <- filesHere
				if file.getName.endsWith(".scala")
			} yield file


		val forLineLengths =
			for {
				file <- filesHere
				if file.getName.endsWith(".scala")
				line <- fileLines(file)
				trimmed = line.trim
				//if trimmed.matches(".*for.*")
			} yield trimmed.length

		println(forLineLengths)

		import java.io.FileReader
		import java.io.FileNotFoundException
		import java.io.IOException

		try {
			val file = new FileReader("input.txt")
		} catch {
			case ex: FileNotFoundException => ""
			case ex: IOException => ""
		} finally{
			//file.close()
		}

		import java.net.URL
		import java.net.MalformedURLException

		def urlFor(path: String) =
			try {
				new URL(path)
			} catch {
				case e: MalformedURLException =>new URL("http://scala-lang.org")
			}

		//returns a row as a sequence
		def makeRowSeq(row: Int): = 
			for (col <- 1 to 10) yield {
				val prod = (row * col).toString
				val padding = " " * (4 - prod.length)
				padding + prod
			}

		//returns a row as a String
		def makeRow(row: Int) = makeRowSeq(row).mkString

		//returns table as a string with one row per line
		def multiTable() = {
			val tableSeq = 
				for (row <- 1 to 10)
				yield makeRow(row)
			tableSeq.mkString("\n")
		}

		println(multiTable())
	}
}