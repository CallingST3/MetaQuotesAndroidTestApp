package com.example.metaquotesandroidtestapp.di

import android.content.Context
import com.example.metaquotesandroidtestapp.network.MainApi
import com.example.metaquotesandroidtestapp.logic.repositories.MainRepository
import com.example.metaquotesandroidtestapp.logic.repositories.MainRepositoryImpl
import com.example.metaquotesandroidtestapp.logic.use_cases.impl.ClipboardUtilImpl
import com.example.metaquotesandroidtestapp.logic.use_cases.impl.FileLoggerImpl
import com.example.metaquotesandroidtestapp.logic.use_cases.impl.StreamParserImpl
import com.example.metaquotesandroidtestapp.logic.use_cases.interfaces.ClipboardUtils
import com.example.metaquotesandroidtestapp.logic.use_cases.interfaces.FileLogger
import com.example.metaquotesandroidtestapp.logic.use_cases.interfaces.StreamParser
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val STUB_BASE_URL = "http://localhost/"

    @Singleton
    @Provides
    fun provideApi(): MainApi = Retrofit.Builder()
        .baseUrl(STUB_BASE_URL) // У нас динамический url и мы переопределяем его аннотацией @Url, но retrofit без вызова baseUrl() не работает
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MainApi::class.java)

    @Singleton
    @Provides
    fun provideFileParser(): StreamParser = StreamParserImpl()

    @Singleton
    @Provides
    fun provideFileLogger(@ApplicationContext context: Context): FileLogger = FileLoggerImpl(context)

    @Singleton
    @Provides
    fun provideClipboardUtil(@ApplicationContext context: Context): ClipboardUtils = ClipboardUtilImpl(context)

    @Singleton
    @Provides
    fun provideMainRepository(api: MainApi): MainRepository = MainRepositoryImpl(api)
}