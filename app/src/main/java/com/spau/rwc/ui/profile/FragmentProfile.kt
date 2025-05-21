package com.spau.rwc.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.spau.rwc.authentication.data.AuthActivity
import com.spau.rwc.authentication.data.AuthViewModel
import com.spau.rwc.databinding.FragmentProfileBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


class FragmentProfile : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()
    private var authStateJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupLogoutButton()
    }

    private fun setupObservers() {
        // Отменяем предыдущую подписку, если она есть
        authStateJob?.cancel()

        // Создаем новую подписку с учетом жизненного цикла
        authStateJob = lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.authState.collect { state ->
                    when (state) {
                        is AuthViewModel.AuthState.Idle -> {
                            // Проверяем, был ли выход (чтобы не срабатывало при инициализации)
                            if (viewModel.isLoggedOut) {
                                navigateToAuthActivity()
                            }
                        }
                        // Остальные состояния
                        else -> {}
                    }
                }
            }
        }
    }

    private fun setupLogoutButton() {
        binding.btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    private fun showLogoutConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Выход из аккаунта")
            .setMessage("Вы уверены, что хотите выйти?")
            .setPositiveButton("Выйти") { _, _ ->
                viewModel.logout()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun navigateToAuthActivity() {
        val intent = Intent(requireContext(), AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        authStateJob?.cancel()
        _binding = null
        super.onDestroyView()
    }
}