package com.example.mobileteamproject

import com.example.mobileteamproject.dto.*
import retrofit2.http.*

interface ApiService {

    // USER
    @POST("/api/auth/signup")
    suspend fun signup(
        @Body req: UserSignupDto
    ): UserResponseDto

    @POST("/api/auth/login")
    suspend fun login(
        @Body req: UserLoginDto
    ): UserResponseDto


    // TODO
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
    @POST("/api/post")
    suspend fun createPost(
        @Body req: PostCreateDto
    ): PostResponseDto

    @GET("/api/post")
    suspend fun getAllPosts(): List<PostResponseDto>

    @GET("/api/post/user/{userId}")
    suspend fun getPostsByUser(
        @Path("userId") userId: Long
    ): List<PostResponseDto>
}
