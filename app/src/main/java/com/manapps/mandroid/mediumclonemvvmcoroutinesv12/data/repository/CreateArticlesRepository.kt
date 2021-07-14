package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.repository

import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.safeApiCall
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.api.ApiService
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.request.CreateArticleRequest

class CreateArticlesRepository(val apiService: ApiService) {
    suspend fun sendCreateArticleRequest(createArticleRequest: CreateArticleRequest) = safeApiCall {
        apiService.sendCreateArticleRequest(createArticleRequest)
    }

}