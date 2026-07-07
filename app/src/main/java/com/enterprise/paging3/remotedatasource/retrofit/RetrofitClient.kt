package com.enterprise.paging3.remotedatasource.retrofit

import android.content.Context
import com.enterprise.paging3.remotedatasource.retrofit.interceptor.NetworkInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitClient {

    private const val BASE_URL: String = "https://jsonplaceholder.typicode.com/"

    fun getRetrofitPostApi(context: Context): PostApi{

        val retrofitPost = getRetrofitPost(context = context)

        val postApiApi = retrofitPost.create(PostApi::class.java)

        return postApiApi

    }

    private fun getRetrofitPost(context: Context): Retrofit {

        val client = getOkHttpClient(context = context)

        val retrofit =
            Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .client(client)
                .build()

        return retrofit

    }


    private fun getOkHttpClient(context: Context): OkHttpClient {

        val client = OkHttpClient.Builder()
            .addInterceptor(NetworkInterceptor(context = context))
            .build()

        return client

    }


}