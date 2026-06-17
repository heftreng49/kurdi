package com.heftreng.kurdi

import android.app.Application
import com.heftreng.kurdi.util.VersiyonKontrolWorker

class KurdiApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        VersiyonKontrolWorker.planla(this)
    }
}
