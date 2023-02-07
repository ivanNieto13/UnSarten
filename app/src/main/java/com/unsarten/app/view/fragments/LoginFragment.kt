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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.room.Room
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.unsarten.app.Constants
import com.unsarten.app.R
import com.unsarten.app.databinding.FragmentLoginBinding
import com.unsarten.app.dto.VerifyNumberInput
import com.unsarten.app.helpers.RetrofitHelper
import com.unsarten.app.model.VerifyNumber
import com.unsarten.app.room.dao.DBUserData
import com.unsarten.app.room.dao.UserData
import com.unsarten.app.service.lib.LoginAPI
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.Serializable

class LoginFragment : Fragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var room: DBUserData
    private var phoneNumber: String? = null

    private var baseUrl = Constants.URL_SERVICES + "api/"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val root: View = binding.root

        activity?.let {
            room = Room.databaseBuilder(it, DBUserData::class.java, "db_user_data").build()
        }
        lifecycleScope.launch {
            val users = room.daoUser().getUserData()
            if (users.isNotEmpty()) {
                val bundle = Bundle()
                val user = users[0] as Serializable
                bundle.putSerializable("user", user)
                NavHostFragment.findNavController(this@LoginFragment)
                    .navigate(R.id.action_loginFragment_to_homeActivity, bundle)
            }
        }


        validateInput()

        return root
    }

    private fun validateInput() {
        binding.btnLogin.setOnClickListener {
            phoneNumber = binding.etPhoneNumber.editText?.text.toString()
            if (phoneNumber?.length == 0) {
                activity?.let {
                    MaterialAlertDialogBuilder(it)
                        .setTitle(R.string.login_dialog_title_error)
                        .setMessage(R.string.login_dialog_body_error)
                        .setPositiveButton(R.string.login_dialog_positive_accept) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
            if (phoneNumber?.length in 1..9) {
                binding.etPhoneNumber.editText?.error = getString(com.unsarten.app.R.string.login_dialog_title_error)
            }
            if (phoneNumber?.length == 10) {
                activity?.let {
                    MaterialAlertDialogBuilder(it)
                        .setTitle(R.string.login_dialog_title)
                        .setMessage("${getString(R.string.login_dialog_body)} $phoneNumber?")
                        .setPositiveButton(R.string.login_dialog_positive) { dialog, which ->
                            val loginApi = RetrofitHelper.getInstance(baseUrl).create(LoginAPI::class.java)
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
                        .setNegativeButton(R.string.login_dialog_negative) { dialog, _ ->
                            dialog.dismiss()
                        }

                        .show()
                }
            }
        }
    }

    private fun beginTransaction(verifyNumber: VerifyNumber) {
        val serial = verifyNumber as Serializable
        val navController = NavHostFragment.findNavController(this)
        val bundle = Bundle()
        bundle.putSerializable("verifyNumber", serial)
        bundle.putString("phoneNumber", verifyNumber.data.VerifyNumber.phoneNumber)
        if (verifyNumber.data.VerifyNumber.userId != null && verifyNumber.data.VerifyNumber.userId != "") {
            val user = UserData(
                verifyNumber.data.VerifyNumber.userId,
                verifyNumber.data.VerifyNumber.email ?: "",
                verifyNumber.data.VerifyNumber.firstName ?: "",
                verifyNumber.data.VerifyNumber.lastName ?: "",
                verifyNumber.data.VerifyNumber.phoneNumber,
            )
            lifecycleScope.launch {
                room.daoUser().addUser(user)
            }
        }
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
                imm.toggleSoftInput(
                    InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY
                )
            }
        }
    }
}