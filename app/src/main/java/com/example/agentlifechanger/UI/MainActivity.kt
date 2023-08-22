package com.example.agentlifechanger.UI

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.agentlifechanger.SharedPrefManager
import com.example.agentlifechanger.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity(){


    private lateinit var binding : ActivityMainBinding
    private lateinit var sharedPrefManager : SharedPrefManager

    private lateinit var mContext: Context



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext=this@MainActivity
        sharedPrefManager = SharedPrefManager(mContext)

        binding.tveditfa.setOnClickListener{
            sharedPrefManager.logOut(isLoggedOut = false)
            startActivity(Intent(mContext, ActivityLogin::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            })
            finish()
        }

        }

        }


