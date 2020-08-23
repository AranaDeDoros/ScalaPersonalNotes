object Mess3 extends App {
	
	import scala.collection.mutable._
	
	val lb = List[Int](1,2,3)
	val ab = List[Int](-1,-2,3)

	//assert returns Unit
	val sameLengthTest:Unit = assert( lb.length == ab.length, "they have different lengths")
  	val filterNegatives: (List[Int]) => List[Int] = (list) => list.filter{ e => e > 0 }
	//val onlyPositives = assert(filterNegatives(lb) == filterNegatives(ab), "different elems")

	val multiplyAllElements:((List[Int]) => List[Int]) = (list => list.map{ e => e * 2})


	val withoutMessage = assert( lb.length == ab.length, "my message")

	val a = assume( lb.length == ab.length, "wrong")

	val r = require(lb.length > 0 && ab.length > 0, "ok")

	println(sameLengthTest)
	println(withoutMessage)
	//println(onlyPositives)

	//closure
	def appending(list: List[Int]): List[Int] = list ++ List[Int](11,22,44) ++ ab ++ lb

	def tbi(arg:Any*): Nothing = ???

	//will throw NotImplementedException
	//tbi(2)

	val l1 = List[Int](231,238,538,539,584,592,621,309,314,319,532,535,536,538,539,540,579,584,585,588,589,597,607,619,625,640,647,669,678,710)
	val l2 = List[Int](277,280,309,319,532,535,536,538,539,540,579,584,585,597,607,615,619,625,640,647,669,678)

	val l3 = l1 ++ l2

	val another = List[Int](88,277,280,309,319,532,535,536,538,539,540,579,584,585,597,607,615,619,625,640,647,669,678,999)

	val unique = l3.toSet[Int].toList ++ another.toSet[Int].toList

	unique.toSet[Int].toList.sorted

	l3.toSet[Int].toList.sorted

	def addTwo(a: List[Char])(b: List[Int]) = b zip a

	val resZip = addTwo(List[Char]('a', 'b', 'c'))(List[Int](1,2,3))
	resZip.unzip


	val testList = List[Double](1,2.8,3, 0.2, 2.0, 0.12)
	testList.partition{ e => e > 0.5}

	testList.isEmpty

	testList.head
	testList.tail
	testList.init
	testList.last
	testList.reverse
	testList.drop(2)
	testList.take(3)
	testList.splitAt(2)
	testList(2)


	val tup =  (2, "k", 3, "m")
	val tup2 = ("k", 2, "m", 3)

	val restup = tup.productIterator.toList zip tup2.productIterator.toList
	println(restup)

	
	val immuSet = Set(1,2,3) //Set
	val immuHashSet = Set(1,2,3,4,5) //HashSet

	val immuMap = Map("test"->1, "ok"->2) //Map
	val immuMapSet = Map("another test"-> true, "yes"-> false) //HashMap


	object TrafficLightColor extends Enumeration {
 	type TrafficLightColor = Value
 	//val Red, Yellow, Green = Value
 	val Red = Value(0, "Stop")
	val Yellow = Value(10) // Name "Yellow"
	val Green = Value("Go") // ID 11

	}

	println(TrafficLightColor.values)
	println(TrafficLightColor.Red)
	println(TrafficLightColor(0)) // Calls Enumeration.apply
	println(TrafficLightColor.withName("Red"))
	//println(TrafficLightColor.withName("Red")) //exception

	val t1 = Task(1, "test", 100, true)
	val t2 = Task(2, "another", 20, false)
	val sch = Schedule("17/08/2020", 22, "me")
	sch.addTask(t1)
	sch.addTask(t2)

	val frst = sch.findTask(1)
	frst.description ="updated desc"
	frst.priority = 20
	println(frst)

	sch.removeTask(1)
	sch.listTasks


  	case class Task(id: Int, var description: String, var priority: Int, var done: Boolean)

	case class Schedule(date: String, nTasks: Int, user: String){

		private val tasks = new ListBuffer[Task]()
		
		def addTask(task: Task):List[Task] = (this.tasks += task).toList

		def findTask(taskId : Int): Task = {
			
			val current:Option[Task]  = this.tasks.find(t => t.id == taskId)
		  current.getOrElse(Task(0, "err", 0, false))
      
		}

		def editTask(taskDetails: Task): ListBuffer[Task] =  {
			
			//there's bound to be a better way to do this...
			val foundTask = findTask(taskDetails.id)
			foundTask.description = taskDetails.description
			foundTask.priority = taskDetails.priority
			foundTask.done = taskDetails.done
			this.tasks
      
		}

		def removeTask(taskId: Int): List[Task] = {

			val task = findTask(taskId)
			val afterRemoval = this.tasks -= task
      		afterRemoval.toList

		}

		def listTasks: Unit = println(s"""Current tasks:${this.tasks.map{_.toString}}""")

	}

	object Writer  {
		def saveAllTasks(schedule: Schedule):Unit = println("saved")
		def readAllTasks(path: String): Unit = println("loaded")
	}
}