package com.example.mobileteamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileteamproject.dto.UserLoginDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnGoSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtEmail = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnGoSignup = findViewById(R.id.btnGoSignup)

        // 로그인 버튼
        btnLogin.setOnClickListener {
            val email = edtEmail.text.toString().trim()
            val password = edtPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "이메일과 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            doLogin(email, password)
        }

        // 회원가입 화면으로 이동 (이미 있는 SignupActivity로 바꿔줘)
        btnGoSignup.setOnClickListener {
            // TODO: 실제 네가 쓰는 회원가입 액티비티 이름으로 변경
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }

    private fun doLogin(email: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val req = UserLoginDto(email, password)
                val user = RetrofitInstance.api.login(req)  // UserResponseDto 리턴 가정

                withContext(Dispatchers.Main) {
                    if (user.id != null) {
                        // 로그인 성공 → 유저 정보 저장(예: SharedPreferences)
                        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
                        prefs.edit()
                            .putLong("userId", user.id)
                            .putString("email", user.email)
                            .putString("username", user.username)
                            .apply()

                        Toast.makeText(
                            this@LoginActivity,
                            "로그인 성공! 환영합니다 ${user.username ?: ""}",
                            Toast.LENGTH_SHORT
                        ).show()

                        // TODO: 로그인 후 이동할 메인 화면 Activity로 변경
                        val intent = Intent(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "로그인 실패: 사용자 정보를 불러오지 못했습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@LoginActivity,
                        "로그인 중 오류 발생: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
