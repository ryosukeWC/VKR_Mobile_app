package com.spau.rwc.authentication

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

object SessionManager {

    private val auth = Firebase.auth

    fun isUserLoggedIn(): Boolean {
        return auth.currentUser != null && auth.currentUser?.isEmailVerified == true
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}