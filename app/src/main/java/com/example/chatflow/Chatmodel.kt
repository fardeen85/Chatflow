package com.example.chatflow

import android.os.Parcel
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Chatmodel
    (

    val firestname : String? = null,
    val username : String? = null
    ) : Parcelable

