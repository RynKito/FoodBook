package com.example.foodbook.util

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

/** Lưu phiên đăng nhập bằng EncryptedSharedPreferences (mã hóa AES256) */
class SecurePrefs(context: Context) {
    private val prefs = EncryptedSharedPreferences.create(
        context, "secure_prefs",
        MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    fun saveEmail(email: String) = prefs.edit().putString("email", email).apply()
    fun email(): String = prefs.getString("email", "") ?: ""
    fun clear() = prefs.edit().clear().apply()
}
