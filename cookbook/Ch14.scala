//Command-line tasks

//14.1
//REPL command-line options
//setting java props
//env JAVA_OPTS="-Xmx512M -Xms64M" scala

Runtime.getRuntime.maxMemory / 1024

//-J arguments
scala -J-Xms256m -J-Xmx512m

012
:warning //shows the warning from the most recent line


//14.2
//loading from file, packages don't work though
:paste //Ctrl+D to quit
:load File.scala


//14.3
//adding JAR files and classes to the REPL classpath
//scala -cp DateUtils.jar
//import com.package.dateutils._

/*if you realize you need a JAR file on your classpath
after you’ve started a REPL session,
you can add one dynamically with the :cp command*/
:cp DateUtils.jar

/*
Compiled class files in the current directory (
*.class) are automatically loaded into
the REPL environment, so if a simple
Person.class file is in the current directory when
you start the REPL, you can create a new Person
instance without requiring a classpath

However, if your class files are in a subdirectory,
you can add them to the environment
when you start the session, just as with JAR files.
If all the class files are located in a subdirectory
named classes, you can include them by starting
your REPL session like this:
*/

//14.4 Running a Shell Command from the REPL
:sh ls -al
res0.show

//or
import scala.sys.process._
"ls -al".!

//-i option

//repl-commands.scala
import sys.process._
def clear = "clear".!

def cmd(cmd: String) = cmd.!!

def ls(dir: String) { println(cmd(s"ls -al $dir")) }

def help {  println("\n=== MY CONFIG ===")
 "cat /Users/Al/tmp/repl-commands".!
}

//This makes those pieces of code available to me inside the REPL
clear
ls("tmp")
cmd("cat etc/passwd")


alias repl="scala -i /Users/Al/tmp/repl-commands"

//14.5
// Compiling with scalac and Running with scala
scalac -classpath classes Pizza.scala
scalac -classpath lib/DateUtils.jar -d classes ↵ src/com/alvinalexander/pizza

//14.6
//dissasembling and decompiling Scala code
javap Person
class Main {
for (i <- 1 to 10) println(i)
}

scalac -Xprint:parse Main.scala

jad Main


//14.8
//generating scaladoc
scaladoc Person.scala
sbt doc


//14.9
//Faster Command-Line Compiling with fsc
fsc *.scala
//check manpage for a few caveats on its usage


//14.10
//Using Scala as a Scripting Language

#!/bin/sh
exec scala
 "$0" "$@"
!
#
println
("Hello, world"
)
./hello.sh

#!/bin/sh
exec scala
 "$0" "$@"
!
#
object
Hello extends App {
	println("Hello, world")
	// if you want to access the command line args:
	//args.foreach(println)
	}
Hello.main(args)

#!/bin/sh
exec scala
 "$0" "$@"
!
#
object Hello {
	def main(args:Array[String]) {
    println("Hello, world")
	// if you want to access the command line args:
	//args.foreach(println)  }
	}
Hello.main(args)


#!/bin/sh
exec scala
 -classpath
 "lib/htmlcleaner-2.2.jar:lib/scalaemail_2.10.0-↵
 1.0.jar:lib/stockutils_2.10.0-1.0.jar" " $0" "$@"
!#

import
java.io._
import
scala.io.Source
import
com.devdaily.stocks.StockUtils
import
scala.collection.mutable.ArrayBuffer

object GetStocks {
case class Stock(symbol:String, name:String, price:BigDecimal)
val DIR = System.getProperty("user.dir")
val SLASH = System.getProperty("file.separator")
val CANON_STOCKS_FILE = DIR + SLASH + "stocks.dat"
val CANON_OUTPUT_FILE = DIR + SLASH + "quotes.out"
def main(args:Array[String]) {
// read the stocks file into a list of strings ("AAPL|Apple")
val lines = Source.fromFile(CANON_STOCKS_FILE).getLines.toList
// create a list of Stock from the symbol, name, and by
// retrieving the price
var  stocks= new ArrayBuffer[Stock]()
    lines.foreach{ line =>
    	val fields = line.split("\\|")
    	val symbol = fields(0)
    	val html = StockUtils.getHtmlFromUrl(symbol)
    	val price = StockUtils.extractPriceFromHtml(html, symbol)
    	val stock = Stock(symbol, fields(1), BigDecimal(price))
    	stocks += stock
    }

 	// build a string to output
 	var sb = new StringBuilder
    stocks.foreach { stock=>sb.append("%s is %s\n".format(stock.name, stock.price))
    }
	val output =  sb.toString
	// write the string to the file
	val pw = new PrintWriter(new File(CANON_OUTPUT_FILE))
	pw.write(output)
	pw.close
  }
}
GetStocks.main(args)



//14.11
//Accessing Command-Line Arguments from a Script
#!/bin/sh
exec scala
 "$0" "$@"
!
#
args.foreach
(println)

./args.sh a b c

//14.12
#!/bin/sh
exec scala
 "$0" "$@"
!
#
// write some text out to the user with Console.println
Console
.println("Hello")
// Console is imported by default, so it's not really needed, just use println
println("World")
// readLine lets you prompt the user and read their input as a String
val name = readLine("What's your name? ")
// readInt lets you read an Int, but you have to prompt the user manually
print("How old are you? ")
val age = readInt()
// you can also print output with printf
println(s"Your name is $name and you are $age years old.")


//Reading multiple values from one line
import java.util.Scanner
// simulated input
val input = "Joe 33 200.0"
val line = new Scanner(input)
val name = line.next
val age = line.nextInt
val weight = line.nextDouble

val(a,b,c) = readf3("{0} {1,number} {2,number}")
/*
If  the  user  enters  a  string  followed  by  two  numbers,
a  result  is  returned,  but  if  he enters  an  improperly  formatted  string,
such  as  1  a  b,  the  expression  fails  with  a ParseException
 */

//casting...
val name = a
val age = b.asInstanceOf[Long]
val weight= c.asInstanceOf[Double]

val name = a.toString
val age = b.toString.toInt
val weight= c.toString.toDouble

//spliting into tokens
val input = "Michael 54 250.0"val tokens = input.split(" ")
val name = tokens(0)
val age = tokens(1).toInt
val weight = tokens(2).toDouble

//regex
val ExpectedPattern = "(.*) (\\d+) (\\d*\\.?\\d*)".r
val input = "Paul 36 180.0"
val ExpectedPattern(a, b, c) = input
val name = a
val age = b.toInt
val weight = c.toDouble

//output
val qty = 2
val pizzaType = "Cheese"
val total =  20.10
print(Console.UNDERLINED)
println(f"$qty%d $pizzaType pizzas coming up, $$$total%.2f.")
print(Console.RESET)



//14.13
//Make Your Scala Scripts Run Faster

/*
Use the -savecompiled argument of the Scala interpreter
to save a compiled version of your script.
 */


#!/bin/sh
exec scala "$0" "$@"
!#
println("Hello, world!")
args foreach println



#!/bin/sh
exec -savecompiled scala "$0" "$@"
!#
println("Hello, world!")
args foreach println


/*
When  you  run  your  script  the  first  time,
Scala  generates  a  JAR  file  that  matches  the
name of your script.
Scala creates this new file and also
leaves your original script in place.
On subsequent runs, Scala sees that there’s a JAR file
associated with the script, and if the script hasn’t
been modified since the JAR file was created,
it runs the precompiled code from the JAR file
instead of the source code in the script.
*/