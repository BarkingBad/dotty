package dotty.dokka.components

import dotty.dokka.common._

import org.scalajs.dom._

/**
 * @typedef { import("./Filter").Filter } Filter
 * @typedef { { ref: Element; name: string; description: string } } ListElement
 *  @typedef { [key: string, value: string][] } Dataset
 */
class DocumentableList(props: js.Object) extends Component(props):

  case class Refs(tabs: NodeList, sections: NodeList)
  val refs = Refs(
    findRefs(".names .tab[data-togglable]", findRef(".membersList")).concat(
      findRefs(".contents h2[data-togglable]", findRef(".membersList"))
    ),
    findRefs(".contents .tab[data-togglable]", findRef(".membersList"))
  )
  state = js.Dynamic.literal(list = List(refs.tabs, refs.sections))
  render(props);

  def toggleElementDatasetVisibility(isVisible: Boolean, ref: html.HTMLElement): Unit =
    ref.dataset.visibility = isVisible

  def toggleDisplayStyles(condition: Boolean, ref: html.HTMLElement): Unit =
    ref.style.display = if condition then null else "none"

  def render =
    state.list.sectionsRefs.map(sectionRef => {
      
    })

  render({ filter }) {
    this.state.list.sectionsRefs.map(sectionRef => {
      const isTabVisible = this.state.list
        .getSectionListRefs(sectionRef)
        .filter((listRef) => {
          const isListVisible = this.state.list
            .getSectionListElementsRefs(listRef)
            .map(elementRef => this.state.list.toListElement(elementRef))
            .filter(elementData => {
              const isElementVisible = this.state.list.isElementVisible(elementData, filter);

              this.toggleDisplayStyles(isElementVisible, elementData.ref);
              this.toggleElementDatasetVisibility(isElementVisible, elementData.ref);

              return isElementVisible;
            }).length;

          this.toggleDisplayStyles(isListVisible, listRef);

          return isListVisible;
        }).length;

        const outerThis = this
        this.state.list.getTabRefFromSectionRef(sectionRef).forEach(function(tabRef){
          outerThis.toggleDisplayStyles(isTabVisible, tabRef);
        })
    });
  }
}

class List {
  /**
   * @param tabsRef { Element[] }
   * @param sectionRefs { Element[] }
   */
  constructor(tabsRef, sectionRefs) {
    this._tabsRef = tabsRef;
    this._sectionRefs = sectionRefs;
  }

  get tabsRefs() {
    return this._tabsRef.filter(tabRef => this.filterTab(this._getTogglable(tabRef)));
  }

  get sectionsRefs() {
    return this._sectionRefs.filter(sectionRef => this.filterTab(this._getTogglable(sectionRef)));
  }

  /**
  * @param name { string }
  */
  filterTab(name) {
    return name !== "Linear supertypes" && name !== "Known subtypes" && name !== "Type hierarchy"
  }

  /**
   * @param sectionRef { Element }
   */
  getTabRefFromSectionRef(sectionRef) {
    return this.tabsRefs.filter(
      (tabRef) => this._getTogglable(tabRef) === this._getTogglable(sectionRef)
    );
  }

  /**
   * @param sectionRef { Element }
   * @returns { Element[] }
   */
  getSectionListRefs(sectionRef) {
    return findRefs(".documentableList", sectionRef);
  }

  /**
   * @param listRef { Element }
   * @returns { Element[] }
   */
  getSectionListElementsRefs(listRef) {
    return findRefs(".documentableElement", listRef);
  }

  /**
   * @param elementRef { Element }
   * @returns { ListElement }
   */
  toListElement(elementRef) {
    return {
      ref: elementRef,
      name: getElementTextContent(getElementNameRef(elementRef)),
      description: getElementTextContent(getElementDescription(elementRef)),
    };
  }

  /**
  * @param elementData { ListElement }
  * @param filter { Filter }
  */
  isElementVisible(elementData, filter) {
    return !areFiltersFromElementSelected()
      ? false
      : includesInputValue()

    function includesInputValue() {
      return elementData.name.includes(filter.value) || elementData.description.includes(filter.value);
    }

    function areFiltersFromElementSelected() {
      /** @type { Dataset } */
      const dataset = Object.entries(elementData.ref.dataset)

      /** @type { Dataset } */
      const defaultFilters = Object.entries(Filter.defaultFilters)
        .filter(([key]) => !!filter.filters[getFilterKey(key)])

       /** @type { Dataset } */
      const defaultFiltersForMembersWithoutDataAttribute =
        defaultFilters.reduce((acc, [key, value]) => {
          const filterKey = getFilterKey(key)
          const shouldAddDefaultFilter = !dataset.some(([k]) => k === filterKey)
          return shouldAddDefaultFilter ? [...acc, [filterKey, value]] : acc
        }, [])

      /** @type { Dataset } */
      const datasetWithAppendedDefaultFilters = dataset
        .filter(([k]) => isFilterData(k))
        .map(([k, v]) => {
          const defaultFilter = defaultFilters.find(([defaultKey]) => defaultKey === k)
          return defaultFilter ? [k, `${v},${defaultFilter[1]}`] : [k, v]
        })

      const datasetWithDefaultFilters = [
        ...defaultFiltersForMembersWithoutDataAttribute,
        ...datasetWithAppendedDefaultFilters
      ]

      const isVisible = datasetWithDefaultFilters
        .every(([filterKey, value]) => {
          const filterGroup = filter.filters[filterKey]

          return value.split(",").some(v => filterGroup && filterGroup[v].selected)
        })

      return isVisible
    }
  }

  /**
  * @private
  * @param elementData { ListElement }
  */
  _getTogglable = elementData => elementData.dataset.togglable;
}

