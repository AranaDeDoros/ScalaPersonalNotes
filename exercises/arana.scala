package arana{

	package notations{

		import scala.annotation._

		class Prototype(target:String) extends StaticAnnotation
		class Dummy(target:String) extends StaticAnnotation

		class HasBugs(bugs:String]) extends StaticAnnotation
		class Fixed(status: Boolean = true) extends StaticAnnotation

		class Refactorizable(improvements:String) extends StaticAnnotation
		class Duplicate(paths:Vector[String]) extends StaticAnnotation
		class Environment(env: String) extends StaticAnnotation
		class Impure extends StaticAnnotation



	}

	package utils{

		import java.util.Calendar
		import java.text.SimpleDateFormat
		import java.text.ParseException;
		import java.util.Date;

		object DateUtils{

			val cal = Calendar.getInstance()

			//as Thursday, November 29
			def getCurrentTime: String = getCurrentDateTime("EEEE, MMM, d")
			//as 6:20 p.m.
			def getCurrentDate: String = getCurrentDateTime("yyyy/MM/dd")
			//common function used by other date/time functions
			private def getCurrentDateTime(dateTimeFormat: String): String = {
			  val dateFormat = new SimpleDateFormat(dateTimeFormat)
			  dateFormat.format(cal.getTime())
			}

			def addDays(days:Int) = {
			  val dateFormat = new SimpleDateFormat("yyyy/MM/dd")
			  val later = cal.add(Calendar.DAY_OF_MONTH, days)
			  dateFormat.format(cal.getTime)
			}

		}

		import java.sql._

		object Time{

			def now = Instant.now()
			def localNow = LocalTime.now()

		}
	}
}