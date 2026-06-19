package com.heftreng.kurdi.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.heftreng.kurdi.data.local.IlerlemeEntity
import com.heftreng.kurdi.data.local.KurdiDatabase
import com.heftreng.kurdi.data.local.TercihYoneticisi
import com.heftreng.kurdi.data.model.UniteMeta
import com.heftreng.kurdi.data.remote.NetworkModule
import com.heftreng.kurdi.data.repository.KurdiRepository
import com.heftreng.kurdi.util.AppLanguage
import com.heftreng.kurdi.util.LearningMode
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val yukleniyorMu: Boolean = true,
    val yenileniyorMu: Boolean = false,
    val hataMesaji: String? = null,
    val uniteler: List<UniteMeta> = emptyList(),
    val ilerlemeMap: Map<String, IlerlemeEntity> = emptyMap(),
    val learningMode: LearningMode = LearningMode.KURMANCI_TO_SORANI,
    val appLanguage: AppLanguage = AppLanguage.TR
)

class HomeViewModel(app: Application) : AndroidViewModel(app) {

    private val db         = KurdiDatabase.getInstance(app)
    private val tercih     = TercihYoneticisi(app)
    private val repository = KurdiRepository(
        NetworkModule.primaryApi,
        NetworkModule.fallbackApi,
        db.ilerlemeDao()
    )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        // İlerleme, öğrenme modu ve arayüz dili akışlarını birleştir
        viewModelScope.launch {
            combine(
                db.ilerlemeDao().hepsiniAl(),
                tercih.learningModeAkisi,
                tercih.appLanguageAkisi
            ) { ilerlemeList, mode, lang ->
                Triple(ilerlemeList.associateBy { it.unitId }, mode, lang)
            }.collect { (ilerlemeMap, mode, lang) ->
                _uiState.update { it.copy(ilerlemeMap = ilerlemeMap, learningMode = mode, appLanguage = lang) }
            }
        }
        uniteleriYukle()
    }

    fun uniteleriYukle() {
        viewModelScope.launch {
            _uiState.update { it.copy(yukleniyorMu = true, hataMesaji = null) }
            repository.indexGetir()
                .onSuccess { index ->
                    _uiState.update { it.copy(yukleniyorMu = false, uniteler = index.units) }
                }
                .onFailure { hata ->
                    _uiState.update { it.copy(yukleniyorMu = false, hataMesaji = hata.message) }
                }
        }
    }

    // Pull-to-refresh: cache'i temizle, yeni veri çek
    fun yenile() {
        viewModelScope.launch {
            _uiState.update { it.copy(yenileniyorMu = true, hataMesaji = null) }
            repository.cacheSifirla()
            repository.indexGetir()
                .onSuccess { index ->
                    _uiState.update { it.copy(yenileniyorMu = false, uniteler = index.units) }
                }
                .onFailure { hata ->
                    _uiState.update { it.copy(yenileniyorMu = false, hataMesaji = hata.message) }
                }
        }
    }
}
