package com.trustledger.adminaitrust.fragments.chat

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.trustledger.adminaitrust.R
import com.trustledger.adminaitrust.ViewModel.ChatViewModel
import com.trustledger.adminaitrust.adapter.chat.ChatPreviewAdapter
import com.trustledger.adminaitrust.databinding.FragmentChatBinding
import com.trustledger.adminaitrust.models.chat.ChatPreview
import com.trustledger.adminaitrust.repository.chat.ChatRepository
import com.trustledger.adminaitrust.repository.chat.ChatViewModelFactory

class ChatFragment : Fragment(), ChatPreviewAdapter.OnChatPreviewClickListener {
    private var chatViewModel: ChatViewModel? = null
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private var adapter: ChatPreviewAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        binding.chatListRecyclerView.layoutManager = LinearLayoutManager(context)
        adapter = ChatPreviewAdapter(ArrayList(), this)
        binding.chatListRecyclerView.adapter = adapter

        val chatRepository = ChatRepository()
        val factory = ChatViewModelFactory(chatRepository)

        chatViewModel = ViewModelProvider(this, factory)[ChatViewModel::class.java]

        chatViewModel!!.getChatPreviewList().observe(viewLifecycleOwner) { chatPreviews ->
            for (preview in chatPreviews) {
                Log.d("ChatFragment", "ChatPreview User ID: ${preview.getUserId()}")
            }
            adapter!!.setChatPreviews(chatPreviews)
        }
        return binding.root
    }

    override fun onChatPreviewClick(chatPreview : ChatPreview) {
        val bundle = Bundle().apply {
            putString("userId", chatPreview.userId)
            putString("userName", chatPreview.userName)
        }
        findNavController().navigate(R.id.action_chatFragment_to_detailChatFragment, bundle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Avoid memory leaks
    }
}