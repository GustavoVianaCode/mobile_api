package com.devmasterteam.mybooks.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Entidade que representa um membro de um time
 * Um time pode ter no máximo 6 Pokémon
 */
@Entity(
    tableName = "team_members",
    primaryKeys = ["teamId", "pokemonId"],
    foreignKeys = [
        ForeignKey(
            entity = TeamEntity::class,
            parentColumns = ["teamId"],
            childColumns = ["teamId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PokemonEntity::class,
            parentColumns = ["id"],
            childColumns = ["pokemonId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("teamId"), Index("pokemonId")]
)
data class TeamMemberEntity(
    val teamId: Long,
    val pokemonId: Int,
    val position: Int = 0,
    val addedAt: Long = System.currentTimeMillis()
)
