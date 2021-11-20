package com.yml.fundo.data.room.database

import android.content.Context
import androidx.room.*
import com.yml.fundo.data.room.DateTypeConverter
import com.yml.fundo.data.room.dao.NotesDao
import com.yml.fundo.data.room.dao.OperationDao
import com.yml.fundo.data.room.dao.UserDao
import com.yml.fundo.data.room.entity.NotesEntity
import com.yml.fundo.data.room.entity.OperationEntity
import com.yml.fundo.data.room.entity.UserEntity

@Database(entities = [UserEntity::class, NotesEntity::class, OperationEntity::class],version = 1,exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class LocalDatabase: RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun notesDao(): NotesDao
    abstract fun operationDao(): OperationDao

    companion object{
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        fun getInstance(context: Context): LocalDatabase{
            val tempInstance = INSTANCE
            if(tempInstance != null){
                return tempInstance
            }
            synchronized( this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "fundo_db"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}