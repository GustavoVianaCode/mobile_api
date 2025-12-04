package com.devmasterteam.mybooks.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "TeamPokemon")
data class TeamPokemonEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "team_id")
    val teamId: Int = 0,

    @ColumnInfo(name = "pokemon_id")
    val pokemonId: Int,

    @ColumnInfo(name = "pokemon_name")
    val pokemonName: String,

    @ColumnInfo(name = "sprite_url")
    val spriteUrl: String,

    @ColumnInfo(name = "types")
    val types: String,

    @ColumnInfo(name = "added_date")
    val addedDate: Long = System.currentTimeMillis()
)
