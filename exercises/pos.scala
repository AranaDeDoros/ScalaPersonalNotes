object PointOfSale extends App {

	type ProductList = Vector[Product]

	import scala.collection.immutable._

	case class Product(id: Int, sku: String, name: String, model: String, unitPrice: Double,
					   price: Double, offers: Vector[Int], categoryId: Int) extends Ordered[Product]{

		def compare(that: Product) = {
			 		if(this.price == that.price)
			 			{0}
			 		else if(this.price > that.price)
			 			{1}
			 		else
			 			{-1}
	 	}

	}

	object Product{
		def unapply(p: Product): (Int, String, String, String,
				                     Double, Double, Vector[Int], Int) ={
			(p.id, p.sku, p.name, p.model, p.unitPrice, p.price, p.offers, p.categoryId)
		}
	}

	class LimitedProduct(id: Int, sku: String, name: String, model: String,
											 unitPrice: Double, price: Double, offers: Vector[Int],
											 categoryId: Int, val reserved:Int, val span:(String, String))
			extends Product(id, sku, name, model, unitPrice, price, offers, categoryId) {
				def period= s"${span._1} to ${span._2} "
				def reserve(n: Int): Boolean = ???
		}

	import scala.language.implicitConversions
	object ProductOps{
		implicit def regular2Limited(p: Product): LimitedProduct  = {
				val (id, sku, name, model, unitPrice, price, offers, categoryId) = 	Product.unapply(p)
				val l = new LimitedProduct(id, sku, name, model, unitPrice, price, offers, categoryId, 0, ("",""))
				l
		}
	}

	case class Category(id:Int, description: String){}

	case class Offer(id:Int, description: String, active: Boolean, discount: Double,
					 start: String, end: String , productList: ProductList){

		val markedForOffer = productList.map{ _.id }

		val productListStr = productList.mkString(",")

		override def toString = s"#$id | $description | $start | $end | \n\n $productListStr"

	}


	object TaxCalculator{
		val currentVAT: Double = ???
	}


	case class Location(id: Int, managerId: String)

	class Stock(id: Int, val locationId:Int){

		val products  = Stock.retrieveProducts(id) match {
			case Some(p) => p
			case None    => Vector[Product]()
		}

		val location = Stock.isLocatedAt(locationId)

		def filterBy(products: ProductList, customFilter:(Product) => Boolean) = {

				products.filter{customFilter}

		}

		def sortBy(f: ProductList => ProductList) = f(this.products)

		def sortProductsByPrice =  this.products.sortWith(_.price > _.price)

		def sortProductsByName = this.products.sortWith(_.name > _.name)

		def search(s: String) = this.products.filter{ p => p.name.contains(s) | p.model.contains(s) | p.sku.contains(s)}

	}

	object Stock{

		def search(f:(String) => Option[ProductList], query:String) = f(query)

		def retrieveProducts(locationId: Int) : Option[ProductList] = ???

		def retrieveProductsByStockId(id: Int) : Option[ProductList] = ???

		def retrieveProductById(productId:Int) : Option[Product] = ???

		def retrieveProductsByCategory(categoryId: Int) : Option[ProductList] = ???

		def retrieveProductsBySupplier(supplierId: Int) : Option[Vector[Supplier]] = ???

		def isLocatedAt(locationId: Int) : Location = ???

	}

	case class Sale(transactionId: String, date: String,
					locationId: Int, products: ProductList, offer: Option[Offer]){

		implicit val vat = TaxCalculator.currentVAT

		val productsName = this.products.map{ _.name}.mkString(",")

		val discount = offer match{
						          case Some(o) => o.discount
						          case _ => 1.0
										}
		def total (implicit vat:Double) = this.products.map{ _.price * discount * vat }.reduce((t, n) => t + n)

		override def toString = s"#$transactionId | $date |  $locationId | $total | \n\n $products"

	}

	case class Customer(id: String, name: String, kind: Int = 0,
						registeredAt: String, modifiedAt: String){
		override def toString = s"$kind |  '$name'  registeredAt $registeredAt, modifiedAt $modifiedAt"
	}

	case class Supplier(id: Int, name: String, location: String, since: String)

	case class Purchase(transactionId: String, date: String, isPaid: Boolean)

	object Reporter{
		def printReport(period: (String, String) ) = ???
	}



}