package com.example.agentlifechanger.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.parser.IntegerParser
import com.bumptech.glide.Glide
import com.example.agentlifechanger.Adapters.AdapterClient
import com.example.agentlifechanger.Constants
import com.example.agentlifechanger.Data.Repo
import com.example.agentlifechanger.Models.InvesterViewModel
import com.example.agentlifechanger.Models.InvestmentModel
import com.example.agentlifechanger.Models.ModelFA
import com.example.agentlifechanger.Models.User
import com.example.agentlifechanger.R
import com.example.agentlifechanger.SharedPrefManagar
import com.example.agentlifechanger.Utils
import com.example.agentlifechanger.databinding.ActivityMainBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import org.bouncycastle.util.Integers
import java.util.Locale


class MainActivity : AppCompatActivity(), AdapterClient.OnItemClickListener {

    private lateinit var repo: Repo

    private lateinit var mContext: Context
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefManager: SharedPrefManagar
    private lateinit var utils: Utils
    private var constants = Constants()
    private val db = Firebase.firestore
    private lateinit var modelFA: ModelFA

    private lateinit var userArraylist: ArrayList<User>
    private lateinit var investorIDArraylist: ArrayList<InvestmentModel>
    private lateinit var balanceArraylist: ArrayList<Int>


    private val investerViewModel: InvesterViewModel by viewModels()



    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: AdapterClient

    private var id = ""
    private var photo = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@MainActivity
        utils = Utils(mContext)
        sharedPrefManager = SharedPrefManagar(mContext)

        modelFA = ModelFA()
        repo = Repo(mContext)

        recyclerView = findViewById(R.id.rvClients)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        investorIDArraylist = arrayListOf()
        userArraylist = arrayListOf()
        balanceArraylist = arrayListOf()
       getFAID()
        getData()

        getBalance()

        myAdapter = AdapterClient(
            id,
           sharedPrefManager.getAssignedInvestor(),
            this
        )

        recyclerView.adapter = myAdapter






        binding.svClients.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterclients(newText)
                return false
            }
        })

        binding.tveditfa.setOnClickListener {
            startActivity(Intent(this,ActivityEditProfile::class.java))
        }

    }




    override fun onRestart() {
        super.onRestart()
        startActivity(Intent(mContext,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
    }


    private fun filterclients(text: String) {
        // creating a new array list to filter our data.
        val filteredlist = ArrayList<User>()
        if (text.isEmpty() || text.equals("")) {
            binding.rvClients.adapter =
                AdapterClient(constants.FROM_ASSIGNED_FA, sharedPrefManager.getAssignedInvestor(), this@MainActivity)

        } else {
            for (user in sharedPrefManager.getAssignedInvestor()) {
                if (user.firstName.toLowerCase(Locale.getDefault())
                        .contains(text.toLowerCase(Locale.getDefault()))
                ) {
                    filteredlist.add(user)
                }
            }
            if (filteredlist.isEmpty()) {
                Toast.makeText(mContext, "No Data Found..", Toast.LENGTH_SHORT).show()
            } else {

                binding.rvClients.adapter = AdapterClient(
                    constants.FROM_ASSIGNED_FA,
                    filteredlist,
                    this@MainActivity
                )

            }
        }

    }







    fun getData() {
        utils.startLoadingAnimation()
        lifecycleScope.launch {
            investerViewModel.getInvestors()
                .addOnCompleteListener { task ->
                    utils.endLoadingAnimation()
                    if (task.isSuccessful) {
                        val AssignedInvestorList = ArrayList<User>()
                        if (task.result.size() > 0) {
                            for (document in task.result) {
                                if (document.toObject(User::class.java).fa_id == sharedPrefManager.getToken())
                                {
                                    AssignedInvestorList.add(
                                        document.toObject(User::class.java)
                                    )
                                      }

                            }
                            sharedPrefManager.putAssignedInvestor(AssignedInvestorList)


                        } else Toast.makeText(
                            mContext,
                            "SOMETHING_WENT_WRONG_MESSAGE",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                .addOnFailureListener {
                    utils.endLoadingAnimation()


                }

        }
    }

    fun getBalance() {
        var balance:Int=0
        lifecycleScope.launch {
            investerViewModel.getInvestement()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result.size() > 0) {
                            for (document in task.result) {
                                for (invest in sharedPrefManager.getAssignedInvestor()) {
                                    if (document.toObject(InvestmentModel::class.java).investorID == invest.id) {

                                        balance += document.toObject(InvestmentModel::class.java).investmentBalance.toInt()
                                        Toast.makeText(mContext, ""+document.toObject(InvestmentModel::class.java).investmentBalance.toString(), Toast.LENGTH_SHORT).show()
                                    }

                                }
                            }
                            binding.tvInvestment.text=balance.toString()


                        } else Toast.makeText(
                            mContext,
                            "SOMETHING_WENT_WRONG_MESSAGE",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
                .addOnFailureListener {


                }

        }
    }
        override fun onItemClick(user: User) {
            Toast.makeText(mContext, "clicked", Toast.LENGTH_SHORT).show()
        }



    private fun getFAID() {
        db.collection(constants.FA_COLLECTION).whereEqualTo(FieldPath.documentId(),sharedPrefManager.getToken())
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    snapshot.documents?.forEach { document ->
                        id =document.getString("id").toString()
                        photo = document.getString("photo").toString()
                        sharedPrefManager.putId(document.id)
                        binding.tvInvestorName.setText(document.getString("firstName")+" "+document.getString("lastName"))
                        binding.tvInvestordesignation.setText(document.getString("designantion"))
                        binding.tvInvestorCnic.setText(document.getString("cnic"))
                        binding.tvInvestorPhoneNumber.setText(document.getString("phone"))
                        Glide.with(mContext)
                            .load(photo)
                            .centerCrop()
                            .placeholder(R.drawable.ic_launcher_background) // Placeholder image while loading
                            .into(binding.imageView)


                    }
                }
            }

    }


    }


