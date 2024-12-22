package com.example.ucp2.repository

import com.example.ucp2.data.entity.Dokter
import kotlinx.coroutines.flow.Flow

interface RepositoryDokter {
    fun getAllDokter() : Flow<List<Dokter>>

    fun getDokter (id: String) : Flow<Dokter>

    suspend fun createDokter(dokter: Dokter)
}