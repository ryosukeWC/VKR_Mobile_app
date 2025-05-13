package com.spau.rwc.authentication.data

import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.PhoneAuthCredential
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val authManager = AuthManager()

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // Состояния аутентификации
    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val user: FirebaseUser) : AuthState()
        data class Error(val message: String) : AuthState()
        object VerificationEmailSent : AuthState()
        object PasswordResetSent : AuthState()
        data class CodeSent(val verificationId: String) : AuthState()
    }

    // Регистрация по email
    fun registerWithEmail(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            authManager.registerWithEmail(email, password) { user, error ->
                if (user != null) {
                    _authState.value = AuthState.VerificationEmailSent
                } else {
                    _authState.value = AuthState.Error(error ?: "Unknown error")
                }
            }
        }
    }

    // Вход по email
    fun loginWithEmail(email: String, password: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            authManager.loginWithEmail(email, password) { user, error ->
                if (user != null) {
                    _authState.value = AuthState.Success(user)
                } else {
                    _authState.value = AuthState.Error(error ?: "Unknown error")
                }
            }
        }
    }

    // Отправка кода на телефон
    fun sendPhoneVerificationCode(phoneNumber: String, activity: ComponentActivity) {
        _authState.value = AuthState.Loading

        val callbacks = PhoneAuthCallbacks(
            onCodeSent = { verificationId, _ ->
                _authState.value = AuthState.CodeSent(verificationId)
            },
            onVerificationCompleted = { credential ->
                verifyPhoneCode(credential)
            },
            onVerificationFailed = { e ->
                _authState.value = AuthState.Error(e.message ?: "Verification failed")
            },
            onCodeAutoRetrievalTimeout = { verificationId ->
                _authState.value = AuthState.CodeSent(verificationId)
            }
        )

        authManager.sendPhoneVerificationCode(phoneNumber, callbacks, activity)
    }

    // Проверка кода из SMS
    fun verifyPhoneCode(credential: PhoneAuthCredential) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            authManager.signInWithPhoneAuthCredential(credential) { user, error ->
                if (user != null) {
                    _authState.value = AuthState.Success(user)
                } else {
                    _authState.value = AuthState.Error(error ?: "Unknown error")
                }
            }
        }
    }

    // Сброс пароля
    fun resetPassword(email: String) {
        _authState.value = AuthState.Loading
        viewModelScope.launch {
            authManager.resetPassword(email) { success, error ->
                if (success) {
                    _authState.value = AuthState.PasswordResetSent
                } else {
                    _authState.value = AuthState.Error(error ?: "Unknown error")
                }
            }
        }
    }

    // Выход
    fun logout() {
        authManager.logout()
        _authState.value = AuthState.Idle
    }
}