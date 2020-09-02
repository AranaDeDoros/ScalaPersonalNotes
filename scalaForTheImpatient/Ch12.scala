object Ch12 extends App {

	//Higher-Order functions
		
	//12.9 Control Abstractions
	def runInThread(block: () => Unit) {
	 new Thread {
	 	override def run() { block() }
	 }.start()
	}

	runInThread { () => println("Hi"); Thread.sleep(10000); println("Bye") }

	//call by name
	def runInThread(block: => Unit) {
	 new Thread {
	 	override def run() { block }
	 }.start()
	}

	runInThread { println("Hi"); Thread.sleep(10000); println("Bye") }

	def until(condition: => Boolean)(block: => Unit) {
	 if (!condition) {
	 	block
	 	until(condition)(block)
	 }
	}

	var x = 10
	until (x == 0) {
	 x -= 1
	 println(x)
	}

	/*The technical term for such a function parameter is a call-by-name parameter.
	Unlike a regular (or call-by-value) parameter, the parameter expression is not
	evaluated when the function is called. After all, we don’t want x == 0 to evaluate
	to false in the call to until. Instead, the expression becomes the body of a function
	with no arguments. That function is passed as a parameter*/

	//12.10 return
	/*In Scala, you don’t use a return statement to return function values. The return
	value of a function is simply the value of the function body.
	However, you can use return to return a value from an anonymous function to
	an enclosing named function. This is useful in control abstractions. For example,
	consider this function:*/

	def indexOf(str: String, ch: Char): Int = {
	 var i = 0
	 until (i == str.length) {
		 if (str(i) == ch) return i
		 i += 1
	 }
	 	return -1
	}
}