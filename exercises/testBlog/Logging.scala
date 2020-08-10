package blog.logging{

import blog.util._
	//logger

  object Logger {

    private def info(message: String): String = s" INFO - $message"
    private def debug(message: String, scope: String = "regular"): String =
      s"DEBUG $message - $scope"
    private def warning(message: String) = s"WARNING $message"
    private def error(message: String, severity: Int = 5) =
      s"ERROR - $message - $severity"

  }

  class Logger(message: String, `type`: String) {

    import Logger._

    val log = `type` match {

      case "info"    => Logger.info(message)
      case "debug"   => Logger.debug(message)
      case "warning" => Logger.warning(message)
      case "error"   => Logger.error(message)
      case _         => ""
    }

    def registerToDatabase() = {
      val registeredAt = DateUtils.getCurrentDate
      println(s"registering ${log}  at $registeredAt")

    }

  }
}