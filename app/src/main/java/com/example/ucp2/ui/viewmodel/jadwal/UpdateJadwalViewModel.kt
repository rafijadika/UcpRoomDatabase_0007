package com.example.ucp2.ui.viewmodel.jadwal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pamucp2.data.entity.Jadwal
import com.example.pamucp2.repository.RepositoryRs
import com.example.pamucp2.ui.navigation.DestinasiUpdate
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class UpdateJdlViewModel (
    savedStateHandle: SavedStateHandle,
    private val repositoryRs: RepositoryRs
) : ViewModel(){
    var updateUIState by mutableStateOf(JdlUIState())
        private set

    private val _idJdl: Int = checkNotNull(savedStateHandle[DestinasiUpdate.IDJDL])

    init {
        viewModelScope.launch {
            try {
                val dokterList = repositoryRs.getAllDok().first()
                updateUIState = repositoryRs.getJdl(_idJdl)
                    .filterNotNull()
                    .first()
                    .toUIStateJdl()
                    .copy(dokterList = dokterList)
            } catch (e: Exception) {
            }
        }
    }

    fun updateState(jadwalEvent: JadwalEvent) {
        updateUIState = updateUIState.copy(
            jadwalEvent = jadwalEvent,
        )
    }

    fun validateFields(): Boolean {
        val event = updateUIState.jadwalEvent
        val errorState = FormErrorStateJdl(
            namaDokter = if (event.namaDokter.isNotEmpty()) null else "Nama Dokter tidak boleh kosong",
            namaPasien = if (event.namaPasien.isNotEmpty()) null else "Nama Pasien tidak boleh kosong",
            noHp = if (event.noHp.isNotEmpty()) null else "noHp tidak boleh kosong",
            tanggalKonsul = if (event.tanggalKonsul.isNotEmpty()) null else "tanggalKonsul tidak boleh kosong",
            status = if (event.status.isNotEmpty()) null else "Status tidak boleh kosong",

            )
        updateUIState = updateUIState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    fun updateData() {
        val currentEvent = updateUIState.jadwalEvent

        if (validateFields()) {
            viewModelScope.launch {
                try {
                    repositoryRs.updateJdl(currentEvent.toJadwalEntity())
                    updateUIState = updateUIState.copy(
                        snackBarMessage = "Data berhasil diupdate",
                        jadwalEvent = JadwalEvent(),
                        isEntryValid = FormErrorStateJdl()
                    )
                    println("snackBarMessage diatur: ${updateUIState.
                    snackBarMessage}")
                } catch (e: Exception) {
                    updateUIState = updateUIState.copy(
                        snackBarMessage = "Data gagal diupdate"
                    )
                }
            }
        } else {
            updateUIState = updateUIState.copy(
                snackBarMessage = "Data gagal diupdate"
            )
        }
    }

    fun resetSnackBarMessage() {
        updateUIState = updateUIState.copy(snackBarMessage = null)
    }
}

fun Jadwal.toUIStateJdl() : JdlUIState = JdlUIState(
    jadwalEvent = this.toDetailUiEvent(),
)