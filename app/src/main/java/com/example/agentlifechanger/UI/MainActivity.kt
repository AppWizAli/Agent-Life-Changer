package com.example.agentlifechanger.UI

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.agentlifechanger.Constants
import com.example.agentlifechanger.Models.ModelUser
import com.example.agentlifechanger.R
import com.example.agentlifechanger.SharedPrefManager
import com.example.agentlifechanger.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity(){


    private lateinit var binding : ActivityMainBinding
    private lateinit var sharedPrefManager : SharedPrefManager
    private lateinit var rvInvestors: RecyclerView


    private lateinit var mContext: Context
    private lateinit var dialog: BottomSheetDialog

    var constant = Constants()


    private val db = Firebase.firestore
    private lateinit var modelUser: ModelUser




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext=this@MainActivity
        sharedPrefManager = SharedPrefManager(mContext)



       /* binding.tveditfa.setOnClickListener {
            startActivity(Intent(this,ActivityLogin::class.java))
        }
*/
        }




        }



