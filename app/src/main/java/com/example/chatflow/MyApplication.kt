package com.example.chatflow

import android.app.Application
import io.getstream.chat.android.client.ChatClient
import io.getstream.chat.android.client.logger.ChatLogLevel
import io.getstream.chat.android.livedata.ChatDomain


open public  class MyApplication : Application() {


    override fun onCreate() {
        super.onCreate()
        val client = ChatClient.Builder(getString(R.string.chat_api_key),this).logLevel(ChatLogLevel.ALL).build()
        ChatDomain.Builder(client,this).build()



    }
}