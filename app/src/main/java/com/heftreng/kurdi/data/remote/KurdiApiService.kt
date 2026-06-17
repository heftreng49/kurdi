package com.heftreng.kurdi.data.remote

import com.heftreng.kurdi.data.model.IndexResponse
import com.heftreng.kurdi.data.model.UniteDetay
import retrofit2.http.GET
import retrofit2.http.Path

interface KurdiApiService {
    @GET("data/index.json")
    suspend fun indexGetir(): IndexResponse

    @GET("data/units/{dosya}")
    suspend fun uniteGetir(@Path("dosya") dosya: String): UniteDetay
}
