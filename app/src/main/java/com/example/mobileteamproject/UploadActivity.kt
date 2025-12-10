package com.example.mobileteamproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mobileteamproject.dto.PostCreateDto
import kotlinx.coroutines.launch

class UploadActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.upload)  // upload.xml 사용

        val nextBtn = findViewById<Button>(R.id.nextBtn)
        val postTitle = findViewById<EditText>(R.id.postTitle)
        val contentInput = findViewById<EditText>(R.id.contentInput)
        val timePicker = findViewById<TimePicker>(R.id.timePicker)

        findViewById<Button>(R.id.backBtn).setOnClickListener {
            finish()
        }

        // 업로드(다음) 버튼
        nextBtn.setOnClickListener {
            val title = postTitle.text.toString().trim()
            val content = contentInput.text.toString().trim()
            val hour = timePicker.hour
            val minute = timePicker.minute
            val time = String.format("%02d:%02d", hour, minute)

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "제목과 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val req = PostCreateDto(
                title = title,
                content = content,
                time = time
            )

            // 서버로 전송
            lifecycleScope.launch {
                try {
                    val res = RetrofitInstance.api.createPost(req)
                    Toast.makeText(
                        this@UploadActivity,
                        "게시글 업로드 성공! (id=${res.id})",
                        Toast.LENGTH_SHORT
                    ).show()
                    finish() // 성공하면 화면 종료
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(
                        this@UploadActivity,
                        "업로드 실패: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
