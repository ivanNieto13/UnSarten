package com.unsarten.app.view.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.unsarten.app.R
import com.unsarten.app.activties.HomeActivity
import com.unsarten.app.databinding.FragmentConfirmCodeBinding
import com.unsarten.app.dto.VerifyCodeInput
import com.unsarten.app.helpers.RetrofitHelper
import com.unsarten.app.model.VerifyCode
import com.unsarten.app.model.VerifyNumber
import com.unsarten.app.service.lib.LoginAPI
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.Serializable


class ConfirmCodeFragment : Fragment() {
    private var _binding: FragmentConfirmCodeBinding? = null
    private val binding get() = _binding!!

    private var codeNumber: String? = null
    private var phoneNumber: String? = null
    private var verifyNumber: VerifyNumber? = null

    private var showInputError = false

    private lateinit var registerUserFragment: RegisterUserFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConfirmCodeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        phoneNumber = arguments?.getString("phoneNumber")
        verifyNumber = arguments?.getSerializable("verifyNumber") as VerifyNumber?

        binding.tvConfirmCodeTitle.text = getString(R.string.action_confirm_code, phoneNumber)

        binding.btnConfirmCode.setOnClickListener {
            codeNumber = binding.etCodeNumber.editText?.text.toString()
            if (codeNumber?.length!! < 4) {
                binding.etCodeNumber.editText?.error = getString(R.string.invalid_code_number)
                activity?.let {
                    MaterialAlertDialogBuilder(it)
                        .setTitle(R.string.login_dialog_code_title_error)
                        .setMessage(R.string.invalid_code_number)
                        .setPositiveButton(R.string.login_dialog_positive_accept) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            } else {
                if (verifyNumber?.data?.VerifyNumber?.isVerified!!) {
                    beginTransactionToHome()
                } else {
                    val loginApi = RetrofitHelper.getInstance().create(LoginAPI::class.java)
                    MainScope().launch {
                        val input = VerifyCodeInput(codeNumber!!)
                        val result = loginApi.verifyCode(input)
                        Log.d("result: ", result.body().toString())
                        if (result.isSuccessful) {
                            val verifyCode = result.body() as VerifyCode
                            if (verifyCode.data.VerifyCode.isValid) {
                                beginTransactionToRegisterUserFragment(verifyCode)
                            } else {
                                binding.etCodeNumber.editText?.error =
                                    getString(R.string.invalid_code_number)
                            }
                        } else {
                            binding.etCodeNumber.editText?.error =
                                getString(R.string.invalid_code_number)
                            Log.d("result: ", "error")
                        }
                    }
                }
            }
        }

        binding.btnEditPhoneNumber.setOnClickListener {
            onBackPressed()
        }

        return root
    }

    private fun beginTransactionToRegisterUserFragment(verifyCode: VerifyCode) {
        registerUserFragment = RegisterUserFragment()
        val verifyCodeSerial = verifyCode as Serializable
        val verifyNumberSerial = verifyNumber as Serializable
        val navController = NavHostFragment.findNavController(this)
        val bundle = Bundle()
        bundle.putSerializable("verifyNumber", verifyNumberSerial)
        bundle.putSerializable("verifyCode", verifyCodeSerial)
        navController.navigate(R.id.action_confirmCodeFragment_to_registerUserFragment, bundle)
    }

    private fun beginTransactionToHome() {
        val intent = Intent(activity, HomeActivity::class.java)
        activity?.startActivity(intent)
        activity?.finish()
    }

    private fun onBackPressed() {
        activity?.onBackPressed()
    }


}