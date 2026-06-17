package com.heftreng.kurdi.ui.lesson

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.heftreng.kurdi.data.model.Soru
import com.heftreng.kurdi.util.LearningMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreen(
    uniteDosya: String,
    onBitti: (puan: Int, yildiz: Int) -> Unit,
    onGeri: () -> Unit,
    vm: LessonViewModel = viewModel()
) {
    val state by vm.uiState.collectAsState()

    LaunchedEffect(uniteDosya) { vm.uniteYukle(uniteDosya) }

    // Ders bittiğinde sonuç ekranına yönlendir
    LaunchedEffect(state.bitti) {
        if (state.bitti) {
            val soruSayisi = state.adimlar.count { it is DersAdimi.SoruAdimi }
            val yildiz = when {
                state.dogruSayisi >= soruSayisi                    -> 3
                state.dogruSayisi >= (soruSayisi * 0.6).toInt()   -> 2
                state.dogruSayisi > 0                              -> 1
                else                                               -> 0
            }
            onBitti(state.dogruSayisi, yildiz)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    LinearProgressIndicator(
                        progress    = { state.ilerlemeYuzdesi },
                        modifier    = Modifier.fillMaxWidth().height(8.dp),
                        trackColor  = MaterialTheme.colorScheme.surfaceVariant
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onGeri) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Geri")
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding).padding(16.dp)) {
            when {
                state.yukleniyorMu -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                state.hataMesaji != null -> Column(
                    Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Ünite yüklenemedi")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { vm.uniteYukle(uniteDosya) }) { Text("Tekrar Dene") }
                }

                state.mevcutAdim != null -> AdimIcerigi(
                    adim        = state.mevcutAdim!!,
                    mode        = state.learningMode,
                    onDogru     = vm::dogruCevap,
                    onYanlis    = vm::yanlisCevap,
                    onDevam     = vm::sonrakiAdim
                )
            }
        }
    }
}

@Composable
private fun AdimIcerigi(
    adim: DersAdimi,
    mode: LearningMode,
    onDogru: () -> Unit,
    onYanlis: () -> Unit,
    onDevam: () -> Unit
) {
    when (adim) {
        is DersAdimi.VocabAdimi -> VocabKart(adim.kelime, mode, onDevam)
        is DersAdimi.SoruAdimi  -> SoruIcerigi(adim.soru, mode, onDogru, onYanlis)
    }
}

@Composable
private fun VocabKart(
    kelime: com.heftreng.kurdi.data.model.Kelime,
    mode: LearningMode,
    onDevam: () -> Unit
) {
    val anaKelime = if (mode.isKurmanci()) kelime.kurmanciLatin else kelime.soraniArami
    val ceviriKelime = if (mode.isKurmanci()) kelime.soraniArami else kelime.kurmanciLatin
    val phoneticAna = if (mode.isKurmanci()) kelime.phoneticKurmanci else kelime.phoneticSorani
    val aciklama = if (mode.isKurmanci()) kelime.description.kurmanci else kelime.description.sorani

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                Modifier.padding(24.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(anaKelime, style = MaterialTheme.typography.displaySmall)
                Spacer(Modifier.height(4.dp))
                Text("[$phoneticAna]", style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                HorizontalDivider(Modifier.padding(vertical = 12.dp))
                Text(ceviriKelime, style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary)
                Spacer(Modifier.height(4.dp))
                Text(kelime.meaningTr, style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                Spacer(Modifier.height(12.dp))
                Text(aciklama, style = MaterialTheme.typography.bodySmall)
            }
        }
        Spacer(Modifier.height(32.dp))
        Button(onClick = onDevam, modifier = Modifier.fillMaxWidth()) {
            Text("Devam →")
        }
    }
}

@Composable
private fun SoruIcerigi(
    soru: Soru,
    mode: LearningMode,
    onDogru: () -> Unit,
    onYanlis: () -> Unit
) {
    val yonege = if (mode.isKurmanci()) soru.yonege.kurmanci else soru.yonege.sorani

    Column(Modifier.fillMaxSize()) {
        Text(yonege, style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(24.dp))

        when (soru) {
            is Soru.MultipleChoice  -> MultipleChoiceView(soru, mode, onDogru, onYanlis)
            is Soru.TrueFalse       -> TrueFalseView(soru, mode, onDogru, onYanlis)
            is Soru.FillInTheBlank  -> FillBlankView(soru, mode, onDogru, onYanlis)
            is Soru.WordMatching    -> WordMatchingView(soru, mode, onDogru)
        }
    }
}

