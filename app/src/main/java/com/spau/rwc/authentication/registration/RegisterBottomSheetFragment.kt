package com.spau.rwc.authentication.registration

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.spau.rwc.R
import com.spau.rwc.databinding.FragmentRegisterBottomSheetBinding

class RegisterBottomSheetFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentRegisterBottomSheetBinding? = null
    private val binding get() = _binding!!
    var onRegisterClicked: ((String, String, String, String) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        setupValidation()
    }

    private fun setupViews() {
        binding.btnRegister.setOnClickListener {
            val name = binding.etFullName.text.toString()
            val email = binding.etEmail.text.toString()
            val phone = binding.etPhone.text.toString()
            val password = binding.etPassword.text.toString()

            onRegisterClicked?.invoke(name, email, phone, password)
            dismiss()
        }
    }

    private fun setupValidation() {
        val fields = listOf(
            binding.etFullName,
            binding.etEmail,
            binding.etPhone,
            binding.etPassword
        )

        fields.forEach { field ->
            field.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    validateFields()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    private fun validateFields() {
        val allFilled = listOf(
            binding.etFullName.text?.isNotEmpty() == true,
            binding.etEmail.text?.let {
                it.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()
            } ?: false,
            binding.etPhone.text?.isNotEmpty() == true,
            binding.etPassword.text?.let {
                it.isNotEmpty() && it.length >= 6
            } ?: false
        ).all { it }

        binding.btnRegister.isEnabled = allFilled
        binding.btnRegister.backgroundTintList = ColorStateList.valueOf(
            ContextCompat.getColor(
                requireContext(),
                if (allFilled) R.color.green else R.color.gray
            )
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "RegisterBottomSheet"
    }
}