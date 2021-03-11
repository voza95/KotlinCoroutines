package com.example.kotlincoroutines

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import java.util.concurrent.TimeUnit

interface MyApi {

    @GET("quotes")
    fun getMovies(): Call<Any>

    companion object{

        val builder = OkHttpClient.Builder()
            .readTimeout(300, TimeUnit.SECONDS)
            .connectTimeout(300, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Client-Service", "COAS2020SCP")
                    .header("Auth-Key", "SYS5ccd7b534b19d30030c6503f3a852d00SCP")
                    .header("Content-Type", "application/json")
                //val requestBuilder = original.newBuilder().addHeader("Content-type", "application/json; charset=utf-8")
                val request = requestBuilder.build()
                chain.proceed(request)
            }

        val client = builder.build()
        operator  fun invoke(): MyApi{
            return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://devhouse.syscraft-pro.tk/CoastalScrap/api/")
                .client(client)
                .build()
                .create(MyApi::class.java)
        }
    }
}