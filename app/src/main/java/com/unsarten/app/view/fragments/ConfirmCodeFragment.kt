package com.unsarten.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.unsarten.app.R
import com.unsarten.app.databinding.FragmentConfirmCodeBinding


class ConfirmCodeFragment : Fragment() {
    private var _binding: FragmentConfirmCodeBinding? = null
    private val binding get() = _binding!!

    private var codeNumber: String? = null
    private var phoneNumber: String? = null

    private lateinit var registerUserFragment: RegisterUserFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmCodeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        phoneNumber = arguments?.getString("phoneNumber")

        binding.tvConfirmCodeTitle.text = getString(R.string.action_confirm_code, phoneNumber)

        binding.btnConfirmCode.setOnClickListener {
            codeNumber = binding.etCodeNumber.editText?.text.toString()
            if (codeNumber?.length!! < 4) {
                binding.etCodeNumber.editText?.error = getString(R.string.invalid_code_number)
            } else {
                registerUserFragment = RegisterUserFragment()
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(this.id, registerUserFragment)
                    ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    ?.addToBackStack(null)
                    ?.apply {
                        registerUserFragment.arguments = Bundle().apply {
                            putString("phoneNumber", phoneNumber)
                        }
                    }
                    ?.commit()
            }
        }

        binding.btnEditPhoneNumber.setOnClickListener {
            onBackPressed()
        }

        return root
    }

    private fun onBackPressed() {
        activity?.onBackPressed()
    }


}