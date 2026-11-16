package com.example.foodbook.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.foodbook.data.local.AppDatabase
import com.example.foodbook.data.local.UserEntity
import com.example.foodbook.util.Resource
import com.example.foodbook.util.SecurePrefs
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel(app: Application) : AndroidViewModel(app) {
    private val auth = FirebaseAuth.getInstance()
    private val userDao = AppDatabase.get(app).userDao()
    private val prefs = SecurePrefs(app)

    private val _state = MutableLiveData<Resource<Unit>>()
    val state: LiveData<Resource<Unit>> = _state

    fun login(email: String, password: String) =
        submit(email, password) { auth.signInWithEmailAndPassword(email, password).await() }

    fun register(email: String, password: String) =
        submit(email, password) { auth.createUserWithEmailAndPassword(email, password).await() }

    private fun submit(email: String, password: String, action: suspend () -> AuthResult) {
        if (email.isBlank() || password.length < 6) {
            _state.value = Resource.Error("Nhập email và mật khẩu (tối thiểu 6 ký tự)")
            return
        }
        _state.value = Resource.Loading
        viewModelScope.launch {
            try {
                val user = action().user!!
                userDao.insert(UserEntity(user.uid, user.email.orEmpty()))
                prefs.saveEmail(user.email.orEmpty())
                _state.value = Resource.Success(Unit)
            } catch (e: Exception) {
                _state.value = Resource.Error(e.localizedMessage ?: "Đăng nhập thất bại")
            }
        }
    }

    fun savedEmail(): String = prefs.email()

    fun logout() {
        auth.signOut()
        prefs.clear()
        viewModelScope.launch { userDao.clear() }
    }
}
