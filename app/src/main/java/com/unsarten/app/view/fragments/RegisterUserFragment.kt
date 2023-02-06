package com.unsarten.app.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.unsarten.app.R
import com.unsarten.app.databinding.FragmentRegisterUserBinding
import com.unsarten.app.dto.SaveUserDataInput
import com.unsarten.app.dto.VerifyCodeInput
import com.unsarten.app.helpers.RetrofitHelper
import com.unsarten.app.model.SaveUserData
import com.unsarten.app.model.VerifyCode
import com.unsarten.app.model.VerifyNumber
import com.unsarten.app.room.dao.DBUserData
import com.unsarten.app.room.dao.UserData
import com.unsarten.app.service.lib.LoginAPI
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class RegisterUserFragment : Fragment() {
    private var _binding: FragmentRegisterUserBinding? = null
    private val binding get() = _binding!!

    private lateinit var room: DBUserData

    private var phoneNumber: String? = null
    private var verifyNumber: VerifyNumber? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        activity?.let {
            room = Room.databaseBuilder(it, DBUserData::class.java, "db_user_data").build()
        }

        verifyNumber = arguments?.getSerializable("verifyNumber") as VerifyNumber?
        phoneNumber = verifyNumber?.data?.VerifyNumber?.phoneNumber

        binding.btnRegisterUser.setOnClickListener {
            val name = binding.etFirstName.editText?.text.toString()
            val lastName = binding.etLastname.editText?.text.toString()
            val email = binding.etEmail.editText?.text.toString()

            if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                println("Registrar usuario: $name $lastName $email $phoneNumber")
                val loginApi = RetrofitHelper.getInstance().create(LoginAPI::class.java)
                MainScope().launch {
                    val input = SaveUserDataInput(name, lastName, email, phoneNumber!!)
                    val result = loginApi.saveUserData(input)
                    Log.d("result: ", result.body().toString())
                    if (result.isSuccessful) {
                        val saveUserData = result.body() as SaveUserData
                        if (saveUserData.data.SaveUserData.userId != "") {
                            saveUserData(saveUserData)
                        } else {
                            // error
                        }
                    } else {
                        Log.d("result: ", "error")
                    }
                }
            } else {
                binding.etEmail.editText?.error = getString(R.string.invalid_email)
            }
        }

        return root
    }

    private fun saveUserData(user: SaveUserData) {
        val userData = UserData(
            user.data.SaveUserData.userId,
            user.data.SaveUserData.email,
            user.data.SaveUserData.firstName,
            user.data.SaveUserData.lastName,
            user.data.SaveUserData.phoneNumber,
        )
        lifecycleScope.launch {
            room.daoUser().addUser(userData)
        }
    }

    private fun onBackPressed() {
        activity?.onBackPressed()
    }


}