package com.example.practicafundandroid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.Credentials
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class LoginViewModel : ViewModel() {

    private val BASE_URL = "https://dragonball.keepcoding.education"


    private val _uiState = MutableStateFlow<State>(State.Idle(true))
    val uiState : StateFlow<State> = _uiState
    var token: String = ""

    sealed class State {
        class Idle(val started: Boolean): State()
        class Error(val message: String): State()
        class SuccessLogin(val token: String): State()
    }

    fun lauchLogin(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val client = OkHttpClient()
            val url = "${BASE_URL}/api/auth/login"
            val credentials = Credentials.basic(email, password)
            val formBody = FormBody.Builder() //Esto hace que la request sea de tipo POST
                .build()
            val request = Request.Builder()
                .url(url)
                .addHeader("Authorization", credentials)
                .post(formBody)
                .build()
            val call = client.newCall(request)
            val response = call.execute()
            _uiState.value = if (response.isSuccessful)
                response.body?.let {
                    token = it.string()
                    State.SuccessLogin(token)
                } ?: State.Error("Empty token")
            else
                State.Error(response.message)
        }
    }

}

