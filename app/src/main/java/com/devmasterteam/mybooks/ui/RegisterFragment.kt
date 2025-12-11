package com.devmasterteam.mybooks.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.devmasterteam.mybooks.R
import com.devmasterteam.mybooks.databinding.FragmentRegisterBinding
import com.devmasterteam.mybooks.viewmodel.AuthViewModel

class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()
            
            viewModel.register(username, email, password, confirmPassword)
        }

        binding.tvLogin.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupObservers() {
        viewModel.registerSuccess.observe(viewLifecycleOwner) { success ->
            if (success) {
                Toast.makeText(
                    requireContext(),
                    "Usuário criado com sucesso! Faça login para continuar.",
                    Toast.LENGTH_LONG
                ).show()
                
                // Voltar para tela de login
                findNavController().navigateUp()
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
                binding.btnRegister.isEnabled = false
                binding.btnRegister.text = "Carregando..."
                
                // Desabilitar campos durante carregamento
                binding.etUsername.isEnabled = false
                binding.etEmail.isEnabled = false
                binding.etPassword.isEnabled = false
                binding.etConfirmPassword.isEnabled = false
            } else {
                binding.btnRegister.isEnabled = true
                binding.btnRegister.text = "CADASTRAR"
                
                // Habilitar campos novamente
                binding.etUsername.isEnabled = true
                binding.etEmail.isEnabled = true
                binding.etPassword.isEnabled = true
                binding.etConfirmPassword.isEnabled = true
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
