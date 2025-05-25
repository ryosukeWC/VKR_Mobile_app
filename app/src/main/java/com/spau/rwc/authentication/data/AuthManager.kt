package com.spau.rwc.authentication.data

import androidx.activity.ComponentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class AuthManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerWithEmail(
        name: String,
        email: String,
        phone: String,
        password: String,
        callback: (FirebaseUser?, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let { firebaseUser ->
                        // 1. Отправляем email для верификации
                        sendVerificationEmail(firebaseUser) { verificationError ->
                            if (verificationError != null) {
                                callback(null, verificationError)
                                return@sendVerificationEmail
                            }
                        }
                    } ?: run {
                        callback(null, "User is null")
                    }
                } else {
                    callback(null, task.exception?.message ?: "Registration failed")
                }
            }
    }

    private fun sendVerificationEmail(
        user: FirebaseUser,
        callback: (String?) -> Unit
    ) {
        user.sendEmailVerification()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(null)
                } else {
                    callback(task.exception?.message ?: "Failed to send verification email")
                }
            }
    }

    // Вход по email и паролю
    fun loginWithEmail(
        email: String,
        password: String,
        callback: (FirebaseUser?, String?) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user?.isEmailVerified == true) {
                        callback(user, null)
                    } else {
                        callback(null, "Потвердите Email на почте")
                    }
                } else {
                    callback(null, task.exception?.message)
                }
            }
    }

    // Отправка SMS с кодом для телефона
    fun sendPhoneVerificationCode(
        phoneNumber: String,
        callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks,
        activity: ComponentActivity
    ) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber)
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(activity)
            .setCallbacks(callbacks)
            .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    // Вход с кодом из SMS
    fun signInWithPhoneAuthCredential(
        credential: PhoneAuthCredential,
        callback: (FirebaseUser?, String?) -> Unit
    ) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(auth.currentUser, null)
                } else {
                    callback(null, task.exception?.message)
                }
            }
    }

    // Сброс пароля
    fun resetPassword(email: String, callback: (Boolean, String?) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, task.exception?.message)
                }
            }
    }

    // Выход
    fun logout() {
        auth.signOut()
    }

    // Проверка авторизации
    fun getCurrentUser(): FirebaseUser? = auth.currentUser
}