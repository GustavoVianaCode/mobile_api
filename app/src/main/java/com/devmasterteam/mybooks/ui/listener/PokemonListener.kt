package com.devmasterteam.mybooks.ui.listener

import com.devmasterteam.mybooks.entity.PokemonEntity

interface PokemonListener {
    fun onPokemonClick(pokemon: PokemonEntity)
    fun onAddToTeamClick(pokemon: PokemonEntity)
}
