package com.example.ucp2.repository

import com.example.ucp2.data.entity.Dokter
import kotlinx.coroutines.flow.Flow

interface RepositoryDokter {
    suspend fun  insertDokter(dokter: Dokter)

    fun getAllDokter() : Flow<List<Dokter>>

    fun getDokter (nim: String) : Flow<Dokter>

    suspend fun  deleteDokter (dokter: Dokter)

    suspend fun updateDokter (dokter: Dokter)
}