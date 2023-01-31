package com.unsarten.app.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.unsarten.app.R
import com.unsarten.app.databinding.FragmentChatListBinding
import com.unsarten.app.model.MessageList
import com.unsarten.app.model.adapter.MessageListAdapter

class ChatListFragment() : Fragment() {
    private var _binding: FragmentChatListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatListBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val recyclerView = binding.rvChatList
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val adapter = MessageListAdapter(requireActivity(), MessageList.messageList)
        recyclerView.adapter = adapter


        return root
    }
}
