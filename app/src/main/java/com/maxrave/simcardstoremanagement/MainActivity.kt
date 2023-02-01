package com.maxrave.simcardstoremanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.maxrave.simcardstoremanagement.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAuth = FirebaseAuth.getInstance()
        binding.etEmail.doOnTextChanged { text, start, before, count ->
            binding.tvIncorrectPassword.visibility = android.view.View.GONE
        }
        binding.etPassword.doOnTextChanged { text, start, before, count ->
            binding.tvIncorrectPassword.visibility = android.view.View.GONE
        }
        binding.btLogin.setOnClickListener() {
            if (binding.etEmail.text.toString().isNotEmpty() && binding.etPassword.text.toString().isNotEmpty()) {
                binding.maskedLoadingView.visibility = android.view.View.VISIBLE
                binding.progressBar.visibility = android.view.View.VISIBLE
                firebaseAuth.signInWithEmailAndPassword(binding.etEmail.text.toString(), binding.etPassword.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information

                            val user = firebaseAuth.currentUser
                            val intent = Intent(this, AdminActivity::class.java)
                            binding.progressBar.visibility = android.view.View.GONE
                            binding.maskedLoadingView.visibility = android.view.View.GONE
                            startActivity(intent)
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        } else {
//                            Toast.makeText(this, "Login Failed", Toast.LENGTH_SHORT).show()
                            val loginLayoutView = binding.loginLayout
                            Snackbar.make(loginLayoutView, "Đăng nhập thất bại", Snackbar.LENGTH_SHORT).
                            setAction("Bỏ qua"){
                                Snackbar.Callback()
                            }.show()
                            binding.progressBar.visibility = android.view.View.GONE
                            binding.maskedLoadingView.visibility = android.view.View.GONE
                            binding.tvIncorrectPassword.visibility = android.view.View.VISIBLE
                        }
                    }
            }
        }


    }
}