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

    // Регистрация по email и паролю
    fun registerWithEmail(
        email: String,
        password: String,
        callback: (FirebaseUser?, String?) -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.sendEmailVerification()?.addOnCompleteListener { verificationTask ->
                        if (verificationTask.isSuccessful) {
                            callback(user, null)
                        } else {
                            callback(null, verificationTask.exception?.message)
                        }
                    } ?: run {
                        callback(null, "User is null")
                    }
                } else {
                    callback(null, task.exception?.message)
                }
            }
    }

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

                            // 2. Сохраняем дополнительные данные в Firestore
//                            saveUserData(firebaseUser.uid, name, email, phone) { dbError ->
//                                if (dbError != null) {
//                                    callback(null, dbError)
//                                } else {
//                                    callback(firebaseUser, null)
//                                }
//                            }
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

//    private fun saveUserData(
//        userId: String,
//        name: String,
//        email: String,
//        phone: String,
//        callback: (String?) -> Unit
//    ) {
//        val userData = hashMapOf(
//            "name" to name,
//            "email" to email,
//            "phone" to phone,
//            "createdAt" to FieldValue.serverTimestamp(),
//            "isAdmin" to false // По умолчанию пользователь не админ
//        )
//
//        db.collection("users")
//            .document(userId)
//            .set(userData)
//            .addOnSuccessListener {
//                callback(null)
//            }
//            .addOnFailureListener { e ->
//                callback(e.message)
//            }
//    }

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