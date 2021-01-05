package dotty.dokka.common

object Component:
  def apply(props: js.Object) =
    Component(props)

case class Component protected (
  props: js.Object = js.Object,
  prefProps: js.Object = js.Object,
  var state: js.Object = js.Object,
  render: Option[js.Function] = None
):
  def setState(nextState: js.Object, cb: jsFunction0[js.Object] = () => js.Object) =
    val newState = nextState match
      case _: js.Function =>
        nextState(state)
      case _ =>
        nextState

    cb()
    state = js.Object(state:_*, newState:_*)
    render.map(_.apply())
