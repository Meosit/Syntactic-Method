package by.mksn.miapr.grammar

interface Type {
    fun isTerminal(): Boolean
}

open class ElementType(val name: String) : Type {

    override fun isTerminal() = false

    fun isSame(compared: ElementType) =
            this.name == compared.name
}

class TerminalElementType(name: String, private val standardLine: Line) : ElementType(name) {

    override fun isTerminal() = true

    fun getStandardElement(): Element {
        val lineCopy = Line(Point(standardLine.start.x, standardLine.start.y),
                Point(standardLine.end.x, standardLine.end.y))
        return Element(this, lineCopy)
    }
}