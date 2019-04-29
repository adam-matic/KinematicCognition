package com.example.kinematiccognition

import android.Manifest
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.ArrayAdapter
import com.example.brainawarenesweekexperiment.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),43)

        ArrayAdapter.createFromResource(
            this,
            R.array.months_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerMonths.adapter = adapter
        }

        yearPicker.minValue = 1950
        yearPicker.maxValue = 2015
        yearPicker.value = 2010


        btnStart.setOnClickListener {

            val month = spinnerMonths.selectedItem
            val year = yearPicker.value
            val sexo = radioSex.checkedRadioButtonId
            val mano = radioMano.checkedRadioButtonId

            if (sexo > 0 && mano > 0) {
                var s = when (sexo) {  R.id.Fem -> "Fem"
                                       R.id.Mas -> "Mas"
                                       R.id.Sin -> "Sin"
                                       else -> "" }
                var m = when (mano) {  R.id.radioDerecha -> "Der"
                                       R.id.radioIzquierda -> "Izq"
                                       else -> "" }

                val intent = Intent(this, InitAcitvity::class.java)
                val user = month.toString() + year.toString() + s + m
                intent.putExtra("user", user)
                startActivity(intent)
            }
        }


    }

    override fun finish() {
        android.os.Process.killProcess(android.os.Process.myPid())
        System.exit(1)
    }

}
