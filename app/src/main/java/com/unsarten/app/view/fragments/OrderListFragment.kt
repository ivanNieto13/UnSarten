package com.unsarten.app.view.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.unsarten.app.databinding.FragmentOrdersBinding
import com.unsarten.app.helpers.RetrofitHelper
import com.unsarten.app.model.GetOrders
import com.unsarten.app.model.Order
import com.unsarten.app.model.OrderList
import com.unsarten.app.model.adapter.OrderListAdapter
import com.unsarten.app.service.lib.OrdersAPI
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class OrderListFragment : Fragment() {
    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!

    private var orderList = ArrayList<Order>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView = binding.rvOrderList
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val ordersApi = RetrofitHelper.getInstance().create(OrdersAPI::class.java)
        var adapter = OrderListAdapter(requireActivity(), orderList)
        MainScope().launch {
            val result = ordersApi.getOrders()
            Log.d("result: ", result.body().toString())
            if (result.isSuccessful) {
                val orders = result.body() as GetOrders
                for (order in orders.data.GetOrders) {
                    orderList.add(Order(
                        order.userId,
                        order.orderName,
                        order.budget,
                        order.persons,
                        order.optionalIngredients,
                        order.orderPicture,
                        order.orderStatus,
                    ))
                }
                if (orderList.size > 0) {
                    adapter = OrderListAdapter(requireActivity(), orderList)
                    recyclerView.adapter = adapter
                }
            } else {
                Log.d("result: ", "error")
            }
        }
        recyclerView.adapter = adapter
        return root
    }
}