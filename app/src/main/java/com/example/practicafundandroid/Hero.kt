package com.example.practicafundandroid

data class Hero (
    val name: String,
    val id: String,
    val favorite: Boolean,
    val description: String,
    val photo: String,
    var vidamax : Int = 100,
    var vidaactual : Int = 100,
    var isDead : Boolean = false

)