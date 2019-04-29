package com.example.kinematiccognition

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import kotlinx.android.synthetic.main.activity_tracing_ellipse.*
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule
import android.app.Activity
import android.content.Intent
import android.view.MotionEvent
import android.view.Window
import android.view.WindowManager
import com.example.brainawarenesweekexperiment.R


class TraceActivity : AppCompatActivity() {
    private val timer = Timer("schedule", true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_tracing_ellipse)
        supportActionBar?.hide()
        fullscreen_content.value = intent.getStringExtra("value")

        val duration = intent.getLongExtra("duration", 5L)

        timer.schedule(duration * 1000L) {
            finish()
        }


    }


    override fun finish() {

        val save = this.intent.getBooleanExtra("saveData", false)
        if (save) {
            val user = this.intent.getStringExtra("user")
            val shape = this.intent.getStringExtra("value")
            val sdf = SimpleDateFormat("dd.M.yyyy. HH.mm.ss")
            val timestamp = sdf.format(Date())
            val filename = "$user tracing $shape $timestamp.txt"
            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            path.mkdir()
            val outfile = File(path, filename)
            println("**********************************************************************************************")
            println("saved as $path $filename")
            val ts = fullscreen_content.ts
            val xs = fullscreen_content.xs
            val ys = fullscreen_content.ys

            if (ts.size > 0) {
                val writer = outfile.writer()
                try {
                    writer.use {
                        for (i in 0 until ts.size) {
                            writer.write("${ts[i]} ${xs[i]} ${ys[i]}\n")
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
