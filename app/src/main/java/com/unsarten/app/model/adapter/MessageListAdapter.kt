package com.unsarten.app.model.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.unsarten.app.R
import com.unsarten.app.model.Message

class MessageListAdapter(
    private val context: Context,
    private val data: List<Message>
) :
    RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {
    private val mOnClickListener: View.OnClickListener = View.OnClickListener { v ->

    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var userName: TextView
        var previewMessage: TextView
        var layoutParent: ConstraintLayout

        init {
            userName = view.findViewById(R.id.tvChatListName)
            previewMessage = view.findViewById(R.id.tvChatListPreviewMessage)
            layoutParent = view.findViewById(R.id.clItemChatList)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val li = LayoutInflater.from(parent.context)
        val v = li.inflate(R.layout.item_chat_list, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = data[position]
        holder.userName.text = info.userName
        holder.previewMessage.text = info.messagePreview
        holder.layoutParent.setOnClickListener {
            mOnClickListener.onClick(it)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}