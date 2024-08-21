package com.angellira.eduardoApp.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


//private const val BASE_URL = "http://127.0.0.1:8080/"
//
//object RetrofitInstance {
//    private val retrofit by lazy {
//        Retrofit.Builder()
//            .baseUrl(BASE_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }

//    interface ApiService {
//
//        // User Endpoints
//        @GET("/users")
//        fun getUsers(): Call<List<UserResponse>>
//
//        @GET("/users/{id}")
//        fun getUserById(@Path("id") id: String): Call<UserResponse>
//
//        @GET("/users/login")
//        fun loginUser(
//            @Query("email") email: String,
//            @Query("password") password: String
//        ): Call<UserResponse>
//
//        @PATCH("/users/{id}")
//        fun updateUser(@Path("id") id: String, @Body user: UserRequest): Call<Unit>
//
//        @POST("/users")
//        fun addUser(@Body user: UserRequest): Call<Unit>
//
//        @DELETE("/users/{id}")
//        fun deleteUser(@Path("id") id: String): Call<Unit>
//
//        // MarketItem Endpoints
//        @GET("/marketItem/all")
//        fun getMarketItems(): Call<List<MarketItemResponse>>
//
//        @GET("/marketItem/{id}")
//        fun getMarketItemById(@Path("id") id: Long): Call<MarketItemResponse>
//
//        @POST("/marketItem")
//        fun addMarketItem(@Body marketItem: MarketItemRequest): Call<Unit>
//
//        // Post Endpoints
//        @GET("/posts")
//        fun getPosts(): Call<List<PostResponse>>
//
//        @GET("/posts/{id}")
//        fun getPostById(@Path("id") id: String): Call<PostResponse>
//
//        @PUT("/posts")
//        fun addPost(@Body post: PostRequest): Call<Unit>
//
//        @DELETE("/posts/{id}")
//        fun deletePost(@Path("id") id: String): Call<Unit>
//    }

//    val api: ApiService by lazy {
//        retrofit.create(ApiService::class.java)
//    }
//}
