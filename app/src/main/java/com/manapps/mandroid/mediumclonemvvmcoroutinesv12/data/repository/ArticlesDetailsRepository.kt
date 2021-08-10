package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.repository

import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.safeApiCall
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.api.ApiService
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.local.dao.ArticleDao
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.entities.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ArticlesDetailsRepository(val apiService: ApiService, private val dao: ArticleDao) {
    suspend fun getArticleBySlug(slugId: String) = safeApiCall {
        apiService.getArticleBySlug(slugId)
    }

    fun insertArticles(article: Article) {
        CoroutineScope(Dispatchers.Default).launch { dao.insertArticle(article) }
    }

    fun getArticle(slugId: String): Flow<Article> {
        return dao.getArticle(slugId)
    }

    fun deleteAllArticles() {
        CoroutineScope(Dispatchers.Default).launch { dao.deleteAllArticles() }
    }
}