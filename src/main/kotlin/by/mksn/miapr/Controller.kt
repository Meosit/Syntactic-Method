package by.mksn.miapr

import by.mksn.miapr.grammar.Element
import by.mksn.miapr.grammar.ElementGenerator
import by.mksn.miapr.grammar.Line
import by.mksn.miapr.grammar.Point
import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.canvas.Canvas
import javafx.scene.control.Label
import javafx.scene.input.MouseEvent
import java.util.ArrayList

class Controller {
    @FXML
    private lateinit var canvas: Canvas
    @FXML
    private lateinit var resultLabel: Label

    private lateinit var drawer: Drawer
    private lateinit var generator: ElementGenerator

    private var from: Point? = null
    private var drawedElements: MutableList<Element> = ArrayList()
    private var drawedLines: MutableList<Line> = ArrayList()

    fun initialize() {
        drawer = Drawer(canvas)
        drawer.cleanCanvas()

        generator = ElementGenerator()

        resultLabel.text = ""
        resultLabel.isVisible = true
    }

    fun generate(actionEvent: ActionEvent) {
        clean(null)
        val element = generator.generateElement()
        drawer.draw(element)

        for (line in element.lines) {
            val terminal = generator.getTerminalElement(line)
            drawedElements.add(terminal)
        }
    }

    fun validate(actionEvent: ActionEvent) {
        if (generator.isImageCorrect(drawedElements)) {
            resultLabel.text = "Image is correct."
        } else {
            resultLabel.text = "Image is not correct."
        }
    }

    fun clean(actionEvent: ActionEvent?) {
        drawer.cleanCanvas()
        drawedElements.clear()
        drawedLines.clear()
        resultLabel.text = ""
    }

    fun onCanvasMouseClicked(mouseEvent: MouseEvent) {
        if (from == null) {
            from = Point(mouseEvent.x, mouseEvent.y)
        } else {
            val to = Point(mouseEvent.x, mouseEvent.y)
            drawer.drawLine(from!!, to)
            drawedLines.add(Line(from!!, to))

            val factFrom = drawer.getFactPoint(from!!)
            val factTo = drawer.getFactPoint(to)
            val line = Line(factFrom, factTo)
            val drewElement = generator.getTerminalElement(line)
            drawedElements.add(drewElement)

            from = null
        }
    }

    fun onCanvasMouseMove(mouseEvent: MouseEvent) {
        if (from != null) {
            drawer.cleanCanvas()
            for (line in drawedLines) {
                drawer.drawLine(line)
            }
            drawer.drawLine(from!!, Point(mouseEvent.x, mouseEvent.y))
        }
    }
}

