package com.devmasterteam.mybooks.ui.viewholder

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.devmasterteam.mybooks.R
import com.devmasterteam.mybooks.entity.TeamPokemonEntity
import com.devmasterteam.mybooks.ui.listener.TeamPokemonListener

class TeamPokemonViewHolder(itemView: View, private val listener: TeamPokemonListener) :
    RecyclerView.ViewHolder(itemView) {

    private val ivTeamPokemonSprite: ImageView = itemView.findViewById(R.id.ivTeamPokemonSprite)
    private val tvTeamPokemonName: TextView = itemView.findViewById(R.id.tvTeamPokemonName)
    private val btnRemoveFromTeam: ImageButton = itemView.findViewById(R.id.btnRemoveFromTeam)

    fun bind(teamPokemon: TeamPokemonEntity) {
        // Nome capitalizado e em maiúsculas
        tvTeamPokemonName.text = teamPokemon.pokemonName.uppercase()
        
        // Carregar imagem com Coil
        ivTeamPokemonSprite.load(teamPokemon.spriteUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground)
            error(R.drawable.ic_launcher_foreground)
        }
        
        // Click listeners
        itemView.setOnClickListener {
            listener.onTeamPokemonClick(teamPokemon)
        }
        
        btnRemoveFromTeam.setOnClickListener {
            listener.onRemoveFromTeamClick(teamPokemon)
        }
    }
}
