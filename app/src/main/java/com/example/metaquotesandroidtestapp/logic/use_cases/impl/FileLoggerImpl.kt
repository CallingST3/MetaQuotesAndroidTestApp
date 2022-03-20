package com.example.metaquotesandroidtestapp.logic.use_cases.impl

import android.content.Context
import com.example.metaquotesandroidtestapp.logic.use_cases.interfaces.FileLogger
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

class FileLoggerImpl @Inject constructor(
    @ApplicationContext private val context: Context
    ) : FileLogger {

    private val FILE_NAME = "results.log"
    private var file: File? = null

    override fun start(text1: String, text2: String) {
        file = File(context.filesDir, FILE_NAME)
        file?.apply {
            createNewFile()
            appendText(
                "\n///" +
                "\n/// New logging task" +
                "\n/// $text1" +
                "\n/// $text2" +
                "\n///" +
                "\n"
            )
        }
    }

    override fun log(line: String) {
        file?.appendText("$line\n")
    }

    override fun finish() {
        file?.appendText(
            "///" +
            "\n/// finished" +
            "\n///" +
            "\n"
        )
    }
}