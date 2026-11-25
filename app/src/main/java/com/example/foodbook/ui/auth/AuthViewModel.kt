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
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
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
                _state.value = Resource.Error(toVietnamese(e))
            }
        }
    }

    // Thông báo ngắn cho người dùng (vd: đã gửi email đặt lại mật khẩu)
    private val _info = MutableLiveData<String?>()
    val info: LiveData<String?> = _info
    fun infoShown() { _info.value = null }

    /** Quên mật khẩu: Firebase gửi email đặt lại mật khẩu cho tài khoản. */
    fun resetPassword(email: String) {
        if (email.isBlank()) {
            _info.value = "Nhập email vào ô Email rồi bấm Quên mật khẩu."
            return
        }
        viewModelScope.launch {
            _info.value = try {
                auth.sendPasswordResetEmail(email).await()
                "Đã gửi email đặt lại mật khẩu tới $email. Hãy kiểm tra hộp thư (cả mục Spam)."
            } catch (e: Exception) {
                toVietnamese(e)
            }
        }
    }

    private fun toVietnamese(e: Exception): String = when (e) {
        is FirebaseAuthUserCollisionException -> "Email này đã được đăng ký. Hãy bấm Đăng nhập."
        is FirebaseAuthWeakPasswordException -> "Mật khẩu quá yếu (tối thiểu 6 ký tự)."
        is FirebaseAuthInvalidUserException,
        is FirebaseAuthInvalidCredentialsException ->
            "Email hoặc mật khẩu không đúng. Nếu chưa có tài khoản, hãy bấm Đăng ký tài khoản mới."
        is FirebaseNetworkException -> "Không có kết nối mạng. Vui lòng kiểm tra Wi-Fi/4G."
        else -> "Có lỗi xảy ra, vui lòng thử lại."
    }

    fun savedEmail(): String = prefs.email()

    fun logout() {
        auth.signOut()
        prefs.clear()
        viewModelScope.launch { userDao.clear() }
    }
}
