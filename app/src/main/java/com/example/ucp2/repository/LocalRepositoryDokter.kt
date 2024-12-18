package com.example.ucp2.repository

import com.example.ucp2.data.dao.DokterDao
import com.example.ucp2.data.entity.Dokter
import kotlinx.coroutines.flow.Flow

class LocalRepositoryDokter (
    private val dokterDao: DokterDao
):RepositoryDokter{
    override suspend fun  insertDokter(dokter: Dokter){
        dokterDao.insertDokter(dokter)
    }

    //get all mahasiswa
    override fun getAllDokter(): Flow<List<Dokter>> {
        return dokterDao.getAllDokter()
    }

    override fun getDokter(id: String): Flow<Dokter> {  //mengambil data mahasiswa berdasarkan nim
        return dokterDao.getDokter(id)
    }

    override suspend fun deleteDokter(dokter: Dokter) { //menghapus data mahasiswa
        dokterDao.deleteDokter(dokter)
    }

    override suspend fun updateDokter(dokter: Dokter) { //memperbarui data mahasiswa dalam database
        dokterDao.updateDokter(dokter)
    }
}