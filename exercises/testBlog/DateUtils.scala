package blog.util{

  import java.util.Calendar
  import java.text.SimpleDateFormat
  import java.text.ParseException; 
  import java.util.Date; 

  object DateUtils{
    //as Thursday, November 29
    def getCurrentTime: String = getCurrentDateTime("EEEE, MMM, d")
    //as 6:20 p.m.
    def getCurrentDate: String = getCurrentDateTime("yyyy/MM/dd")
    //common function used by other date/time functions
    private def getCurrentDateTime(dateTimeFormat: String): String = {
      val dateFormat = new SimpleDateFormat(dateTimeFormat)
      val cal = Calendar.getInstance()
      dateFormat.format(cal.getTime())
    }
  }
}