// ── Soru tipleri ─────────────────────────────────────────────────────────────

@Composable
fun MultipleChoiceView(
    soru: Soru.MultipleChoice,
    mode: LearningMode,
    onDogru: () -> Unit,
    onYanlis: () -> Unit
) {
    val hedef    = if (mode.isKurmanci()) soru.targetWord.kurmanciUserSees else soru.targetWord.soraniUserSees
    val secenekler = if (mode.isKurmanci()) soru.options.forKurmanciUser else soru.options.forSoraniUser
    val dogruIdx = if (mode.isKurmanci()) soru.correctIndex.kurmanci else soru.correctIndex.sorani

    var secili by remember { mutableStateOf<Int?>(null) }

    Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
        Text(hedef, style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally))
    }
    Spacer(Modifier.height(16.dp))

    secenekler.forEachIndexed { index, secenek ->
        val renk = when {
            secili == null                     -> ButtonDefaults.outlinedButtonColors()
            index == dogruIdx                  -> ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            index == secili                    -> ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            else                               -> ButtonDefaults.outlinedButtonColors()
        }
        OutlinedButton(
            onClick = {
                if (secili == null) {
                    secili = index
                    if (index == dogruIdx) onDogru() else onYanlis()
                }
            },
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            colors = renk
        ) { Text(secenek) }
    }
}

@Composable
fun TrueFalseView(
    soru: Soru.TrueFalse,
    mode: LearningMode,
    onDogru: () -> Unit,
    onYanlis: () -> Unit
) {
    val ifade = if (mode.isKurmanci()) soru.statement.kurmanci else soru.statement.sorani

    Card(Modifier.fillMaxWidth(), elevation = CardDefaults.cardElevation(2.dp)) {
        Text(ifade, style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(16.dp))
    }
    Spacer(Modifier.height(24.dp))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Button(
            onClick = { if (soru.correctAnswer) onDogru() else onYanlis() },
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) { Text("✓ Doğru") }
        OutlinedButton(
            onClick = { if (!soru.correctAnswer) onDogru() else onYanlis() },
            modifier = Modifier.weight(1f)
        ) { Text("✗ Yanlış") }
    }
}

@Composable
fun FillBlankView(
    soru: Soru.FillInTheBlank,
    mode: LearningMode,
    onDogru: () -> Unit,
    onYanlis: () -> Unit
) {
    val cumle  = if (mode.isKurmanci()) soru.sentence.kurmanci else soru.sentence.sorani
    val cevap  = if (mode.isKurmanci()) soru.correctBlank.kurmanci else soru.correctBlank.sorani

    var girdi by remember { mutableStateOf("") }
    var kontrol by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(cumle.prefix, style = MaterialTheme.typography.bodyLarge)
        OutlinedTextField(
            value = girdi,
            onValueChange = { if (!kontrol) girdi = it },
            modifier = Modifier.width(120.dp).padding(horizontal = 4.dp),
            singleLine = true
        )
        Text(cumle.suffix, style = MaterialTheme.typography.bodyLarge)
    }
    Spacer(Modifier.height(16.dp))
    Button(
        onClick = {
            kontrol = true
            if (girdi.trim().equals(cevap.trim(), ignoreCase = true)) onDogru() else onYanlis()
        },
        modifier = Modifier.fillMaxWidth(),
        enabled = !kontrol
    ) { Text("Kontrol Et") }
}

@Composable
fun WordMatchingView(
    soru: Soru.WordMatching,
    mode: LearningMode,
    onBitti: () -> Unit
) {
    var eslesmeler by remember { mutableStateOf(0) }
    val toplam = soru.pairs.size

    Text("${eslesmeler}/${toplam} eşleşti", style = MaterialTheme.typography.labelLarge)
    Spacer(Modifier.height(12.dp))

    soru.pairs.forEach { pair ->
        val sol = if (mode.isKurmanci()) pair.kurmanci else pair.sorani
        val sag = if (mode.isKurmanci()) pair.sorani   else pair.kurmanci
        Row(Modifier.fillMaxWidth().padding(vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Card(Modifier.weight(1f).padding(end = 4.dp)) {
                Text(sol, Modifier.padding(12.dp))
            }
            Card(Modifier.weight(1f).padding(start = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)) {
                Text(sag, Modifier.padding(12.dp))
            }
        }
    }

    Spacer(Modifier.height(16.dp))
    Button(onClick = onBitti, Modifier.fillMaxWidth()) { Text("Tamamlandı →") }
}
