object Ch17 extends App {

	//17 futures
	/*
	A block of code wrapped in a Future { ... } executes concurrently.
	• A future succeeds with a result or fails with an exception.
	• You can wait for a future to complete, but you don’t usually want to.
	• You can use callbacks to get notified when a future completes, but that gets
	tedious when chaining callbacks.
	• Use methods such as map/flatMap, or the equivalent for expressions, to compose
	futures.
	• A promise has a future whose value can be set (once), which gives added
	flexibility for implementing tasks that produce results.
	• Pick an execution context that is suitable for the concurrent workload of your
	computation.
	 */

	//17.1 running tasks in the future
	import java.time._
	import scala.concurrent._
	import scala.concurrent.ExecutionContext.Implicits.global
	Future {
	 Thread.sleep(10000)
	 println(s"This is the future at ${LocalTime.now}")
	}
	println(s"This is the present at ${LocalTime.now}")

	/*When you create a Future, its code is run on some thread. One could of course
	create a new thread for each task, but thread creation is not free. It is better to
	keep some pre-created threads around and use them to execute tasks as needed.
	A data structure that assigns tasks to threads is usually called a thread pool. In Java,
	the Executor interface describes such a data structure. Scala uses the ExecutionContext
	trait instead*/

	import scala.concurrent.ExecutionContext.Implicits.global
	import java.time._
	import scala.concurrent._
	Future {
	 Thread.sleep(10000)
	 println(s"This is the future at ${LocalTime.now}")
	}


	val f = Future {
	 Thread.sleep(10000)
	 42
	}

	val f2 = Future {
	 if (LocalTime.now.getHour > 12)
	 throw new Exception("too late")
	 42
	}

	/*
	you should stay away from computations with
	side effects. It is best if you don’t increment shared counters—even atomic
	ones. Don’t populate shared maps—even threadsafe ones. Instead, have
	each future compute a value. Then you can combine the computed values
	after all contributing futures have completed.
	*/

	//17.2 Waiting for results
	import scala.concurrent.duration._
	val f = Future { Thread.sleep(10000); 42}
	val result = Await.result(f, 10.seconds)

	/* Importing scala.concurrent.duration._
	enables conversion methods from integers to Duration
	objects, called seconds, millis, and so on
	If the task is not ready by the allotted time, the Await.ready method throws a
	TimeoutException
	If the task throws an exception, it is rethrown in the call to Await.result.
	To avoid that, you can call Await.ready and then get the result.*/

	val f = Future { ... }
	Await.ready(f, 10.seconds)
	val Some(t) = f.value

	//The value method returns an Option[Try[T]]
	/*
	The Future class also has result and ready methods, but you should
	not call them. If the execution context uses a small number of threads (which
	is the case for the default fork-join pool), you don’t want them all to block.
	Unlike the Future methods, the Await methods notify the execution context so
	that it can adjust the pooled threads.
	 */

	//17.3 the Try class
	//A Try[T] instance is either a Success(v),
	//where v is a value of type T or a Failure(ex),
	//where ex is a Throwable.
	import scala.util.Try._
	t match {
	 case Success(v) => println(s"The answer is $v")
	 case Failure(ex) => println(ex.getMessage)
	}

	//you can use the isSuccess or isFailure methods to find out whether
	//the Try object represents success or failure.
	if (t.isSuccess) println(s"The answer is ${t.get}")

	//To get the exception in case of failure, first apply the failed method which turns
	//the failed Try[T] object into a Try[Throwable] wrapping the exception.
	if (t.isFailure) println(t.failed.get.getMessage)

	// turn a Try object into an Option with the toOption method
	//To construct a Try object, call Try(block) with some block of code. For example,
	val result = Try(str.toInt)

	//17.4 callbacks
	/*one does not usually use a blocking wait to get the result of a future
	 For better performance, the future should report its result to a callback
	function.*/
	val f = Future { Thread.sleep(10000)
		if(random() < 0.5) throw new Exception
		42
	}

	f.onComplete{
		case Success(v) => println(s" The answer is $v")
		case Failure(ex) => println(ex.getMessage)
	}

	/*
	There are callback methods onSuccess and onFailure that are only
	called on success or failure of a future. However, these are deprecated
	because they are even bigger contributors to callback hell.
	*/

	//17.5 composing future tasks
	val future1 = Future {getData1()}
	val future2 = Future {getData2()}

	future1 onComplete {
	 case Success(n1) =>
	 future2 onComplete {
	 case Success(n2) => {
	 val n = n1 + n2
	 println(s"Result: $n")
	 }
	 case Failure(ex) => ...
	 }
	 case Failure(ex) => ...
	}

