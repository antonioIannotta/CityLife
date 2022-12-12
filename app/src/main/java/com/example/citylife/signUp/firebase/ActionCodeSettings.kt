package com.example.citylife.signUp.firebase

import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.ktx.actionCodeSettings

class ActionCodeSettings {

    /**
     * imposta firebase per poter inviare la mail per la verifica dell'account durante la registrazione
     */
    fun returnActionCodeSettings(): ActionCodeSettings {
        return actionCodeSettings {
            url = "citylife-98ec7.firebaseapp.com"
            handleCodeInApp = true
            setIOSBundleId("com.example.ios")
            setAndroidPackageName(
                "com.example.android",
                true,
                "12"
            )
        }
    }
}