package com.devmasterteam.mybooks.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.devmasterteam.mybooks.entity.BookEntity

@Database(entities = [BookEntity::class], version = 1, exportSchema = false)
abstract class BookDatabase: RoomDatabase() {
    abstract fun bookDAO(): BookDAO
    companion object{
        private lateinit var instace: BookDatabase
        private const val  DATABASE_NAME = "books_db"

        fun getDatabase(context: Context): BookDatabase{
            if (!::instace.isInitialized){
                synchronized(this){
                    instace = Room.databaseBuilder(context, BookDatabase::class.java, DATABASE_NAME)
                        .addMigrations(Migrations.migrationFromV1toV2)
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return instace
        }
        private object Migrations{
            val migrationFromV1toV2: Migration = object : Migration(1,2) {
                override fun migrate(db: SupportSQLiteDatabase) {
                    db.execSQL("DELETE FROM Books")
                }

            }
        }
    }
}