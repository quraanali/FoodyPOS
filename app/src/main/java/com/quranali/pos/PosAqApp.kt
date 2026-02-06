package com.quranali.pos

import android.app.Application
import com.quranali.pos.di.initKoin

class PosAqApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin(this@PosAqApp)
    }
}
