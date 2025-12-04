package com.devmasterteam.mybooks.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmasterteam.mybooks.databinding.DialogTeamSelectionBinding
import com.devmasterteam.mybooks.entity.TeamEntity
import com.devmasterteam.mybooks.ui.adapter.TeamSelectionAdapter
import com.devmasterteam.mybooks.viewmodel.TeamViewModel
import kotlinx.coroutines.launch

/**
 * Dialog para selecionar um time ao adicionar Pokémon
 */
class TeamSelectionDialog(
    private val pokemonId: Int,
    private val onTeamSelected: (Long, String) -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogTeamSelectionBinding
    private lateinit var viewModel: TeamViewModel
    private lateinit var adapter: TeamSelectionAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogTeamSelectionBinding.inflate(LayoutInflater.from(context))
        viewModel = ViewModelProvider(requireActivity())[TeamViewModel::class.java]

        setupRecyclerView()
        observeTeams()
        
        binding.btnCreateNewTeam.setOnClickListener {
            showCreateTeamDialog()
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }

    private fun setupRecyclerView() {
        adapter = TeamSelectionAdapter { teamId, teamName ->
            onTeamSelected(teamId, teamName)
            dismiss()
        }

        binding.rvTeams.layoutManager = LinearLayoutManager(context)
        binding.rvTeams.adapter = adapter
    }

    private fun observeTeams() {
        viewModel.allTeams.observe(this) { teams ->
            lifecycleScope.launch {
                // Filtrar times com espaço disponível (menos de 6 Pokémon)
                val teamsWithSize = teams.mapNotNull { team ->
                    val size = viewModel.getTeamSize(team.teamId)
                    if (size < 6) {
                        Pair(team, size)
                    } else {
                        null
                    }
                }
                
                if (teamsWithSize.isEmpty()) {
                    binding.rvTeams.visibility = View.GONE
                    binding.tvEmptyState.visibility = View.VISIBLE
                } else {
                    binding.rvTeams.visibility = View.VISIBLE
                    binding.tvEmptyState.visibility = View.GONE
                    adapter.submitList(teamsWithSize)
                }
            }
        }
    }

    private fun showCreateTeamDialog() {
        val createDialog = CreateTeamDialog()
        createDialog.show(parentFragmentManager, "CreateTeamDialog")
        dismiss()
    }
}
