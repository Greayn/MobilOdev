package com.example.grads_hw.validator

import android.util.Patterns
import com.example.grads_hw.models.LoginCredentials

class LoginValidator {

    fun validate(args: LoginCredentials) {
        if(args.email.isEmpty()) {
            error("E-posta boş olamaz")
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(args.email).matches()) {
            error("Geçersiz e-posta")
        }
        if(args.password.isEmpty()) {
            error("Şifre boş olamaz")
        }
    }

}