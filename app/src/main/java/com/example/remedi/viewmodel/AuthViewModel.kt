package com.example.remedi.viewmodel

import androidx.lifecycle.ViewModel

class AuthViewModel : ViewModel() {

    fun validateLogin(
        email: String,
        password: String
    ): Boolean {

        return email == "user@remedi.com" &&
                password == "123456"
    }

    fun validateRegister(
        name: String,
        email: String,
        password: String
    ): Boolean {

        return name.isNotEmpty() &&
                email.isNotEmpty() &&
                password.isNotEmpty()
    }
}