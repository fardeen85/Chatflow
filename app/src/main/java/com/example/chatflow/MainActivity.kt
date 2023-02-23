package com.example.chatflow

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.example.chatflow.databinding.ActivityMainBinding
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.models.name

class MainActivity : AppCompatActivity() {

    lateinit var navController: NavController
    private val client = ChatClient.instance()
    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        navController = findNavController(R.id.fragmenthost)


        if (navController.currentDestination?.label.toString().contains("login")){

            val currentuser = client.getCurrentUser()

            if (currentuser != null){

                val user = Chatmodel(currentuser.name,currentuser.id)
                val action = SigninDirections.actionSigninToParticipants(user)
                navController.navigate(action)


            }
        }





    }
}