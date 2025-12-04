package com.devmasterteam.mybooks.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.devmasterteam.mybooks.R
import com.devmasterteam.mybooks.databinding.ItemTeamPokemonBinding
import com.devmasterteam.mybooks.entity.PokemonEntity

/**
 * Adapter para Pokémon dentro de um time (horizontal) - Sistema novo
 */
class TeamPokemonHorizontalAdapter(
    private val onClick: (Int) -> Unit,
    private val onRemove: (Int) -> Unit
) : ListAdapter<PokemonEntity, TeamPokemonHorizontalAdapter.PokemonViewHolder>(PokemonDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val binding = ItemTeamPokemonBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PokemonViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PokemonViewHolder(
        private val binding: ItemTeamPokemonBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(pokemon: PokemonEntity) {
            binding.tvTeamPokemonName.text = pokemon.name
            binding.ivTeamPokemonSprite.load(pokemon.spriteUrl) {
                crossfade(true)
                placeholder(R.drawable.ic_launcher_foreground)
                error(R.drawable.ic_launcher_foreground)
            }

            binding.root.setOnClickListener {
                onClick(pokemon.id)
            }

            binding.btnRemoveFromTeam.setOnClickListener {
                onRemove(pokemon.id)
            }
        }
    }

    class PokemonDiffCallback : DiffUtil.ItemCallback<PokemonEntity>() {
        override fun areItemsTheSame(oldItem: PokemonEntity, newItem: PokemonEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PokemonEntity, newItem: PokemonEntity): Boolean {
            return oldItem == newItem
        }
    }
}
