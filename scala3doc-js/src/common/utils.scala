package dotty.dokka.common

import org.scalajs.dom._

def findRef(searchBy: String, element: html.Document = document): Element =
  element.querySelector(searchBy)

def findRefs(searchBy: String, element: html.Document = document): NodeList =
  element match
    case null => NodeList
    case _ => element.querySelector(searchBy)

def withEvent(element: html.HTMLElement, listener: String, callback: js.Function1[Event, ...]): js.Function0[Unit] =
  Option(element).map(_.addEventListener(listener, callback))
  () => Option(element).map(_.removeEventListener(listener, callback))

def init(cb: js.Function) = window.addEventListner("DOMContentLoaded", cb)

def attachDOM(element: html.HTMLElement, html: js.Object): Unit =
  Option(element).map(_.innerHTML = htmlToString(html))

def htmlToString(html: js.Object): String =
  html match
    case js.Array[String] => html.join("")
    case _ => html.toString

def isFilterData(key: ...) = key.startsWith("f")

def getFilterKey(key: ...) = s"f${key(0).toUpperCase}${key.slice(1)}"

def attachListeners(elementsRefs: ..., `type`: ..., callback: ...): ... =
  elementsRefs.map((elRef) => withEvent(elRef, `type`, callback))

def getElementTextContent(element: ...) = Option(element).fold("")(element.textContent)

def getElementDescription(elementRef: ...) = findRef(".documentableBrief", elementRef)

def getElementNameRef(elementRef: ...) =
  findRef(".documentableName", elementRef)
