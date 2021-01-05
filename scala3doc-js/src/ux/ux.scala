package dotty.dokka.ux

import org.scalajs.dom._
import org.querki.jquery._

def ux =
  window.addEventListener("DOMContentLoaded", () => {
    Option(document.getElementById("leftToggler")).map {
      _.onclick = _ => document.getElementById("leftColumn").classList.toggle("open")
    }
    document.getElementsByClassName("documentableElement").each({ (ths: dom.HTMLElement, e) =>
      e.onclick = _ => ths.classList.toggle("expand")
    }: js.ThisFunction1)

    $("#sideMenu2 span").on("click", (el: html.HTMLElement) => {
      $(el).parent.toggleClass("expanded")
    }: js.ThisFunction);

    $(".names .tab").on("click", (el: html.HTMLElement) => {
      val parent = $(el).parents(".tabs").first()
      val shown = $(el).hasClass("selected")
      val single = parent.hasClass("single")

      if (single) parent.find(".tab.selected").removeClass("selected")

      val id = $(el).attr("data-togglable")
      val myTab = parent.find("[data-togglable='" + id + "'].tab")

      if (!shown) { myTab.addClass("selected") }
      if (shown && !single) myTab.removeClass("selected")

      if (!shown && $(el).find(".showGraph")) {
        showGraph()
        $(el).find(".showGraph").removeClass("showGraph")
      }
    }: js.ThisFunction)

    Option(
      Option(location.hash).map(
        document.getElementById(_.substring(1))
      )
    ).map(_.classList.toggle("expand"))

    Option(document.getElementById("logo")).map(
      _.onclick = () => window.location = pathToRoot // global variable pathToRoot is created by the html renderer
    )
    hljs.registerLanguage("scala", highlightDotty);
    hljs.registerAliases(["dotty", "scala3"], "scala");
    hljs.initHighlighting();
  })

  def showGraph =
    if ($("svg#graph").children().length == 0) {
      var dotNode = document.querySelector("#dot")
      if (dotNode){
        var svg = d3.select("#graph");
        var inner = svg.append("g");

        // Set up zoom support
        var zoom = d3.zoom()
          .on("zoom", function({transform}) {
            inner.attr("transform", transform);
          });
        svg.call(zoom);

        var render = new dagreD3.render();
        var g = graphlibDot.read(dotNode.text);
        g.graph().rankDir = 'BT';
        g.nodes().forEach(function (v) {
          g.setNode(v, {
            labelType: "html",
            label: g.node(v).label,
            style: g.node(v).style
          });
        });
        g.edges().forEach(function(v) {
          g.setEdge(v, {
            arrowhead: "vee"
          });
        });
        render(inner, g);
      }
    }

