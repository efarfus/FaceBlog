package com.angellira.eduardoApp.network

import androidx.room.Query
import com.angellira.eduardoApp.model.MarketItem
import com.angellira.eduardoApp.model.Posts
import com.angellira.eduardoApp.model.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path


private const val BASE_URL = "http://192.168.115.151:8080/"


private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("/users/login")
    suspend fun getUserByEmailAndPassword(
        @retrofit2.http.Query("email") email: String,
        @retrofit2.http.Query("password") password: String
    ): User

    @GET("users/{id}")
    suspend fun getUserById(@Path("id") id: String): User

    @GET("marketItem/{id}")
    suspend fun getItemById(@Path("id") id: String): MarketItem

    @GET("posts/{id}")
    suspend fun getPostId(@Path("id") id: String): Posts

    @GET("posts")
    suspend fun getPosts(): List<Posts>

    @GET("posts/user/{user}")
    suspend fun getUserPosts(@Path("user") user: String): List<Posts>

    @GET("marketItem/all")
    suspend fun getItens(): List<MarketItem>

    @POST("users")
    suspend fun saveUser(@Body user: User)

    @POST("posts")
    suspend fun savePost(@Body post: Posts)

    @POST("marketItem")
    suspend fun saveItem(@Body marketItem: MarketItem)
//
//    @POST("marketItem")
//    suspend fun saveMarketItem(@Body item: MarketItem)

    @PATCH("users/{id}")
    suspend fun putUser(@Path("id") id: String, @Body user: User)

    @DELETE("users/{id}")
    suspend fun deleteUser(@Path("id") id: String): Response<Void>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: String): Response<Void>

    @DELETE("marketItem/{id}")
    suspend fun deleteItem(@Path("id") id: String): Response<Void>
}

object ApiServiceFaceBlog {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}

