package com.example.ucp2.ui.viewmodel.jadwal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ucp2.data.entity.Dokter
import com.example.ucp2.data.entity.Jadwal
import com.example.ucp2.repository.RepositoryJadwal
import kotlinx.coroutines.launch

data class JadwalEvent(
    val idJdl: Int = 0,
    val namaDokter: String = "",
    val namaPasien: String = "",
    val noHp: String = "",
    val tanggalKonsul: String = "",
    val status: String = "",

    )

class JadwalViewModel(
    private val repositoryRs: RepositoryJadwal
) : ViewModel() {

    var uiState by mutableStateOf(JdlUIState())

    init {
        viewModelScope.launch {
            try {
                repositoryRs.getAllJadwal().collect { dokterList ->
                    uiState = uiState.copy(dokterList = dokterList)
                }
            } catch (e: Exception) {
                uiState = uiState.copy(snackBarMessage = "Gagal memuat daftar dokter")
            }
        }
    }

    fun updateState(JadwalEvent: JadwalEvent) {
        uiState = uiState.copy(
            jadwalEvent = JadwalEvent,
        )
    }

    private fun validateField(): Boolean {
        val event = uiState.jadwalEvent
        val errorState = FormErrorStateJdl(
            namaDokter = if (event.namaDokter.isNotEmpty()) null else "Nama Dokter tidak boleh kosong",
            namaPasien = if (event.namaPasien.isNotEmpty()) null else "Nama Pasien tidak boleh kosong",
            noHp = if (event.noHp.isNotEmpty()) null else "No Hp tidak boleh kosong",
            tanggalKonsul = if (event.tanggalKonsul.isNotEmpty()) null else "Tanggal Konsul tidak boleh kosong",
            status = if (event.status.isNotEmpty()) null else "Status tidak boleh kosong",
        )

        uiState = uiState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    fun saveData() {
        val currentEvent = uiState.jadwalEvent

        if (validateField()) {
            viewModelScope.launch {
                try {
                    repositoryRs.insertJadwal(currentEvent.toJadwalEntity())
                    uiState = uiState.copy(
                        snackBarMessage = "Data berhasil disimpan",
                        jadwalEvent = JadwalEvent(),
                        isEntryValid = FormErrorStateJdl()
                    )
                } catch (e: Exception) {
                    uiState = uiState.copy(
                        snackBarMessage = "Data gagal disimpan"
                    )
                }
            }
        } else {
            uiState = uiState.copy(
                snackBarMessage = "Input tidak valid. Periksa kembali data anda."
            )
        }
    }

    fun resetSnackBarMessage() {
        uiState = uiState.copy(snackBarMessage = null)
    }
}

data class JdlUIState(
    val jadwalEvent: JadwalEvent = JadwalEvent(),
    val isEntryValid: FormErrorStateJdl = FormErrorStateJdl(),
    val snackBarMessage: String? = null,
    val dokterList: List<Dokter> = emptyList()
)

data class FormErrorStateJdl(
    val namaDokter: String? = null,
    val namaPasien: String? = null,
    val noHp: String? = null,
    val tanggalKonsul: String? = null,
    val status: String? = null
) {
    fun isValid(): Boolean {
        return  namaDokter == null
                && namaPasien == null
                && noHp == null
                && tanggalKonsul == null
                && status == null
    }
}

fun JadwalEvent.toJadwalEntity(): Jadwal = Jadwal(
    id = id,
    namaDokter = namaDokter,
    namaPasien = namaPasien,
    noHP = noHp,
    tanggalKonsultasi = tanggalKonsultasi,
    status = status
)