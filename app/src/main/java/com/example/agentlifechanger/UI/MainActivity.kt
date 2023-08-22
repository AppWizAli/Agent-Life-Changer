package com.example.agentlifechanger.UI

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.lifechanger.Models.UserViewModel
import com.example.agentlifechanger.Constants
import com.example.agentlifechanger.Models.ModelFA
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

    private lateinit var modelFA: ModelFA

    var constant = Constants()



    private val userViewModel: UserViewModel by viewModels()

    private val db = Firebase.firestore




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext=this@MainActivity
        sharedPrefManager = SharedPrefManager(mContext)


        binding.rvClients.layoutManager = LinearLayoutManager(mContext)
//     modelFA =ModelFA.fromString(intent.getStringExtra("FA").toString())!!

        getData()
       /* binding.tveditfa.setOnClickListener {
            startActivity(Intent(this,ActivityLogin::class.java))
        }
*/
        }


    fun getData() {

        binding.rvClients.adapter = userViewModel.getAssignedInvestorsAdapter(
            sharedPrefManager.getToken(),
            constant.FROM_ASSIGNED_FA,
            this@MainActivity
        )

    }

    fun setdata() {
        val modelFAStr = intent.getStringExtra("FA")
        val model: ModelFA? = modelFAStr?.let { ModelFA.fromString(it) }
        if (model != null) {
            binding.tvInvestorName.text = model.firstName
            binding.tvInvestorCnic.text = model.cnic
            binding.tvInvestorPhoneNumber.text = model.phone
            binding.tvInvestordesignation.text = model.designantion

        }
    }

        }



