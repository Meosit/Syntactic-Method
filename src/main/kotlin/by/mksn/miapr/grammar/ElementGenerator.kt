package by.mksn.miapr.grammar

//terminals
private val HORIZONTAL_LINE = "horizontal"
private val VERTICAL_LINE = "vertical"
private val SLOPE_LINE = "slope"
private val BACK_SLOPE_LINE = "back_slope"

//non-terminals
private val UP_ANGLE = "up_angle"
private val DOWN_ANGLE = "down_angle"
private val DOWN_TRIANGLE = "down_triangle"
private val UP_TRIANGLE = "up_triangle"
private val EYES = "eyes"
private val EARS = "ears"
private val HEAD = "head"
private val FOX = "fox"

class ElementGenerator {

    private val dictionary: Map<String, ElementType>
    private val rules: Map<String, Rule>
    private val startElementType: ElementType

    init {
        dictionary = createDictionary()
        rules = createRules()
        startElementType = ElementType(FOX)
    }

    private fun createDictionary() = mapOf(
            HORIZONTAL_LINE to TerminalElementType(HORIZONTAL_LINE, Line(Point(.0, .0), Point(10.0, .0))),
            VERTICAL_LINE to TerminalElementType(VERTICAL_LINE, Line(Point(.0, .0), Point(.0, 10.0))),
            SLOPE_LINE to TerminalElementType(SLOPE_LINE, Line(Point(.0, .0), Point(10.0, 10.0))),
            BACK_SLOPE_LINE to TerminalElementType(BACK_SLOPE_LINE, Line(Point(10.0, .0), Point(.0, 10.0))),

            UP_ANGLE to ElementType(UP_ANGLE),
            DOWN_ANGLE to ElementType(DOWN_ANGLE),
            DOWN_TRIANGLE to ElementType(DOWN_TRIANGLE),
            UP_TRIANGLE to ElementType(UP_TRIANGLE),
            EYES to ElementType(EYES),
            EARS to ElementType(EARS),
            HEAD to ElementType(HEAD),
            FOX to ElementType(FOX)
    )


    private fun createRules() = mapOf(
            FOX to UpRule(dictionary[FOX]!!, dictionary[HEAD]!!, dictionary[UP_TRIANGLE]!!),
            EARS to LeftRule(dictionary[EARS]!!, dictionary[UP_ANGLE]!!, dictionary[UP_ANGLE]!!),
            UP_ANGLE to LeftRule(dictionary[UP_ANGLE]!!, dictionary[SLOPE_LINE]!!, dictionary[BACK_SLOPE_LINE]!!),
            HEAD to UpRule(dictionary[HEAD]!!, dictionary[EARS]!!, dictionary[DOWN_TRIANGLE]!!),
            DOWN_TRIANGLE to UpRule(dictionary[DOWN_TRIANGLE]!!, dictionary[HORIZONTAL_LINE]!!, dictionary[DOWN_ANGLE]!!),
            DOWN_ANGLE to LeftRule(dictionary[DOWN_ANGLE]!!, dictionary[BACK_SLOPE_LINE]!!, dictionary[SLOPE_LINE]!!),
            UP_TRIANGLE to UpRule(dictionary[UP_TRIANGLE]!!, dictionary[UP_ANGLE]!!, dictionary[HORIZONTAL_LINE]!!)
    )

    fun getTerminalElement(line: Line): Element {
        val elementName = getTerminalElementName(line)
        return Element(dictionary[elementName]!!, line)
    }

    private fun getTerminalElementName(line: Line): String {
        val deltaX = line.start.x - line.end.x
        val deltaY = line.start.y - line.end.y
        if (Math.abs(deltaY) < 1) {
            return HORIZONTAL_LINE
        }
        if (Math.abs(deltaX) < 1) {
            return VERTICAL_LINE
        }

        if (Math.abs(deltaX / deltaY) < 0.2) {
            return VERTICAL_LINE
        }

        if (Math.abs(deltaY / deltaX) < 0.2) {
            return HORIZONTAL_LINE
        }
        val highPoint = if (line.end.y > line.start.y) line.end else line.start
        val lowPoint = if (line.end.y < line.start.y) line.end else line.start
        if (highPoint.x < lowPoint.x) {
            return BACK_SLOPE_LINE
        }
        return SLOPE_LINE
    }

    private fun generateElement(elementType: ElementType = startElementType): Element {
        if (elementType.isTerminal()) {
            return (elementType as TerminalElementType).standardElement
        }

        val rule = rules[elementType.name]!!
        return rule.transformConnect(generateElement(rule.firstElementType), generateElement(rule.secondElementType))
    }

    fun imageIsCorrect(elements: List<Element>): Boolean {

        val correctLines = generateElement().lines
        val correctElements = correctLines.map { getTerminalElement(it) }

        if (correctElements.size != elements.size) {
            return false
        }

        for (i in correctElements.indices) {
            if (!(elements[i] isSameTypeWith correctElements[i].elementType)) {
                return false
            }
        }

        return true
    }

}