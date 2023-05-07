package com.example.grads_hw.validator

import android.util.Patterns
import com.example.grads_hw.models.SendVerificationEmailCredentials

class SendVerificationEmailValidator{

    fun validate(args: SendVerificationEmailCredentials) {
        if(args.email.isEmpty()) {
            error("E-posta boş olamaz")
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(args.email).matches()) {
            error("Geçersiz e-posta")
        }
    }

}