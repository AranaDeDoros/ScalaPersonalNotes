package scalafirst

	//GUI Programming
	//**************34.1****************//
	//A first Swing application
	/*import scala.swing._
	object FirstSwingApp extends SimpleSwingApplication {
			def top = new MainFrame {
				title = "First Swing App"
				contents = new Button {
				text = "Click me"
			}
		}
	}*/

	//The SimpleSwingApplication class already defines a main method
	//that contains some setup code for Java's Swing framework.
	/*the top method. This method contains the code that defines your top-level GUI component.
	This is usually some kind of Frame— i.e. , a window that can contain arbitrary data.
	A MainFrame is like a normal Swing Frame except that closing it will also close the whole GUI application
	frames have a number of attributes. Two of the most important are the frame's title,
	which will be written in the title bar, and its contents, which will be displayed in the window itself.
	In Scala's Swing API, such attributes are modeled as properties properties are encoded
	in Scala as pairs of getter and setter methods. The top frame is the root component of the Swing application.
	It is a Container, which means that further components can be defined in it.
	Every Swing container has a contents property, which allowsyou to get and set the components it contains.
	The getter contents of this property has typeSeq[Component],indicating that a component can in general
	have several objects as its contents. Frames, however, always have just a single component as theircontents.
	This component is set and potentially changed using the setter contents_= */


	//**************34.2****************//
	//PANELS AND LAYOUTS
	/*import scala.swing._
	object SecondSwingApp extends SimpleSwingApplication {
		def top = new MainFrame {
			title = "Second Swing App"
			val button = new Button {
				text = "Click me"
			}
			val label = new Label {
				text = "No button clicks registered"
			}
			contents = new BoxPanel ( Orientation.Vertical ) {
				contents += button
				contents += label
				border = Swing.EmptyBorder ( 30 , 30 , 10 , 30 )
			}
		}
	}*/

	/*The contents property of the BoxPanel is an (initially empty) buffer, to which the button
	and labelelements are added with the+= operator EmptyBorder is a factory method in object Swing
	that takes four parameters indicating the width of the borders on the top, right,bottom,
	and left sides of the objects to be drawn.
	 */


	//**************34.3****************//
	//HANDLING EVENTS
	/*In Scala, subscribing to an event source source is done by the call listenTo(source).
	There's also a way to unsubscribe from an event source using deafTo(source).
	In Scala, an event is a real object that gets sent to subscribing components
	much like messages are sent to actors. For instance, pressing a button will create
	an event which is an instance of the following case class:
	case class ButtonClicked(source:Button)
	To have your component react to incoming events you need to add a handler to a property
	called reactions. Handlers are functions defined by pattern matching on events,
	much like Akka actor receive methods are defined by pattern matching on messages.
	Generally, a handler is a PartialFunction that matches on events and performs some actions*/
	/*import scala.swing._
	import scala.swing.event._
	object ReactiveSwingApp extends SimpleSwingApplication {
		def top = new MainFrame {
			title = "Reactive Swing App"
			val button = new Button {
				text = "Click me"
			}
			val label = new Label {
				text = "No button clicks registered"
			}
			contents = new BoxPanel ( Orientation.Vertical ) {
			contents += button
			contents += label
			border = Swing.EmptyBorder ( 30 , 30 , 10 , 30 )
			}
			listenTo(button)
			var nClicks = 0
			reactions += {
				case ButtonClicked (b) => {
					nClicks += 1
					label.text = "Number of button clicks: " + nClicks
				}
			}
		}
	}*/

	/*Some components come with predefined reactions.
	For instance, a Frame has a predefined reaction that it will close
	if the 	user presses the close button on the upper right.
	Install your own reactions by adding them with += to the reactions property,
	the reactions you define will be considered in addition to the standard	ones
	Conceptually, the handlers installed in	reactionsform a stack.
	if	the top	frame receives an event, the first handler tried will be the one
	that matches on ButtonClicked, because it was the last handler installed for the frame
	*/


	//**************34.4****************//
	//EXAMPLE: CELSIUS/FAHRENHEIT CONVERTER
	import scala.swing._
	import scala.swing.event._
	object TempConverter extends SimpleSwingApplication {
		def top = new MainFrame {
			title = "Celsius/Fahrenheit Converter"
			object celsius extends TextField { columns = 5 }
			object fahrenheit extends TextField { columns = 5 }
			contents = new FlowPanel {
				contents += celsius
				contents += new Label ( " Celsius = " )
				contents += fahrenheit
				contents += new Label ( " Fahrenheit" )
				border = Swing.EmptyBorder ( 15 , 10 , 10 , 10 )
			}
				listenTo(celsius, fahrenheit)
				reactions += {
					case EditDone (`fahrenheit`) => {
						val f = fahrenheit.text.toInt
						val c = (f - 32 ) * 5 / 9
						celsius.text = c.toString
					}
					case EditDone (`celsius`) => {
						val c = celsius.text.toInt
						val f = c * 9 / 5 + 32
						fahrenheit.text = f.toString
									}
				}
			}
	}

	/*The reactionsofTempConverterare defined by a handler that contains two cases.
	Each case matches an EditDone event for one of the two text fields.
	Such an event gets issued when a text field has been edited by the user
	the back ticks around celsius ensure that the pattern matches only
	if the source of the event was the celsius object.
	If you had omitted the back ticks and just written case EditDone(celsius),
	the pattern would have matched every event of class EditDone.
	The changed field would then be stored in the pattern variable celsius.*/


