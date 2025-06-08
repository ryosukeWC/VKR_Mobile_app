package com.spau.rwc.authentication.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.spau.rwc.admins.MainActivityAdmin
import com.spau.rwc.authentication.SessionManager
import com.spau.rwc.authentication.data.AuthActivity
import com.spau.rwc.ui.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Здесь делаем проверку и запускаем соответствующую активность
        if (isAdminUser()) {
//            startActivity(Intent(this, MainActivityAdmin::class.java))
            startActivity(Intent(this, AuthActivity::class.java))
        } else {
            startActivity(Intent(this, MainActivity::class.java))
        }
        finish() // Закрываем SplashActivity
    }

    private fun isAdminUser(): Boolean {
        // Ваша логика проверки (например, проверка в SharedPreferences или Firebase)
//        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
//        return sharedPref.getBoolean("is_admin", false)
        return true
        // Или проверка по логину/паролю/токену
        // return checkAdminCredentials()
    }
}