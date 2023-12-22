package com.example.practicafundandroid

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.Exception
import kotlin.random.Random

class SharedViewModel: ViewModel() {

    lateinit var token: String
    private val BASE_URL = "https://dragonball.keepcoding.education"

    var herosList: List<Hero> = listOf()
    lateinit var HeroeSeleccionado: Hero

    private val _uiStateHA = MutableStateFlow<StateHA>(StateHA.Started(true))
    val uiState: StateFlow<StateHA> = _uiStateHA

    fun lauchGetHeroes() {
        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val url = "${BASE_URL}/api/heros/all"
            val formBody = FormBody.Builder() //Esto hace que la request sea de tipo POST
                .add("name", "")
                .build()
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer $token")
                .post(formBody)
                .build()
            val call = client.newCall(request)
            val response = call.execute()
            response.body?.let { responseBody ->
                val gson = Gson()
                try {
                    val herosDtoArray = gson.fromJson(responseBody.string(),Array<HeroDto>::class.java)
                    herosList = herosDtoArray.toList().map { Hero(it.name,it.id,it.favorite,it.photo,it.description)}

                    _uiStateHA.value = StateHA.SuccessGetHeroes(herosList)
                }catch (e: Exception){
                    _uiStateHA.value = StateHA.Error("Error decoding Api response")
                }
            } ?: kotlin.run { Log.w("Error", "Error Api Call") }
        }
    }

    fun selectedHeroToDetail(hero: Hero) {
        HeroeSeleccionado = hero

        _uiStateHA.value = StateHA.onSelectedHeroToDetail(hero)
    }

    private fun cureHero(): Int {
        var cureValue = 20
        HeroeSeleccionado.let {
            when(HeroeSeleccionado.vidaactual){
                in 80 .. 99 -> cureValue= HeroeSeleccionado.vidamax - HeroeSeleccionado.vidaactual
                0,100 -> cureValue = 0
                else -> cureValue = 20
            }
        }
        return cureValue
    }

    private fun hitHero(): Int{
        val hit = Random.nextInt(-60, -10)
        return hit
    }

    fun actionOnClickButton(buttonTag: String){
        var changeLife = 0
        _uiStateHA.value = StateHA.Idle

        when(buttonTag){
            "cure" -> changeLife = cureHero()
            "hit" -> changeLife = hitHero()
        }

        HeroeSeleccionado.let {
            it.vidaactual = it.vidaactual + changeLife
            if(it.vidaactual <=0){
                it.isDead = true
                val position = herosList.indexOf(it)

                herosList[position].vidaactual = it.vidaactual
                herosList[position].isDead = it.isDead

                _uiStateHA.value = StateHA.OnHeroIsDead
            }else{
                _uiStateHA.value = StateHA.OnHeroeChange
            }
        }

    }

    fun cureAllHeros(){
        _uiStateHA.value = StateHA.Idle

        herosList.forEach{
            it.vidaactual = 100
            it.isDead = false
        }

        _uiStateHA.value = StateHA.OnHeroeChange
    }

    sealed class StateHA{
        data class Started(val started: Boolean) : StateHA()
        data class Error(val error: String): StateHA()
        data class SuccessGetHeroes(val herosList: List<Hero>) : StateHA()
        data class onSelectedHeroToDetail(var hero: Hero) : StateHA()
        object OnHeroeChange: StateHA()
        object OnHeroIsDead: StateHA()
        object Idle: StateHA()

    }
}