object Ch17 extends App {

	//17.1. Going to and from Java Collections
	// java
	//use the methods of the JavaConversions object
	/*public static List<Integer> getNumbers() {
	 List<Integer> numbers = new ArrayList<Integer>();
	 numbers.add(1);
	 numbers.add(2);
	 numbers.add(3);
	 return numbers;
	}*/


	val numbers = JavaExamples.getNumbers()
	numbers.foreach(println) // this won't work

	import scala.collection.JavaConversions.asScalaBuffer
	val numbers = asScalaBuffer(JavaExamples.getNumbers)
	numbers.foreach(println)
	// prints 'scala.collection.convert.Wrappers$JListWrapper'
	println(numbers.getClass)

	import scala.collection.JavaConversions.asScalaBuffer
	val numbers = JavaExamples.getNumbers
	numbers.foreach(println)
	// prints 'java.util.ArrayList'
	println(numbers.getClass)



	// java
	/*public static Map<String, String> getPeeps() {
	 Map<String, String> peeps = new HashMap<String, String>();
	 peeps.put("captain", "Kirk");
	 peeps.put("doctor", "McCoy");
	 return peeps;
	}*/

	import scala.collection.JavaConversions.mapAsScalaMap
	// explicit call
	val peeps1 = mapAsScalaMap(JavaExamples.getPeeps)
	// implicit conversion
	val peeps2 = JavaExamples.getPeeps


	//Going from Scala collections to Java collections
	/* java
	public static int sum(List<Integer> list) {
	 int sum = 0;
	 for (int i: list) { sum = sum + i; }
	 return sum;
	}*/

	import scala.collection.JavaConversions._
	import scala.collection.mutable._
	val sum1 = ConversionExamples.sum(seqAsJavaList(Seq(1, 2, 3)))
	val sum2 = ConversionExamples.sum(bufferAsJavaList(ArrayBuffer(1,2,3)))
	val sum3 = ConversionExamples.sum(bufferAsJavaList(ListBuffer(1,2,3)))


	//17.2
	//17.2. Add Exception Annotations to Scala Methods to	Work with Java
	// scala
	class Thrower {
	 @throws(classOf[Exception])
	 def exceptionThrower {
	 throw new Exception("Exception!")
	 }
	}

	/*If you want to declare that your Scala method throws multiple exceptions, add an an‐
	notation for each exception:*/
	@throws(classOf[IOException])
	@throws(classOf[LineUnavailableException])
	@throws(classOf[UnsupportedAudioFileException])
	def playSoundFileWithJavaAudio {
	 // exception throwing code here ...
	}


	/*If you don’t mark the Scala exceptionThrower method with the @throws annotation, a
	Java developer can call it without using a try/catch block in her method, or declaring
	that her method throws an exception. For example, you can define the Scala method as
	follows, without declaring that it throws an exception:*/
	//scala
	def exceptionThrower {
	 throw new Exception("Exception!")
	}


	/*java
	public static void main(String[] args) {
	 Thrower t = new Thrower();
	 t.exceptionThrower();
	}*/

	//17.5. Annotating varargs Methods
	package varargs
	import scala.annotation.varargs
	class Printer {
	 @varargs def printAll(args: String*) {
	 args.foreach(print)
	 println
	 }
	}

	/*
	package varargs;
	public class Main {
	 public static void main(String[] args) {
	 Printer p = new Printer();
	 p.printAll("Hello");
	 p.printAll("Hello, ", "world");
	 }
	}*/

	//17.6. When Java Code Requires JavaBeans
	//The @BeanProperty annotation can be used on fields in a Scala class constructor:
	import scala.reflect.BeanProperty
	class Person(@BeanProperty var firstName: String,
	 @BeanProperty var lastName: String) {
	 override def toString = s"Person: $firstName $lastName"
	}

	//It can also be used on the fields in a Scala class:
	import scala.reflect.BeanProperty
	class EmailAccount {
	 @BeanProperty var username: String = ""
	 @BeanProperty var password: String = ""
	 override def toString = s"Email Account: ($username, $password)"
	}


	package foo
	import scala.reflect.BeanProperty
	class Person(@BeanProperty var firstName: String,
	 @BeanProperty var lastName: String) {
	}
	class EmailAccount {
	 @BeanProperty var username: String = ""
	 @BeanProperty var password: String = ""
	}

	/*
	package foo;
	public class Main {
	 public static void main(String[] args) {
	 // create instances
	 Person p = new Person("Regina", "Goode");
	 EmailAccount acct = new EmailAccount();
	 // demonstrate 'setter' methods
	 acct.setUsername("regina");
	 acct.setPassword("secret");
	 // demonstrate 'getter' methods
	 System.out.println(p.getFirstName());
	 System.out.println(p.getLastName());
	 System.out.println(acct.getUsername());
	 System.out.println(acct.getPassword());
	 }
	}*/


	//17.7. Wrapping Traits with Implementations
	// scala
	package foo
	// the original trait
	trait MathTrait {
	 def sum(x: Int, y: Int) = x + y
	}
	// the wrapper class
	class MathTraitWrapper extends MathTrait


	/* java
	package foo;
	public class JavaMath extends MathTraitWrapper {
	 public static void main(String[] args) {
	 new JavaMath();
	 }
	 public JavaMath() {
	 System.out.println(sum(2,2));
	 }
	}*/

}