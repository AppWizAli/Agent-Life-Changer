package com.example.agentlifechanger.Models

import com.google.firebase.Timestamp

data class ModelUser @JvmOverloads constructor(
    var cnic: String = "",
    var firstName: String = "",
    var lastName: String = "",
    var address: String = "",
    var phone: String = "",
    var status: String = "",
    var photo: String = "",
    var cnic_front: String = "",
    var cnic_back: String = "",
    var pin: String = "",
    var id: String = "",
    var designantion: String = "",
    val createdAt: Timestamp = Timestamp.now(), // Creation timestamp ,
) {

}