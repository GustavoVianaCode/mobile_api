package com.devmasterteam.mybooks.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.devmasterteam.mybooks.entity.PokemonEntity
import com.devmasterteam.mybooks.entity.TeamEntity
import com.devmasterteam.mybooks.entity.TeamMemberEntity
import com.devmasterteam.mybooks.entity.TeamPokemonEntity

@Database(
    entities = [
        PokemonEntity::class, 
        TeamPokemonEntity::class,
        TeamEntity::class,
        TeamMemberEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class PokemonDatabase : RoomDatabase() {
    
    abstract fun pokemonDAO(): PokemonDAO
    abstract fun teamPokemonDAO(): TeamPokemonDAO
    abstract fun teamDao(): TeamDao
    
    companion object {
        @Volatile
        private var INSTANCE: PokemonDatabase? = null
        private const val DATABASE_NAME = "pokemon_db"
        
        fun getDatabase(context: Context): PokemonDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PokemonDatabase::class.java,
                    DATABASE_NAME
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}