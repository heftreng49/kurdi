package com.heftreng.kurdi.ui.lesson

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.heftreng.kurdi.data.local.IlerlemeEntity
import com.heftreng.kurdi.data.local.KurdiDatabase
import com.heftreng.kurdi.data.local.TercihYoneticisi
import com.heftreng.kurdi.data.model.Kelime
import com.heftreng.kurdi.data.model.Soru
import com.heftreng.kurdi.data.remote.NetworkModule
import com.heftreng.kurdi.data.repository.KurdiRepository
import com.heftreng.kurdi.util.LearningMode
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

sealed class DersAdimi {
    data class VocabAdimi(val kelime: Kelime) : DersAdimi()
    data class SoruAdimi(val soru: Soru) : DersAdimi()
}

data class LessonUiState(
    val yukleniyorMu: Boolean = true,
    val hataMesaji: String? = null,
    val adimlar: List<DersAdimi> = emptyList(),
    val mevcutIndex: Int = 0,
    val dogruSayisi: Int = 0,
    val learningMode: LearningMode = LearningMode.KURMANCI_TO_SORANI,
    val bitti: Boolean = false
) {
    val mevcutAdim: DersAdimi? get() = adimlar.getOrNull(mevcutIndex)
    val toplamAdim: Int get() = adimlar.size
    val ilerlemeYuzdesi: Float get() =
        if (toplamAdim == 0) 0f else mevcutIndex.toFloat() / toplamAdim
}

class LessonViewModel(app: Application) : AndroidViewModel(app) {

    private val db         = KurdiDatabase.getInstance(app)
    private val tercih     = TercihYoneticisi(app)
    private val repository = KurdiRepository(
        NetworkModule.primaryApi,
        NetworkModule.fallbackApi,
        db.ilerlemeDao()
    )

    private val _uiState = MutableStateFlow(LessonUiState())
    val uiState: StateFlow<LessonUiState> = _uiState.asStateFlow()

    private var uniteDosya: String = ""

    fun uniteYukle(dosya: String) {
        uniteDosya = dosya
        viewModelScope.launch {
            val mode = tercih.learningModeAkisi.first()
            _uiState.update { it.copy(yukleniyorMu = true, learningMode = mode) }

            repository.uniteGetir(dosya)
                .onSuccess { unite ->
                    // Önce vocab kartları, sonra sorular
                    val adimlar: List<DersAdimi> =
                        unite.vocabulary.map { DersAdimi.VocabAdimi(it) } +
                        unite.questions.map  { DersAdimi.SoruAdimi(it)  }
                    _uiState.update { it.copy(yukleniyorMu = false, adimlar = adimlar) }
                }
                .onFailure { hata ->
                    _uiState.update { it.copy(yukleniyorMu = false, hataMesaji = hata.message) }
                }
        }
    }

    fun dogruCevap() {
        _uiState.update { it.copy(dogruSayisi = it.dogruSayisi + 1) }
        sonrakiAdim()
    }

    fun yanlisCevap() = sonrakiAdim()

    fun sonrakiAdim() {
        val state = _uiState.value
        if (state.mevcutIndex >= state.toplamAdim - 1) {
            dersBitti()
        } else {
            _uiState.update { it.copy(mevcutIndex = it.mevcutIndex + 1) }
        }
    }

    private fun dersBitti() {
        viewModelScope.launch {
            val state  = _uiState.value
            val soruSayisi = state.adimlar.count { it is DersAdimi.SoruAdimi }
            val yildiz = when {
                state.dogruSayisi >= soruSayisi             -> 3
                state.dogruSayisi >= (soruSayisi * 0.6).toInt() -> 2
                state.dogruSayisi > 0                       -> 1
                else                                        -> 0
            }
            repository.ilerlemeKaydet(
                IlerlemeEntity(
                    unitId        = uniteDosya.removeSuffix(".json"),
                    tamamlandiMi  = true,
                    puan          = state.dogruSayisi,
                    yildiz        = yildiz
                )
            )
            _uiState.update { it.copy(bitti = true) }
        }
    }
}
