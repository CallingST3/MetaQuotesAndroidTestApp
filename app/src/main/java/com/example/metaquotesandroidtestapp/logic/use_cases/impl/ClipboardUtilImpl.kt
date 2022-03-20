package com.example.metaquotesandroidtestapp.logic.use_cases.impl

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import com.example.metaquotesandroidtestapp.logic.use_cases.interfaces.ClipboardUtils

class ClipboardUtilImpl(private val context: Context) : ClipboardUtils {

    override fun copy(text: String) {
        (context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager?)?.apply {
            setPrimaryClip(ClipData.newPlainText("label", text))
        }
    }
}