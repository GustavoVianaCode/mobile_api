package com.devmasterteam.mybooks.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devmasterteam.mybooks.R
import com.devmasterteam.mybooks.databinding.FragmentLoginBinding
import com.devmasterteam.mybooks.viewmodel.AuthViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Verificar se usuário já está logado
        checkUserSession()
        
        setupListeners()
        setupObservers()
    }

    private fun checkUserSession() {
        val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val userId = sharedPref.getInt("user_id", -1)
        
        if (userId != -1) {
            // Usuário já está logado, navegar para lista de pokémon
            navigateToPokemonList()
        }
    }

    private fun setupListeners() {
        binding.btnLogin.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            
            viewModel.login(username, password)
        }

        binding.tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun setupObservers() {
        viewModel.loginSuccess.observe(viewLifecycleOwner) { user ->
            user?.let {
                // Salvar sessão do usuário
                val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putInt("user_id", it.id)
                    putString("username", it.username)
                    putString("email", it.email)
                    apply()
                }
                
                Toast.makeText(requireContext(), "Bem-vindo, ${it.username}!", Toast.LENGTH_SHORT).show()
                navigateToPokemonList()
            }
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                viewModel.resetMessages()
            }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.btnLogin.isEnabled = false
                binding.btnLogin.text = ""
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.btnLogin.isEnabled = true
                binding.btnLogin.text = "Entrar"
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun navigateToPokemonList() {
        findNavController().navigate(R.id.action_loginFragment_to_pokemonListFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
