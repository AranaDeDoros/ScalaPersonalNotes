object Ch32 extends App{

	//32 Futures and concurrency

	//32.1 TROUBLE IN PARADISE

	//Scala'sstandard library offers an alternative that avoids these difficulties
	//by focusing on asynchronoustransformations of immutable state: the Future.

	/*You can specify transformations on a Scala Future whether it has completed or not.
	Each transformation results in a new Future representing the asynchronous result
	of the original Future transformed by the function.
	The thread that performs the computation is determined by
	an implicitly provided execution context.
	This allows you to describe asynchronous computations as a
	series of transformations of immutable values,
	with no need to reason about shared memory and locks.

	For compatibility's sake, Scala provides access to Java's concurrency primitives.
	The wait,notify, and notifyAll methods can be called in Scala,
	and they have the same meaning as in Java.
	Scala doesn'technically have a synchronized keyword,
	but it includes a predefined synchronized method that:*/
	var counter = 0
	synchronized {
	// One thread in here at a time
	counter = counter + 1
	}
	//33.2
	/*When you invoke a Scala method, it performs a computation "while you wait"
	and returns a result. If that result is a Future, the Future represents
	another computation to be performed asynchronously, often by a completely different thread.

	if you try to create a future via the Future.apply factory method
	without providing an implicit execution context, an instance
	ofscala.concurrent.ExecutionContext, you'll get a compiler error

	Two methods on Future allow you to poll: isCompleted and value.
	When invoked on a future that has not yet completed,
	isCompleted will return false and value will return None.*/

	import scala.concurrent.ExecutionContext.Implicits.global

	val fut = Future { Thread.sleep(10000); 21 + 21 }
	fut.isCompleted
	fut.value

	/*Once the future completes (in this case, after at least ten seconds has gone by),
	isCompleted will return true and value will return a Some:*/

	fut.isCompleted
	fut.value

	/*The option returned by value contains a Try. Try is either a Success,
	which contains a value of type T, or a Failure, which contains an exception.
	When working with a Futurerepresenting an asynchronous activity,
	you use Try to deal with the possibility that the activity fails to yield a value*/

	val fut = Future { Thread.sleep(10000); 21 / 0 }
	fut.value
	fut.value


	//32.3
	//working with futures
	//transforming with map

	/*Instead of blocking then continuing with another computation,
	you can just map the next computation onto the future.
	The result will be a new future	that represents the original asynchronously computed result
	transformed asynchronously by the function passed to map*/

	val fut = Future { Thread.sleep(10000); 21 + 21 }
	val result = fut.map{x => x + 1 }
	result.value

	//transforming with for expressions
	val fut1 = Future { Thread.sleep(10000); 21 + 21}
	val fut2 = Future { Thread.sleep(10000); 23 + 23}

	val forRes = for{
	x <- fut1
	y <- fut2
	} yield x + y

	forRes

	//Once the original futures have completed,
	//and the subsequent sum completes,
	//you'll be able to see the result
	/*Because for expressions serialize their transformations, if you don't create the futures before
	the for expression, they won't run in parallel. */

	val forNP = for{
	x <- Future { Thread.sleep(10000); 21+21}
	y <- Future { Thread.sleep(10000); 23+23}
	}

	forNP //needs at least twenty seconds

	/*Creating the Future: Future.failed, Future.successful, Future.fromTry, and Promises
	the Future companion object also includes three factory methods
	for creating already-completed futures: successful,failed, and fromTry.
	These factory methods do not require an ExecutionContext*/

	Future.successful{ 21 + 21 }
	Future.failed{ new Exception("fug") }

	//already completed from Try
	import scala.util.{Success, Failure}
	Future.fromTry( Success { 21 + 21 })
	Future.fromTry( Failure (new Exception("fug")))

	/*The most general way to create a future is to use a Promise.
	Given a promise you can obtain a future	that is controlled by the promise.
	The future will complete when you complete the promise*/

	import scala.concurrent.Promise._
	val pro = Promise[Int]
	val fut = pro.future
	pro.success(Some(42))
	pro.failure("fugggg")
	fut.value

	/*The failure method takes an exception that
	will cause the future to fail with that exception.
	The complete method takes a Try.
	A completeWith method, which takes a future, also exists;
	the promise's future will thereafter mirror the completion status
	of the future you passed tocompleteWith*/

	//filter and collect
	val fut = Future { 42 }
	val valid = fut.filter{res => res > 0}
	valid.value

	val invalid = fut.filter(res=>res<0)
	invalid.value

	val valid = for (res <- fut if res > 0) yield res
	valid.value
	val invalid = for(res <- fut if res < 0) yield res
	invalid.value

	/*Future's collect method allows you to validate the future value
	and transform it in one operation.
	If the partial function passed to collect is defined at the future result,
	the future returned by collect will succeed
	with that value transformed by the function*/

	val valid = fut collect {
		case res if res > 0 => res+46
	}

	valid.value

	/*Dealing with failure: failed, fallBackTo,
	recover, and recoverWith

	The failed method will transform a failed future of any type into a successfulFuture[Throwable]*/

	val failure = Future { 42 / 0 }
	failure.value
	val expectedFailure = failure.failed

	/*If the future on which the failed method is called ultimately succeeds, the future returned byfailed
	will itself fail with a NoSuchElementException. The failed method is appropriate, therefore, only when you
	expect that the future will fail. */

	val success = Future{42/1}
	success.value
	val unexpectedSuccess.value = success.failed
	unexpectedSuccess.value

	/*The fallbackTo method allows you to provide an alternate future to use
	in case the future on which you invoke fallbackTo fails.*/

	val fallback = failure.fallbackTo(success)
	fallback.value

	/*If the original future on which	fallbackTo
	is invoked fails, a failure of the future passed tofallbackTo
	is 	essentially ignored. The future returned by fallbackTo
	will fail with the initial exception. Here's an example:*/

	val failedFallback = failure.fallbackTo(
		Future{ val res = 42; require(res <0); res}
	)

	failedFallback.value
	//the recover methodallows you to transform a failed future into a successful one
	val recovered = failedFallback recover{
		case ex:ArithmeticException=> -1
	}

	recovered.value
	/*If the original future doesn't fail,
	the future returned by recover will complete with the same value
	*/
    val unrecovered = fallback recover{
    	case ex: ArithmeticException => -1
	}

	unrecovered.value
	/*Similarly, if the partial function passed to
	recover
	isn't defined at the exception with which the original
	future ultimately fails, that original failure will pass through*/
	val alsoUnrecovered = failedFallback recover{
		case ex: IllegalArgumentException => - 2
	}

	alsoUnrecovered.value

	/*The recoverWith method is similar to recover,
	except instead of recovering to a value like recover,
	the recoverWith method allows you to recover to a future value.*/
	val alsoRecovered = failedFallback recoverWith{
		case ex: ArithmeticException => Future{
			42+46
		}
	}
	alsoRecovered.value

	//mapping both posibilities: transform

	/*futures transform method accepts two functions with which to transform a future:
	one to use in case of success and the ot her in case of failure*/

	val first = success.transform(
		res => res * -1,
		ex => new Exception("see clause", ex))

	//if the future succeds the first func is used
	firt.value
	//if fails, the second
	val second = failure.transform(
		res => res * -1,
		ex => new Exception("see calsue", ex))

	second.value

	/*Note that with the transform method shown in the previous examples,
	you can't change a successful future into a failed one,
	nor can you change a failed future into a successful one.
	To make this kind of transformation easier,
	introduced an alternate overloaded form of transform
	that takes a func from Try to Try.
	*/

	val firstCase = success.transform{
		case Success(res) => Success(res * -1)
		case Failure(ex) => Failure(new Exception("see clause", ex))

	}

	firstCase.value

	val secondCase = failure.transform{
		case Success(res) => Success(res * -1)
		case Failure(ex) => Failure(new Exception("see clause", ex))

	}


	secondCase.value

	val nonNegative = failure.transform {
		case Success(res) => Success(res.abs + 1)
		case Failure => Succes(0)
	}

	nonNegative.value


	//combining futures: zip, Future.fold, Future.reduce, Future.sequence, and Future.traverse
	val zippedSuccess = success zip recovered
	zippedSuccess.value

	//if either fails, the future returned by zip will also fail with the same Exception
	val zippedFailure = success zip failure
	zippedFailure.value

	//if both fail, the failed future that results will contain
	//the Ex stored in the initial future on which zip was invoked

	/*
	Future's companion object offers a fold method 	that allows you
	to accumulate a result across a TraversableOnce collection of futures,
	yielding a future result. If all futures in the collection succeed,
	the resulting future will succeed with the accumulated result.
	If any future in the collection fails, the resulting future will fail.
	If multiple futures fail, the result will fail with the same exception with
	which the first future (earliest in the TraversableOnce collection) fails.
	*/

	val fortyTwo = Future { 21 + 21}
	val fortySix = Future { 23 + 23}
	val furureNums = List(fortyTwo, fortySix)
	val folded =
		Future.fold(futureNums)(0){ (acc, num) =>
			acc + num
		}

	folded.value

	//reduce
	val reduced = Future.reduce(futureNums){(acc, num) => acc + num }
	reduced.value


	/*
	The Future.sequence method transforms a TraversableOnce collection of futures
	into a futureTraversableOnce of values. For instance, in the following example,
	sequence is used to transform a List[Future[Int]] to a Future[List[Int]]
	*/
	val futureList = Future.sequence(futureNums)
	futureList.value

	//peforming side-effects: forEach, onComplete, and andThen
	//in the following example a println is not executed in the case
	//of a failed future, just a successful future:

	failure.foreach(ex => println(ex))
	success.foreach(res => println(res))

	for(res <- failure) println(res)
	for(res <- failure) println(res)

	/*
	Future also offers two methods for registering "callback" functions.
	The onComplete method will be executed whether the future ultimately succeeds or fails.
	The function will be passed a Try— a Success holding the result if the future succeeded,
	else a Failure holding the exception that caused the future to fail
	*/


	import scala.utiil.{Success, Failure}
	success onCOmplete {
		case Success(res) => println(res)
		case Failure(ex) => println(ex)
	}

	failure onComplete {
		case Success(res) => println(res)
		case Failure(ex) => println(ex)
	}

	/*
	Future does not guarantee any order of execution
	for callback functions registered with onComplete.
	If you want to enforce an order for callback functions,
	you must use andThen instead. The andThen method returns
	a new future that mirrors (succeeds or fails in the same way as)
	the original future	on which you invoke andThen,
	but it does not complete until the callback function has been fully executed
	*/

	val newFuture = success andThen {
		case Success(res) => println(res)
		case Failure(ex) => println(ex)
	}

	newFuture.value

	/*
	Note that if a callback function passed to andThen
	throws an exception when executed, that exception will not
	be propagated to subsequent callbacks or reported via the resulting future
	*/

	//flatten, zipWith and transformWith
	val nestedFuture = Future { Future { 42 } }
	val flattened = nestedFuture.flatten

	//zipWith zips and map
	val futNum = Future { 21 + 21}
	val futStr = Future { "ans" + "wer" }
	val zipped = futNum zip futStr
	val mapped = zipped map{
		case(num, str) => s"$num is the $str"
	}
	mapped.value

	val fut = futNum.zipWith(futStr){
		case(num, str) => s"$num is the $str"
	}

	fut.value

	//transFormWith allows you to transform a future using a function from Try to Future
	val flipped = success.transformWith {
		case Succes(res) => Future { throw new Exception(res.toString)}
		case Failure(ex) => Future { 21 + 21 }
	}

	flipped.value

	/*The transformWith method is similar to the new,
	overloaded transform method,except instead of yielding
	a Try in your passed function as in transform,
	transformWithallows you to yield a future.*/



	val total = Vector[Int](1,2,3,4)
	val veg = total.filter{ veg }
	val veg = total.filter{ meat }

	val alph = 'a' to 'z'
	alph.mkString
	alph.foldLeft("")(_ + _)
	alph.toVector.mkString
	val v = Vector.empty //no type
	alph.flatMap(c => {
		v :+ c
	}).mkString



	total.reduce((acc, num) => {
		case n if(n == 3 || n == 4) => acc + num
		case _ => 0
	})

	total.filter{n => n>2}.sum



	//32.4
	/*
	One advantage of Scala's futures is that they help you avoid blocking.
	On most JVM implementations, after creating just a few thousand threads,
	the cost of context switching between threads will degrade	performance to an unnacceptable level.
	By avoiding blocking, you can keep the finite number of	threads you decide to work with busy.
	Nevertheless, Scala does allow you to block on a future result	when you need to.
	Scala's Await object facilitates blocking to wait for future results.
	*/

	import scala.concurrent.Await
	import scala.concurrent.duration._
	val x = Await.result(fut, 15.seconds) //blocks

	/*
	One place where blocking has been generally accepted is in tests of asynchronous code.
	you can use blocking constructs provided by ScalaTest's trait ScalaFutures.
	The futureValue method, implicitly added to Future by ScalaFutures,
	will block until the future completes. If the future fails, futureValue will
	throw a TestFailedException describing the problem. If the future succeeds,
	futureValue will return the successful resul

	import org.scalatest.concurrent.ScalaFutures._
	val fut = Future { Thread.sleep(10000); 21 + 21 }
	fut.futureValue should be (42) //blocks

	ScalaTest 3.0 adds "async" testing styles that allow you to test
	futures without blocking. Given a future, instead of blocking and
	performing assertions on the result, you can map assertions directly onto that future and
	return the resultingFuture[Assertion] to ScalaTest
	When the future assertion completes, ScalaTest will fire events
	(test succeeded, test failed, etc.) to the test reporter asynchronously.


	import org.scalatest.AsyncFunSpec
	import scala.concurrent.Future

	class AddSpec extends AsyncFunSpec {

	def addSoon(addends: Int*): Future[Int] =
	Future { addends.sum }

	describe("addSoon") {
	it("will eventually compute a sum of passed Ints") {
	val futureSum: Future[Int] = addSoon(1, 2)
	// You can map assertions onto a Future, then return
	// the resulting Future[Assertion] to ScalaTest:
	futureSum map { sum => assert(sum == 3) }
	}
	}
	}

	Once in "future space," try to stay in future space.
	Don't block on a future then continue the computation with the result.
	Stay asynchronous by performing a series of transformations, each of which returns a new future to transform.
	To get results out of future space, register side effects to be performed asynchronously once futures complete.
	*/

}