package com.unsarten.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.unsarten.app.Constants
import com.unsarten.app.R
import com.unsarten.app.databinding.FragmentOrderDetailBinding
import com.unsarten.app.model.Order


class OrderDetailFragment(inputOrder: Order) : Fragment() {
    private var _binding: FragmentOrderDetailBinding? = null
    private val binding get() = _binding!!

    private var order: Order = inputOrder

    private var baseUrl = Constants.URL_SERVICES

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrderDetailBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.tvOrderName.text = order.orderName
        binding.tvOrderBudget.text = getString(R.string.orders_detail_budget, order.budget.toString())
        binding.tvOrderPersons.text = getString(R.string.orders_detail_persons, order.persons.toString())
        binding.tvOrderIngredients.text = getString(R.string.orders_detail_ingredients, order.optionalIngredients)
        binding.tvOrderDate.text = order.date
        binding.tvOrderAuthor.text = getString(R.string.orders_detail_author, order.author)

        val urlImage = order.orderPicture
        if (urlImage != null && urlImage != "") {
            val imageURL = baseUrl + urlImage
            getBitmapFromURL(imageURL)
        }

        return root
    }

    private fun getBitmapFromURL(url: String) {
        activity?.let {
            Picasso.with(it).load(url).into(binding.ivOrderImage)
        }
    }

}