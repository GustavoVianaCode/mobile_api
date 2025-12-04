package com.devmasterteam.mybooks.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Pokemon")
data class PokemonEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "sprite_url")
    val spriteUrl: String,

    @ColumnInfo(name = "shiny_sprite_url")
    val shinySpriteUrl: String,

    @ColumnInfo(name = "types")
    val types: String, // Armazenado como string separada por vírgula

    @ColumnInfo(name = "height")
    val height: Int,

    @ColumnInfo(name = "weight")
    val weight: Int,

    @ColumnInfo(name = "generation")
    val generation: Int
)
