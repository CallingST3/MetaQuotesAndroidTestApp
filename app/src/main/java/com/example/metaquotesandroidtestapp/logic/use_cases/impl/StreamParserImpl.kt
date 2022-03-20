package com.example.metaquotesandroidtestapp.logic.use_cases.impl

import com.example.metaquotesandroidtestapp.logic.use_cases.interfaces.StreamParser
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import java.io.InputStream

class StreamParserImpl : StreamParser {

    override suspend fun parseStream(inputStream: InputStream, filter: String) = flow {
        val regex = prepareRegex(filter)
        inputStream.bufferedReader().lineSequence().asFlow().collect { line ->
            if(regex.matches(line)) emit(line)
        }
    }

    private fun prepareRegex(filter: String): Regex {
        val sb: StringBuilder = StringBuilder(filter.length * 2)
        sb.append('^')
        for(element in filter) {
            val c: Char = element
            when {
                c == '*' -> sb.append(".*")
                c == '?' -> sb.append('.')
                "\\.[]{}()+-^$|".indexOf(c) >= 0 -> {
                    sb.append('\\')
                    sb.append(c)
                }
                else -> sb.append(c)
            }
        }
        sb.append('$')

        return sb.toString().toRegex()
    }
}