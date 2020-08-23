object Ch14 extends App {
	
	//**************14.1****************//

	def above(that: Element):Element = {
	val this1 =	this widen that.width
	val that1 = that widen this.width
	assert(this1.width == that1.width)
	elem(this1.contents ++ that1.contents)
	}

	private def widen(w:Int): Element ={
		if (w <= width)	this
		else{
			val left = elem(' ', (w - width) / 	2, height)
			var right = elem(' ', w - width - left.width, height)
			left beside	this beside right
		} ensuring (w <= _.width)
	}


	//**************14.2****************//

	import org.scalatest.Suite
	import Element.elem
	class ElementSuite extends	Suite{
		def testUniformElement() {
			val ele = elem('x',	2,  3)
			assert(ele.width ==	2)
		}
	}	

	(new ElementSuite).execute()

	import org.scalatest.FunSuite
	import Element.elem
	class ElementSuite extends FunSuite	{
		test("elem result should have passed width") {
			val ele = elem('x',	2,  3)
			assert(ele.width ==	2)
		}
	}

	//**************14.3****************//

	expect(2){
		ele.width
	}	

	intercept[IllegalArgumentException]{
		elem('x', -2, 3)
	}


	//**************14.4****************//

	import	junit.framework.TestCase
	import	junit.framework.Assert.assertEquals
	import	junit.framework.Assert.fail
	import	Element.elem
	
	class ElementTestCase extends TestCase{
		def testUniformElement() {
			val ele = elem('x',	2,  3)
			assertEquals(2, ele.width)
			assertEquals(3, ele.height)
			try {
				elem('x', -2, 3)
				fail()
			}
			catch
			{
				case
				e: IllegalArgumentException
				=> // expected
			}
		}
	}	


	import org.scalatest.junit.JUnit3Suite
	import Element.elem
	class ElementSuite extends JUnit3Suite{
		def testUniformElement() {
			val ele = elem('x',	2,  3)
			assert(ele.width === 2)
			expect(3) { ele.height }
			intercept[IllegalArgumentException] {
			elem('x', -2, 3)
			}
		}
	}



	import	org.testng.annotations.Test
	import	org.testng.Assert.assertEquals
	import	Element.elem

	class ElementTests{

	@Test
	def verifyUniformElement() {
		val ele = elem('x',	2,  3)
		assertEquals(ele.width,	2)
		assertEquals(ele.height, 3)
	}

	@Test(expectedExceptions =Array(classOf[IllegalArgumentException]))
	def elemShouldThrowIAE() { elem('x', -2, 3) }
	
	}
	

	import org.scalatest.testng.TestNGSuite
	import org.testng.annotations.Test
	import Element.elem
	class ElementSuite extends TestNGSuite{
		@Test
		def verifyUniformElement() {
			val ele = elem('x',	2,  3)
			assert(ele.width === 2)
			expect(3) { ele.height }
			intercept[IllegalArgumentException] {
				elem('x', -2, 3)
			}
		}
	}

	//**************14.5****************//
	
	import org.scalatest.FlatSpec
	import org.scalatest.matchers.ShouldMatchers
	import Element.elem
	class ElementSpec extends FlatSpec	with ShouldMatchers	{

	"A UniformElement"	should	"have a width equal to the passed value"
	in {
		val ele = elem('x',	2,  3)
		ele.width should be (2)
	}

	it should "have a height equal to the passed value"	in {
		val ele = elem('x',	2,  3)
		ele.height should be (3)
	}

	it should "throw an IAE if passed a negative width"	in {
		evaluating {
			elem('x', -2, 3)
		} should produce [IllegalArgumentException]
	 }

	}

	(new ElementSpec).execute()


	import org.specs._
	import Element.elem
	object ElementSpecification extends	Specification{
		
		"A UniformElement"	should {
			"have a width equal to the passed value" in {
			val ele = elem('x',	2,  3)
			ele.width must be_==(2)
		}

		"have a height equal to the passed value" in {
			val ele = elem('x',	2,  3)
			ele.height must be_==(3)
		}
		
		"throw an IAE if passed a negative width"
		in {elem('x', -2, 3) must throwA[IllegalArgumentException]}
		}
	}

	//**************14.6****************//

	import org.scalatest.WordSpec
	import org.scalatest.prop.Checkers
	import org.scalacheck.Prop._
	import Element.elem
	
	class ElementSpec extends WordSpec with	Checkers{
	
		"elem result" must {
			"have passed width"	in {
			check((w:Int) => w > 0 ==> (elem('x', w,3).width == w))
			}
			"have passed height" in {
				check((h:Int) => h > 0 ==> (elem('x',2, h).height == h))
			}
		}
	}

}