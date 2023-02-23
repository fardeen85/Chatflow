package com.example.chatflow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.NavArgs
import androidx.navigation.fragment.navArgs
import com.example.chatflow.databinding.FragmentChatBinding
import com.getstream.sdk.chat.viewmodel.MessageInputViewModel
import com.getstream.sdk.chat.viewmodel.messages.MessageListViewModel
import io.getstream.chat.android.ui.message.input.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.header.MessageListHeaderView
import io.getstream.chat.android.ui.message.list.header.viewmodel.MessageListHeaderViewModel
import io.getstream.chat.android.ui.message.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.bindView
import io.getstream.chat.android.ui.message.list.viewmodel.factory.MessageListViewModelFactory


class FragmentChat : Fragment() {

   private val chatargs by navArgs<FragmentChatArgs>()
    private var chatbinding : FragmentChatBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        chatbinding =  FragmentChatBinding.inflate(inflater,container,false)
        chatbinding!!.messagesHeaderView.setBackButtonClickListener(MessageListHeaderView.OnClickListener {



        })

        setupMessages()
        return chatbinding!!.root
    }

    private fun setupMessages() {
        val factory = MessageListViewModelFactory(cid = chatargs.cid)

        val messageListHeaderViewModel: MessageListHeaderViewModel by viewModels { factory }
        val messageListViewModel: MessageListViewModel by viewModels { factory }
        val messageInputViewModel: MessageInputViewModel by viewModels { factory }

        messageListHeaderViewModel.bindView(chatbinding!!.messagesHeaderView, viewLifecycleOwner)
        messageListViewModel.bindView(chatbinding!!.messageList, viewLifecycleOwner)
        messageInputViewModel.bindView(chatbinding!!.messageInputView, viewLifecycleOwner)
    }

    override fun onDestroy() {
        super.onDestroy()
        chatbinding = null
    }

}