package com.mangesh.sugerbox

import android.app.Application
import android.content.Context

class RestaurantApplication:Application() {

    init {
        instance=this
    }

    companion object {
        private var instance: RestaurantApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }
    }
}