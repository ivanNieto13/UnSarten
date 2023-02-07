package com.unsarten.app.view.fragments

import android.app.Activity.RESULT_OK
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.unsarten.app.Constants
import com.unsarten.app.R
import com.unsarten.app.databinding.FragmentNewOrderBinding
import com.unsarten.app.dto.SaveOrderDataInput
import com.unsarten.app.helpers.RetrofitHelper
import com.unsarten.app.model.SaveOrderData
import com.unsarten.app.room.dao.DBUserData
import com.unsarten.app.room.dao.UserData
import com.unsarten.app.service.lib.OrdersAPI
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*


class NewOrderFragment : Fragment() {
    private var _binding: FragmentNewOrderBinding? = null
    private val binding get() = _binding!!

    private var orderName: String? = null
    private var orderBudget: Float = 0.0f
    private var orderPersons: Long = 0
    private var orderOptionalIngredients: String? = null

    private var user: UserData? = null
    private var userId = ""
    private var author = ""
    private lateinit var room: DBUserData

    private var validateOrderName = false
    private var validateOrderBudget = false
    private var validateOrderPersons = false

    private val pickImage = 100
    private var imageUri: Uri? = null
    var encodedUrlImage: String? = null

    private var baseUrl = Constants.URL_SERVICES + "api/"
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewOrderBinding.inflate(inflater, container, false)
        val root: View = binding.root

        activity?.let {
            room = Room.databaseBuilder(it, DBUserData::class.java, "db_user_data").build()
        }

        lifecycleScope.launch {
            val users = room.daoUser().getUserData()
            if (users.isNotEmpty()) {
                user = users[0]
                userId = user?.userId.toString()
                author = user?.firstName + " " + user?.lastName
            }
        }

        validateInput()

        return root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            binding.ivOrderImage.setImageURI(data?.data)
            binding.ivOrderImage.visibility = ImageView.VISIBLE
            imageUri = data?.data
            var os = ByteArrayOutputStream()
            var bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, imageUri)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            var byteArray = os.toByteArray()
            val imageAsString = Base64.getEncoder().encodeToString(byteArray)
            activity?.let {
                encodedUrlImage = "data:image/"+ imageUri?.let { it1 -> getMimeType(it, it1) } +";base64,"+imageAsString
            }
        }
    }

    private fun validateInput() {
        binding.btnAddPictures.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }
        binding.btnCreateOrder.setOnClickListener {
            orderName = binding.etOrderName.editText?.text.toString()
            val orderBudgetString = binding.etOrderBudget.editText?.text.toString()
            val orderPersonsString = binding.etOrderPersons.editText?.text.toString()
            val orderOptionalIngredientsString = binding.etOrderIngredients.editText?.text.toString()

            if (orderName?.length == 0) {
                validateOrderName = false
                activity?.let {
                    MaterialAlertDialogBuilder(it)
                        .setTitle(R.string.create_order_dialog_empty_name_title_error)
                        .setMessage(R.string.create_order_dialog_empty_name_body_error)
                        .setPositiveButton(R.string.login_dialog_positive_accept) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            } else {
                validateOrderName = true
            }

            if (orderBudgetString == "") {
                validateOrderBudget = false
                activity?.let {
                    MaterialAlertDialogBuilder(it)
                        .setTitle(R.string.create_order_dialog_empty_budget_title_error)
                        .setMessage(R.string.create_order_dialog_empty_budget_body_error)
                        .setPositiveButton(R.string.login_dialog_positive_accept) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            } else {
                validateOrderBudget = true
                orderBudget = orderBudgetString.toFloat()
            }

            if (orderPersonsString == "") {
                validateOrderPersons = false
                activity?.let {
                    MaterialAlertDialogBuilder(it)
                        .setTitle(R.string.create_order_dialog_empty_persons_title_error)
                        .setMessage(R.string.create_order_dialog_empty_persons_body_error)
                        .setPositiveButton(R.string.login_dialog_positive_accept) { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            } else {
                validateOrderPersons = true
                orderPersons = orderPersonsString.toLong()
            }

            val imageString = encodedUrlImage ?: ""

            if (validateOrderName && validateOrderBudget && validateOrderPersons) {
                val ordersApi = RetrofitHelper.getInstance(baseUrl).create(OrdersAPI::class.java)
                MainScope().launch {
                    val input = SaveOrderDataInput(
                        orderName!!,
                        orderBudget,
                        orderPersons,
                        orderOptionalIngredientsString,
                        imageString,
                        userId,
                        author,
                    )
                    val result = ordersApi.saveOrderData(input)
                    Log.d("result: ", result.body().toString())
                    if (result.isSuccessful) {
                        val saveOrderData = result.body() as SaveOrderData
                        println("saveOrderData -> $saveOrderData")
                        loadFragment(OrderListFragment())
                    } else {
                        Log.d("result: ", "error" + result.message())
                    }
                }
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        if (activity != null) {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(this.id, fragment)
            transaction?.commit()
        }
    }

    private fun getMimeType(context: Context, uri: Uri): String? {
        val extension: String? = if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val mime = MimeTypeMap.getSingleton()
            mime.getExtensionFromMimeType(context.contentResolver.getType(uri))
        } else {
            MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(uri.path?.let { File(it) }).toString())
        }
        return extension
    }

}