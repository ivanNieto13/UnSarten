package com.unsarten.app.model.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unsarten.app.R
import com.unsarten.app.model.Order

class OrderListAdapter(
    private val onClickListener: OnClickListener,
    private val data: ArrayList<Order>,
) :
    RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var orderName: TextView
        var orderBudget: TextView
        var orderPersons: TextView
        var parentLayout: LinearLayout

        init {
            orderName = view.findViewById(R.id.tvOrder_name)
            orderBudget = view.findViewById(R.id.tvOrder_budget)
            orderPersons = view.findViewById(R.id.tvOrder_persons)
            parentLayout = view.findViewById(R.id.llOrderItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li = LayoutInflater.from(parent.context)
        val v = li.inflate(R.layout.item_order_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = data[position]
        holder.orderName.text = info.orderName
        holder.orderBudget.text = "$ ${info.budget}"
        holder.orderPersons.text = "${info.persons} personas"
        holder.parentLayout.setOnClickListener {
            onClickListener.onClick(info)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    class OnClickListener(val clickListener: (order: Order) -> Unit) {
        fun onClick(order: Order) = clickListener(order)
    }
}