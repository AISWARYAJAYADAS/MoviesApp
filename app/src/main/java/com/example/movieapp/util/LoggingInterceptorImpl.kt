package com.example.movieapp.util

import okhttp3.Interceptor
import okhttp3.Response

class LoggingInterceptorImpl : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(request)
    }


}