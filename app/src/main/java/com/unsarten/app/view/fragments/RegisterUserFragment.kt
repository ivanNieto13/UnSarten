package com.unsarten.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.unsarten.app.R
import com.unsarten.app.databinding.FragmentRegisterUserBinding

class RegisterUserFragment : Fragment() {
    private var _binding: FragmentRegisterUserBinding? = null
    private val binding get() = _binding!!

    private var phoneNumber: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        phoneNumber = arguments?.getString("phoneNumber")

        binding.btnRegisterUser.setOnClickListener {
            val name = binding.etFirstName.editText?.text.toString()
            val lastName = binding.etLastname.editText?.text.toString()
            val email = binding.etEmail.editText?.text.toString()

            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                println("Registrar usuario: $name $lastName $email $phoneNumber")
            } else {
                binding.etEmail.editText?.error = getString(R.string.invalid_email)
            }
        }

        return root
    }

    private fun onBackPressed() {
        activity?.onBackPressed()
    }


}