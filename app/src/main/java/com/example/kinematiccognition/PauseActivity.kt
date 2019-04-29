package com.example.kinematiccognition

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import com.example.brainawarenesweekexperiment.R
import kotlinx.android.synthetic.main.activity_pause.*
import java.util.*
import kotlin.concurrent.schedule
import kotlin.concurrent.scheduleAtFixedRate

class PauseActivity : AppCompatActivity() {
    private val timer = Timer("schedule", true)
    private val timer2 = Timer("schedule", true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_pause)
        supportActionBar?.hide()

        val duration = intent.getLongExtra("duration", 5L)
        var d = duration.toInt()
        val pause_text = getString(R.string.pause_text)

        if (this.intent.getStringExtra("value") == "end") {
            fullscreen_content.text = getString (R.string.thank_you_message)
        }
        else {
            timer2.scheduleAtFixedRate(0L,1000L) {
                if (d == 0) {
                    timer2.cancel()
                    timer2.purge()
                } else {
                    runOnUiThread {fullscreen_content.text = pause_text + "\n" +  "*".repeat(d) }
                    d -= 1
                }
            }
        }

        timer.schedule(duration * 1000L) { finish() }

    }

    override fun finish() {
        timer2.cancel()

        super.finish()
        setResult(Activity.RESULT_OK, Intent())
    }

}
