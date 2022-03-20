package com.example.metaquotesandroidtestapp.logic.repositories

import com.example.metaquotesandroidtestapp.network.MainApi
import com.example.metaquotesandroidtestapp.logic.entities.Resource
import java.io.InputStream
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(private val api: MainApi) : MainRepository {

    override suspend fun loadFileStream(url: String): Resource<InputStream> {
        return try {
            val response = api.loadFile(url)
            val result = response.body()
            if(response.isSuccessful && result != null) Resource.Success(result.byteStream())
            else Resource.Error(response.errorBody().toString())
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage)
        }
    }
}