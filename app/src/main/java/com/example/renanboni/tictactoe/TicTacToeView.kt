package com.example.renanboni.tictactoe

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class TicTacToeView: View, ValueAnimator.AnimatorUpdateListener {

    companion object {
        const val X_PARTITION_RATIO = 1 / 3f
        const val Y_PARTITION_RATIO = 1 / 3f
    }

    private val moveX = "X"
    private val moveY = "O"

    private val path = Path()

    private val paint = Paint()
    private val textPaint = Paint()
    private val highLightPaint = Paint()

    private lateinit var squares: Array<Array<Rect>>
    private lateinit var squareData: Array<Array<String>>

    private var touching: Boolean = false
    private var rectIndex = Pair(0, 0)

    private var winCoordinates: Array<Int> = Array(4) { -1 }
    private var shouldAnimate = false

    var squarePressListener: SquarePressedListener? = null

    init {
        paint.color = ContextCompat.getColor(context, R.color.colorPrimary)
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = resources.displayMetrics.density * 5

        textPaint.color = paint.color
        textPaint.isAntiAlias = true
        textPaint.typeface = ResourcesCompat.getFont(context, R.font.nunito)
        textPaint.textSize = resources.displayMetrics.scaledDensity * 70

        highLightPaint.color = ContextCompat.getColor(context, R.color.highlight_color)
        highLightPaint.style = Paint.Style.FILL
        highLightPaint.isAntiAlias = true
    }

    constructor(ctx: Context) : super(ctx)
    constructor(ctx: Context, attrs: AttributeSet) : super(ctx, attrs)

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawVerticalLines(canvas)
        drawHorizontalLines(canvas)
        drawSquaresState(canvas)

        if(shouldAnimate) {
            canvas.drawPath(path, paint)
        }

        if(touching) {
            drawHighLightRectangle(canvas)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val size = Math.min(measuredHeight, measuredWidth)
        setMeasuredDimension(size, size)
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

    fun getRectIndexesFor(x: Float, y: Float): Pair<Int, Int> {
        squares.forEachIndexed { i, rects ->
            for((j, rect) in rects.withIndex()) {
                if(rect.contains(x.toInt(), y.toInt())) {
                    return Pair(i, j)
                }
            }
        }
        return Pair(-1, -1)
    }

    private fun drawHighLightRectangle(canvas: Canvas) {
        canvas.drawRect(squares[rectIndex.first][rectIndex.second], highLightPaint)
    }

    private fun drawSquaresState(canvas: Canvas) {
        for ((i, textArray) in squareData.withIndex()) {
            for ((j, text) in textArray.withIndex()) {
                if (text.isNotEmpty()) {
                    drawTextInsideRectangle(canvas, squares[i][j], text)
                }
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        initializeTicTacToeSquares()
    }

    fun drawXAtPosition(x: Int, y: Int) {
        squareData[x][y] = moveX
        invalidate(squares[x][y])
    }

    fun drawOAtPosition(x: Int, y: Int) {
        squareData[x][y] = moveY
        invalidate(squares[x][y])
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        val x = event.x
        val y = event.y

        when(event.action) {
            MotionEvent.ACTION_DOWN -> {
                rectIndex = getRectIndexesFor(x, y)
                touching = true
                invalidate(squares[rectIndex.first][rectIndex.second])
            }
            MotionEvent.ACTION_UP -> {
                touching = false
                invalidate(squares[rectIndex.first][rectIndex.second])

                val (finalX1, finalY1) = getRectIndexesFor(x, y)

                if((finalX1 == rectIndex.first) && (finalY1 == rectIndex.second)) {
                    squarePressListener?.onSquarePressed(rectIndex.first, rectIndex.second)
                }
            }
        }

        return true
    }

    fun animateWin(x1: Int, y1: Int, x3: Int, y3: Int) { // will be called from activity or fragment
        winCoordinates = arrayOf(x1, y1, x3, y3) // first and last coordinate of winning line

        if (winCoordinates[0] < 0) {
            return
        }

        val centerX = squares[winCoordinates[0]][winCoordinates[1]].exactCenterX()
        val centerY = squares[winCoordinates[0]][winCoordinates[1]].exactCenterY()
        val centerX2 = squares[winCoordinates[2]][winCoordinates[3]].exactCenterX()
        val centerY2 = squares[winCoordinates[2]][winCoordinates[3]].exactCenterY()

        path.reset()
        path.moveTo(centerX, centerY) // moving to centre of first square
        path.lineTo(centerX2, centerY2) // creating a line till centre of last square
        shouldAnimate = true
        invalidate()
    }

    fun reset() {
        squareData = Array(3, { Array(3, { "" }) })
        winCoordinates = Array(4, { -1 })
        path.reset()
        shouldAnimate = false
        invalidate()
    }

    private fun animateWin() {
        val valueAnimator = ValueAnimator.ofFloat(1f, 0f)
        valueAnimator.duration = 600
        valueAnimator.addUpdateListener(this)
        valueAnimator.start()
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
        val measure = PathMeasure(path, false)
        val phase = (measure.length * (animation.animatedValue as Float))
        paint.pathEffect = createPathEffect(measure.length, phase)
        invalidate()
    }

    private fun createPathEffect(pathLength: Float, phase: Float): PathEffect = DashPathEffect(floatArrayOf(pathLength, pathLength), phase)

    interface SquarePressedListener {
        fun onSquarePressed(i: Int, j: Int)
    }
}