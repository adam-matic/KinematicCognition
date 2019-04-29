package com.example.kinematiccognition

import android.content.Context
import android.graphics.*
import android.os.SystemClock
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View


class TraceView(context: Context, attrs: AttributeSet): View(context, attrs) {
    var xs : MutableList<Float> = mutableListOf<Float>()
    var ys : MutableList<Float> = mutableListOf<Float>()
    var ts : MutableList<Long> = mutableListOf<Long>()

    val paint = Paint()

    var value: String = ""
    var t0 : Long

    init {
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 25f
        t0 = SystemClock.uptimeMillis()
    }

    override fun draw(canvas: Canvas?) {
        super.draw(canvas)
        canvas?.drawPath(Experiment.paths[value], paint)
    }

    override fun onTouchEvent(event: MotionEvent?) : Boolean {
        val historySize = event!!.historySize
        for (h in 0 until historySize) {
            xs.add(event!!.getHistoricalX(h))
            ys.add(event!!.getHistoricalY(h))
            ts.add(event!!.getHistoricalEventTime(h) - t0)
        }
        return true
    }

}

