package com.example.metaquotesandroidtestapp.logic.viewmodels

import android.util.SparseBooleanArray
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.metaquotesandroidtestapp.logic.use_cases.interfaces.FileLogger
import com.example.metaquotesandroidtestapp.logic.use_cases.interfaces.StreamParser
import com.example.metaquotesandroidtestapp.logic.entities.Resource
import com.example.metaquotesandroidtestapp.logic.use_cases.interfaces.ClipboardUtils
import com.example.metaquotesandroidtestapp.logic.repositories.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import java.lang.StringBuilder
import javax.inject.Inject

@HiltViewModel class MainViewModel @Inject constructor(
    private val repository: MainRepository,
    private val streamParser: StreamParser,
    private val fileLogger: FileLogger,
    private val clipboardUtil: ClipboardUtils
    ) : ViewModel() {

    sealed class Event {
        class Result(val result: String) : Event()
        class Failure(val errorText: String?) : Event()
        object Complete : Event()
        object Loading : Event()
    }

    private var job: Job? = null
    private val _source = MutableSharedFlow<Event>()
    val source: MutableSharedFlow<Event> = _source

    fun loadData(fileUrl: String, filter: String) {
        if(job?.isActive == true) {
            job?.cancel()
            return
        }

        job = viewModelScope.launch(Dispatchers.IO) {
            _source.emit(Event.Loading)
            fileLogger.start(fileUrl, filter)
            when(val response = repository.loadFileStream(fileUrl)) {
                is Resource.Success -> {
                    streamParser.parseStream(response.data!!, filter).collect { result ->
                        _source.emit(Event.Result(result))
                        fileLogger.log(result)
                    }
                }
                is Resource.Error -> _source.emit(Event.Failure(response.errorMessage))
            }
        }
        job?.invokeOnCompletion {
            viewModelScope.launch(Dispatchers.Main) {
                _source.emit(Event.Complete)
                fileLogger.finish()
            }
        }
    }

    fun copy(selectedItems: SparseBooleanArray, adapter: ArrayAdapter<String>) {
        val sb = StringBuilder()
        for(i in 0 until selectedItems.size()) {
            val key = selectedItems.keyAt(i)
            if(selectedItems.get(key))
                adapter.getItem(key)?.let {
                    sb.append(it)
                }
        }
        clipboardUtil.copy(sb.toString())
    }
}