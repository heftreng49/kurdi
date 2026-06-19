package com.heftreng.kurdi.data.repository

import com.heftreng.kurdi.data.local.IlerlemeDao
import com.heftreng.kurdi.data.local.IlerlemeEntity
import com.heftreng.kurdi.data.model.IndexResponse
import com.heftreng.kurdi.data.model.UniteDetay
import com.heftreng.kurdi.data.remote.KurdiApiService

class KurdiRepository(
    private val primaryApi:  KurdiApiService,
    private val fallbackApi: KurdiApiService,
    private val ilerlemeDao: IlerlemeDao
) {
    // Bellek cache — uygulama oturumu boyunca GitHub'a tekrar istek atılmaz
    private var indexCache: IndexResponse? = null
    private var indexCacheTime: Long = 0L
    private val uniteCache = mutableMapOf<String, UniteDetay>()

    // Birincil başarısız olursa yedek API'yi dene
    private suspend fun <T> withFallback(block: suspend (KurdiApiService) -> T): T =
        try { block(primaryApi) } catch (e: Exception) { block(fallbackApi) }

    // Pull-to-refresh: cache'i sıfırla, bir sonraki indexGetir() çağrısı yeni veri çeker
    fun cacheSifirla() {
        indexCache = null
        indexCacheTime = 0L
        uniteCache.clear()
    }

    suspend fun indexGetir(): Result<IndexResponse> = runCatching {
        val now = System.currentTimeMillis()
        // 24 saatlik cache — 60 istek/saat rate limitine karşı koruma
        if (indexCache != null && now - indexCacheTime < 86_400_000L) {
            return@runCatching indexCache!!
        }
        withFallback { it.indexGetir() }.also {
            indexCache = it
            indexCacheTime = now
        }
    }

    suspend fun uniteGetir(dosya: String): Result<UniteDetay> = runCatching {
        uniteCache.getOrPut(dosya) { withFallback { it.uniteGetir(dosya) } }
    }

    fun ilerlemeAkisi()                              = ilerlemeDao.hepsiniAl()
    suspend fun ilerlemeGetir(id: String)            = ilerlemeDao.getir(id)
    suspend fun ilerlemeKaydet(e: IlerlemeEntity)    = ilerlemeDao.kaydet(e)
}
