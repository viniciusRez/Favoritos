package com.example.favoritos.Service

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.favoritos.ViewModel.FilmeModel

@Database(entities = [FilmeModel :: class], version = 3)
abstract class AppDataBase: RoomDatabase(){
    abstract fun filmeDao() : FilmeDao

    companion object {
        @Volatile
        private var INSTACE: AppDataBase? = null

        fun getDatabase(context: Context): AppDataBase {
            val tempInstance = INSTACE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(context, AppDataBase::class.java, "FilmeModel").fallbackToDestructiveMigration().allowMainThreadQueries().build()
                INSTACE = instance
                return instance
            }
        }
    }

}