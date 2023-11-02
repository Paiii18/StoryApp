package com.example.submission1storyapp.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.submission1storyapp.R
import com.example.submission1storyapp.databinding.ActivitySignupBinding
import com.example.submission1storyapp.view.login.LoginActivity

class SignUpActivity : AppCompatActivity() {

    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        viewModel.isLoading.observe(this) {
            load(it)
        }
        initAction()
        playAnim()
    }

    @SuppressLint("Recycle")
    private fun playAnim() {
        ObjectAnimator.ofFloat(binding.Ivsignup, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.Tvsignup, View.ALPHA, 1f).setDuration(100)

        val nama = ObjectAnimator.ofFloat(binding.Tvname, View.ALPHA, 1f).setDuration(100)
        val inputNama = ObjectAnimator.ofFloat(binding.ETname, View.ALPHA, 1f).setDuration(100)

        val email = ObjectAnimator.ofFloat(binding.Tvemail, View.ALPHA, 1f).setDuration(100)
        val inputEmail = ObjectAnimator.ofFloat(binding.ETemail, View.ALPHA, 1f).setDuration(100)

        val password = ObjectAnimator.ofFloat(binding.Tvpass, View.ALPHA, 1f).setDuration(100)
        val inputPassword = ObjectAnimator.ofFloat(binding.ETpass, View.ALPHA, 1f).setDuration(100)

        val signup = ObjectAnimator.ofFloat(binding.signupButton, View.ALPHA, 1f).setDuration(100)
        val toLogin =
            ObjectAnimator.ofFloat(binding.layoutTextRegister, View.ALPHA, 1f).setDuration(100)

        val together = AnimatorSet().apply {
            playTogether(
                title,
                nama,
                inputNama,
                email,
                inputEmail,
                password,
                inputPassword,
                signup,
                toLogin
            )
        }

        AnimatorSet().apply {
            playSequentially(
                title,
                nama,
                inputNama,
                email,
                inputEmail,
                password,
                inputPassword,
                signup,
                toLogin
            )
            start()
        }
    }

    private fun initAction() {
        binding.tvToLogin.setOnClickListener {
            LoginActivity.start(this)
        }

        binding.signupButton.setOnClickListener {
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val pass = binding.passwordEditText.text.toString().trim()

            when {
                name.isBlank() -> {
                    binding.nameEditText.requestFocus()
                    binding.nameEditText.error = getString(R.string.error_empty_name)
                }

                email.isBlank() -> {
                    binding.emailEditText.requestFocus()
                    binding.emailEditText.error = getString(R.string.error_empty_email)
                }

                !email.isEmailValid() -> {
                    binding.emailEditText.requestFocus()
                    binding.emailEditText.error = getString(R.string.error_invalid_email)
                }

                pass.isBlank() -> {
                    binding.passwordEditText.requestFocus()
                    binding.passwordEditText.error = getString(R.string.error_empty_password)
                }

                pass.length < 8 -> {
                    binding.passwordEditText.requestFocus()
                    binding.passwordEditText.error = getString(R.string.error_short_password)
                }

                else -> {
                    viewModel.registerUser(name, email, pass)
                    viewModel.isMessage.observe(this) { isSuccess ->
                        Log.i("sukses", "$isSuccess ")
                        if (isSuccess == getString(R.string.success)) {
                            messageToast(getString(R.string.message_register_success))
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish()


                        } else if (isSuccess == getString(R.string.email_is_already_taken)) {
                            messageToast(getString(R.string.email_sudah_ada))
                        }
                    }
                }
            }
        }

    }

    private fun messageToast(str: String) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
    }

    private fun load(result: Boolean) {
        if (result) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, SignUpActivity::class.java)
            context.startActivity(intent)
        }
    }
}
