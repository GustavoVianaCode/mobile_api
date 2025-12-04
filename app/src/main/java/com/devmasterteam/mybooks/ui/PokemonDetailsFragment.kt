package com.devmasterteam.mybooks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import coil.load
import com.devmasterteam.mybooks.R
import com.devmasterteam.mybooks.databinding.FragmentPokemonDetailsBinding
import com.devmasterteam.mybooks.helper.PokemonConstants
import com.devmasterteam.mybooks.viewmodel.PokemonDetailsViewModel

class PokemonDetailsFragment : Fragment() {

    private var _binding: FragmentPokemonDetailsBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: PokemonDetailsViewModel
    private var pokemonId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPokemonDetailsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[PokemonDetailsViewModel::class.java]
        
        // Pegar ID do Pokémon
        pokemonId = arguments?.getInt(PokemonConstants.BUNDLE.POKEMON_ID) ?: 0
        
        if (pokemonId > 0) {
            viewModel.loadPokemonDetails(pokemonId)
        }
        
        setupToolbar()
        setupObservers()
        setupListeners()
        
        return binding.root
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupObservers() {
        viewModel.pokemon.observe(viewLifecycleOwner) { pokemon ->
            binding.tvPokemonId.text = String.format("#%03d", pokemon.id)
            binding.tvPokemonName.text = pokemon.name.replaceFirstChar { it.uppercase() }
            binding.tvPokemonTypes.text = viewModel.getFormattedTypes()
            binding.tvPokemonHeight.text = viewModel.getFormattedHeight()
            binding.tvPokemonWeight.text = viewModel.getFormattedWeight()
            
            updateSprite()
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.isInTeam.observe(viewLifecycleOwner) { isInTeam ->
            if (isInTeam) {
                binding.fabAddToTeam.text = getString(R.string.remove_from_team)
                binding.fabAddToTeam.setIconResource(R.drawable.ic_favorite)
            } else {
                binding.fabAddToTeam.text = getString(R.string.add_to_team)
                binding.fabAddToTeam.setIconResource(R.drawable.ic_favorite_empty)
            }
        }
        
        viewModel.showShiny.observe(viewLifecycleOwner) { showShiny ->
            if (showShiny) {
                binding.btnToggleShiny.text = "Ver Versão Normal"
            } else {
                binding.btnToggleShiny.text = "Ver Versão Shiny"
            }
            updateSprite()
        }
        
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearMessages()
            }
        }
        
        viewModel.successMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearMessages()
            }
        }
    }

    private fun setupListeners() {
        binding.fabAddToTeam.setOnClickListener {
            showTeamSelectionDialog()
        }
        
        binding.btnToggleShiny.setOnClickListener {
            viewModel.toggleShiny()
        }
    }
    
    /**
     * Mostra o dialog de seleção de time
     */
    private fun showTeamSelectionDialog() {
        val pokemon = viewModel.pokemon.value ?: return
        
        val dialog = com.devmasterteam.mybooks.ui.dialog.TeamSelectionDialog(
            pokemonId = pokemon.id
        ) { teamId, teamName ->
            val teamViewModel = ViewModelProvider(requireActivity())[com.devmasterteam.mybooks.viewmodel.TeamViewModel::class.java]
            teamViewModel.addPokemonToTeam(teamId, pokemon.id, teamName)
        }
        dialog.show(parentFragmentManager, "TeamSelectionDialog")
    }

    private fun updateSprite() {
        val spriteUrl = viewModel.getCurrentSpriteUrl()
        binding.ivPokemonSprite.load(spriteUrl) {
            crossfade(true)
            placeholder(R.drawable.ic_launcher_foreground)
            error(R.drawable.ic_launcher_foreground)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
