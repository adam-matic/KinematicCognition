package com.example.kinematiccognition

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.SystemClock
import android.util.AttributeSet
import android.view.Choreographer
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.concurrent.schedule


class TrackingView (context: Context, attrs: AttributeSet): View(context, attrs) {

    // user trajectory
    var xs : MutableList<Float> = mutableListOf<Float>()
    var ys : MutableList<Float> = mutableListOf<Float>()
    var ts : MutableList<Long> = mutableListOf<Long>()

    // target trajectory
    var txs : MutableList<Float> = mutableListOf<Float>()
    var tys : MutableList<Float> = mutableListOf<Float>()
    var tts : MutableList<Long> = mutableListOf<Long>()

    var t0 : Long = 0

    val paint: Paint = Paint()
    var duration: Long = 0L

    lateinit var tr : TrajectoryFunction

    init {
        paint.isAntiAlias = true
        paint.color = Color.GREEN
        t0 = SystemClock.uptimeMillis()
    }

    override fun onDraw(canvas: Canvas) {
        val t = SystemClock.uptimeMillis() - t0
        val x = tr.xsf.value((t.toDouble() / 1000.0)).toFloat()
        val y = tr.ysf.value((t.toDouble() / 1000.0)).toFloat()
        tts.add(t); txs.add(x); tys.add(y)

        canvas.drawCircle(x, y, 50f, paint)
        this.postInvalidate()
    }

    override fun onTouchEvent(event: MotionEvent?) : Boolean {
        val historySize = event!!.historySize
        for (h in 0 until historySize) {
            val x = event!!.getHistoricalX(h)
            val y = event!!.getHistoricalY(h)
            val t = event!!.getHistoricalEventTime(h) - t0
            xs.add(x); ys.add(y); ts.add(t)
        }
        return true
    }

}