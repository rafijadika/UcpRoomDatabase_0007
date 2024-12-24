package com.example.ucp2.ui.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.ucp2.RumahSakitApp
import com.example.ucp2.ui.view.dokter.HomeDokterView
import com.example.ucp2.ui.viewmodel.dokter.DokterViewModel
import com.example.ucp2.ui.viewmodel.jadwal.JadwalViewModel

object PenyediaViewModel {
    val Factory = viewModelFactory {
        initializer {
            DokterViewModel(
                RumahSakitApp().containerApp.repositoryDokter
            )
        }
        initializer {
            HomeDokterView()(
                RumahSakitApp().containerApp.repositoryDokter
            )
        }
        initializer {
            JadwalViewModel(
                RumahSakitApp().containerApp.repositoryJadwal
            )
        }
        initializer {
            HomeJdlViewModel(
                rsApp().containerApp.repositoryRs
            )
        }
        initializer {
            DetailJdlViewModel(
                createSavedStateHandle(),
                rsApp().containerApp.repositoryRs
            )
        }
        initializer {
            UpdateJdlViewModel(
                createSavedStateHandle(),
                rsApp().containerApp.repositoryRs
            )
        }
    }
}

fun CreationExtras.RumahSakitApp(): RumahSakitApp =