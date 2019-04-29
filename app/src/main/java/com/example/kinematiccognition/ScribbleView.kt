package com.example.kinematiccognition

import android.content.Context
import android.graphics.*
import android.os.Build.VERSION_CODES.N
import android.os.SystemClock
import android.support.v4.graphics.PathSegment
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class ScribbleView(context: Context, attrs: AttributeSet):
        View(context, attrs) {

    var mPaint = Paint()
    var mPath = Path()

    var drawLastSeconds = 1f

    var xs : MutableList<Float> = mutableListOf<Float>()
    var ys : MutableList<Float> = mutableListOf<Float>()
    var ts : MutableList<Long> = mutableListOf<Long>()

    init {
        mPaint.apply  {
            color = Color.BLACK
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 8f
            isAntiAlias = true
            pathEffect = CornerPathEffect(10.0f)
        }
    }

    fun createPath() {
        mPath.reset()
        var N = ts.size - 1
        if (N < 0) return

        val lt = SystemClock.uptimeMillis()
        var tt = lt - ts[N]
        mPath.moveTo(xs[N], ys[N])

        while (tt < drawLastSeconds * 1000L && N > 0) {
            N -= 1
            tt = lt - ts[N]
            mPath.lineTo(xs[N], ys[N])
        }
    }

    override fun onDraw(canvas: Canvas) {
        createPath()
        canvas.drawPath(mPath, mPaint)
        this.postInvalidate()
    }


    override fun onTouchEvent(event: MotionEvent?) : Boolean {
        val historySize = event!!.historySize
        for (h in 0 until historySize) {
            val x = event!!.getHistoricalX(h)
            val y = event!!.getHistoricalY(h)
            val t = event!!.getHistoricalEventTime(h)
            xs.add(x); ys.add(y);  ts.add(t)
        }
        return true
    }

}
