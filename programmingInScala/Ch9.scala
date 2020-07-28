object Ch9 extends App {
	
	//**************9.1****************//
	
	object FileMatcher{

		private def filesHere = (new java.io.File(".")).listFiles
		private def filesMatching(matcher: String => Boolean) = {
			for (file <- filesHere
			 if matcher(file.getName))
				yield file
		}

		def filesEnding(query: String) = filesMatching(_.endsWith(query))
		def filesContaining(query: String) = filesMatching(_.contains(query))
		def filesRegex(query: String) = filesMatching(_.matches(query))

	}

	//**************9.3****************//

	//currying
	//non-curried
	def plainOldSum(x : Int, y: Int) = x + y
	println(plainOldSum(1,2))

	def curriedSum(x: Int)(y: Int) = x + y
	println(curriedSum(1)(2))

	def first(x: Int) = (y: Int) => x +y

	val second  = first(1)
	println(second(2))

	val onePlus = curriedSum(1)_
	println(onePlus(2))

	val twoPlus = curriedSum(2)_
	println(twoPlus(2))


	//**************9.4****************//

	def twice(op: Double => Double, x: Double) = op(op(x))

	println(twice(_ + 1, 5))

	/*
	def withPrinterWriter(file: File, op: PrintWriter => Unit) = {
		val writer = new PrintWriter(file)
		try {
			op(writer)
		} finally {
			writer.close()
		}
	}

	withPrinterWriter(
		new File("date.txt"),
		writer => writer.println(new java.util.Date)
	)
	*/

	/*def withPrinterWriter(file: File)(op: PrintWriter => Unit) = {
		val writer = new PrintWriter(file)
		try {
			op(writer)
		} finally {
			writer.close()
		}
	}*/


	/*val file = new File("date.txt")
	withPrinterWriter(file){
		writer => writer.println(new java.util.Date)
	}*/
	
	//**************9.5****************//

	//by-name parameters
	var assertionsEnabled = true
	def myAssert(predicate: () => Boolean) = {
		if(assertionsEnabled && !predicate()) throw new AssertionError
	}

	myAssert(() => 5 > 3)

	def byNameAssert(predicate: => Boolean) = {
		if(assertionsEnabled && !predicate){
			throw new AssertionError		
		}
	}

	//myAssert(5 > 3) 

	def boolAssert(predicate: Boolean) = {
		if(assertionsEnabled && !predicate)
			throw new AssertionError
	}

	boolAssert(5 < 3)

	/*
	the function value passed to boolAssert is evaluated before the call 
	and so wil yield true/false whereas in assertByName it won't,
	it'll be passed as func value that will then apply the expression passed

	for example, using an expression that throws an Exception will crash
	the exceution if used in boolAssert whereas in assertByName won't have this issue
	 */

	assertionsEnabled = false

	//println(boolAssert(0/0 == 0))
	println(byNameAssert(0/0 == 0))


}