package com.heftreng.kurdi.util

import android.content.Context
import androidx.work.*
import com.heftreng.kurdi.data.remote.NetworkModule
import java.util.concurrent.TimeUnit

class VersiyonKontrolWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return try {
            // Sadece versiyon alanını kontrol et — tam JSON indirme yok
            NetworkModule.primaryApi.indexGetir()
            // TODO: DataStore'daki yerel versiyonla karşılaştır, farklıysa cache sıfırla
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        fun planla(context: Context) {
            val istek = PeriodicWorkRequestBuilder<VersiyonKontrolWorker>(24, TimeUnit.HOURS)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                "versiyon_kontrol",
                ExistingPeriodicWorkPolicy.KEEP,
                istek
            )
        }
    }
}
