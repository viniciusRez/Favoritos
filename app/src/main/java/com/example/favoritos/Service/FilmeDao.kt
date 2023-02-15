package com.example.favoritos.Service

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.favoritos.ViewModel.FilmeModel

@Dao
 interface FilmeDao {
     @Query("SELECT * FROM FilmeModel")
     fun getAll():List<FilmeModel>

     @Query("SELECT * FROM FilmeModel WHERE uid IN (:userIds)")
      fun loadAllByIds(userIds:Int): List<FilmeModel>

     @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insertAll(vararg filmeModel: FilmeModel)

     @Delete()
      fun delete(filmeModel: FilmeModel)

 }