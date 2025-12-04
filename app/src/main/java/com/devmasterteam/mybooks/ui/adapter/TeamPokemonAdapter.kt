package com.devmasterteam.mybooks.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devmasterteam.mybooks.R
import com.devmasterteam.mybooks.entity.TeamPokemonEntity
import com.devmasterteam.mybooks.ui.listener.TeamPokemonListener
import com.devmasterteam.mybooks.ui.viewholder.TeamPokemonViewHolder

class TeamPokemonAdapter(private val listener: TeamPokemonListener) :
    RecyclerView.Adapter<TeamPokemonViewHolder>() {

    private var teamPokemonList = listOf<TeamPokemonEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamPokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_team_pokemon, parent, false)
        return TeamPokemonViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: TeamPokemonViewHolder, position: Int) {
        holder.bind(teamPokemonList[position])
    }

    override fun getItemCount(): Int = teamPokemonList.size

    fun updateList(newList: List<TeamPokemonEntity>) {
        teamPokemonList = newList
        notifyDataSetChanged()
    }
}
