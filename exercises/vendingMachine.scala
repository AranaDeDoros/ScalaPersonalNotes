import scala.collection.immutable._
import java.text.SimpleDateFormat
import java.util.Date

//not handling exceptional cases for now

object VendingMachineExample extends App {
	
	case class Order(val date: String, val `import`: Double, val productSelected: String)

	class VendingMachine(val machineId: Int, val location: String){

		val unitId = machineId.toString
		val machineLocation = location

		private val productsList = Map[String, Double](("chocolate bar", 2.98), ("soda", 5.15), ("peanuts", 9.14), ("melon pan", 60.01))
	

		def displayProductList: String = {
			productsList.mkString(" \n")
		}	

		def receiveOrder(order:Order): String = {
			
			val productChosen = order.productSelected
			val money = order.`import`

			
			val find = productsList.get(productChosen)
			val price: Double = find.getOrElse(0)


			def calculateChange(price: Double): String = {
			 val change = money - price
			 if( change < 0) "Invalid" else  s"Your change is  $$${change.toInt}"
			}	
			

			calculateChange(price)


		}

	}

	object VendingMachine{

		def apply(unitId: Int, location:String): VendingMachine = {
				val nvm = new VendingMachine(232, "Some Park")
				nvm
		}
	}	

	val vm =  VendingMachine(232, "Some Park")
	
	println(vm.displayProductList)

	val formatter = new SimpleDateFormat("dd/MM/yyyy")
	val date = new Date()
	val newOrder = Order(formatter.format(date), productSelected= "soda", `import` = 13.22)

	println(newOrder)
	println(vm.receiveOrder(newOrder))

}