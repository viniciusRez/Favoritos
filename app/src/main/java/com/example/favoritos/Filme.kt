package com.example.favoritos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Filme")
data class Filme(
    @PrimaryKey(autoGenerate = true) val uid:Int?,
    @ColumnInfo(name="nome") val nome: String,
    @ColumnInfo(name="duracao") val duracao: String?,
    @ColumnInfo(name="avaliacao") val avaliacao: Double?,
    @ColumnInfo(name="status") val status: String?,
    @ColumnInfo(name="capa") val capa: ByteArray?,

)