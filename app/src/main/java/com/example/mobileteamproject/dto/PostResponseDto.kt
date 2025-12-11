package com.example.mobileteamproject.dto

data class PostResponseDto(
    val id: Long,
    val userId: Long,
    val username: String?,   // 작성자 이름 (서버에서 같이 보내주면 좋음)
    val title: String,
    val content: String,
    val createdAt: String?   // "2025-12-11T01:23:45" 이런 식 (없으면 null 가능)
)
