package com.example.foodbook.util

import com.google.firebase.auth.FirebaseAuth

object CurrentUser {
    fun uid(): String = FirebaseAuth.getInstance().currentUser?.uid.orEmpty()
    fun email(): String = FirebaseAuth.getInstance().currentUser?.email.orEmpty()
}
