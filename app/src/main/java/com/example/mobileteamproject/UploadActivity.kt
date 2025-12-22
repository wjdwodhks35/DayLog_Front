package com.example.mobileteamproject

import android.os.Build
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

            // 시간 가져오기 (SDK 버전 호환)
            val (hour, minute) = getTimeFromPicker(timePicker)
            val time = String.format("%02d:%02d", hour, minute)

            if (title.isEmpty() || content.isEmpty()) {
                Toast.makeText(this, "제목과 내용을 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // ✅ 로그인할 때 저장해둔 userId 가져오기
            val userId = getCurrentUserId()
            if (userId == -1L) {
                Toast.makeText(this, "로그인 정보가 없습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 시간까지 서버에 보내고 싶으면 content 뒤에 붙이는 방식 (서버 DTO에 time 필드가 없으니까)
            val finalContent = "$content\n\n(시간: $time)"

            val req = PostCreateDto(
                userId = userId,     // 🔥 user 대신 userId 사용
                title = title,
                content = finalContent
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

    // SharedPreferences 에서 userId 꺼내오기 (로그인 시 저장했다고 가정)
    private fun getCurrentUserId(): Long {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return prefs.getLong("userId", -1L)
    }

    // TimePicker에서 시/분 가져오기 (SDK 버전 호환)
    private fun getTimeFromPicker(timePicker: TimePicker): Pair<Int, Int> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour to timePicker.minute
        } else {
            @Suppress("DEPRECATION")
            timePicker.currentHour to timePicker.currentMinute
        }
    }
}
