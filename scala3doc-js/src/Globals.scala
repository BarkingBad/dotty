package dotty.dokka

import scala.scalajs.js
import scala.scalajs.js.annotation.JSGlobalScope
import scala.scalajs.js.annotation.JSGlobal
import org.scalajs.dom.Location

@js.native
@JSGlobal
class HLJS extends js.Object:
  def registerLanguage(name: String, fun: js.Function0[HLJS]): Unit = js.native
  def registerAliases(aliases: js.Array[String], name: String): Unit = js.native
  def initHighlighting(): Unit = js.native

@js.native
@JSGlobalScope
object Globals extends js.Object {
  val pathToRoot: Location = js.native
  val hljs: HLJS = js.native
  val highlightDotty: js.Function0[HLJS] = js.native
  def showGraph(): Unit = js.native
}
