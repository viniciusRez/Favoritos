package com.example.favoritos

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
 interface FilmeDao {
     @Query("SELECT * FROM Filme")
     fun getAll():List<Filme>

     @Query("SELECT * FROM Filme WHERE uid IN (:userIds)")
      fun loadAllByIds(userIds:Int): List<Filme>

     @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insertAll(vararg filme: Filme)

     @Delete()
      fun delete(filme: Filme)

 }