package com.angellira.eduardoApp.network

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
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

private const val BASE_URL =
    "https://faceblog-2f28f-default-rtdb.firebaseio.com/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @GET("user.json")
    suspend fun getUsers(): Map<String, User>

    @GET("user/{id}.json")
    suspend fun getUser(@Path("id") id: String): User

    @GET("posts/{id}.json")
    suspend fun getPostId(@Path("id") id: String): Posts

    @GET("posts.json")
    suspend fun getPosts(): Map<String, Posts>

    @POST("user.json")
    suspend fun saveUser(@Body user: User)

    @POST("posts.json")
    suspend fun savePost(@Body post: Posts)

    @POST("marketItem.json")
    suspend fun saveMarketItem(@Body item: MarketItem)

    @PUT("user/{id}.json")
    suspend fun putUser(@Path("id") id: String, @Body user: User)

    @DELETE("user/{id}.json")
    suspend fun deleteUser(@Path("id") id: String): Response<Void>
}

object ApiServiceFaceBlog {
    val retrofitService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
