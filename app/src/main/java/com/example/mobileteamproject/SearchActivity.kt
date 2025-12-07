package com.example.mobileteamproject

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val etKeyword = findViewById<EditText>(R.id.etKeyword)
        val btnSearch = findViewById<ImageView>(R.id.btnSearch)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        val adapter = SearchAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        btnSearch.setOnClickListener {
            val keyword = etKeyword.text.toString()

            val list = listOf(
                SearchItem("$keyword 건물A", "11:24"),
                SearchItem("$keyword 건물B", "11:26"),
                SearchItem("$keyword 카페C", "11:28"),
                SearchItem("$keyword 식당D", "10:01")
            )

            adapter.submitList(list)
        }
    }
}
