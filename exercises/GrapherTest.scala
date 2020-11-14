  package com.aranadedoros

  object GrapherTest extends App{

   /**
     * Companion object for the [[com.aranadedoros.GrapherTest.Grapher]]
     * class in charge of printing and drawing
     *
     * @author AranaDeDoros
     * @version 1.0
     * @todo Add more functionality.
     * @see See [[https://github.com/AranaDeDoros]] for more
     * information.
     */
    object Grapher{

      import scala.util.Random
      val rnd = new Random
      def randomNumber(min:Int, max:Int) = this.rnd.between(min,max).toDouble //if i use default values, it throws error, why?
      def printTable(tabulation: Seq[String]) = {
        val top = "[ X   ,  Y  ]"
        val end = top +: tabulation
        end.foreach{println}
      }
      /**
       * @todo ""implement a way to plot the coordinates""
       */
      def draw() = ???

    }

    /**
     * A grapher instance given two sequences representing x and y values respectively.
     *
     * @author AranaDeDoros
     * @constructor Create a new grapher with a `r`, `age`, and `c`.
     * @param r Sequence for x values
     * @param c Sequence for y values
     * @version 1.0
     * @todo Add more functionality.
     *
     */
    @deprecated("A new class that takes a Seq[(Double, Double)] will be introduced", "0.5")
    final class Grapher(r:Seq[Double], c:Seq[Double]) {

      require(r.length > 0 && c.length > 0)
      val x = r
      val y = c
      val coordinates = x zip y
      val tab = this.coordinates.map{ xy => s"[${xy._1} , ${xy._2}]" }
      def transform(f:Seq[(Double, Double)] => Seq[(Double, Double)]) = f(this.coordinates)
      @deprecated("The `weight` field is going away", "1.0")
      def toCoordinates(c:Seq[(Double, Double)]) = c.map{ xy => s"[${xy._1} , ${xy._2}]" }

    }

    val x = Range.BigDecimal(Grapher.randomNumber(11,20),
                             Grapher.randomNumber(21, 30),
                             0.5).map(_.toDouble)

    val y = Range.BigDecimal(10.0, 20.0, 0.5).map(_.toDouble)
    val grapher = new Grapher(x,y)
    val coordinates = grapher.coordinates
    Grapher.printTable(grapher.tab)

    //PartialFunction[-A, +B] extends (A) => B
    val doubleIt: PartialFunction[(Double,Double), (Double,Double) ] = {
      case (a,b) => ((a*2), (b*2))
    }

    val f:(Seq[(Double, Double)]) => Seq[(Double, Double)] = v => v.map{ doubleIt  }
    val after = grapher.transform(f)
    val newCoordinates = grapher.toCoordinates(after)
    println(newCoordinates)



  }