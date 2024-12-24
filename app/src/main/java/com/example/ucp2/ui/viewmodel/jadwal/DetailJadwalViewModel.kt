package com.example.ucp2.ui.viewmodel.jadwal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.repository.RepositoryJadwal
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DetailJadwalViewModel (
    savedStateHandle: SavedStateHandle,
    private val repositoryJadwal: RepositoryJadwal,
) : ViewModel(){
    private val _idJdl: Int = checkNotNull(savedStateHandle[DestinasiDetail.IDJDL])

    val detailUiState: StateFlow<DetailUiState> = repositoryJadwal.getJdl(_idJdl)
        .filterNotNull()
        .map {
            DetailUiState(
                detailUiEvent = it.toDetailUiEvent(),
                isLoading = false
            )
        }
        .onStart {
            emit(DetailUiState(isLoading = true))
            delay(600)
        }
        .catch {
            emit(
                DetailUiState(
                    isLoading = false,
                    isError = true,
                    errorMessage = it.message ?: "Terjadi Kesalahan",
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = DetailUiState (
                isLoading = true,
            ),
        )

    fun deteleJdl() {
        detailUiState.value.detailUiEvent.toJadwalEntity().let {
            viewModelScope.launch {
                repositoryRs.deleteJdl(it)
            }
        }
    }
}

data class DetailUiState(
    val detailUiEvent: JadwalEvent = JadwalEvent(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
) {
    val isUiEventEmpty: Boolean
        get() = detailUiEvent == JadwalEvent()

    val isUiEventNotEmpty: Boolean
        get() = detailUiEvent != JadwalEvent()
}

fun Jadwal.toDetailUiEvent() : JadwalEvent {
    return JadwalEvent(
        idJdl = idJdl,
        namaDokter = namaDokter,
        namaPasien = namaPasien,
        noHp = noHp,
        tanggalKonsul = tanggalKonsul,
        status = status
    )
}