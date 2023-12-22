package com.example.practicafundandroid

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.practicafundandroid.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val emailText = "MyEmail"
    private val passwordText = "MyPassword"

    private lateinit var binding: ActivityMainBinding
    private val viewModel: LoginViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUserInterface()

        lifecycleScope.launch() {
            viewModel.uiState.collect {
                when (it) {
                    is LoginViewModel.State.Idle -> Log.w("start", "Cargado")
                    is LoginViewModel.State.Error -> Log.w("tag","Error al cargar datos")
                    is LoginViewModel.State.SuccessLogin -> HeroActivity.launch(binding.loginButton.context, viewModel.token)
                }
            }
        }
    }

    private fun setUserInterface(){
        setActionButton()
    }

    private fun setActionButton() {

        binding.loginButton.setOnClickListener {
            if (!binding.emailEditText.text.isEmpty() && !binding.passwordEditText.text.isEmpty()) {
                viewModel.lauchLogin(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString())
                Log.w("Login", "Se ha intentado iniciar sesión.")
            }
        }
    }

}