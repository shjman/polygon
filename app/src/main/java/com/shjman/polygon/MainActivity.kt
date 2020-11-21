package com.shjman.polygon

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val launchAndroidAcademy = findViewById<Button>(R.id.launch_android_academy)
        launchAndroidAcademy.setOnClickListener {
            try {
                intent = Intent(this, Class.forName("com.shjman.android_academy.MainActivity"))
                startActivity(intent)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
        }
    }
}