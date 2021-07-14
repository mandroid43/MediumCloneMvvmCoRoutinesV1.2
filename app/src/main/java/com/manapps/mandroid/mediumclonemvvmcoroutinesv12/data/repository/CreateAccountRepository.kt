package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.repository

import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.utils.safeApiCall
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.api.ApiService
import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.models.request.SignupRequest

class CreateAccountRepository(val apiService: ApiService) {
    suspend fun sendCreateAccountRequest(signupRequest: SignupRequest) = safeApiCall {
        apiService.sendCreateAccountRequest(signupRequest)
    }
}