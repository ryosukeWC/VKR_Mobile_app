package com.spau.rwc.authentication.data

import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class PhoneAuthCallbacks(
    private val onCodeSent: (String, PhoneAuthProvider.ForceResendingToken) -> Unit,
    private val onVerificationCompleted: (PhoneAuthCredential) -> Unit,
    private val onVerificationFailed: (Exception) -> Unit,
    private val onCodeAutoRetrievalTimeout: (String) -> Unit
) : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

    override fun onCodeSent(
        verificationId: String,
        token: PhoneAuthProvider.ForceResendingToken
    ) {
        onCodeSent(verificationId, token)
    }

    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        onVerificationCompleted(credential)
    }

    override fun onVerificationFailed(p0: FirebaseException) {
        onVerificationFailed(p0)
    }

    override fun onCodeAutoRetrievalTimeOut(verificationId: String) {
        onCodeAutoRetrievalTimeout(verificationId)
    }
}