package com.example.kinematiccognition

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.Window
import android.view.WindowManager
import com.example.brainawarenesweekexperiment.R
import kotlinx.android.synthetic.main.activity_target_tracking.*
import java.io.File
import java.io.FileNotFoundException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.concurrent.schedule

class TrackingActivity : AppCompatActivity() {
    private val timer = Timer("schedule", true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        supportActionBar?.hide()

        val trajectory = (this.intent.getStringExtra("value"))
        val duration = this.intent.getLongExtra("duration", 5L)

        setContentView(R.layout.activity_target_tracking)
        fullscreen_content.duration = duration
        fullscreen_content.tr = Experiment.trajectories[trajectory]!!

        timer.schedule(duration * 1000L) { finish() }
    }

    override fun finish() {
        // add saving of target trajectory

        val save = this.intent.getBooleanExtra("saveData", false)
        if (save) {
            val user = this.intent.getStringExtra("user")
            val trajectory = this.intent.getStringExtra("value")
            val sdf = SimpleDateFormat("dd.M.yyyy. HH.mm.ss")
            val timestamp = sdf.format(Date())
            val filename_user = "user $user tracking $trajectory $timestamp .txt"
            val filename_target = "target $user tracking $trajectory $timestamp .txt"

            val path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            path.mkdir()
            val outfile_user = File(path, filename_user)
            val outfile_target = File(path, filename_target)

            val ts = this.fullscreen_content.ts
            val xs = this.fullscreen_content.xs
            val ys = this.fullscreen_content.ys

            if (ts.size > 0) {
                val writer = outfile_user.writer()
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

            val tts = this.fullscreen_content.tts
            val txs = this.fullscreen_content.txs
            val tys = this.fullscreen_content.tys

            if (tts.size > 0) {
                val writer = outfile_target.writer()
                try {
                    writer.use {
                        for (i in 0 until tts.size) {
                            writer.write("${tts[i]} ${txs[i]} ${tys[i]}\n")
                        }
                    }
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                }
            }

        }


        super.finish()
        setResult(Activity.RESULT_OK, Intent())

    }

}
