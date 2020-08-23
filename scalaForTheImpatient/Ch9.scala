object Ch9 extends App {

	//9.1
	import scala.io._
	import java.io._
	val source = Source.fromFile("myfile.txt", "UTF-8")
	// The first argument can be a string or a java.io.File
	// You can omit the encoding if you know that the file uses
	// the default platform encoding
	val lineIterator = source.getLines
	for (l <- lineIterator) println(l)

	val lines = source.getLines.toArray
	val contents = source.mkString
	println(contents)

	//9.2
	val source = Source.fromFile("myfile.txt", "UTF-8")
	val iter = source.buffered
	while(iter.hasNext){
		if(iter.head){
			println(iter.next)
		}
		else{
			println("noo")
		}
	}

	//if file isn't large
	val contents = source.mkString

	//9.3
	val tokens = source.mkString.split("\\s+")
	val numbers = for(w <- tokens) yield w.toDouble
	val numbers = tokens.map{_.toDouble}


	//9.4
	val source1 = Source.fromURL("https://horstman.com", "UTF-8")
	val source2 = Source.fromURL("hello, world!")
	val source3 = source3.stdin

	//9.5
	//this is Java
	
	val file = new File(filename)
	val in = new FileInputStream(file)
	val bytes = new Array[Byte](file.length.toInt)
	in.read(bytes)
	in.close()

	//9.6
	val out = new PrintWriter("numbers.txt")
	for(i <- 1 to 100) out.println(i)
	out.close()

	/*
	Everything works as expected, except for the printf method. When you pass a
	number to printf, the compiler will complain that you need to convert it to
	an AnyRef:
	out.printf("%6d %10.2f",
	 quantity.asInstanceOf[AnyRef], price.asInstanceOf[AnyRef]) // Ugh
	Instead, use the format method of the String class:
	out.print("%6d %10.2f".format(quantity, price))
	or printf("%6d %10.2f", quantity, price)

	 */
	

	//9.7
	import java.nio.file._
	String dirname = "/home/cay/scala-impatient/code"
	val entries = Files.walk(Paths.get(dirname)) //or Files.list
	try {
	  entries.forEach(p => println(p))
	} finally {
	  entries.close()
	}


	//9.8
	//we can remove the annotation if we're alright with the id asigned automatically
	@SerialVersionUID(42L) class Person extends Serializable

	val fred = new Person()
	import java.io._
	val out = new ObjectOutputStream(new FileOutputStream("/tmp/test.obj"))
	out.writeObject(fred)
	out.close()
	val in = new ObjectInputStream(new FileInputStream("/tmp/test.obj"))
	val savedFred = in.readObject().asInstanceOf[Person] //casting

	/*
	The Scala collections are serializable, so you can have them as members of your
	serializable classes:
	class Person extends Serializable {
	 private val friends = new ArrayBuffer[Person] // OK—ArrayBuffer is serializable
	 ..
	}
	 */

	 //9.9
	 //kinda cool
	 import scala.language.postfixOps
	 import sys.process._
	 "ls -al .." !	 
	 /*
	 The sys.process package contains an implicit conversion from strings to ProcessBuilder
	 objects. The ! operator executes the ProcessBuilder object.
	  */
	 val result = "ls -al .." .!! //string
	 "ls -al .." #| "grep sec"! //piping
	 "ls -al .." #> new File("output.txt") ! //output to file
	 "ls -al .." #>> new File("output.txt") ! //append
	 "grep sec" #< new File("output.txt") ! //from file to console
	 "grep Scala" #< new URL("http://horstmann.com/index.html") !

	 /*
	 You can combine processes with p #&& q (execute q if p was successful) and p #|| q
	(execute q if p was unsuccessful). But frankly, Scala is better at control flow than
	the shell, so why not implement the control flow in Scala?
	  
	The process library uses the familiar shell operators | > >> < && ||,
	but it prefixes them with a # so that they all have the same precedence.

	 */
	 
	/*	 
	If you need to run a process in a different directory, or with different environment
	variables, construct the ProcessBuilder with the apply method of the Process object.
	Supply the command, the starting directory, and a sequence of (name, value)
	pairs for environment settings.
	*/

	val p = Process(cmd, new File(dirName), ("LANG", "en_US"))
	//Then execute it with the ! operator:
	"echo 42" #| p !

	//9.10
	val numPattern = "[0-9]+".r
	val wsnumwsPattern = """\\s+[0-9]+\\s+""".r
	for(matchString <- numPattern.findAllIn("99 bottles, 98 bottles"))
		println(matchString)
	val matches = numPattern.findAllIn("99 bottles, 98 bottles").toArray
	val m1 = wsnumwsPattern.findFirstIn("99 bottles, 98 bottles")
 	// Some(" 98 ")

 	//To check whether the beginning of a string matches, use findPrefixOf:
	numPattern.findPrefixOf("99 bottles, 98 bottles")
 	// Some(99)
	wsnumwsPattern.findPrefixOf("99 bottles, 98 bottles")
 	// None

 	//You can replace the first match, or all matches:
	numPattern.replaceFirstIn("99 bottles, 98 bottles", "XX")
 	// "XX bottles, 98 bottles"
	numPattern.replaceAllIn("99 bottles, 98 bottles", "XX")
 	// "XX bottles, XX bottles"

 	//9.11
 	val numitemPattern = "([0-9]+) ([a-z]+)".r
	val numitemPattern(num, item) = "99 bottles"
 	// Sets num to "99", item to "bottles"
	//If you want to extract groups from multiple matches, use a for statement like this:
	for (numitemPattern(num, item) <- numitemPattern.findAllIn("99 bottles, 98 bottles"))
	println(s"$num -  $item")

	
	//parameterized types
	//implicit def

}