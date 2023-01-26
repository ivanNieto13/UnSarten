package com.unsarten.app.view.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.unsarten.app.R
import com.unsarten.app.databinding.FragmentLoginBinding
import com.unsarten.app.dto.VerifyNumberInput
import com.unsarten.app.helpers.RetrofitHelper
import com.unsarten.app.model.VerifyNumber
import com.unsarten.app.service.lib.LoginAPI
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.Serializable

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var confirmCodeFragment: ConfirmCodeFragment
    private var phoneNumber: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
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
                        val loginApi = RetrofitHelper.getInstance().create(LoginAPI::class.java)
                        MainScope().launch {
                            val input = VerifyNumberInput("+52" + phoneNumber!!)
                            val result = loginApi.verifyNumber(input)
                            Log.d("result: ", result.body().toString())
                            if (result.isSuccessful) {
                                val verifyNumber = result.body() as VerifyNumber
                                beginTransaction(verifyNumber)
                            } else {
                                Log.d("result: ", "error")
                            }
                        }

                    }
                    .setNegativeButton(R.string.login_dialog_negative) { dialog, which ->
                        println("Editar")
                    }
                    .show()
            }
        }
    }

    private fun beginTransaction(verifyNumber: VerifyNumber) {
        val serial = verifyNumber as Serializable
        val navController = NavHostFragment.findNavController(this)
        val bundle = Bundle()
        bundle.putSerializable("verifyNumber", serial)
        bundle.putString("phoneNumber", verifyNumber.data.VerifyNumber.phoneNumber)
        navController.navigate(R.id.action_loginFragment_to_confirmCodeFragment, bundle)
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