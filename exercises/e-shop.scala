import scala.collection.mutable.{ListBuffer => Collection}

object EShop extends App {
 
	//don't take this example seriously, some methods are just for show/left without a realistic implementation

	val category = new Category("electronics", 1)
	val brand = new Brand("some brand", 15)	
	val refund = RefundsPolicy("out-of-stock")
	val provider = Provider("some provider", 3)
	val product = new Product ("hdd", "seagate-something", "012345678", 20.0, 121, "hdd 1tb") with Defective

	println(product.markAsDefective(product.id))
	println(product.scheduleForDisposal(product.name, DateUtils.getCurrentDate))

	val freeCoupon = new Coupon(DateUtils.getCurrentDate, "08/09/2020") with FreeProduct
	val discountCoupon = new Coupon(DateUtils.getCurrentDate, "11/09/2020") with Discount
	freeCoupon.retrieveProduct(22)
	discountCoupon.applyDiscount(200, 0.13)

	val secondProduct = new Product ("ssd", "kingston", "111325678", 200.0, 554, "250gb")

	val client = Client("me","me@me.com", "7788994455", "*******", "11/11/2021", 2552)
	val payment = new Payment(222, 10, "effective") with Paypal
	val myOrder = Order(2552, secondProduct, 200.0, 8818)

	println(myOrder)
	payment.connect(client)
	payment.makeDeposit(233.2)
	println(payment.processOrder(myOrder))

	val paymentCard = new Payment(232, 11, "card") with Card
	paymentCard.validate(CreditCard("12345678912345"))
	paymentCard.charge(2000.0)
	println(paymentCard.processOrder(myOrder))

	val wishlist = new WishList(2552)
	val myCart = new Cart(2552)
	wishlist.addProduct(product)
	myCart.addProduct(secondProduct)

	println(wishlist.products.toList)
	myCart.products.toArray.foreach{println}

	val offer = new PerUnitOffer(2, 0.4, "2x1", "08/08/2020", "10/10/2020") 
	println(offer.applyOffer(20.0))


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

	case class RefundsPolicy(description: String)


	trait ProductCRUD {

		def addProduct(product: Product): Collection[Product]

		def editProduct(product: Product): Option[Product]
		
		def removeProduct(product: Product): Collection[Product]

	}

	class WishList(private val userId: Int) extends ProductCRUD{

		val products = Collection[Product]()

		def addProduct(product: Product): Collection[Product] = products += product

		def editProduct(product: Product): Option[Product] = this.products.find{current => current == product} 
		
		def removeProduct(product: Product): Collection[Product] = products -= product

	}


	class Cart(private val userId: Int) extends ProductCRUD{

		val products = Collection[Product]()

		def addProduct(product: Product): Collection[Product] = products += product

		def editProduct(product: Product): Option[Product] = this.products.find{current => current == product} 
		
		def removeProduct(product: Product): Collection[Product] = products -= product

	}

	case class Order(private val clientId: Int, val product: Product, val `import`: Double, val id: Int)

	case class CreditCard(accountNumber: String)


	trait Effective{

		def makeDeposit(`import`: Double): Double

	}

	trait Paypal{

		def connect(user: Client): Boolean = if(user.username != "") true else false
		def makeDeposit(`import`: Double): Double = `import`

	}

	
	trait Card{

		def validate(card: CreditCard): Boolean = if(card.accountNumber != "") true else false
		def charge(`import`: Double): Double = `import`

	}

	class Payment(private val clientId: Int, val purchaseId: Int, val method: String){

		def processOrder(order: Order) = s" ${order.id} by ${this.method}"

	}

	trait Discount {
		def applyDiscount(price: Double, rate: Double): Double =  {
			price * rate
		}
	}

	trait FreeProduct {
		def retrieveProduct(productId: Int): Product = new Product("ok", "ok", "o", 20.0, 1, "okok")
	}

	case class Coupon(issued: String, expires: String) {
		def validUntil: String = s"issued - expires"
	}

	case class Client(username: String, email: String, cellphone: String, password: String, registeredAt: String, userId: Int)

	abstract class Offer(val description: String,  val start: String, val end: String) {

		def applyOffer(price: Double): Double

		override def toString = this.description

		def span: String = s" $start - $end"

	}

	class DiscountOffer(discount: Int, description: String, start: String, end: String) 
						extends Offer(description, start, end) {

		def applyOffer(price: Double): Double = {
			price * this.discount
		}

	}


	class PerUnitOffer(nUnits: Int, discount: Double, description: String,  start: String, end: String) 
					   extends Offer(description, start, end){

		def applyOffer(price: Double): Double = {
			(nUnits * price) / 2
		}

	}


	trait Defective {

		def markAsDefective(productId: Int): String = s" ${productId} marked as defective"

		def scheduleForDisposal(productName: String, date: String): String = s" ${productName} will be disposed of on $date"

	}
	
	case class Product(name: String, model: String, sku: String, price: Double, id: Int, description: String){

		override def toString = s" $name $model ${this.description} "

	}

	class Brand(val name: String, private val id: Int) {

		def products: Collection[Product] = Collection[Product]()

	}

	case class Provider(name: String, id: Int)

	class Category(val name: String, private val id: Int) extends ProductCRUD{

		val categories = Collection[Product]()

		def addProduct(product: Product): Collection[Product] = {
			categories += product
		}

		def editProduct(product: Product): Option[Product] = {
			categories.find{current => current == product} 
		}

		def removeProduct(product: Product): Collection[Product] = {
			categories -= product
		}

	}




}