package com.devmasterteam.mybooks.ui.listener

import com.devmasterteam.mybooks.entity.TeamPokemonEntity

interface TeamPokemonListener {
    fun onTeamPokemonClick(teamPokemon: TeamPokemonEntity)
    fun onRemoveFromTeamClick(teamPokemon: TeamPokemonEntity)
}
