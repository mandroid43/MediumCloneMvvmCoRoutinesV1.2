package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.api

import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.request.CreateArticleRequest
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.request.LoginRequest
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.request.SignupRequest
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.response.ArticleResponse
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.response.ArticlesResponse
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.response.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("users")
    suspend fun sendCreateAccountRequest(
            @Body userCreds: SignupRequest
    ): Response<UserResponse>

    @POST("users/login")
    suspend fun sendLoginRequest(
            @Body userCreds: LoginRequest
    ): Response<UserResponse>


    @GET("articles")
    suspend fun getAuthorsArticles(
            @Query("author") author: String? = null,
            @Query("favourited") favourited: String? = null,
            @Query("tag") tag: String? = null
    ): Response<ArticlesResponse>

    @GET("articles/feed")
    suspend fun getFeedArticles(): Response<ArticlesResponse>

    @GET("articles/{slug}")
    suspend fun getArticleBySlug(
            @Path("slug") slug: String
    ): Response<ArticleResponse>


    @POST("articles")
    suspend fun sendCreateArticleRequest(
            @Body createArticleRequest: CreateArticleRequest
    ): Response<ArticleResponse>


}