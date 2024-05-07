package org.d3if3095.mobpro1_assessment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "mobil")
data class Mobil(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val merek: String,
    val tipe: String,
    val harga: String,
    val spesifikasi: String
)
