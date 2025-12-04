package com.devmasterteam.mybooks.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.devmasterteam.mybooks.R
import com.devmasterteam.mybooks.databinding.FragmentPokemonListBinding
import com.devmasterteam.mybooks.entity.PokemonEntity
import com.devmasterteam.mybooks.helper.PokemonConstants
import com.devmasterteam.mybooks.ui.adapter.PokemonAdapter
import com.devmasterteam.mybooks.ui.listener.PokemonListener
import com.devmasterteam.mybooks.viewmodel.PokemonListViewModel

class PokemonListFragment : Fragment(), PokemonListener {

    private var _binding: FragmentPokemonListBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var viewModel: PokemonListViewModel
    private lateinit var adapter: PokemonAdapter
    
    private val versionList = listOf(
        "Todas as Gerações" to "all",
        "Red/Blue" to "red",
        "Yellow" to "yellow",
        "Gold/Silver" to "gold",
        "Crystal" to "crystal",
        "Ruby/Sapphire" to "ruby",
        "Emerald" to "emerald",
        "FireRed/LeafGreen" to "firered",
        "Diamond/Pearl" to "diamond",
        "Platinum" to "platinum",
        "HeartGold/SoulSilver" to "heartgold",
        "Black/White" to "black",
        "Black 2/White 2" to "black-2",
        "X/Y" to "x",
        "Omega Ruby/Alpha Sapphire" to "omega-ruby",
        "Sun/Moon" to "sun",
        "Ultra Sun/Ultra Moon" to "ultra-sun",
        "Sword/Shield" to "sword",
        "Scarlet/Violet" to "scarlet"
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("PokemonListFragment", "onCreateView chamado")
        _binding = FragmentPokemonListBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[PokemonListViewModel::class.java]
        
        setupRecyclerView()
        setupFilters()
        setupObservers()
        
        // Carregar primeira geração por padrão
        Log.d("PokemonListFragment", "Carregando Geração 1")
        viewModel.loadPokemonByGeneration(1)
        
        return binding.root
    }

    private var allPokemonList = listOf<PokemonEntity>()

    private fun setupRecyclerView() {
        adapter = PokemonAdapter(this)
        binding.recyclerViewPokemon.adapter = adapter
    }

    private fun setupFilters() {
        // SearchView para busca por nome ou número
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterPokemon(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterPokemon(newText)
                return true
            }
        })
        
        // Spinner de gerações (1 a 9)
        val generationList = listOf(
            "Geração 1 - Kanto (151)" to 1,
            "Geração 2 - Johto (100)" to 2,
            "Geração 3 - Hoenn (135)" to 3,
            "Geração 4 - Sinnoh (107)" to 4,
            "Geração 5 - Unova (156)" to 5,
            "Geração 6 - Kalos (72)" to 6,
            "Geração 7 - Alola (88)" to 7,
            "Geração 8 - Galar (96)" to 8,
            "Geração 9 - Paldea (120)" to 9
        )
        
        val generationNames = generationList.map { it.first }
        val generationAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            generationNames
        )
        generationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerGeneration.adapter = generationAdapter
        
        binding.spinnerGeneration.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedGeneration = generationList[position].second
                viewModel.loadPokemonByGeneration(selectedGeneration)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }
    
    private fun filterPokemon(query: String?) {
        if (query.isNullOrBlank()) {
            adapter.updateList(allPokemonList)
            return
        }
        
        val filteredList = allPokemonList.filter { pokemon ->
            // Busca por nome
            pokemon.name.contains(query, ignoreCase = true) ||
            // Busca por número
            pokemon.id.toString() == query ||
            // Busca por número com #
            "#${pokemon.id}" == query
        }
        
        adapter.updateList(filteredList)
    }

    private fun setupObservers() {
        viewModel.pokemonList.observe(viewLifecycleOwner) { pokemons ->
            Log.d("PokemonListFragment", "Lista recebida: ${pokemons.size} pokémons")
            allPokemonList = pokemons
            
            if (pokemons.isEmpty()) {
                binding.tvEmptyState.visibility = View.VISIBLE
                binding.recyclerViewPokemon.visibility = View.GONE
            } else {
                binding.tvEmptyState.visibility = View.GONE
                binding.recyclerViewPokemon.visibility = View.VISIBLE
                adapter.updateList(pokemons)
            }
        }
        
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearErrorMessage()
            }
        }
        
        viewModel.successMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                viewModel.clearSuccessMessage()
            }
        }
    }

    override fun onPokemonClick(pokemon: PokemonEntity) {
        val bundle = Bundle().apply {
            putInt(PokemonConstants.BUNDLE.POKEMON_ID, pokemon.id)
        }
        findNavController().navigate(R.id.action_pokemonListFragment_to_pokemonDetailsFragment, bundle)
    }

    override fun onAddToTeamClick(pokemon: PokemonEntity) {
        viewModel.addPokemonToTeam(pokemon)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
