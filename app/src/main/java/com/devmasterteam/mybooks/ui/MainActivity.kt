package com.devmasterteam.mybooks.ui

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.enableEdgeToEdge
import androidx.core.content.ContextCompat
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.devmasterteam.mybooks.R
import com.devmasterteam.mybooks.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Barra superior com cor roxa
        supportActionBar?.setBackgroundDrawable(
            ColorDrawable(ContextCompat.getColor(this, R.color.background))
        )
        supportActionBar?.show()

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Configura bottom navigation
        setupNavigation()
        
        // Controla visibilidade da bottom navigation
        setupNavigationVisibility()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun logout() {
        // Limpar sessão do usuário
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            clear()
            apply()
        }
        
        // Navegar para tela de login
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navController.navigate(R.id.loginFragment)
    }

    /**
     * Configura a navegação com o `BottomNavigationView` e a `ActionBar`.
     *
     * Este método realiza a configuração da navegação entre os diferentes fragmentos utilizando o `BottomNavigationView`.
     * Ele associa o `BottomNavigationView` ao controlador de navegação (`NavController`) e define a `AppBarConfiguration`
     * para que a `ActionBar` possa se comportar de acordo com a navegação.
     * Além disso, o método faz a configuração do `NavController` para que ele controle a navegação entre os fragmentos definidos,
     * como `navigation_home` e `navigation_favorite`.
     */
    private fun setupNavigation() {
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_pokedex, R.id.navigation_team, R.id.loginFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }
    
    private fun setupNavigationVisibility() {
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.registerFragment -> {
                    // Esconder bottom navigation e action bar nas telas de login/cadastro
                    binding.navView.visibility = android.view.View.GONE
                    supportActionBar?.hide()
                }
                else -> {
                    // Mostrar bottom navigation e action bar nas outras telas
                    binding.navView.visibility = android.view.View.VISIBLE
                    supportActionBar?.show()
                }
            }
        }
    }
}