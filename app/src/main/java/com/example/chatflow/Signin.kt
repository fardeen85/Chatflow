package com.example.chatflow

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.example.chatflow.databinding.FragmentSigninBinding
import com.google.android.material.textfield.TextInputLayout
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.name


class Signin : Fragment() {

    lateinit var binding: FragmentSigninBinding
    private var client = ChatClient.instance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSigninBinding.inflate(inflater,container,false)

        binding.buttonsignin.setOnClickListener {

            authenticateuser()
        }

        return binding.root
    }

    fun authenticateuser(){

        val email = binding.inputemail.text.toString()
        val password = binding.inputpassword.text.toString()
        if (email.toString().length == 0){


            binding.inputemail.error = "enter password"

        }
        else if(password.toString().length == 0){


            binding.inputpassword.error = "enter password"
        }

        else{



                val user = Chatmodel(email,password)
                val action = SigninDirections.actionSigninToParticipants2(user)
                findNavController().navigate(action)


            }

    }



    override fun onDestroy() {
        super.onDestroy()
        binding = null!!
    }


}