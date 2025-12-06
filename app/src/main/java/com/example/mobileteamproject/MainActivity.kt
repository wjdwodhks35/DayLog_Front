package com.example.mobileteamproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 🚀 앱 실행 시 API 테스트 실행!
        testApi()

        findViewById<Button>(R.id.Bmypage).setOnClickListener {
            startActivity(Intent(this, MyPageActivity::class.java))
        }
        findViewById<Button>(R.id.Bmap).setOnClickListener {
            startActivity(Intent(this, MapActivity::class.java))
        }
        findViewById<Button>(R.id.Bmap_name).setOnClickListener {
            startActivity(Intent(this, MapNameActivity::class.java))
        }
        findViewById<Button>(R.id.Bcalendar).setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
        }
        findViewById<Button>(R.id.Bmyupload).setOnClickListener {
            startActivity(Intent(this, MyUploadActivity::class.java))
        }
    }

    // ✅ Retrofit API 테스트 코드
    private fun testApi() {
        lifecycleScope.launch {
            try {
                // 👉 여기서 테스트하고 싶은 API 선택!
                val response = RetrofitInstance.api.getTodoList()
                Log.d("API_TEST", "성공: $response")
            } catch (e: Exception) {
                Log.e("API_TEST", "실패: ${e.message}")
            }
        }
    }
}
