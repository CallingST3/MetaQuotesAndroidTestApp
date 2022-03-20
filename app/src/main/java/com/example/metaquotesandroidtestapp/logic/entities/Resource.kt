package com.example.metaquotesandroidtestapp.logic.entities

sealed class Resource<T> (val data: T?, val errorMessage: String?) {
    class Success<T>(data: T) : Resource<T>(data, null)
    class Error<T>(errorMessage: String?) : Resource<T>(null, errorMessage)
}