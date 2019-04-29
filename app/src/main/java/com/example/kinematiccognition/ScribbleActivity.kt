package com.example.kinematiccognition

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.SystemClock
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.brainawarenesweekexperiment.R
import kotlinx.android.synthetic.main.activity_scribble.*
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule

class ScribbleActivity : AppCompatActivity() {
    private val timer = Timer("schedule", true)
    val t0: Long= SystemClock.uptimeMillis()

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scribble)
        supportActionBar?.hide()
        val duration = this.intent.getLongExtra("duration", 5L)

        timer.schedule(duration * 1000L) {
            finish()
        }
    }


    override fun finish() {

        val ts = fullscreen_content.ts
        val xs = fullscreen_content.xs
        val ys = fullscreen_content.ys

        val save = this.intent.getBooleanExtra("saveData", false)
        if (save) {
            val sdf = SimpleDateFormat("dd.M.yyyy. HH.mm.ss")
            val timestamp = sdf.format(Date())
            val user = this.intent.getStringExtra("user")
            val filename = "$user scribble $timestamp.txt"
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            path.mkdir()
            val outfile = File(path, filename)

            if (ts.size > 0) {
                val writer = outfile.writer()
                try {
                    writer.use {
                        for (i in 0 until ts.size) {
                            writer.write("${ts[i] - t0} ${xs[i]} ${ys[i]}\n")
                        }
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }
        }

        setResult(Activity.RESULT_OK, Intent())
        super.finish()

    }

}
