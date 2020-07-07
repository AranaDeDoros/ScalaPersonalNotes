

object StringUtils{
	//adding own methods to the String CLASS
	implicit class StringImprovements(val s:String) {
		def increment: String = s.map{ c => (c+1).toChar}
		def decrement: String = s.map{ c => (c-1).toChar}
		def hideAll: String = s.map{ c => c.replaceAll(".", "*")}
		def plusOne: Int  s.toInt + 1
		def asBoolean = s match{
			case "0" | "zero" | "" | " " => false
			case  _ => true
		}
	}
}