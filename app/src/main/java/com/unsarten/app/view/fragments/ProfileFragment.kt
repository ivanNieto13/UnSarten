package com.unsarten.app.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.unsarten.app.databinding.FragmentProfileBinding
import com.unsarten.app.room.dao.DBUserData
import com.unsarten.app.room.dao.UserData
import kotlinx.coroutines.launch

class ProfileFragment(userData: UserData) : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private lateinit var room: DBUserData
    private val user = userData

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val author = user.firstName + " " + user.lastName

        activity?.let {
            room = Room.databaseBuilder(it, DBUserData::class.java, "db_user_data").build()
        }

        binding.userName.text = author
        binding.userEmail.text = user.email

        binding.logoutButton.setOnClickListener {
            lifecycleScope.launch {
                room.daoUser().deleteAllUsers()
                activity?.finish()
            }
        }

        return root
    }
}
