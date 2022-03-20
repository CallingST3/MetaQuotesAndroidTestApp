package com.example.metaquotesandroidtestapp.extensions

import android.view.View

object Extensions {

    fun View.invisible(invisible: Boolean) {
        visibility = if(invisible) View.INVISIBLE else View.VISIBLE
    }

}