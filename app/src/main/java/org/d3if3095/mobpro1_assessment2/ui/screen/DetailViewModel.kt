package org.d3if3095.mobpro1_assessment2.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3095.mobpro1_assessment2.database.MobilDao
import org.d3if3095.mobpro1_assessment2.model.Mobil

class DetailViewModel(private val dao: MobilDao) : ViewModel() {

    fun insert(merek: String, tipe: String, spesifikasi: String, harga: String) {
        val mobil = Mobil(
            merek = merek,
            tipe = tipe,
            spesifikasi = spesifikasi,
            harga = harga
        )

        viewModelScope.launch(Dispatchers.IO) {
            dao.insert(mobil)
        }
    }

    suspend fun getMobil(id: Long): Mobil? {
        return dao.getMobilById(id)
    }

    fun update(id: Long, merek: String, tipe: String, spesifikasi: String, harga: String) {
        val mobil = Mobil(
            id = id,
            merek = merek,
            tipe = tipe,
            spesifikasi = spesifikasi,
            harga = harga
        )
        viewModelScope.launch(Dispatchers.IO) {
            dao.update(mobil)
        }
    }

    fun delete(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteById(id)
        }
    }
}