package com.example.mobileteamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MyPageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mypage)


        findViewById<Button>(R.id.backBtn).setOnClickListener {
            finish()
        }
        findViewById<Button>(R.id.homeBtn).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        findViewById<Button>(R.id.postBtn).setOnClickListener {
            startActivity(Intent(this, UploadActivity::class.java))
        }
        findViewById<Button>(R.id.myPageBtn).setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
        }
        findViewById<Button>(R.id.todo_list).setOnClickListener {
            startActivity(Intent(this, TodoActivity::class.java))
      }
    }
}
