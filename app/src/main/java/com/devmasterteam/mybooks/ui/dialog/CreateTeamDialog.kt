package com.devmasterteam.mybooks.ui.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.devmasterteam.mybooks.databinding.DialogCreateTeamBinding
import com.devmasterteam.mybooks.viewmodel.TeamViewModel

/**
 * Dialog para criar novo time
 */
class CreateTeamDialog : DialogFragment() {

    private lateinit var binding: DialogCreateTeamBinding
    private lateinit var viewModel: TeamViewModel

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogCreateTeamBinding.inflate(LayoutInflater.from(context))
        
        viewModel = ViewModelProvider(requireActivity())[TeamViewModel::class.java]
        
        Log.d("CreateTeamDialog", "Dialog criado, ViewModel inicializado")

        binding.btnCreate.setOnClickListener {
            val teamName = binding.etTeamName.text.toString().trim()
            
            Log.d("CreateTeamDialog", "Botão CRIAR clicado")
            Log.d("CreateTeamDialog", "Nome digitado: '$teamName'")
            
            if (teamName.isEmpty()) {
                Log.w("CreateTeamDialog", "Nome vazio - mostrando erro")
                binding.etTeamName.error = "Digite um nome para o time"
                return@setOnClickListener
            }
            
            Log.d("CreateTeamDialog", "Chamando viewModel.createTeam('$teamName')")
            viewModel.createTeam(teamName)
            
            Toast.makeText(context, "Time '$teamName' criado!", Toast.LENGTH_SHORT).show()
            Log.d("CreateTeamDialog", "Toast mostrado, fechando dialog")
            dismiss()
        }

        binding.btnCancel.setOnClickListener {
            Log.d("CreateTeamDialog", "Botão CANCELAR clicado")
            dismiss()
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }
}
