package com.example.badhabbits

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.main_text);
        val resetBtn = findViewById<Button>(R.id.reset_btn)

        val sharedPref = getSharedPreferences("main", Context.MODE_PRIVATE)
        val time = sharedPref.getLong("time", -1)
        if (time == -1L) {
            sharedPref.edit().putLong("time", System.currentTimeMillis()).apply()
            textView.text = "0"
            resetBtn.visibility = View.GONE
        } else {
            val diff = (System.currentTimeMillis() - time) / 1000
            val days = diff / (24 * 3600)
            if (days == 0L) {
                resetBtn.visibility = View.GONE
            }
            textView.text = days.toString()
        }

        resetBtn.setOnClickListener {
            sharedPref.edit().putLong("time", System.currentTimeMillis()).apply()
            textView.text = "0"
        }

    }
}