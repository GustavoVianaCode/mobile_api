package com.devmasterteam.mybooks.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.devmasterteam.mybooks.databinding.ItemTeamSelectionBinding
import com.devmasterteam.mybooks.entity.TeamEntity

/**
 * Adapter para seleção de time
 */
class TeamSelectionAdapter(
    private val onTeamSelected: (Long, String) -> Unit
) : ListAdapter<Pair<TeamEntity, Int>, TeamSelectionAdapter.TeamViewHolder>(TeamDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamViewHolder {
        val binding = ItemTeamSelectionBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TeamViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeamViewHolder, position: Int) {
        val (team, size) = getItem(position)
        holder.bind(team, size)
    }

    inner class TeamViewHolder(
        private val binding: ItemTeamSelectionBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(team: TeamEntity, size: Int) {
            binding.tvTeamName.text = team.teamName
            binding.tvTeamSize.text = "$size/6 Pokémon"
            
            binding.root.setOnClickListener {
                onTeamSelected(team.teamId, team.teamName)
            }
        }
    }

    class TeamDiffCallback : DiffUtil.ItemCallback<Pair<TeamEntity, Int>>() {
        override fun areItemsTheSame(
            oldItem: Pair<TeamEntity, Int>,
            newItem: Pair<TeamEntity, Int>
        ): Boolean {
            return oldItem.first.teamId == newItem.first.teamId
        }

        override fun areContentsTheSame(
            oldItem: Pair<TeamEntity, Int>,
            newItem: Pair<TeamEntity, Int>
        ): Boolean {
            return oldItem == newItem
        }
    }
}
