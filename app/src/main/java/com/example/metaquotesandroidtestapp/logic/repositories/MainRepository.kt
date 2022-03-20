package com.example.metaquotesandroidtestapp.logic.repositories

import com.example.metaquotesandroidtestapp.logic.entities.Resource
import java.io.InputStream

interface MainRepository {
    suspend fun loadFileStream(url: String): Resource<InputStream>
}