package com.devmasterteam.mybooks.ui.viewholder

import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.devmasterteam.mybooks.R
import com.devmasterteam.mybooks.entity.PokemonEntity
import com.devmasterteam.mybooks.ui.listener.PokemonListener

class PokemonViewHolder(itemView: View, private val listener: PokemonListener) :
    RecyclerView.ViewHolder(itemView) {

    private val ivPokemonSprite: ImageView = itemView.findViewById(R.id.ivPokemonSprite)
    private val tvPokemonId: TextView = itemView.findViewById(R.id.tvPokemonId)
    private val tvPokemonName: TextView = itemView.findViewById(R.id.tvPokemonName)
    private val tvPokemonTypes: TextView = itemView.findViewById(R.id.tvPokemonTypes)
    private val btnAddToTeam: ImageButton = itemView.findViewById(R.id.btnAddToTeam)

    fun bind(pokemon: PokemonEntity) {
        // ID com formato #001
        tvPokemonId.text = String.format("#%03d", pokemon.id)
        
        // Nome capitalizado
        tvPokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }
        
        // Tipos formatados
        val types = pokemon.types.split(",").joinToString(" • ") { 
            it.trim().replaceFirstChar { char -> char.uppercase() }
        }
        tvPokemonTypes.text = types
        
        // Carregar imagem com Coil
        ivPokemonSprite.load(pokemon.spriteUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground)
            error(R.drawable.ic_launcher_foreground)
        }
        
        // Click listeners
        itemView.setOnClickListener {
            listener.onPokemonClick(pokemon)
        }
        
        btnAddToTeam.setOnClickListener {
            listener.onAddToTeamClick(pokemon)
        }
    }
}
