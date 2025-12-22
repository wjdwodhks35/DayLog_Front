package com.example.mobileteamproject

import android.os.Build
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileteamproject.dto.PostCreateDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PostCreateActivity : AppCompatActivity() {

    private lateinit var backBtn: Button
    private lateinit var nextBtn: Button
    private lateinit var radioBtn: RadioButton
    private lateinit var postTitle: EditText
    private lateinit var contentInput: EditText
    private lateinit var timePicker: TimePicker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ⬇⬇⬇  이 부분은 XML 파일 이름에 맞게 수정해줘
        // 예: new_post.xml 이면 R.layout.new_post
        setContentView(R.layout.upload)
        // ⬆⬆⬆

        backBtn = findViewById(R.id.backBtn)
        nextBtn = findViewById(R.id.nextBtn)
        radioBtn = findViewById(R.id.radioBtn)
        postTitle = findViewById(R.id.postTitle)
        contentInput = findViewById(R.id.contentInput)
        timePicker = findViewById(R.id.timePicker)

        // 뒤로가기: 현재 화면만 닫기
        backBtn.setOnClickListener {
            finish()
        }

        // ">" 버튼을 게시글 등록 버튼으로 사용
        nextBtn.setOnClickListener {
            submitPost()
        }
    }

    private fun submitPost() {
        val title = postTitle.text.toString().trim()
        val content = contentInput.text.toString().trim()

        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "제목과 내용을 모두 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val userId = getCurrentUserId()
        if (userId == -1L) {
            Toast.makeText(this, "로그인 정보가 없습니다. 다시 로그인 해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        // TimePicker 값 가져와서 문자열로 만들기 (원하면 서버에 같이 보낼 때 사용)
        val (hour, minute) = getSelectedTime()
        val timeText = String.format("%02d:%02d", hour, minute)

        // 지금은 시간은 따로 필드 안 보내고,
        // content 뒤에 붙여서 보내는 식으로 처리 (서버 DTO에 time 필드가 아직 없다고 가정)
        val finalContent = "$content\n\n(시간: $timeText)"

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val req = PostCreateDto(
                    userId = userId,
                    title = title,
                    content = finalContent
                )

                val res = RetrofitInstance.api.createPost(req)

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@PostCreateActivity,
                        "게시물이 등록되었습니다!",
                        Toast.LENGTH_SHORT
                    ).show()
                    // TODO: 여기서 피드 화면으로 돌아가고 싶으면 Intent로 이동시키면 됨
                    finish()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@PostCreateActivity,
                        "게시물 등록 실패: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    private fun getSelectedTime(): Pair<Int, Int> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            timePicker.hour to timePicker.minute
        } else {
            @Suppress("DEPRECATION")
            timePicker.currentHour to timePicker.currentMinute
        }
    }

    private fun getCurrentUserId(): Long {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return prefs.getLong("userId", -1L)
    }
}
