package com.example.kinematiccognition

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.brainawarenesweekexperiment.R
import kotlinx.android.synthetic.main.activity_init.*


class InitAcitvity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init)

        btnStartPractice.setOnClickListener {
            val t = Experiment.practiceTasks.ls[0]
            val i = Intent(this, t.ActivityType)
            i.putExtra("value", t.value)
            i.putExtra("duration", t.duration)
            i.putExtra("saveData", t.saveData)
            startActivityForResult(i, t.returnCode)
        }

        btnStartExperiment.setOnClickListener {
            val t = Experiment.experimentTasks.ls[0]
            val i = Intent(this, t.ActivityType)
            i.putExtra("value", t.value)
            i.putExtra("duration", t.duration)
            i.putExtra("saveData", t.saveData)
            i.putExtra("user", this.intent.getStringExtra("user"))
            startActivityForResult(i, t.returnCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        if (requestCode == 10000) {
            finish()
            super.finish()
            finishAffinity()
        } else {

            val ttl = listOf(Experiment.practiceTasks.ls, Experiment.experimentTasks.ls)
            for (tt in ttl) {
                for (t in tt) {
                    //println("t ${t.value} ${t.type}")
                    //println("codes: $requestCode ${t.startCode} ${t.returnCode}")
                    if (t.startCode == requestCode) {
                        if (t.value == "end") {
                            val i = Intent(this, t.ActivityType)
                            i.putExtra("duration", t.duration)
                            i.putExtra("value", "end")
                            startActivityForResult(i, 10000)
                        } else {

                            val i = Intent(this, t.ActivityType)
                            i.putExtra("value", t.value)
                            i.putExtra("duration", t.duration)
                            i.putExtra("saveData", t.saveData)
                            i.putExtra("user", this.intent.getStringExtra("user"))
                            if (t.returnCode == -1) {
                                startActivity(i)
                            } else {
                                startActivityForResult(i, t.returnCode)
                            }
                            break
                        }
                    }
                }

            }
        }
    }


}
