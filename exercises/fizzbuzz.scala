object FizzBuzz{
	def main(args: Array[String]): Unit = {

  		(1 to 100).map{ _ match {
  				case i:Int  if((i%3 == 0) && (i%5 == 0))  => "fizzbuzz"
  				case i:Int  if(i%3 == 0)  => "fizz"
  				case i:Int  if(i%5 == 0)  => "buzz"
  				case i:Int => i.toString
  			}
  		}.foreach(println)

	}
}