package com.example.metaquotesandroidtestapp.logic.use_cases.interfaces

interface FileLogger {
    fun start(text1: String, text2: String)
    fun log(line: String)
    fun finish()
}