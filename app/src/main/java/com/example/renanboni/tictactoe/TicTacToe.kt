package com.example.renanboni.tictactoe

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.View

class TicTacToe: View {

    private val paint = Paint()
    private val textPaint = Paint()

    private val X_PARTITION_RATIO = 1 / 3f
    private val Y_PARTITION_RATIO = 1 / 3f

    private lateinit var squares: Array<Array<Rect>>
    private lateinit var squareData: Array<Array<String>>

    init {
        paint.color = ContextCompat.getColor(context, R.color.colorPrimary)
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = resources.displayMetrics.density * 5

        textPaint.color = paint.color
        textPaint.isAntiAlias = true
        textPaint.typeface = ResourcesCompat.getFont(context, R.font.nunito)
        textPaint.textSize = resources.displayMetrics.scaledDensity * 70
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawVerticalLines(canvas)
        drawHorizontalLines(canvas)
        initializeTicTacToeSquares()
        drawTextInsideRectangle(canvas, squares[0][0], "X")
    }

    private fun initializeTicTacToeSquares() {
        squares = Array(3) { Array(3) { Rect() } }
        squareData = Array(3) { Array(3) { "" } }

        val xUnit = (width * X_PARTITION_RATIO).toInt()
        val yUnit = (height * Y_PARTITION_RATIO).toInt()

        for (j in 0 until squares.size) {
            for (i in 0 until squares.size) {
                squares[i][j] = Rect(i * xUnit, j * yUnit, (i + 1) * xUnit, (j + 1) * yUnit)
            }
        }
    }

    private fun drawVerticalLines(canvas: Canvas) {
        canvas.drawLine(width * X_PARTITION_RATIO, 0f, width * X_PARTITION_RATIO, height.toFloat(), paint)
        canvas.drawLine(width * (2 * X_PARTITION_RATIO), 0f, width * (2 * X_PARTITION_RATIO), height.toFloat(), paint)
    }

    private fun drawHorizontalLines(canvas: Canvas) {
        canvas.drawLine(0f, height * Y_PARTITION_RATIO, width.toFloat(), height * Y_PARTITION_RATIO, paint)
        canvas.drawLine(0f, height * (2 * Y_PARTITION_RATIO), width.toFloat(), height * (2 * Y_PARTITION_RATIO), paint)
    }

    private fun drawTextInsideRectangle(canvas: Canvas, rect: Rect, str: String) {
        val xOffset = textPaint.measureText(str) * 0.5f
        val yOffset = textPaint.fontMetrics.ascent * -0.4f

        val x = rect.exactCenterX() - xOffset
        val y = rect.exactCenterY() + yOffset

        canvas.drawText(str, x, y, textPaint)
    }
}