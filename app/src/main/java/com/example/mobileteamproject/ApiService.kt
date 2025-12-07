package com.example.mobileteamproject

import com.example.mobileteamproject.dto.LikeDto
import com.example.mobileteamproject.dto.PostCreateDto
import com.example.mobileteamproject.dto.PostResponseDto
import com.example.mobileteamproject.dto.SaveDto
import com.example.mobileteamproject.dto.TodoCreateDto
import com.example.mobileteamproject.dto.TodoResponseDto
import com.example.mobileteamproject.dto.TodoUpdateDto
import com.example.mobileteamproject.dto.UserLoginDto
import com.example.mobileteamproject.dto.UserSignupDto
import com.example.mobileteamproject.dto.VoteDto
import retrofit2.http.*

interface ApiService {

    // USER
    @POST("/signup")
    suspend fun signup(@Body req: UserSignupDto): String

    @POST("/login")
    suspend fun login(@Body req: UserLoginDto): String


    // TODO — ★ 수정된 버전
    @POST("/todo/{userId}")
    suspend fun createTodo(
        @Path("userId") userId: Long,
        @Body req: TodoCreateDto
    ): TodoResponseDto

    @GET("/todo/{userId}")
    suspend fun getTodoList(
        @Path("userId") userId: Long
    ): List<TodoResponseDto>

    @PATCH("/todo/{todoId}")
    suspend fun updateTodo(
        @Path("todoId") todoId: Long,
        @Body req: TodoUpdateDto
    ): TodoResponseDto

    @DELETE("/todo/{todoId}")
    suspend fun deleteTodo(
        @Path("todoId") todoId: Long
    ): String


    // CALENDAR
    @GET("/calendar")
    suspend fun getCalendar(): List<String>

    @POST("/calendar")
    suspend fun saveCalendar(@Body req: Map<String, String>): String


    // SOCIAL ACTIONS
    @POST("/like")
    suspend fun doLike(@Body req: LikeDto): String

    @POST("/save")
    suspend fun doSave(@Body req: SaveDto): String

    @POST("/vote")
    suspend fun doVote(@Body req: VoteDto): String


    // POST UPLOAD
    @POST("/posts")
    suspend fun createPost(@Body req: PostCreateDto): PostResponseDto
}

//interface ApiService {
//
//    // USER
//    @POST("/signup")
//    suspend fun signup(@Body req: UserSignupDto): String
//
//    @POST("/login")
//    suspend fun login(@Body req: UserLoginDto): String
//
//
//    // TODO
//    @POST("/todo")
//    suspend fun createTodo(@Body req: TodoCreateDto): TodoResponseDto
//
//    @GET("/todo")
//    suspend fun getTodoList(): List<TodoResponseDto>
//
//    @PUT("/todo/{id}")
//    suspend fun updateTodo(
//        @Path("id") id: Long,
//        @Body req: TodoUpdateDto
//    ): TodoResponseDto
//
//    @DELETE("/todo/{id}")
//    suspend fun deleteTodo(@Path("id") id: Long): String
//
//
//    // CALENDAR
//    @GET("/calendar")
//    suspend fun getCalendar(): List<String>
//
//    @POST("/calendar")
//    suspend fun saveCalendar(@Body req: Map<String, String>): String
//
//
//    // SOCIAL ACTIONS (like/save/vote)
//    @POST("/like")
//    suspend fun doLike(@Body req: LikeDto): String
//
//    @POST("/save")
//    suspend fun doSave(@Body req: SaveDto): String
//
//    @POST("/vote")
//    suspend fun doVote(@Body req: VoteDto): String
//
//
//    // -----------------------
//    // POST UPLOAD (게시글 생성) ← ★ 새로 추가됨
//    // -----------------------
//    @POST("/posts")
//    suspend fun createPost(@Body req: PostCreateDto): PostResponseDto
//}
