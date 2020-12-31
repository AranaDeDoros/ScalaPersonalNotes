object TypeParams extends App {
		def combineWith[A , B](a: Seq[A], b: Seq[B]) : Seq[Any]  = a ++ b
		val a = List(1,2,3)
		val b = List(4.2,5.3,6.4)
		println(combineWith[Int, Double](a,b))

		def combineTuples[A,B](t1:(A,B), t2:(A,B)): ((A,A), (B,B)) =  {
			( (t1._1 , t2._1), (t1._2, t2._2) )
		}

		println(combineTuples[Int, String]((1,"test"),(2, "this")))


		def makeTuple[A ,B](a:(A,A), b:(B,B)) = (a._1, b._2)
		println(makeTuple((1,2),("test", "this")))

		def sumLists[A](t1:List[A], t2:List[A]) : List[A] = {
			t1 ++ t2
	  	}
		println(sumLists(List(1,2,3), List(4,5,6)))

		def sumMins[A: Numeric](x:List[A], y:List[A])(implicit ev:Integral[A]): A = {
		   val a = x.min
		   val b = y.min
		   ev.plus(a,b)
		}
		println(sumMins[Int](List(1,2,3,4), List(2,3,4,5)))


		import scala.reflect.ClassTag
		def ret[B:ClassTag](l:List[B]): Array[B] = l.toArray
		println(ret(List(1,2,3)).toVector)

		def l[A,B](a:Seq[A], b:Seq[B]) : Seq[Any] = a ++ b
			l(Seq(1,2,3), Seq(4,5,6))

		val at = List(1,2,3,4,5)

		def sumBothFold[A: Numeric](l: List[A])(implicit ev:Integral[A]) = {
			//elem +: for ordered appending
			val left = l.foldLeft(List.empty[A])((acc,elem) =>   (ev.times(elem, elem)) :: acc)
			val right = l.foldRight(List.empty[A])((elem,acc) => (ev.times(elem,elem)) :: acc)
			left ++ right
			//(left, right)
		}

		val rest = sumBothFold(at)
		println(rest)

		val ar = List(1,2,3,4,5)

		def reducedValues[A: Numeric](l: List[A], f: A => A)(implicit ev:Integral[A]) = {
			val left = l.reduceLeft((acc,elem) =>   {
	        println(acc,elem)
	      	ev.plus(f(elem), acc)
	    })
	     println(left)
			val right = l.reduceRight((elem,acc) => {
	      	println(acc,elem)
	      	ev.plus(acc, f(elem))
	    })
	     println(right)
			(left , right)
		}

		def reducedValues(l: List[Int], f:(Int) => Int)= {
			val left = l.reduceLeft((acc,elem) =>   {
	       println(acc,elem)
	      f(elem) + acc
	    })
	     println(left)
			val right = l.reduceRight((elem,acc) => {
	      println(acc,elem)
	        acc + f(elem)
	    })
	     println(right)
			(left , right)
		}
		val f: (Int) => Int = (a:Int) => a * a
		val resf = reducedValues(ar, f)
		println(resf)
}