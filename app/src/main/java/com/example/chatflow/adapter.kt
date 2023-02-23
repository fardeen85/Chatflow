package com.example.chatflow

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.chatflow.databinding.UserslayoutBinding
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.User
import io.getstream.chat.android.client.models.name
import java.text.DateFormat

class adapter : RecyclerView.Adapter<adapter.myviewholder>() {

    private val client  = ChatClient.instance()
    private var userlist = emptyList<User>()



    class myviewholder(val binding : UserslayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewholder {


        return myviewholder(
                UserslayoutBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                )
        )

    }

    override fun onBindViewHolder(holder: myviewholder, position: Int) {

        val currentuser = userlist[position]
        holder.binding.profileavatar.setUserData(currentuser)
        holder.binding.username.setText(currentuser.name.toString())
        holder.binding.lastseentime.setText(currentuser.lastActive!!.time.toString())
        holder.binding.rootview.setOnClickListener {

            createnewchannel(currentuser.id,holder)
        }

    }

    override fun getItemCount(): Int {

        return userlist.size
    }

    fun convertdate(milliseconds : Long) : String{

        return DateFormat.getDateInstance().format("dd/MM/YYYY hh:mm a").toString()
    }

    fun createnewchannel(selecteduser : String, holder: myviewholder){

        client.createChannel(

                channelType = "messaging",
                members = listOf(client.getCurrentUser()!!.id,selecteduser)

        ).enqueue{
            if (it.isSuccess){

                navigatetoCHatFragment(holder,it.data().cid)
            }
            else{

                Log.d("TAg",it.error().message.toString())
            }

        }
    }

    fun navigatetoCHatFragment(holder : myviewholder, cid : String){

       /* val action = UsersDirections.actionUsersToParticipants()
        holder.itemView.findNavController().navigate(action)*/
    }

    fun setdata(list : List<User>){

        userlist = list
        notifyDataSetChanged()
    }



}