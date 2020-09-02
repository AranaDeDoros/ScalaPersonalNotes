object Ch12 extends App {
	
	//12
	//File and processes

	//***************12.1*****************//	
	//how to open and read a file
	
	//concise syntax, file is open until the JVM shutdowns
	import scala.io.Source
/*	val fileName = "Ch12.scala"

	for(line <- Source.fromFile(fileName).getLines()) {
		println(line)
	}*/

	/*val lines = Source.fromFile("Ch12.scala").getLines.toList
	val fileContents = Source.fromFile("Ch12.scala").getLines.mkString

	val bufferedSource = Source.fromFile("Ch12.scala")
	for(line <- bufferedSource.getLines){
		println(line.toUpperCase())
	}

	bufferedSource.close()*/

	/*
	getLines returns an Iterator
	and it returns each line without any newline chars
	 */
	
	//loan pattern
	/*
	“ensures that a resource
	is deterministically disposed of once it goes out of scope
	 */

/*	 def using[A](r: Resource)(f: Resource => A): A = {
	 	try {
	 		f(r)
	 	} finally {
	 		r.dispose()	 	}
	 }

	 object Control {
		 def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
		 try {
		 	f(resource)
		 } finally {
		 	resource.close()
		 }
	}

	//import Control._

	using(io.Source.fromFile("Ch12.scala")){
		source => {
			for(line <- source.getLines()){
				println(line)
			}
		}
	}*/

	//handling exceptions
	/*import java.io.{FileNotFoundException, IOException}

	val fileNamee = "no-such-file.scala"
	try {
		for(line <- Source.fromFile(fileNamee).getLines){
			println(line)
		}
	}catch{
		case e: FileNotFoundException => println("couldn't find that file")
		case e: IOException => println("got an IOException")
	}*/

	//import Control._
	/*def readTextFile(filename: String): Option[List[String]] = {
		 try {
			 val lines = using(io.Source.fromFile(filename)) { source =>
			 (for (line <- source.getLines) yield line).toList
			 }
		 	Some(lines)
		 } catch {
		 	case e: Exception => None
		 }
	}*/

	/*val fileNameee = "Ch12.scala"
	println("--foreach--")
	val result = readTextFile(fileNameee)
	result.foreach{
		strings => strings.foreach{println}
	}*/

/*	println("--match--")
	readTextFile(fileNameee) match {
		case Some(lines) => lines.foreach{println}
		case None => println("couldn't read the file")
	}*/

	//multiple fromFile methods
	/*there are eight variations of the fromFile method that let you
	specify a character encoding, buffer size, codec, and URI. 
	For instance, you can specify an expected character encoding for a file like this*/

	//Source.fromFile("Ch12.scala", "UTF-8")

	//***************12.2*****************//	

	//writting text files

	import java.io._
	val pw = new PrintWriter(new File("hello.txt"))	
	pw.write("hello world")
	pw.close()

	val file = new File("helloo.txt")
	val bw = new BufferedWriter(new FileWriter(file))
	bw.write("text")
	bw.close()

	//***************12.3*****************//	
	
	//reading and writing binary files

	object CopyBytes {
		var in = None: Option[FileInputStream]
		var out = None: Option[FileOutputStream]

		try{
			in = Some(new FileInputStream("/Ch11.scala"))
			out = Some(new FileOutputStream("/Ch11.class.copy"))		
			var c = 0
			while( {c = in.get.read; c!= -1}){
				out.get.write(c)
			}
		} catch{
			case e: IOException => e.printStackTrace
		} finally{
			println("finally...")
			if(in.isDefined) in.get.close
			if(out.isDefined) out.get.close
		}

	}

	//***************12.4*****************//	
	
	//process every character

	/*val sourcez = io.Source.fromFile("Ch12.scala")
	for(char <- sourcez) {
		println(char.toUpper)
	}
	sourcez.close()

	def countLines1(source: io.Source): Long = {
		 val NEWLINE = 10
		 var newlineCount = 0L
		 for {
			 char <- source
			 if char.toByte == NEWLINE
		} 
		newlineCount += 1
		newlineCount
	}

	def countLines2(source: io.Source): Long = {
		val NEWLINE = 10
		var newlineCount = 0L
		for {
		line <- source.getLines
		c <- line
		if c.toByte == NEWLINE
		} 
		newlineCount += 1
		newlineCount
	}*/

	//***************12.5*****************//	

	//process a csv file

	object CSVDemo {
		println("Month, Income, Expenses, Profit")
		val bufferedSource = io.Source.fromFile("/tmp/finance.csv")
		for (line <- bufferedSource.getLines()) {
		val cols = line.split(",").map(_.trim)
		// do whatever you want with the columns here
		println(s"${cols(0)}|${cols(1)}|${cols(2)}|${cols(3)}")
	 }
	 	bufferedSource.close
	}

	 //named variables
	 /*for(line <- bufferedSource.getLines){
	 	val Array(month, revenue, expenses, profit) = line.stplit(",").map{_.trim()}
	 	println(s"$month $revenue $expenses $profit")
	 }*/

	 //dropping first line if it's the header
	 //for (line <- bufferedSource.getLines.drop(1))

	object CSVDemo2{
	 	val nrows = 3
	 	val ncols = 4
	 	val rows = Array.ofDim[String](nrows, ncols)

	 	val bufferedSource = io.Source.fromFile("finance.csv")
	 	var count = 0
	 	for(line <- bufferedSource.getLines()){
	 		rows(count) = line.split(",").map{_.trim()}
	 		count += 1
	 	}
	 	bufferedSource.close

	 	for(i <- 0 until nrows){
	 		println(s"${rows(i)(0)} ${rows(i)(1)} ${rows(i)(2)} ${rows(i)(3)}")
	 	}

	 }

	/*
	val bufferedSource = io.Source.fromFile("/tmp/finance.csv")
	for ((line, count) <- bufferedSource.getLines.zipWithIndex) {
	 rows(count) = line.split(",").map(_.trim)
	}
	bufferedSource.close
	*/

	import scala.collection.mutable.ArrayBuffer
	object CSVDemo3{
		 // each row is an array of strings (the columns in the csv file)
		val rows = ArrayBuffer[Array[String]]()
		 // (1) read the csv data
		 using(io.Source.fromFile("/finance.csv")) { source =>
		 for (line <- source.getLines()) {
			 rows += line.split(",").map(_.trim)
			 }
		 }
		 // (2) print the results
		 for (row <- rows) {
		 	println(s"${row(0)}|${row(1)}|${row(2)}|${row(3)}")
		 }

		 def using[A <: { def close(): Unit }, B](resource: A)(f: A => B): B =
		 try {
		 	f(resource)
		 } finally {
		 	resource.close()
		 }
	 }

	//***************12.6*****************//	 
	
	//pretending that string is a file

	import io.Source

	def printLines(source: Source) = {
	 	for(line <- source.getLines()){
	 		println(line)
	 	}
	 }

	 val s = Source.fromString("foo\nbar\n")
	 printLines(s)
	 /*val f = Source.fromFile("Ch12.scala")
	 printLines(f)*/

	 object FileUtils {
		def getLinesUppercased(source: io.Source): List[String] = {
		 	(for (line <- source.getLines()) yield line.toUpperCase).toList
		 }
	}

	//***************12.7*****************//	
	
	//serialization
	
	/*@SerialVersionUID(100L)
	class Stock(var symbol: String, var price: BigDecimal) extends Serializable
	*/

	/*@SerialVersionUID(114L)
	class Employee extends Person with Serializable
	*/

	import java.io._
	// create a serializable Stock class
	@SerialVersionUID(123L)
	class Stock(
		var symbol:String, var price:BigDecimal)
		extends Serializable {
		override def toString =  f"$symbol%s is ${price.toDouble}%.2f"
	}

		// (1) create a Stock instance
		val nflx = new Stock("NFLX", BigDecimal(85.00))
		// (2) write the instance out to a file
		/*val oos = new ObjectOutputStream(new FileOutputStream("/tmp/nflx"))
		oos.writeObject(nflx)
		oos.close*/
		// (3) read the object back in
		/*val ois = new ObjectInputStream(new FileInputStream("/tmp/nflx"))
		val stock = ois.readObject.asInstanceOf[Stock]
		ois.close*/
		// (4) print the object that was read back in
		//println(stock)

	//***************12.8*****************//	

	//listing files in a directory
	
	def getListOfFiles(dir: String):List[File] = {
		val d = new File(dir)
		if(d.exists() && d.isDirectory()){
			d.listFiles.filter{_.isFile}.toList
		} else{
			List[File]()
		}
	}

	import java.io.File

	val filesz = getListOfFiles("../scalaForTheImpatient")
	println(filesz)

	def getListOfFilesz(dir:File, extensions: List[String])
	: List[File] = {
		dir.listFiles.filter(_.isFile()).toList.filter(file => extensions.exists(file.getName.endsWith(_)))
	}

	val okFileExtensions = List(".wav", ".mp3")
	val files2 = getListOfFilesz(new File("../scalaForTheImpatient"), okFileExtensions)

	files2.foreach{println}

	//***************12.9*****************//	

	//listing subdirectories beneath a directory

	//assumes tha dir is directory known to exist
	def getListOfSubdirectories(dir: File): List[String] = {
		dir.listFiles
			.filter(_.isDirectory)
			.map{_.getName}
			.toList
	}

	getListOfSubdirectories(new File("../scalaForTheImpatient/")).foreach(println)

	//returning List[File]
	//dir.listFiles.fiter{_.isDirectory}.toList

	/*//imperative

	def getListOfSubdirectories(dir: FIle): List[String] = {
		val files = dir.listFiles
		val dirNames = collection.mutable.ArrayBuffer[String]()
		for(file <- files) {
			if(file.isDirectory){
				dirNames += file.getName
			}
		}
	  dirNames.toList
	}

	//functional
	def getListOfSubdirectories(dir: File): List[String] = {
		val files = dir.listFiles
		val dirs = for {
			file <- files
			if file.isDirectory
		} yield file.getName
		dirs.toList
	}

	//using transformers
	def getListOfSubdirectories(dir: File): List[String] = {
		val files = dir.listFiles
		var dirs = files.filter{f => f.isDirectory}.map{f => f.getName}
		dirs
	}*/

	//***************12.10*****************//	

	//executing external commands

	import sys.process._

	//! execute the command and get its exit status
	//!! execute the command and get its output
	//lines to execute the command in the background and gets its result as a Stream
	"ls -al".!
	val exitCode = "ls -al".!
	println(exitCode)


	def playSoundFile(fileName: String): Int = {
		val cmd = "afplay "+ fileName
		val exitCode = cmd.!
		exitCode
	}

	/*val exitCode = Seq("ls", " -al").!
	val exitCode = Seq("ls", " -al", " -l").!
	val exitCode = Seq("ls", " -al", " -l", " /HP").!
	val exitCode = Process("ls").!*/

	//lines method
	//execute command immediately in the background
	val process = Process("find / -print").lazyLines
	process.foreach{println}

	/*will throw an exception if the exit status is nonzero
	if you also want to retrieve the error, use lines_! method instead*/

	/*
	external commands vs built-in commands
	However, there’s a big difference between an external command
	and a shell built-in command. The ls command is an external command that’s available
	on all Unix systems, and can be found as a file in the /bin directory:
	
	Some other commands that can be used at a Unix command line, such as the cd or for
	commands in the Bash shell, are actually built into the shell; you won’t find them as files
	on the filesystem. Therefore, these commands can’t be executed unless they’re executed
	from within a shell. 
	 */
	
	 //***************12.11*****************//

	//executing external commands using STDOUT

	val resultcmd = "ls -al".!!
	println(resultcmd)
	val result2 = Process("ls -al").!!
	val result3 = Seq("ls -al").!!

	val output = Seq("ls", " -al").!!
	val output2 = Seq("ls", " -a", " -l").!!
	val output3 = Seq("ls", " -a", " -l", "/HP").!!

	val dir = "HP/Documents/loc"
	val searchTerm = "dawn"
	val results = Seq("find", dir, " -type", " f", "-exect", " grep", " -il", searchTerm, "{}", " ;")
	println(results)

	//unexpected newline characters
	val dir2 ="pwd".!!
	val dir3 = "pwd".!!.trim()

	//!_
	/*
	You may want to check to see whether an executable program is available on your system.
	For instance, suppose you wanted to know whether the hadoop2 executable is available
	on a Unix-based system. A simple way to handle this situation is to use the Unix which
	command with the ! method, where a nonzero exit code indicates that the command
	isn’t available:
	 */
	val executable = "which hadoop2".!
	//non-zero, not available

	val executable2 = "which hadoop2".lazyLines_!.headOption


	//***************12.12*****************//	
	
	//handling STDOUT and STDERR for external commands

	/*
	#!/bin/sh
	exec scala "$0" "$@"
	!#
	import sys.process._
	val stdout = new StringBuilder
	val stderr = new StringBuilder
	val status = "ls -al FRED" ! ProcessLogger(stdout append _, stderr append _)
	println(status)
	println("stdout: " + stdout)
	println("stderr: " + stderr)

	val status = Seq("find", "/usr", "-name", "make") !
	ProcessLogger(stdout.append(_), stderr.append(_))
	println(stdout)
	println(stderr)

	"if one desires, full control over input and output, then a ProcessIO
	can be used with run", see scala.sys.process
	*/


	//***************12.13*****************//	
	
	//build a pipeline of commands

	val numProcs = ("ps auxw" #| "wc -l").!!.trim
	println(s"#procs = $numProcs")

	val javaProcs = ("ps auxw" #| "grep java").!!.trim

	//doesn't work
	val result = ("ls -al | grep Foo").!!
	//because the piping capability comes from a shell

	val r = Seq("/", " -c", " ls | grep .scala").!!
	println(r)
	
	//***************12.14*****************//
		
	//redirecting to the STDIN and STDOUT

	import java.io.File
	//stdout
	("ls -al" #> new File("files.txt")).!!
	("ps aux" #> new File("processes.txt")).!!

	//stdin
	("ps aux" #| "grep http" #> new File("http-processes.out")).!
	val status = ("cat /etc/passwd" #> new File("passwd.copy")).!
	println(status)

	import sys.process._
	import scala.language.postfixOps
	import java.net.URL

	new URL("http://www.google.com") #> new File("output.html") !

	//redirect STDIN
	val contents = ("cat" #> new File("/etc/passwd")).!!
	println(contents)

	val numLines = ("cat /etc/passwd" #> " wc -l").!!.trim
	println(numLines)

	/*
	The ProcessBuilder Scaladoc states that #> and #< “may take as input either
	another ProcessBuilder, or something else such as a java.io.File or a
	java.lang.InputStream.”
	*/
	
	//append
	("ps aux" #>> new File("ps.out")).!
	
	 //***************12.15*****************//	
	
	//using && and || with processes

	/*val result = ("ls temp" #&& "rm temp" #|| "echo 'temp' not found").!!.trim

	#!/bin/sh
	exec scala "$0" "$@"
	!#
	import scala.sys.process._
	val filesExist = Seq("/bin/sh", "-c", "ls *.scala")
	val compileFiles = Seq("/bin/sh", "-c", "scalac *.scala")
	(filesExist #&& compileFiles #|| "echo no files to compile").!!

	*/

	 //***************12.16*****************//

	//handling wildcard characters

	/*
	Putting a shell wildcard character like * into a command 
	doesn’t work because the * needs to be interpreted and expanded by a shell
	 */
	/*In each example, you can make these commands work
	 by invoking a shell in the first two parameters to a Seq*/
	//val status = Seq("/bin/sh", "-c", "ls *.scala").!
	/*
	As an exception to this general rule, the -name option of
	the find command may work because it treats the * character 
	as a wildcard character itself
	 */
	//val status = Seq("find", ".", "-name", "*.scala", "-type", "f").mkString.!


	//***************12.17*****************//

	//run a process in a different directory

	val outputz = Process("ls -al", new File("/tmp")).!!
	println(outputz)

	//***************12.18*****************//

	//setting environment variables while running processes

	//using apply, factory method of Process
	/*val p = Process("runFoo.sh",
                    new File("/Users/Al/bin"),
                    "PATH" -> ".:/usr/bin:/opt/scala/bin")

	val output = p.!!

	println(output)*/

	//last parameter is a vararg

	//***************12.19*****************//	
	//tables*/

}