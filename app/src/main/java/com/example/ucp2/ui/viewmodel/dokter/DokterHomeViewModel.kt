package com.example.ucp2.ui.viewmodel.dokter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pamucp2.data.entity.Dokter
import com.example.pamucp2.repository.RepositoryRs
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

class HomeDokViewModel(
    private val repositoryRs: RepositoryRs
): ViewModel() {
    val homeUiState: StateFlow<HomeUiStateDok> = repositoryRs.getAllDok()
        .filterNotNull()
        .map {
            HomeUiStateDok(
                listDok = it.toList(),
                isLoading = false,
            )
        }
        .onStart {
            emit(HomeUiStateDok(isLoading = true))
            delay(900)
        }
        .catch {
            emit(
                HomeUiStateDok(
                    isLoading = false,
                    isError = true,
                    errorMessage = it.message ?: "Terjadi Kesalahan"
                )
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = HomeUiStateDok(
                isLoading = true,
            )
        )
}

data class HomeUiStateDok(
    val listDok: List<Dokter> = listOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val errorMessage: String = ""
)