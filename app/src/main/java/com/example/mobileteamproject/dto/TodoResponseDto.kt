package com.example.mobileteamproject.dto

data class TodoResponseDto(
    val id: Long,
    val content: String,
    val isCompleted: Boolean
)
