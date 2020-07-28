object Ch7 extends App {
	
	//we can place imports anywhere
	//import classes, packages or objects
	//hide an rename members when imported

	//imported by default
	import java.lang._
	import scala._
	
	//**************7.1****************//
	
	/*
	//packaging with curly braces
	package com.acme.store{
		class Foo {override def toString = "I am com.acme.store.foo"}
	}

	//canonical name com.acme.store.Foo
	package com.acme.store

	class Foo {override def toString = "I am com.acme.store.foo"}


	//multiple
	package orderentry {
		class Foo {override def toString = "I am orderentry.Foo"}
	}
	//nested
	package customers{
		class Foo {override def toString = "I am customers.Foo"}

		package database {
			class Foo {override def toString = "I am customers.database.Foo"}			
		}
	}

	object PackageTests extends App{
		println(new orderentry.Foo)
		println(new customers.Foo)
		println(new cusomters.database.Foo)
	}

	//**************7.2****************//
	
	//importing one or more
	import java.io{File, IOException, FileNotFoundException}
	import java.io._

	package foo
	import java.io.File
	import java.io.PrintWriter

	class Foo{
		import javax.swing.JFrame //only visible in this class
	}

	class Bar{
		import scala.util.Random //only visible in this class
	}

	class Bar{
		def doBar = {
			import scala.util.Random
			println("")
		}
	}
	*/

	//**************7.3****************//

	//renaming members on import
	import java.util.{ArrayList => JavaList}

	val list = new JavaList[String]
	//if using alias, the old names won't work anymore
	import java.util.{Date => JDate, HashMap => JHashMap}

	//renaming all class that may be conlficting
	import java.util.{HashMap => JavaHashMap}
	import scala.collection.mutable.{Map => ScalaMutableMap}

	//renaming class memebers
	import System.out.{println => p}

	p("hello")
	


	//**************7.4****************//
	
	//hiding a class during the import process
	//hiding the Random class while import everything else
	import java.util.{Random => _, _}
	val al = new ArrayList
	//val r = new Random

	//this is what "hides" the class
	import java.util.{Random => _}
	//second _ means we import everything else
	import java.util._
	//wildcard must be last

	import java.util.{List => _, Map => _, Set => _, _}
	val all = new ArrayList
	

	//**************7.5****************//

	//using static imports
	import java.lang.Math._
	val sen = sin(0)
	val cose = cos(PI)

	import java.awt.Color._
	println(RED)

	val currentColor = BLUE

	
	/*
	//great for Java enums
	package com.alvinalexander.dates
	public enum Day{
		SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY
	}

	import com.alvinalexander.dates.Day._
	//somehwere after the import
	if (date == SUNDAY || date == SATURDAY) println("It's the weekend")
	*/

	//**************7.6****************//

	///using import statements anywhere
	//package foo
	/*
	import scala.util.Random
	class ImportTests{
		def printRandom = {
			val r = new Random
		}
	}
	*/
	//package foo
	/*
	class ImporTests{
		import scala.util.Random
		def printRandom = {
			val r = new Random
		}
	}*/
	/*
	def getRandomWaitTimeInMinutes: Int = {
		import com.alvinalexander.pandorasbox._
		val p = new Pandora
		p.release
	}
	
	def printRandom{
	  {
		import scala.util.Random
		val r1 = new Random
	  }
		val r2 = new Random
	}*/

	/*
	package orderentry{
		import foo._
	}

	package customers{
		import bar._

		package database{
			import baz._
		}
	}
	*/


}