package dotty.dokka.components

import dotty.dokka.common._

case class Input(props: js.Object) extends Component(props):
  val inputRef = findRef(".filterableInput")
  val onChangeFn = withEvent(inputRef, "input", onInputChange)
  def onInputChange(event: Event): Unit =
    props.onInputChange(event.currentTarget.value)
  def componentWillUnmount: Unit =
    Option(onChangeFn).map(_.apply())
