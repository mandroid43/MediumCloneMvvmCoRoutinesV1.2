package com.manapps.mandroid.mediumclonemvvmcoroutinesv12.data.api


import com.manapps.mandroid.mediumclonemvvmcoroutinesv12.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object APIClinet {

    var authToken: String? = null


    private val authInterceptor = Interceptor { chain ->
        var req = chain.request()

        authToken?.let {
            req = req.newBuilder()
                .header("Authorization", "Token $it")
                .build()
        }
        chain.proceed(req)
    }

    var interceptor: HttpLoggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)


    val okHttpBuilder = OkHttpClient.Builder()
        .readTimeout(5, TimeUnit.SECONDS)
        .connectTimeout(2, TimeUnit.SECONDS).addInterceptor(interceptor)

    val retrofitBuilder = Retrofit.Builder()
        .baseUrl(BuildConfig.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())

    val publicApi = retrofitBuilder
        .client(okHttpBuilder.build())
        .build()
        .create(ApiService::class.java)

    val authApi = retrofitBuilder
        .client(okHttpBuilder.addInterceptor(authInterceptor).build())
        .build()
        .create(ApiService::class.java)


}