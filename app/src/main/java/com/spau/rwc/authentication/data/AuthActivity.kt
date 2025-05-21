package com.spau.rwc.authentication.data

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.spau.rwc.authentication.registration.RegisterBottomSheetFragment
import com.spau.rwc.databinding.ActivityAuthBinding
import com.spau.rwc.ui.main.MainActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()
    private var authStateJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {

        authStateJob = lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authState.collect { state ->
                    handleAuthState(state)
                }
            }
        }
    }

    private fun handleAuthState(state: AuthViewModel.AuthState) {
        when (state) {
            is AuthViewModel.AuthState.Loading -> {
                showProgress(true)
            }
            is AuthViewModel.AuthState.Success -> {
                showProgress(false)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            is AuthViewModel.AuthState.Error -> {
                showProgress(false)
                Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
            }
            is AuthViewModel.AuthState.VerificationEmailSent -> {
                showProgress(false)
                Toast.makeText(this, "Verification email sent", Toast.LENGTH_SHORT).show()
            }
            is AuthViewModel.AuthState.PasswordResetSent -> {
                showProgress(false)
                Toast.makeText(this, "Password reset email sent", Toast.LENGTH_SHORT).show()
            }
            is AuthViewModel.AuthState.CodeSent -> {
                showProgress(false)
                showCodeInput(state.verificationId)
            }
            else -> showProgress(false)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        authStateJob?.cancel() // Важно отменять подписку при уничтожении
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            binding.btnRegister.setOnClickListener {
                showRegisterBottomSheet()
            }
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (validateInput(email, password)) {
                viewModel.loginWithEmail(email, password)
            }
        }

        binding.tvResetPassword.setOnClickListener {
            val email = binding.etEmail.text.toString()
            if (email.isNotEmpty()) {
                viewModel.resetPassword(email)
            } else {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private fun showProgress(show: Boolean) {
        binding.progressBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun showCodeInput(verificationId: String) {
        // Реализуйте экран для ввода кода из SMS
    }

    private fun showRegisterBottomSheet() {
        val bottomSheet = RegisterBottomSheetFragment()
        bottomSheet.onRegisterClicked = { name, email, phone, password ->
            viewModel.registerWithEmail(name, email, phone, password)
        }
        bottomSheet.show(supportFragmentManager, RegisterBottomSheetFragment.TAG)
    }
}