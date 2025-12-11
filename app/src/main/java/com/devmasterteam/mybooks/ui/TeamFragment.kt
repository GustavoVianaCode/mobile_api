package com.devmasterteam.mybooks.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmasterteam.mybooks.R
import com.devmasterteam.mybooks.databinding.FragmentTeamBinding
import com.devmasterteam.mybooks.entity.TeamEntity
import com.devmasterteam.mybooks.helper.PokemonConstants
import com.devmasterteam.mybooks.repository.PokemonDatabase
import com.devmasterteam.mybooks.ui.adapter.TeamAdapter
import com.devmasterteam.mybooks.ui.dialog.CreateTeamDialog
import com.devmasterteam.mybooks.viewmodel.TeamViewModel
import kotlinx.coroutines.launch

class TeamFragment : Fragment() {

    private var _binding: FragmentTeamBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: TeamViewModel
    private lateinit var adapter: TeamAdapter
    private lateinit var database: PokemonDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTeamBinding.inflate(inflater, container, false)
        
        viewModel = ViewModelProvider(requireActivity())[TeamViewModel::class.java]
        database = PokemonDatabase.getDatabase(requireContext())
        
        Log.d("TeamFragment", "onCreateView - ViewModel inicializado")
        
        setupRecyclerView()
        setupFab()
        setupObservers()
        
        return binding.root
    }

    private fun setupRecyclerView() {
        adapter = TeamAdapter(
            onPokemonClick = { pokemonId ->
                navigateToPokemonDetails(pokemonId)
            },
            onRemovePokemon = { teamId, pokemonId ->
                Log.d("TeamFragment", "Removendo Pokémon $pokemonId do time $teamId")
                viewModel.removePokemonFromTeam(teamId, pokemonId)
                Toast.makeText(requireContext(), "Pokémon removido!", Toast.LENGTH_SHORT).show()
            },
            onDeleteTeam = { team ->
                showDeleteTeamDialog(team)
            }
        )
        
        binding.recyclerViewTeam.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewTeam.adapter = adapter
        
        Log.d("TeamFragment", "RecyclerView configurado com TeamAdapter")
    }
    
    private fun setupFab() {
        binding.fabCreateTeam?.setOnClickListener {
            Log.d("TeamFragment", "FAB clicado - abrindo CreateTeamDialog")
            CreateTeamDialog().show(childFragmentManager, "CreateTeamDialog")
        }
    }

    private fun setupObservers() {
        Log.d("TeamFragment", "========================================")
        Log.d("TeamFragment", "Configurando observer para allTeams")
        
        viewModel.allTeams.observe(viewLifecycleOwner) { teams ->
            Log.d("TeamFragment", "========================================")
            Log.d("TeamFragment", "Observer CHAMADO! Teams recebidos: ${teams?.size ?: 0}")
            
            if (teams == null) {
                Log.e("TeamFragment", "ERRO: teams é NULL!")
                return@observe
            }
            
            if (teams.isEmpty()) {
                Log.d("TeamFragment", "Nenhum time encontrado - mostrando empty state")
                showEmptyState(true)
                adapter.submitList(emptyList())
            } else {
                Log.d("TeamFragment", "Times encontrados: ${teams.size}")
                teams.forEachIndexed { index, team ->
                    Log.d("TeamFragment", "  Time $index: ${team.teamName} (ID: ${team.teamId})")
                }
                
                showEmptyState(false)
                
                // Carregar pokémons de cada time
                lifecycleScope.launch {
                    try {
                        val teamsWithPokemons = teams.map { team ->
                            val pokemons = database.teamDao().getTeamPokemonsSync(team.teamId)
                            Log.d("TeamFragment", "Time '${team.teamName}' tem ${pokemons.size} Pokémon")
                            
                            pokemons.forEach { pokemon ->
                                Log.d("TeamFragment", "  - ${pokemon.name} (#${pokemon.id})")
                            }
                            
                            team to pokemons
                        }
                        
                        Log.d("TeamFragment", "Submetendo ${teamsWithPokemons.size} times ao adapter")
                        adapter.submitList(teamsWithPokemons)
                        
                    } catch (e: Exception) {
                        Log.e("TeamFragment", "ERRO ao carregar Pokémon dos times", e)
                    }
                }
            }
            Log.d("TeamFragment", "========================================")
        }
    }
    
    override fun onResume() {
        super.onResume()
        Log.d("TeamFragment", "========================================")
        Log.d("TeamFragment", "onResume - Fragment visível")
        
        // Verificar estado atual
        val currentTeams = viewModel.allTeams.value
        Log.d("TeamFragment", "Times no LiveData: ${currentTeams?.size ?: "null"}")
        
        // Verificar banco diretamente
        lifecycleScope.launch {
            try {
                val teamsFromDb = database.teamDao().getAllTeamsSync()
                Log.d("TeamFragment", "Verificação DIRETA no banco: ${teamsFromDb.size} times")
                teamsFromDb.forEach { team ->
                    Log.d("TeamFragment", "  [DB] ${team.teamName} (ID: ${team.teamId})")
                }
            } catch (e: Exception) {
                Log.e("TeamFragment", "Erro ao verificar banco", e)
            }
        }
        Log.d("TeamFragment", "========================================")
    }
    
    private fun showEmptyState(show: Boolean) {
        binding.layoutEmptyState?.visibility = if (show) View.VISIBLE else View.GONE
        binding.recyclerViewTeam.visibility = if (show) View.GONE else View.VISIBLE
    }
    
    private fun navigateToPokemonDetails(pokemonId: Int) {
        val bundle = Bundle().apply {
            putInt(PokemonConstants.BUNDLE.POKEMON_ID, pokemonId)
        }
        findNavController().navigate(R.id.action_teamFragment_to_pokemonDetailsFragment, bundle)
    }
    
    private fun showDeleteTeamDialog(team: TeamEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Deletar Time")
            .setMessage("Tem certeza que deseja deletar o time '${team.teamName}'?")
            .setPositiveButton("Deletar") { _, _ ->
                Log.d("TeamFragment", "Deletando time: ${team.teamName}")
                viewModel.deleteTeam(team)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("TeamFragment", "onDestroyView")
        _binding = null
    }
}
