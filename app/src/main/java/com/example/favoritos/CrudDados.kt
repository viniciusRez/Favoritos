package com.example.favoritos

import android.content.Context

class CrudDados(val viewContext: Context) {
    private  var appdb : AppDataBase= AppDataBase.getDatabase(this.viewContext)

    fun readData(uid: Int): List<Filme> {
        val data: List<Filme> = appdb.filmeDao().loadAllByIds(uid)
        return data
    }

    fun writeData(filme: Filme): Boolean {
        val exists = appdb.filmeDao().getAll().find { filme.nome == it.nome }
        return if (exists != null) {
            false
        } else {
            appdb.filmeDao().insertAll(filme)
            true
        }
            }

    fun delete(filme:Filme) {
        appdb.filmeDao().delete(filme)
    }
}