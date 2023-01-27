package com.unsarten.app.activties

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.unsarten.app.R
import com.unsarten.app.databinding.ActivityHomeBinding
import com.unsarten.app.view.fragments.BlankFragment

class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private val itemByDefault = R.id.order_list

    private lateinit var bottomNav : BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadFragment(BlankFragment())
        bottomNav = findViewById(binding.bottomNav.id)
        bottomNav.menu.findItem(itemByDefault).isChecked = true
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.order_list -> {
                    loadFragment(BlankFragment())
                    true
                }
                R.id.chat_list -> {
                    loadFragment(BlankFragment())
                    true
                }
                R.id.new_order -> {
                    loadFragment(BlankFragment())
                    true
                }
                R.id.profile_user -> {
                    loadFragment(BlankFragment())
                    true
                }
                else -> {
                    loadFragment(BlankFragment())
                    true
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }
}