import scala.language.postfixOps
object FindLongLines extends App{

		//8.1
		//functions and cloures
		/*
		import scala.io.Source

		object LongLines {
			def processFile(filename: String, width: Int):Unit = {
				val source = Source.fromFile(filename)
				for( line <- source.getLines()){
					processLine(filename,width,line)
				}
			}

			private def processLine(filename:String,
				width: Int, line:String):Unit = {
				if(line.length > width){
					println(s"$filename+: ${line.trim}")
				}
			}

		}


		def main(args: Array[String]):Unit = {
			val width = args(0).toInt
			for( arg <- args.drop(1))
				LongLines.processFile(arg, width)
		}
		*/

		//8.2
		//local functions
		/*def processFile(filename:String, width:Int):Unit = {
			def processLine(filename: String,
				width: Int, line:String):Unit = {
				if(line.length > width)
					println(s"$filename+: ${line.trim}")
			}
		}*/

		//nesting
		/*def processFile(filename:String, width:Int):Unit = {
			def processLine(line:String): Unit = {
				if(line.length > width)
					println(s"$filename+: ${line.trim}")
			}

			val source = Source.fromFile(filename)
			for (line <- source.getLines())
				processLine(line)
		}*/

		//8.3

		//function literal = code
		//when compiled = function value
		var increase = (x: Int) => x + 1
		increase(10) //11

		increase = (x: Int) => x + 999
		increase(10) //1009

		increase = (x: Int) => {
			println("We")
			println("are")
			println("here!")
			x + 1
		} //function1 <- trait

		increase(10) //we are here! 11

		val someNumbers = List(-11, -10, -5, 0, 5, 10)
		someNumbers.foreach((x: Int) => println(x))

		//8.4
		someNumbers.filter(x => x > 0)

		//8.5
		someNumbers.filter( _ > 0)

		//val ff = _ + _ 

		val ff = (_:Int) + (_:Int)
		println(ff(5, 10))

		//8.6

		//partially applied functions
		//love them
		def sum(a: Int, b: Int, c: Int):Int = {
			a+b+c
		}

		sum(1,2,3)
		val a = sum _ // _ parameter list, function3
		a(1,2,3)

		val b = sum(1, _: Int, 3)
		b(2) //6
		b(5)

		//val c = sum error

		val d = sum _
		d(10, 20, 30)


		//8.7
		//closures
		// more -> free variable
		//x bound variable, has meaning
		var more  =1
		val addMore = (x: Int) => x + more

		println(addMore(10))

		// a function literal with no free variable
		//is a closed term where term is a bit of source code

		more = 9999
		println(addMore(10))

		more = 0
		
		println(addMore(1))

		//function that creates and returns closures
		def makeIncreaser(more: Int): Int => Int = (x:Int) => x + more

		val inc1 = makeIncreaser(1)
		val inc9999 = makeIncreaser(9999)

		println(inc1(10))
		println(inc9999(10))

		//8.8
		//special function call forms
		def echo(args: String*):Unit = {
			for(arg <- args) println(arg)
		}
		echo()
		echo("one")
		echo("hello", "world!")

		val arr = Array("What's", "up")
		//echo(arr)}
		//append array argument
		echo(arr: _*)
		//pass each argument to echo

		//named arguments
		def speed(distance: Float, time: Float): Float = {
			distance / time
		}

		println(speed(100,10))

		speed(distance = 100, time = 10)

		//order doesn't matter
		speed(time=10, distance = 100)
		//positional always first
		speed(100, time = 10)


		//default
		def printTime(out: java.io.PrintStream = Console.out,
						divisor: Int = 1):Unit = {
			out.println(s"time ${System.currentTimeMillis()}")
		}

		printTime(out = Console.err)
		printTime(divisor= 1000)

		//8.9
		//tail recursion
		/*def approximate(guess: Double): Double = {
			if(isGoodEnough(guess)) guess
			else approximate(improve(guess))
		}*/

		//faster

		/*def approximateLoop(initialGuess:Double): Double = {
			var guess = initialGuess
			while(!isGoodEnough(guess))
				guess = improve(guess)
			guess
		}*/

		//approximate is bieng optimized
		//since the recursive call is the last thing
		//in that function, approximate is tail recursive
		//the compiler replaces it with a jump back to the begining
		//of the function, after updating the function params with new vals


		//tracing tail-recursive functions

		//not recursive
		def boom(x: Int): Int = {
			if(x == 0) throw new Exception("boom!")
			else boom(x -1) +1
		}

		boom(3)

	   def bang(x: Int): Int = {
	   		if(x == 0) throw new Exception("bang!")
	   		else bang(x -1)
	   }

	   bang(5)

	   //only a single stack frame for bang
	   //but not really use -g:notailcails

	   //bang(5)

	   //limits of tail recursion
	   //if the recursion is indirect
	   //these two mutually recursive functions
	   //cannot be optimized
	   
	   /*def isEven(x: Int): Boolean = if (x == 0) true else isOdd(x-1)

	   def isOdd(x: Int): Boolean = if (x == 0) false else isEven(x-1)
	   */

	   //won't get a tail-call optimization either
	   //if the final call goes to a function value
	   /*val funValue = nestedFun _
	   def nestedFun(x: Int):Unit = {
	   		if(x!=0) {println(x); funValue(x-1)}
	   }*/













}
