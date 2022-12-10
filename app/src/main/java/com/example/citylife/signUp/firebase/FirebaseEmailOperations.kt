package com.example.citylife.signUp.firebase

import android.nfc.Tag
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class FirebaseEmailOperations {

    val actionCodeSettings = ActionCodeSettings().returnActionCodeSettings()

    fun sendEmail(email: String) {
        Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("FIREBASE", "Email sent")
                }
            }
    }

    fun completeSignUp(email: String): String {
        var firebaseSignUpResult = ""

        val auth = Firebase.auth
        val intent =
    }
}