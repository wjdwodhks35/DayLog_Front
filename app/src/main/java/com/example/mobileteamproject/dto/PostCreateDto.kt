package com.example.mobileteamproject.dto

data class PostCreateDto(
    val userId: Long,      // 글 작성한 사람
    val title: String,     // 글 제목
    val content: String    // 글 내용
)
