package com.heftreng.kurdi.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.heftreng.kurdi.data.local.IlerlemeEntity
import com.heftreng.kurdi.data.local.KurdiDatabase
import com.heftreng.kurdi.data.local.TercihYoneticisi
import com.heftreng.kurdi.data.local.tercihDataStore
import com.heftreng.kurdi.data.model.UniteMeta
import com.heftreng.kurdi.data.remote.NetworkModule
import com.heftreng.kurdi.data.repository.KurdiRepository
import com.heftreng.kurdi.util.LearningMode
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class HomeUiState(
    val yukleniyorMu: Boolean = true,
    val hataMesaji: String? = null,
    val uniteler: List<UniteMeta> = emptyList(),
    val ilerlemeMap: Map<String, IlerlemeEntity> = emptyMap(),
    val learningMode: LearningMode = LearningMode.KURMANCI_TO_SORANI
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
        // İlerleme ve mod akışlarını birleştir
        viewModelScope.launch {
            combine(
                db.ilerlemeDao().hepsiniAl(),
                tercih.learningModeAkisi
            ) { ilerlemeList, mode ->
                ilerlemeList.associateBy { it.unitId } to mode
            }.collect { (ilerlemeMap, mode) ->
                _uiState.update { it.copy(ilerlemeMap = ilerlemeMap, learningMode = mode) }
            }
        }
        uniteleriYukle()
    }

    fun uniteleriYukle() {
        viewModelScope.launch {
            _uiState.update { it.copy(yukleniyorMu = true, hataMesaji = null) }
            repository.indexGetir()
                .onSuccess  { index ->
                    _uiState.update { it.copy(yukleniyorMu = false, uniteler = index.units) }
                }
                .onFailure  { hata ->
                    _uiState.update { it.copy(yukleniyorMu = false, hataMesaji = hata.message) }
                }
        }
    }
}