	/*
	Instead of nesting callbacks, we will use an approach that you already know
	from working with Scala collections.
	Think of a Future as a collection with (hopefully, eventually) one element.
	 */
	val future1 = Future {getData1()}
	val combined = future1.map{n1 => n1 + getData2()}

	/*
	Here future1 is a Future[Int]—a collection of (hopefully, eventually) one value. We
	map a function Int => Int and get another Future[Int]—a collection of (hopefully,
	eventually) one integer
	*/
	/*The call to getData2 is
	running after getData1, not concurrently. Let’s fix that with a second map:*/
	val future1 = Future { getData1() }
	val future2 = Future { getData2() }
	val combined = future1.map(n1 => future2.map(n2 => n1 + n2))

	//When future1 and future2 have delivered their results, the sum is computed.
	//now combined is a Future[Future[Int]]
	val combined = f1.flatMap(n1 => f2.map(n2 => n1 + n2))
	val combined = for (n1 <- future1; n2 <- future2) yield n1 + n2

	//This is exactly the same code since for expressions are translated
	//to chains of map and flatMap.

	//using guards
	val combined = for (int n1 <- future1; n2 <- future2 if n1 != n2) yield n1 + n2

	//If the guard fails, the computation fails with a NoSuchElementException


	/*
	What if something goes wrong? The map and flatMap implementations take care of
	all that. As soon as one of the tasks fails, the entire pipeline fails, and the exception
	is captured. In contrast, when you manually combine callbacks, you have to deal
	with failure at every step.
	*/

	/*So far, you have seen how to run two tasks concurrently. Sometimes, you need
	one task to run after another. A Future starts execution immediately when it is
	created. To delay the creation, use functions.*/

	def future1 = Future { getData() }
	def future2 = Future { getMoreData() } // def, not val
	val combined = for (n1 <- future1; n2 <- future2) yield n1 + n2

	//Now future2 is only evaluated when future1 has completed.
	/*It doesn’t matter whether you use val or def for future1. If you use def, its creation
	is slightly delayed to the start of the for expression.
	This is particularly useful if the second step depends on the output of the first:*/

	def future1 = Future { getData() }
	def future2(arg: Int) = Future { getMoreData(arg) }
	val combined = for (n1 <- future1; n2 <- future2(n1)) yield n1 + n2

	//17.6 other future transformations
	/*The foreach method works exactly like it does for collections, applying a method
	for its side effect. The method is applied to the single value in the future. It is
	convenient for harvesting the answer when it materializes.*/

	val combined = for (n1 <- future1; n2 <- future2) yield n1 + n2
	combined.foreach(n => println(s"Result: $n"))

	/*The recover method accepts a partial function that can turn an exception into a
	successful result. Consider this call:*/

	val f = Future { persist(data) } recover { case e: SQLException => 0 }

	/*The fallbackTo method provides a different recovery mechanism. When you call
	f.fallbackTo(f2), then f2 is executed if f fails, and its value becomes the value of
	the future. However, f2 cannot inspect the reason for the failure

	The failed method turns a failed Future[T] into a successful Future[Throwable], just
	like the Try.failed method. You can retrieve the failure in a for expression like this:*/
	val f = Future { ... }
	for (ex <- f.failed) println(ex)

	/*Finally, you can zip two futures together. The call f1.zip(f2) yields a future whose
	result is a pair (v, w) if v was the result of f1 and w the result of f2, or an exception
	if either f1 or f2 failed. (If both fail, the exception of f1 is reported.)*/


	//17.7 methods in the future object
	//the companion object
	/*Suppose that, as you are computing a result, you organize the work so that you
	can concurrently work on different parts. For example, each part might be a range
	of the inputs. Make a future for each part:
	val futures = parts.map(p => Future { compute result in p })
	Now you have a collection of futures. Often, you want to combine the results.
	By using the Future.sequence method, you can get a collection of all results for further
	processing:*/
	val result = Future.sequence(futures);

	/*Note that the call doesn’t block—it gives you a future to a collection. For example,
	assume futures is a Set[Future[T]]. Then the result is a Future[Set[T]]. When the results
	for all elements of futures are available, the result future will complete with a set
	of the results.
	If any of the futures fail, then the resulting future fails as well with the exception
	of the leftmost failed future. If multiple futures fail, you don’t get to see the
	remaining failures.
	The traverse method combines the map and sequence steps. Instead of*/
	val futures = parts.map(p => Future { compute result in p })
	val result = Future.sequence(futures);
	you can call
	val result = Future.traverse(parts)(p => Future { compute result in p })
	/*The function in the second curried argument is applied to each element of parts.
	You get a future to a collection of all results.
	There are also reduceLeft and foldLeft operations that are analogous to the reductions and folds,
	You supply an operation that combines the results of all futures as they
	become available. For example, here is how you can compute the sum of the results:*/
	val result = Future.reduceLeft(futures)(_ + _)
	//Yields a future to the sum of the results of all futures
	/*So far, we have collected the results from all futures. Suppose you are willing to
	accept a result from any of the parts. Then call
	Future[T] result = Future.firstCompletedOf(futures)
	You get a future that, when it completes, has the result or failure of the first
	completed element of futures.
	The find method is similar, but you also supply a predicate.*/
	val result = Future.find(futures)(predicate)
	 // Yields a Future[Option[T]]
	/*You get a future that, when it completes successfully, yields Some(r), where r is
	the result of one of the given futures that fulfills the predicate. Failed futures are
	ignored. If all futures complete but none yields a result that matches the predicate,
	then find returns None*/

