package com.unsarten.app.model.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.unsarten.app.R
import com.unsarten.app.model.Order

class OrderListAdapter(
    private val context: Context,
    private val data: ArrayList<Order>
) :
    RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {
    private val mOnClickListener: View.OnClickListener = View.OnClickListener { v ->

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var orderName: TextView
        var orderBudget: TextView
        var orderAuthor: TextView
        var parentLayout: LinearLayout

        init {
            orderName = view.findViewById(R.id.tvOrder_name)
            orderBudget = view.findViewById(R.id.tvOrder_budget)
            orderAuthor = view.findViewById(R.id.tvOrder_author)
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
        holder.orderBudget.text = info.budget.toString()
        holder.orderAuthor.text = info.orderStatus
        holder.parentLayout.setOnClickListener {
            mOnClickListener.onClick(it)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}