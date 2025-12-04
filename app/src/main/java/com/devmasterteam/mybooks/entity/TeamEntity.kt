package com.devmasterteam.mybooks.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidade que representa um Time de Pokémon
 */
@Entity(tableName = "teams")
data class TeamEntity(
    @PrimaryKey(autoGenerate = true)
    val teamId: Long = 0,
    
    val teamName: String,
    
    val createdAt: Long = System.currentTimeMillis(),
    
    val isActive: Boolean = true
)
