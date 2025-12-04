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
import com.devmasterteam.mybooks.databinding.FragmentFavoriteBinding
import com.devmasterteam.mybooks.entity.PokemonEntity
import com.devmasterteam.mybooks.entity.TeamEntity
import com.devmasterteam.mybooks.helper.BookConstants
import com.devmasterteam.mybooks.repository.PokemonDatabase
import com.devmasterteam.mybooks.ui.adapter.TeamAdapter
import com.devmasterteam.mybooks.ui.dialog.CreateTeamDialog
import com.devmasterteam.mybooks.viewmodel.TeamViewModel
import kotlinx.coroutines.launch

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: TeamViewModel
    private lateinit var adapter: TeamAdapter
    private lateinit var database: PokemonDatabase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, b: Bundle?): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        
        viewModel = ViewModelProvider(requireActivity())[TeamViewModel::class.java]
        database = PokemonDatabase.getDatabase(requireContext())
        
        Log.d("FavoriteFragment", "onCreateView - ViewModel inicializado")

        setupAdapter()
        setupRecyclerView()
        setupFab()
        setupObservers()

        return binding.root
    }
    
    private fun setupAdapter() {
        adapter = TeamAdapter(
            onPokemonClick = { pokemonId ->
                navigateToPokemonDetails(pokemonId)
            },
            onRemovePokemon = { teamId, pokemonId ->
                viewModel.removePokemonFromTeam(teamId, pokemonId)
                Toast.makeText(requireContext(), "Pokémon removido!", Toast.LENGTH_SHORT).show()
            },
            onDeleteTeam = { team ->
                showDeleteTeamDialog(team)
            }
        )
    }
    
    private fun setupRecyclerView() {
        binding.recyclerFavoriteBooks.layoutManager = LinearLayoutManager(context)
        binding.recyclerFavoriteBooks.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    
    override fun onResume() {
        super.onResume()
        Log.d("FavoriteFragment", "========================================")
        Log.d("FavoriteFragment", "onResume - Fragment visível")
        
        // Forçar verificação do estado atual dos times
        val currentTeams = viewModel.allTeams.value
        Log.d("FavoriteFragment", "Times no LiveData agora: ${currentTeams?.size ?: "null"}")
        if (currentTeams != null) {
            currentTeams.forEach { team ->
                Log.d("FavoriteFragment", "  - ${team.teamName} (ID: ${team.teamId})")
            }
        }
        
        // Verificar diretamente no banco (DEBUG)
        lifecycleScope.launch {
            try {
                val teamsFromDb = database.teamDao().getAllTeamsSync()
                Log.d("FavoriteFragment", "Verificação DIRETA no banco: ${teamsFromDb.size} times")
                teamsFromDb.forEach { team ->
                    Log.d("FavoriteFragment", "  [DB] ${team.teamName} (ID: ${team.teamId})")
                }
            } catch (e: Exception) {
                Log.e("FavoriteFragment", "Erro ao verificar banco", e)
            }
        }
        Log.d("FavoriteFragment", "========================================")
    }
    
    private fun setupObservers() {
        Log.d("FavoriteFragment", "========================================")
        Log.d("FavoriteFragment", "Iniciando observação de teams")
        
        viewModel.allTeams.observe(viewLifecycleOwner) { teams ->
            Log.d("FavoriteFragment", "========================================")
            Log.d("FavoriteFragment", "Observer CHAMADO! Teams recebidos: ${teams?.size ?: 0}")
            
            if (teams == null) {
                Log.e("FavoriteFragment", "ERRO: teams é NULL!")
                return@observe
            }
            
            if (teams.isEmpty()) {
                Log.d("FavoriteFragment", "Nenhum time encontrado - mostrando empty state")
                showEmptyState(true)
                adapter.submitList(emptyList())
            } else {
                Log.d("FavoriteFragment", "Times encontrados: ${teams.size}")
                teams.forEachIndexed { index, team ->
                    Log.d("FavoriteFragment", "  Time $index: ${team.teamName} (ID: ${team.teamId})")
                }
                
                showEmptyState(false)
                
                // Carregar pokémons de cada time
                lifecycleScope.launch {
                    try {
                        val teamsWithPokemons = teams.map { team ->
                            val pokemons = database.teamDao().getTeamPokemonsSync(team.teamId)
                            Log.d("FavoriteFragment", "Time '${team.teamName}' tem ${pokemons.size} Pokémon")
                            
                            pokemons.forEach { pokemon ->
                                Log.d("FavoriteFragment", "  - ${pokemon.name} (#${pokemon.id})")
                            }
                            
                            Pair(team, pokemons)
                        }
                        
                        Log.d("FavoriteFragment", "Submetendo ${teamsWithPokemons.size} times ao adapter")
                        adapter.submitList(teamsWithPokemons)
                        
                    } catch (e: Exception) {
                        Log.e("FavoriteFragment", "ERRO ao carregar Pokémon dos times", e)
                    }
                }
            }
            Log.d("FavoriteFragment", "========================================")
        }
        
        viewModel.successMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                viewModel.clearMessages()
            }
        }
        
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                viewModel.clearMessages()
            }
        }
    }
    
    private fun showEmptyState(show: Boolean) {
        binding.textviewNoBooks.visibility = if (show) View.VISIBLE else View.INVISIBLE
        binding.imageviewNoBooks.visibility = if (show) View.VISIBLE else View.INVISIBLE
        binding.recyclerFavoriteBooks.visibility = if (show) View.INVISIBLE else View.VISIBLE
        
        if (show) {
            binding.textviewNoBooks.text = "Nenhum time criado"
        }
    }
    
    private fun navigateToPokemonDetails(pokemonId: Int) {
        val bundle = Bundle()
        bundle.putInt(BookConstants.KEY.BOOK_ID, pokemonId)
        findNavController().navigate(R.id.navigation_book_details, bundle)
    }
    
    private fun showDeleteTeamDialog(team: TeamEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("Deletar Time")
            .setMessage("Tem certeza que deseja deletar o time '${team.teamName}'?")
            .setPositiveButton("Deletar") { _, _ ->
                viewModel.deleteTeam(team)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun setupFab() {
        Log.d("FavoriteFragment", "FAB configurado")
        binding.fabCreateTeam.setOnClickListener {
            Log.d("FavoriteFragment", "FAB clicado - abrindo CreateTeamDialog")
            CreateTeamDialog().show(childFragmentManager, "CreateTeamDialog")
        }
    }
}