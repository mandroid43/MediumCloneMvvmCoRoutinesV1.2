package com.manapps.mandroid.mediumclonemvckotlin.data.repository

import android.content.Context
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.safeApiCall
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.api.ApiService
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.dao.ArticleDao
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.response.ArticlesResponse
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.profile.ProfileActivity
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.SharedPref
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.Utils

class FeedArticlesRepository(private val apiService: ApiService, private val dao: ArticleDao) {
    suspend fun getAuthorsArticles() = safeApiCall {
        apiService.getAuthorsArticles()
    }

    fun logoutSession(context: Context) {
        SharedPref.logoutSessionAndGoToLogin(context)
    }

    fun gotoProfile(context: Context) {
        Utils.moveTo(context, ProfileActivity::class.java)
    }


    fun insertArticles(articlesResponse: ArticlesResponse) {
        dao.insertArticles(articlesResponse)
    }

    fun getArticles()= dao.getArticles()


}