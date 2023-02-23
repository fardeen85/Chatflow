package com.example.chatflow

import android.app.SearchManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.chatflow.databinding.FragmentParticipantsBinding
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.events.ChannelDeletedEvent
import io.getstream.chat.android.client.models.Channel
import io.getstream.chat.android.client.models.Filters
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.models.name
import io.getstream.chat.android.livedata.ChatDomain
import io.getstream.chat.android.livedata.usecase.DeleteChannel
import io.getstream.chat.android.ui.channel.list.ChannelListView
import io.getstream.chat.android.ui.channel.list.header.ChannelListHeaderView
import io.getstream.chat.android.ui.channel.list.header.viewmodel.ChannelListHeaderViewModel
import io.getstream.chat.android.ui.channel.list.header.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.ChannelListViewModel
import io.getstream.chat.android.ui.channel.list.viewmodel.bindView
import io.getstream.chat.android.ui.channel.list.viewmodel.factory.ChannelListViewModelFactory


class Participants : Fragment() {

    lateinit var binding: FragmentParticipantsBinding
    private val client = ChatClient.instance()
    lateinit var user : User
    private val args by navArgs<ParticipantsArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentParticipantsBinding.inflate(inflater,container,false)
        setup_user()
        setupchannel()
        binding.channellist.setChannelLongClickListener(object : ChannelListView.ChannelLongClickListener{


            override fun onLongClick(channel: Channel): Boolean {

                deletechanneltemporary()
                return true
            }


        })



        binding.channelListHeader.setOnActionButtonClickListener(ChannelListHeaderView.ActionButtonClickListener {
            findNavController().navigate(R.id.action_participants_to_users)
        })

        binding.channelListHeader.setOnUserAvatarClickListener(ChannelListHeaderView.UserAvatarClickListener {

            client.disconnect()
            findNavController().navigate(R.id.action_participants_to_signin)
        })

        binding.channellist.setChannelItemClickListener(ChannelListView.ChannelClickListener {

            val action =  ParticipantsDirections.actionParticipantsToFragmentChat(it.cid)
            findNavController().navigate(action)
        })

        return binding.root
    }

   fun setup_user(){

       if (client.getCurrentUser() == null){

           user = if (args.chatmodel.firestname!!.contains("fardeen")){

               User(
                       id = args.chatmodel.username!!,
                       extraData = mutableMapOf(
                               "name" to args.chatmodel.firestname!!,
                       "image" to "https://brandlogos.net/wp-content/uploads/2021/01/android-robot-logo.png"
                       )
               )
           }
           else{

               User(
                       id = args.chatmodel.username!!,
                       extraData = mutableMapOf(

                               "name" to args.chatmodel.firestname!!
                       )

               )
           }
       }

       val token = client.devToken(user.id)
       client.connectUser(

               user,
               token
       ).enqueue{
           if (it.isSuccess){ Log.d("TAG","connecting user")}

           else{ Log.d("TAG",it.error().message.toString())}
       }



   }


    fun setupchannel(){

        val filter = Filters.and(

                Filters.eq("type","messaging"),
                Filters.`in`("members", listOf(client.getCurrentUser()!!.id))

        )

        val viewModelFactory = ChannelListViewModelFactory(
                filter,
                ChannelListViewModel.DEFAULT_SORT
        )

        val listViewModel : ChannelListViewModel by viewModels{ viewModelFactory}
        val listHeaderViewModel : ChannelListHeaderViewModel by viewModels()
        listHeaderViewModel.bindView(binding.channelListHeader, viewLifecycleOwner)
        listViewModel.bindView(binding.channellist,viewLifecycleOwner)

    }

    fun deletechannel(channel : Channel){

        ChatDomain.instance().useCases.deleteChannel(channel.cid).enqueue(){ result ->

            if (result.isSuccess){

                Toast.makeText(requireContext(),"${channel.name} removed!",Toast.LENGTH_SHORT).show()
            }
            else{

                Toast.makeText(requireContext(),"${result.error()}",Toast.LENGTH_SHORT).show()
            }

        }

    }


    fun deletechanneltemporary(){

        val channelClient = client.channel("messaging", "general")

        channelClient.delete().enqueue { result ->
            if (result.isSuccess) {
                val channel = result.data()
            } else {
                // Handle result.error()
            }
        }

    }

}