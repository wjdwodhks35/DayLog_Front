<<<<<<< HEAD
//package com.example.mobileteamproject
//
//import android.content.Intent
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import androidx.appcompat.app.AppCompatActivity
//import androidx.lifecycle.lifecycleScope
//import com.example.mobileteamproject.dto.TodoCreateDto
//import kotlinx.coroutines.launch
//
//class MainActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        // 🚀 앱 실행 시 API 테스트 실행!
//        testApi()
//
//        findViewById<Button>(R.id.Bmypage).setOnClickListener {
//            startActivity(Intent(this, MyPageActivity::class.java))
//        }
//        findViewById<Button>(R.id.Bmap).setOnClickListener {
//            startActivity(Intent(this, MapActivity::class.java))
//        }
//        findViewById<Button>(R.id.Bmap_name).setOnClickListener {
//            startActivity(Intent(this, MapNameActivity::class.java))
//        }
//        findViewById<Button>(R.id.Bcalendar).setOnClickListener {
//            startActivity(Intent(this, CalendarActivity::class.java))
//        }
//        findViewById<Button>(R.id.Bmyupload).setOnClickListener {
//            startActivity(Intent(this, MyUploadActivity::class.java))
//        }
//    }
//
//
=======
package com.example.mobileteamproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.mobileteamproject.dto.TodoCreateDto
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


    private fun testApi() {
        lifecycleScope.launch {
            try {
                val userId = 1L

                // Todo 생성 요청
                val newTodo = TodoCreateDto(
                    content = "안드로이드에서 보낸 할일",
                    done = null
                )

                val created = RetrofitInstance.api.createTodo(userId, newTodo)
                Log.d("API_TEST", "생성 성공: $created")

                // 생성 후 조회
                val list = RetrofitInstance.api.getTodoList(userId)
                Log.d("API_TEST", "조회 성공: $list")

            } catch (e: Exception) {
                Log.e("API_TEST", "실패: ${e.message}")
            }
        }
    }
}


// ✅ Retrofit API 테스트 코드


>>>>>>> 00f90b0f76985402e2d0e948db458571c420a57f
//    private fun testApi() {
//        lifecycleScope.launch {
//            try {
//                val userId = 1L
//
//                // Todo 생성 요청
//                val newTodo = TodoCreateDto(
//                    content = "안드로이드에서 보낸 할일",
//                    done = null
//                )
//
//                val created = RetrofitInstance.api.createTodo(userId, newTodo)
//                Log.d("API_TEST", "생성 성공: $created")
//                // 생성 후 조회
//                val list = RetrofitInstance.api.getTodoList(userId)
//                Log.d("API_TEST", "조회 성공: $list")
//
//            } catch (e: Exception) {
//                Log.e("API_TEST", "실패: ${e.message}")
//            }
//        }
//    }
//}
//
//
//// ✅ Retrofit API 테스트 코드
//
//
////    private fun testApi() {
////        lifecycleScope.launch {
////            try {
////                // 👉 여기서 테스트하고 싶은 API 선택!
////                val response = RetrofitInstance.api.getTodoList()
////                Log.d("API_TEST", "성공: $response")
////            } catch (e: Exception) {
////                Log.e("API_TEST", "실패: ${e.message}")
////            }
////        }
////    }
////}
