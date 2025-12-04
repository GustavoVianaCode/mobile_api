package com.devmasterteam.mybooks.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.devmasterteam.mybooks.R
import com.devmasterteam.mybooks.entity.PokemonEntity
import com.devmasterteam.mybooks.ui.listener.PokemonListener
import com.devmasterteam.mybooks.ui.viewholder.PokemonViewHolder

class PokemonAdapter(private val listener: PokemonListener) :
    RecyclerView.Adapter<PokemonViewHolder>() {

    private var pokemonList = listOf<PokemonEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_pokemon, parent, false)
        return PokemonViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(pokemonList[position])
    }

    override fun getItemCount(): Int = pokemonList.size

    fun updateList(newList: List<PokemonEntity>) {
        Log.d("PokemonAdapter", "Atualizando lista com ${newList.size} itens")
        pokemonList = newList
        notifyDataSetChanged()
    }
}
