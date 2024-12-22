package com.example.ucp2.repository

import com.example.ucp2.data.dao.DokterDao
import com.example.ucp2.data.entity.Dokter
import kotlinx.coroutines.flow.Flow

class LocalRepositoryDokter (
    private val dokterDao: DokterDao
): RepositoryDokter {

    //get all dokter
    override fun getAllDokter(): Flow<List<Dokter>> {
        return dokterDao.getAllDokter()
    }

    override fun getDokter(id: String): Flow<Dokter> {
        return dokterDao.getDokter(id)
    }

    override suspend fun createDokter(dokter: Dokter) {
        dokterDao.insertDokter(dokter)
    }
}