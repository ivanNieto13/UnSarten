package com.unsarten.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.unsarten.app.databinding.ActivityMainBinding
import com.unsarten.app.view.fragments.ConfirmCodeFragment
import com.unsarten.app.view.fragments.LoginFragment

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var loginFragment: LoginFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        loginFragment = LoginFragment()
        setContentView(binding.root)
        supportFragmentManager
            .beginTransaction()
            .add(binding.flLoginFragment.id, loginFragment)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        println("onResume")

    }
}
