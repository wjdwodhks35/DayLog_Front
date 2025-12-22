package com.example.mobileteamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileteamproject.dto.UserSignupDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SignupActivity : AppCompatActivity() {

    private lateinit var edtSignupEmail: EditText
    private lateinit var edtSignupUsername: EditText
    private lateinit var edtSignupPassword: EditText
    private lateinit var edtSignupPasswordCheck: EditText
    private lateinit var btnSignup: Button
    private lateinit var btnGoLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        edtSignupEmail = findViewById(R.id.edtSignupEmail)
        edtSignupUsername = findViewById(R.id.edtSignupUsername)
        edtSignupPassword = findViewById(R.id.edtSignupPassword)
        edtSignupPasswordCheck = findViewById(R.id.edtSignupPasswordCheck)
        btnSignup = findViewById(R.id.btnSignup)
        btnGoLogin = findViewById(R.id.btnGoLogin)

        // 회원가입 버튼
        btnSignup.setOnClickListener {
            val email = edtSignupEmail.text.toString().trim()
            val username = edtSignupUsername.text.toString().trim()
            val password = edtSignupPassword.text.toString()
            val passwordCheck = edtSignupPasswordCheck.text.toString()

            if (email.isEmpty() || username.isEmpty() || password.isEmpty() || passwordCheck.isEmpty()) {
                Toast.makeText(this, "모든 칸을 입력하세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != passwordCheck) {
                Toast.makeText(this, "비밀번호가 서로 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            doSignup(email, username, password)
        }

        // 로그인 화면으로 이동
        btnGoLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun doSignup(email: String, username: String, password: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val req = UserSignupDto(
                    email = email,
                    password = password,
                    username = username
                )

                // /api/auth/signup 호출 (ApiService에 이렇게 정의되어 있어야 함)
                val user = RetrofitInstance.api.signup(req) // UserResponseDto 반환 가정

                withContext(Dispatchers.Main) {
                    if (user.id != null) {
                        Toast.makeText(
                            this@SignupActivity,
                            "회원가입 성공! 로그인 해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()

                        // 회원가입 후 로그인 화면으로 이동
                        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(
                            this@SignupActivity,
                            "회원가입 실패: 사용자 정보를 불러오지 못했습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@SignupActivity,
                        "회원가입 중 오류 발생: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
