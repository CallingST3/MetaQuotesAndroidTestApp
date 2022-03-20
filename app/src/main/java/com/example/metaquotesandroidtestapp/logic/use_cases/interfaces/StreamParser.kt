package com.example.metaquotesandroidtestapp.logic.use_cases.interfaces

import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface StreamParser {
    suspend fun parseStream(inputStream: InputStream, filter: String): Flow<String>
}