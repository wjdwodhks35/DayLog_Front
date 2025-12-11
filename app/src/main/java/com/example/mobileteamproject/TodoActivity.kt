package com.example.mobileteamproject

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mobileteamproject.dto.TodoCreateDto
import com.example.mobileteamproject.dto.TodoResponseDto
import com.example.mobileteamproject.dto.TodoUpdateDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TodoActivity : AppCompatActivity() {

    private lateinit var rootLayout: LinearLayout
    private lateinit var backBtn: Button
    private lateinit var saveBtn: Button
    private lateinit var midTitleBox: EditText
    private lateinit var addListBtn: Button
    private lateinit var topBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.todolist)

        rootLayout = findViewById(R.id.toDoList)
        backBtn = findViewById(R.id.backBtn)
        saveBtn = findViewById(R.id.saveBtn)
        midTitleBox = findViewById(R.id.midTitleBox)
        addListBtn = findViewById(R.id.addList)
        topBar = findViewById(R.id.topBar)

        // 뒤로가기
        backBtn.setOnClickListener { finish() }

        // + 버튼: 새로운 비어있는 행 추가 (todo == null → 새 todo)
        addListBtn.setOnClickListener {
            addTodoRow(todo = null)
        }

        // 저장 버튼: 화면의 모든 행을 서버에 반영 (create/update)
        saveBtn.setOnClickListener {
            saveTodosToServer()
        }

        // 액티비티 시작 시, 서버에서 기존 todo 목록 불러오기
        loadTodosFromServer()
    }

    // -----------------------------
    // 1) 서버에서 todo 목록 불러오기
    // -----------------------------
    private fun loadTodosFromServer() {
        val userId = getCurrentUserId()
        if (userId == -1L) {
            Toast.makeText(this, "로그인 정보(userId)가 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val todos = RetrofitInstance.api.getTodoList(userId)

                withContext(Dispatchers.Main) {
                    clearTodoRows()
                    todos.forEach { todo ->
                        addTodoRow(todo)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@TodoActivity,
                        "서버에서 To-do 불러오기 실패: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // topBar / addList 빼고 나머지 동적 행 제거
    private fun clearTodoRows() {
        for (i in rootLayout.childCount - 1 downTo 0) {
            val child = rootLayout.getChildAt(i)
            if (child.id != R.id.topBar && child.id != R.id.addList) {
                rootLayout.removeViewAt(i)
            }
        }
    }

    // -----------------------------
    // 2) 한 줄(행) 추가하기
    //    todo == null → 새 항목
    //    todo != null → 기존 항목
    // -----------------------------
    private fun addTodoRow(todo: TodoResponseDto?) {
        val rowLayout = LinearLayout(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).also {
                it.topMargin = dpToPx(12)
            }
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, dpToPx(4), 0, dpToPx(4))
        }

        val radioButton = RadioButton(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).also {
                it.rightMargin = dpToPx(8)
            }
        }

        val editText = EditText(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1f
            )
            hint = "할 일을 입력하세요"
            textSize = 18f
            setPadding(dpToPx(6), dpToPx(6), dpToPx(6), dpToPx(6))
            setBackgroundResource(android.R.color.transparent)
            inputType = InputType.TYPE_CLASS_TEXT
        }

        if (todo != null) {
            editText.setText(todo.content ?: "")
            radioButton.isChecked = todo.done == true
            rowLayout.tag = todo.id          // ★ 이 행이 어떤 todoId인지 저장
            editText.alpha = if (radioButton.isChecked) 0.4f else 1.0f
        }

        radioButton.setOnCheckedChangeListener { _, isChecked ->
            editText.alpha = if (isChecked) 0.4f else 1.0f
        }

        rowLayout.addView(radioButton)
        rowLayout.addView(editText)

        val indexOfAddButton = rootLayout.indexOfChild(addListBtn)
        rootLayout.addView(rowLayout, indexOfAddButton)
    }

    // -----------------------------
    // 3) 저장 버튼 → create/update 요청
    // -----------------------------
    private fun saveTodosToServer() {
        val userId = getCurrentUserId()
        if (userId == -1L) {
            Toast.makeText(this, "로그인 정보(userId)가 없습니다.", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            try {
                for (i in 0 until rootLayout.childCount) {
                    val child = rootLayout.getChildAt(i)

                    if (child.id == R.id.topBar || child.id == R.id.addList) continue
                    if (child !is LinearLayout) continue
                    if (child.childCount < 2) continue

                    val radio = child.getChildAt(0) as? RadioButton ?: continue
                    val edit = child.getChildAt(1) as? EditText ?: continue

                    val text = edit.text.toString().trim()
                    val done = radio.isChecked

                    // 내용이 비어 있으면 서버에 안 보냄
                    if (text.isEmpty()) continue

                    val existingId = child.tag as? Long

                    if (existingId == null) {
                        // 새 todo 생성
                        val dto = TodoCreateDto(
                            content = text,
                            done = done
                        )
                        val created = RetrofitInstance.api.createTodo(userId, dto)
                        withContext(Dispatchers.Main) {
                            child.tag = created.id   // 다음부터는 update로
                        }
                    } else {
                        // 기존 todo 수정
                        val dto = TodoUpdateDto(
                            content = text,
                                done = done
                        )
                        RetrofitInstance.api.updateTodo(existingId, dto)
                    }
                }

                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@TodoActivity,
                        "To-do 리스트 저장 완료",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        this@TodoActivity,
                        "저장 중 오류 발생: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // -----------------------------
    // 로그인 시 저장해 둔 userId 가져오기
    // -----------------------------
    private fun getCurrentUserId(): Long {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        return prefs.getLong("userId", -1L)
    }

    private fun dpToPx(dp: Int): Int {
        val scale = resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }
}
