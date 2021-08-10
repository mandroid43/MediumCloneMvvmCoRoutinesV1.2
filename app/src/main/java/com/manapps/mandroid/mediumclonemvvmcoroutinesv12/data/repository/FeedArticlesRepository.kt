package com.manapps.mandroid.mediumclonemvckotlin.data.repository

import android.content.Context
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.safeApiCall
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.api.ApiService
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.dao.ArticleDao
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.dao.ProfileDao
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.Article
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.response.ArticlesResponse
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.ui.profile.ProfileActivity
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.SharedPref
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.Utils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class FeedArticlesRepository(private val apiService: ApiService, private val dao: ArticleDao, private val daoProfile:ProfileDao) {
    suspend fun getAuthorsArticles() = safeApiCall {
        apiService.getAuthorsArticles()
    }

    fun logoutSession(context: Context) {
        SharedPref.logoutSessionAndGoToLogin(context)
    }

    fun gotoProfile(context: Context) {
        Utils.moveTo(context, ProfileActivity::class.java)
    }


    fun insertArticles(articlesList: List<Article>) {
        CoroutineScope(Dispatchers.Default).launch {
            articlesList.forEach { article ->
            }
            dao.insertArticles(articlesList)
        }
    }

    fun getArticles() : Flow<List<Article>> {
        return dao.getArticles()
    }

    fun deleteAllArticles() {
        CoroutineScope(Dispatchers.Default).launch {  dao.deleteAllArticles() }
    }


}