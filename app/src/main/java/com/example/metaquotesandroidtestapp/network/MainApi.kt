package com.example.metaquotesandroidtestapp.network

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface MainApi {
    @Streaming
    @GET
    suspend fun loadFile(@Url fileUrl: String) : Response<ResponseBody>
}