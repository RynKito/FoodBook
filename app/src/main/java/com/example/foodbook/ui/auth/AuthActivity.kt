package com.example.foodbook.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.foodbook.MainActivity
import com.example.foodbook.databinding.ActivityAuthBinding
import com.example.foodbook.util.Resource
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAuthBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (FirebaseAuth.getInstance().currentUser != null) { openMain(); return }

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener {
            viewModel.login(binding.etEmail.text.toString().trim(), binding.etPassword.text.toString())
        }
        binding.btnRegister.setOnClickListener {
            viewModel.register(binding.etEmail.text.toString().trim(), binding.etPassword.text.toString())
        }

        binding.tvForgot.setOnClickListener {
            viewModel.resetPassword(binding.etEmail.text.toString().trim())
        }
        viewModel.info.observe(this) { msg ->
            if (msg != null) {
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
                viewModel.infoShown()
            }
        }

        viewModel.state.observe(this) { state ->
            val loading = state is Resource.Loading
            binding.progress.visibility = if (loading) View.VISIBLE else View.GONE
            binding.btnLogin.isEnabled = !loading
            binding.btnRegister.isEnabled = !loading
            binding.tvError.visibility = View.GONE
            when (state) {
                is Resource.Success -> openMain()
                is Resource.Error -> { binding.tvError.text = state.message; binding.tvError.visibility = View.VISIBLE }
                else -> Unit
            }
        }
    }

    private fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
