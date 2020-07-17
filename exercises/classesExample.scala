import scala.util.Random

object Cipher extends App{

	class Coder(){
		val seed = 5
		val rnd = new Random(seed)
		var cipheringKey = 0

		def cipher(s:String): String ={
			cipheringKey = rnd.nextInt(25)
			s.map(c=> (c+cipheringKey).toChar)
		}

		def decipher(s:String): String ={
			s.map(c=> (c-cipheringKey).toChar)
		}
	}

	val testString = "omae wa mou shindeiru"
	val coder = new Coder()
	val cipheredString = coder.cipher(testString)
	val decipheredString = coder.decipher(cipheredString)
	println(s"Ciphered string $cipheredString")
	println(s"Original string $decipheredString")

}