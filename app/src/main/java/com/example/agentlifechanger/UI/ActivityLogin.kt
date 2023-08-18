package com.example.agentlifechanger.UI

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.agentlifechanger.Constants
import com.example.agentlifechanger.Models.ModelUser
import com.example.agentlifechanger.R
import com.example.agentlifechanger.SharedPrefManager
import com.example.agentlifechanger.Utils
import com.example.agentlifechanger.databinding.ActivityLoginBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ActivityLogin : AppCompatActivity() {

    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var binding : ActivityLoginBinding
    private lateinit var modelUser: ModelUser
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager

    private var db= Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext=this@ActivityLogin
        utils = Utils(mContext)
        constants= Constants()
        sharedPrefManager = SharedPrefManager(mContext)



    }
}