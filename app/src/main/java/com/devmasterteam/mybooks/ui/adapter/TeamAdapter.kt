package com.devmasterteam.mybooks.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devmasterteam.mybooks.databinding.ItemTeamBinding
import com.devmasterteam.mybooks.entity.PokemonEntity
import com.devmasterteam.mybooks.entity.TeamEntity

/**
 * Adapter para exibir times com seus Pokémon
 */
class TeamAdapter(
    private val onPokemonClick: (Int) -> Unit,
    private val onRemovePokemon: (Long, Int) -> Unit,
    private val onDeleteTeam: (TeamEntity) -> Unit
) : ListAdapter<Pair<TeamEntity, List<PokemonEntity>>, TeamAdapter.TeamViewHolder>(TeamDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val binding = ItemTeamBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        android.util.Log.d("TeamAdapter", "onCreateViewHolder called")
        return TeamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val (team, pokemons) = getItem(position)
        android.util.Log.d("TeamAdapter", "Binding team: ${team.teamName} with ${pokemons.size} pokemons")
        holder.bind(team, pokemons)
    }
    
    override fun submitList(list: List<Pair<TeamEntity, List<PokemonEntity>>>?) {
        android.util.Log.d("TeamAdapter", "submitList called with ${list?.size ?: 0} items")
        super.submitList(list)
    }

    inner class TeamViewHolder(
        private val binding: ItemTeamBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private val pokemonAdapter = TeamPokemonHorizontalAdapter(
            onClick = onPokemonClick,
            onRemove = { pokemonId ->
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val (team, _) = getItem(position)
                    onRemovePokemon(team.teamId, pokemonId)
                }
            }
        )

        init {
            binding.rvTeamPokemons.apply {
                layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 3)
                adapter = pokemonAdapter
            }
        }

        fun bind(team: TeamEntity, pokemons: List<PokemonEntity>) {
            binding.tvTeamName.text = team.teamName
            binding.tvTeamSize.text = "${pokemons.size}/6"

            if (pokemons.isEmpty()) {
                binding.rvTeamPokemons.visibility = View.GONE
                binding.tvEmptyTeam.visibility = View.VISIBLE
            } else {
                binding.rvTeamPokemons.visibility = View.VISIBLE
                binding.tvEmptyTeam.visibility = View.GONE
                pokemonAdapter.submitList(pokemons)
            }

            binding.btnDeleteTeam.setOnClickListener {
                onDeleteTeam(team)
            }
        }
    }

    class TeamDiffCallback : DiffUtil.ItemCallback<Pair<TeamEntity, List<PokemonEntity>>>() {
        override fun areItemsTheSame(
            oldItem: Pair<TeamEntity, List<PokemonEntity>>,
            newItem: Pair<TeamEntity, List<PokemonEntity>>
        ): Boolean {
            return oldItem.first.teamId == newItem.first.teamId
        }

        override fun areContentsTheSame(
            oldItem: Pair<TeamEntity, List<PokemonEntity>>,
            newItem: Pair<TeamEntity, List<PokemonEntity>>
        ): Boolean {
            return oldItem == newItem
        }
    }
}