	/*A potential problem with firstCompletedOf and find is that the other
	computations keep on going even when the result has been determined.
	Scala futures do not have a mechanism for cancellation. If you want to stop
	unnecessary work, you have to provide your own mechanism

	Finally, the Future object provides convenience methods for generating simple
	futures:
	Future.successful(r) is an already completed future with result r.
	Future.failed(e) is an already completed future with exception e.
	Future.fromTry(t) is an already completed future with the result or exception
	given in the Try object t.
	Future.unit is an already completed future with Unit result.
	Future.never is a future that never completes.
	*/

	//17.8 promises
	/*A Future object is read-only. The value of the future is set implicitly when the task
	has finished or failed. A Promise is similar, but the value can be set explicitly

	Consider this method that yields a Future:*/
	def computeAnswer(arg: String) = Future {
	 val n = workHard(arg)
	 n
	}
	With a Promise, it looks like this:
	def computeAnswer(arg: String) = {
	 val p = Promise[Int]()
	 Future {
	 val n = workHard(arg)
	 p.success(n)
	 workOnSomethingElse()
	 }
	 p.future
	}

	/*Calling future on a promise yields the associated Future object. Note that the method
	returns the Future right away, immediately after starting the task that will eventually yield the result.
	That task is run in another Future, defined by the expression
	Future { ... }, that is unrelated to the promise’s future.
	Calling success on a promise sets the result. Alternatively,
	you can call failure with an exception to make the promise fail. As soon as one of these methods is called,
	the associated future is completed, and neither method can be called again.
	(An IllegalStateException is thrown otherwise.) From the point of view of the consumer
	(that is, caller of the computeAnswer method), there is no difference between the two approaches.
	Either way, the consumer has a Future and eventually gets the result.
	The producer, however, has more flexibility when using a Promise. As suggested
	in the code sample, the producer can do other work besides fulfilling this promise.
	For example, the producer might work on fulfilling multiple promises*/

	val p1 = Promise[Int]()
	val p2 = Promise[Int]()
	Future {
	 val n1 = getData1()
	 p1.success(n1)
	 val n2 = getData2()
	 p2.success(n2)
	}

	/*It is also possible to have multiple tasks that work concurrently to fulfill a single
	promise. When one of the tasks has a result, it calls trySuccess on the promise.
	Unlike the success method, that method accepts the result and returns true if the
	promise has not yet completed; otherwise it returns false and ignores the result.*/
	val p = Promise[Int]()
	Future {
	 var n = workHard(arg)
	 p.trySuccess(n)
	}
	Future {
	 var n = workSmart(arg)
	 p.trySuccess(n)
	}
	/*The promise is completed by the first task that manages to produce the result.
	With this approach, the tasks might want to periodically call p.isCompleted to check
	whether they should continue.*/

	//Scala promises are equivalent to the CompletableFuture class in Java 8.

	//17.9 execution contest
	/*By default, Scala futures are executed on the global fork-join pool.
	That works well for computationally intensive tasks. However, the fork-join pool
	only manages a small number of threads (by default, equal to the number of cores of all
	processors). This is a problem when tasks have to wait, for example when communicating with a remote resource.
	A program could exhaust all available threads, waiting for results.
	You can notify the execution context that you are about to block,
	by placing the blocking code inside blocking { ... }:*/

	val f = Future {
	 val url = ...
	 blocking {
	 val contents = Source.fromURL(url).mkString
	 ...
	 }
	}

	/*The execution context may then increase the number of threads. The fork-join
	pool does exactly that, but it isn’t designed for perform well for many blocking
	threads. If you do input/output or connect to databases, you are better off using
	a different thread pool. The Executors class from the Java concurrency library gives
	you several choices. A cached thread pool works well for I/O intensive workloads.
	You can pass it explicitly to the Future.apply method, or you can set it as the implicit
	execution context:*/
	val pool = Executors.newCachedThreadPool()
	implicit val ec = ExecutionContext.fromExecutor(pool)

}