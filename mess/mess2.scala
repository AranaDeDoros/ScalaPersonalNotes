 val donutName = "Vanilla Donut"
  val quantityPurchased = 10
  val price = 2.50
  val donutJson =
    s"""
       |{
       |"donut_name":"$donutName",
       |"quantity_purchased":"$quantityPurchased",
       |"price":$price
       |}
      """.stripMargin
  println(donutJson)