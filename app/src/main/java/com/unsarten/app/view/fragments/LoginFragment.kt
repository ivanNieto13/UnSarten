package com.unsarten.app.view.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.unsarten.app.R
import com.unsarten.app.databinding.LoginFragmentBinding

class LoginFragment : Fragment() {
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var confirmCodeFragment: ConfirmCodeFragment
    private var phoneNumber: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        validateInput()

        return root
    }

    private fun validateInput() {
        binding.btnLogin.setOnClickListener {
            phoneNumber = binding.etPhoneNumber.editText?.text.toString()
            activity?.let {
                MaterialAlertDialogBuilder(it)
                    .setTitle(R.string.login_dialog_title)
                    .setMessage("${getString(R.string.login_dialog_body)} $phoneNumber?")
                    .setPositiveButton(R.string.login_dialog_positive) { dialog, which ->
                        confirmCodeFragment = ConfirmCodeFragment()
                        activity?.supportFragmentManager?.beginTransaction()
                            ?.replace(this.id, confirmCodeFragment)
                            ?.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                            ?.addToBackStack(null)
                            ?.apply {
                                confirmCodeFragment.arguments = Bundle().apply {
                                    putString("phoneNumber", phoneNumber)
                                }
                            }
                            ?.commit()
                    }
                    .setNegativeButton(R.string.login_dialog_negative) { dialog, which ->
                        println("Editar")
                    }
                    .show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (phoneNumber != null) {
            binding.etPhoneNumber.editText?.setText(phoneNumber)
            showSoftKeyboard(binding.etPhoneNumber, binding.etPhoneNumber.editText)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showSoftKeyboard(view: View, editText: EditText?) {
        if (view.requestFocus()) {
            activity?.let {
                editText?.setSelection(10)
                val imm = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
            }
        }
    }
}