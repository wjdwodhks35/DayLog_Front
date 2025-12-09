package com.example.mobileteamproject

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MyUploadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myupload)

        val backBtn = findViewById<Button>(R.id.backBtn)
        backBtn.setOnClickListener {
            finish()
        }

    }
}
