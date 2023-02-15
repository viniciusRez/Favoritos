package com.example.favoritos.ViewModel

import android.content.Context
import com.example.favoritos.Service.AppDataBase

class CrudViewModel(val viewContext: Context) {
    private  var appdb : AppDataBase = AppDataBase.getDatabase(this.viewContext)

    fun readData(uid: Int): List<FilmeModel> {
        val data: List<FilmeModel> = appdb.filmeDao().loadAllByIds(uid)
        return data
    }

    fun writeData(filmeModel: FilmeModel): Boolean {
        val exists = appdb.filmeDao().getAll().find { filmeModel.nome == it.nome }
        return if (exists != null) {
            false
        } else {
            appdb.filmeDao().insertAll(filmeModel)
            true
        }
            }

    fun delete(filmeModel: FilmeModel) {
        appdb.filmeDao().delete(filmeModel)
    }
}