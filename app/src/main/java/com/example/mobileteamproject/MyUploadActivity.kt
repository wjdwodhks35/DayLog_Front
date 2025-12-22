package com.example.mobileteamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MyUploadActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.myupload)

<<<<<<< HEAD

=======
>>>>>>> 00f90b0f76985402e2d0e948db458571c420a57f
        findViewById<Button>(R.id.backBtn).setOnClickListener {
            finish()
        }
        findViewById<Button>(R.id.homeBtn).setOnClickListener {
<<<<<<< HEAD
            startActivity(Intent(this, MapActivity::class.java))
=======
            startActivity(Intent(this, MainActivity::class.java))
>>>>>>> 00f90b0f76985402e2d0e948db458571c420a57f
        }
        findViewById<Button>(R.id.postBtn).setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
        }
        findViewById<Button>(R.id.myPageBtn).setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
        }
    }
}
