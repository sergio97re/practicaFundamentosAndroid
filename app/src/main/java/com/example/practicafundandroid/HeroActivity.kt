package com.example.practicafundandroid

import android.content.Context
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.practicafundandroid.databinding.ActivityHeroBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HeroActivity : AppCompatActivity() {

    companion object{
        const val TAG_TOKEN = "TOKEN_KEY"

        fun launch(context: Context, token:String){
            val intent = Intent(context, HeroActivity::class.java)
            intent.putExtra(TAG_TOKEN, token)
            context.startActivity(intent)
        }

    }

    private lateinit var binding: ActivityHeroBinding
    private val viewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeroBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getToken()



        lifecycleScope.launch {
            viewModel.uiState.collect{
                when(it){
                    is SharedViewModel.StateHA.Started -> viewModel.lauchGetHeroes()
                    is SharedViewModel.StateHA.SuccessGetHeroes -> {
                        addHerosListToFragment()
                        binding.tvTitle.text = "DRAGON BALL"
                        binding.tvAction.text = "Selecciona un personaje para empezar el combate"
                    }
                    is SharedViewModel.StateHA.Error -> Log.w("Error", "Error en la carga")
                    is SharedViewModel.StateHA.onSelectedHeroToDetail -> {
                        navigateToFragmentTwo()
                        binding.tvTitle.text = "Pulsa para volver atrás"
                        Toast.makeText(binding.root.context, "Comienza el combate", Toast.LENGTH_LONG).show()

                        binding.tvTitle.setOnClickListener{
                            addHerosListToFragment()
                        }
                    }
                    is SharedViewModel.StateHA.OnHeroIsDead -> {
                        addHerosListToFragment()
                        binding.tvTitle.text = "Selecciona un personaje para empezar el combate"
                    }
                    else -> Unit
                }
            }
        }

    }

    private fun getToken(){
        viewModel.token = intent.getStringExtra(TAG_TOKEN).toString()
        binding.tvTitle.text = viewModel.token
    }

    private fun addHerosListToFragment(){
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fList.id,HerosListFragment())
    }

    private fun navigateToFragmentTwo(){
        supportFragmentManager
            .beginTransaction()
            .replace(binding.fList.id,DetailFragment())
    }

    fun changeFragment(nextFragment: FragmentOptions){
        when(nextFragment){
            FragmentOptions.HerosListFragment -> addHerosListToFragment()
            FragmentOptions.DetailFragment -> navigateToFragmentTwo()
        }
    }

    enum class  FragmentOptions{
        HerosListFragment, DetailFragment
    }
}