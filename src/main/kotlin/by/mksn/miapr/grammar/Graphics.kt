package by.mksn.miapr.grammar

import java.lang.Double.max
import java.lang.Double.min
import java.lang.Math.abs
import java.util.*

data class Point(var x: Double, var y: Double) {
    fun move(deltaX: Double, deltaY: Double) {
        x += deltaX
        y += deltaY
    }

    companion object {
        val NONE = Point(0.0, 0.0)
    }
}

data class Line(var start: Point, var end: Point) {
    fun move(deltaX: Double, deltaY: Double) {
        start.move(deltaX, deltaY)
        end.move(deltaX, deltaY)
    }

    fun resize(xScale: Double, yScale: Double, centralPoint: Point) {
        val xDelta = (end.x - start.x) * xScale
        val yDelta = (end.y - start.y) * yScale

        val xStartDelta = (start.x - centralPoint.x) * xScale
        val yStartDelta = (start.y - centralPoint.y) * yScale

        start = Point(centralPoint.y + xStartDelta, centralPoint.y + yStartDelta)
        end = Point(start.x + xDelta, start.y + yDelta)
    }
}

data class Element(val elementType: ElementType) {

    val lines: ArrayList<Line> = arrayListOf()
    var startPosition: Point = Point.NONE
        private set
    var endPosition: Point = Point.NONE
        private set
    fun length() = abs(endPosition.x - startPosition.x)
    fun height() = abs(endPosition.y - startPosition.y)

    constructor(elementType: ElementType, line: Line) : this(elementType) {
        with(line) {
            lines.add(this)
            startPosition = Point(min(start.x, end.x), max(start.y, end.y))
            endPosition = Point(max(start.x, end.x), min(start.y, end.y))
        }
    }

    constructor(elementType: ElementType, lines: List<Line>, startPosition: Point, endPosition: Point) :
            this(elementType) {
        this.lines.addAll(lines)
        this.startPosition = startPosition
        this.endPosition = endPosition
    }


    fun move(xDelta: Double, yDelta: Double) {
        startPosition.move(xDelta, yDelta)
        endPosition.move(xDelta, yDelta)
        for (line in lines) {
            line.move(xDelta, yDelta)
        }
    }

    fun resize(xScale: Double, yScale: Double) {
        val deltaX = (endPosition.x - startPosition.x) * xScale
        val deltaY = (endPosition.y - startPosition.y) * yScale

        endPosition = Point(startPosition.x + deltaX, startPosition.y + deltaY)
        for (line in lines) {
            line.resize(xScale, yScale, startPosition)
        }
    }

    infix fun isSameTypeWith(compared: ElementType) = elementType.isSame(compared)
}