package com.unsarten.app.activties

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.unsarten.app.R
import com.unsarten.app.databinding.ActivityHomeBinding
import com.unsarten.app.room.dao.UserData
import com.unsarten.app.view.fragments.*

class HomeActivity : AppCompatActivity() {
    private var _binding: ActivityHomeBinding? = null
    private val binding get() = _binding!!

    private val itemByDefault = R.id.order_list

    private lateinit var bottomNav: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val myIntent = intent
        val user = myIntent.getSerializableExtra("user") as UserData
        binding.tvViewTitle.text = getString(R.string.orders_list)
        loadFragment(OrderListFragment())
        bottomNav = findViewById(binding.bottomNav.id)
        bottomNav.menu.findItem(itemByDefault).isChecked = true
        bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.chat_list -> {
                    binding.tvViewTitle.text = getString(R.string.chats_title)
                    loadFragment(ChatListFragment())
                    true
                }
                R.id.order_list -> {
                    binding.tvViewTitle.text = getString(R.string.orders_list)
                    loadFragment(OrderListFragment())
                    true
                }
                R.id.new_order -> {
                    binding.tvViewTitle.text = getString(R.string.action_create_order)
                    loadFragment(NewOrderFragment())
                    true
                }
                R.id.profile_user -> {
                    binding.tvViewTitle.text = getString(R.string.profile_title)
                    loadFragment(ProfileFragment(user))
                    true
                }
                else -> {
                    loadFragment(BlankFragment())
                    true
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}