package com.example.agentlifechanger.UI

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.parser.IntegerParser
import com.bumptech.glide.Glide
import com.example.agentlifechanger.Adapters.AdapterClient
import com.example.agentlifechanger.Constants
import com.example.agentlifechanger.Data.Repo
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
import org.bouncycastle.util.Integers
import java.util.Locale


class MainActivity : AppCompatActivity() , AdapterClient.OnItemClickListener{

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

        myAdapter = AdapterClient(id, userArraylist,this)

        recyclerView.adapter = myAdapter

        getFAID()


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

    private fun EventChangeListener() {
        db.collection(constants.INVESTOR_COLLECTION).whereEqualTo(constants.INVESTOR_FA_ID,sharedPrefManager.getToken())
            .addSnapshotListener(object :
                com.google.firebase.firestore.EventListener<QuerySnapshot> {
                override fun onEvent(
                    value: QuerySnapshot?,
                    error: FirebaseFirestoreException?
                ) {
                    if (error != null) {
                        Log.e("FireStore Error", error.message.toString())
                        return
                    }

                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            userArraylist.add(dc.document.toObject(User::class.java))
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }
            })
    }


    override fun onRestart() {
        super.onRestart()
        startActivity(Intent(mContext,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    private fun getFAID() {
        db.collection(constants.FA_COLLECTION).whereEqualTo(FieldPath.documentId(),sharedPrefManager.getToken())
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    utils.startLoadingAnimation()
                    snapshot.documents?.forEach { document ->
                        id =document.getString("id").toString()
                        photo = document.getString("photo").toString()
                        binding.tvInvestorName.setText(document.getString("firstName")+" "+document.getString("lastName"))
                        binding.tvInvestordesignation.setText(document.getString("designantion"))
                        binding.tvInvestorCnic.setText(document.getString("cnic"))
                        binding.tvInvestorPhoneNumber.setText(document.getString("phone"))
                        Glide.with(mContext)
                            .load(photo)
                            .centerCrop()
                            .placeholder(R.drawable.ic_launcher_background) // Placeholder image while loading
                            .into(binding.imageView)
                        utils.endLoadingAnimation()
                        EventChangeListener()
                    }
                }
            }

    }
   /* fun getData() {

        binding.rvClients.adapter =AdapterClient(id, userArraylist,this)

    }*/

    override fun onItemClick(user: User) {
    }

   /* override fun onAssignClick(user: User) {
        user.fa_id = modelFA.id
        utils.startLoadingAnimation()
        lifecycleScope.launch {
            repo.setUser(user)
                .addOnCompleteListener { task ->
                    lifecycleScope.launch {
                        repo.getUsers()
                            .addOnCompleteListener { task ->
                                utils.endLoadingAnimation()
                                if (task.isSuccessful) {
                                    val list = ArrayList<User>()
                                    if (task.result.size() > 0) {
                                        for (document in task.result) list.add(
                                            document.toObject(
                                                User::class.java
                                            ).apply { id = document.id })
                                        sharedPrefManager.putUserList(list)
                                        dialog.dismiss()

                                        Toast.makeText(mContext, "Assigned", Toast.LENGTH_SHORT)
                                            .show()
                                        getData()
                                    }
                                } else Toast.makeText(
                                    mContext,
                                    constants.SOMETHING_WENT_WRONG_MESSAGE,
                                    Toast.LENGTH_SHORT
                                ).show()

                            }
                            .addOnFailureListener {
                                utils.endLoadingAnimation()
                                dialog.dismiss()

                                Toast.makeText(mContext, it.message + "", Toast.LENGTH_SHORT).show()

                            }
                    }


                }
                .addOnFailureListener {
                    utils.endLoadingAnimation()
                    dialog.dismiss()
                    Toast.makeText(mContext, it.message + "", Toast.LENGTH_SHORT).show()

                }


        }

    }*/

   /* override fun onRemoveClick(user: User) {
        user.fa_id = "" // Set the fa_id to empty to indicate unassigned status

        utils.startLoadingAnimation()
        lifecycleScope.launch {
            repo.setUser(user).addOnCompleteListener { task ->
                lifecycleScope.launch {
                    repo.getUsers().addOnCompleteListener { task ->
                        utils.endLoadingAnimation()
                        if (task.isSuccessful) {
                            val list = ArrayList<User>()
                            if (task.result.size() > 0) {
                                for (document in task.result) list.add(
                                    document.toObject(User::class.java).apply { id = document.id })
                                sharedPrefManager.putUserList(list)
                                Toast.makeText(
                                    mContext,
                                    "Removed from assigned",
                                    Toast.LENGTH_SHORT
                                ).show()
                                getData()
                            }
                        } else {
                            Toast.makeText(
                                mContext,
                                constants.SOMETHING_WENT_WRONG_MESSAGE,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }.addOnFailureListener {
                        utils.endLoadingAnimation()
                        Toast.makeText(mContext, it.message + "", Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener {
                utils.endLoadingAnimation()
                Toast.makeText(mContext, it.message + "", Toast.LENGTH_SHORT).show()
            }
        }
    }*/

    private fun filterclients(text: String) {
        // creating a new array list to filter our data.
        val filteredlist = ArrayList<User>()
        if (text.isEmpty() || text.equals("")) {
            binding.rvClients.adapter =
                AdapterClient(constants.FROM_ASSIGNED_FA, userArraylist, this@MainActivity)

        } else {
            for (user in userArraylist) {

                // Toast.makeText(this@ActivityFADetails, user.cnic +"", Toast.LENGTH_SHORT).show()
                // checking if the entered string matched with any item of our recycler view.
                if (user.firstName.toLowerCase(Locale.getDefault())
                        .contains(text.toLowerCase(Locale.getDefault()))
                ) {
                    filteredlist.add(user)
                }
            }
            if (filteredlist.isEmpty()) {
                // if no item is added in filtered list we are
                // displaying a toast message as no data found.
                Toast.makeText(mContext, "No Data Found..", Toast.LENGTH_SHORT).show()
            } else {
                // at last we are passing that filtered
                // list to our adapter class.


                binding.rvClients.adapter = AdapterClient(
                    constants.FROM_ASSIGNED_FA,
                    filteredlist,
                    this@MainActivity
                )

            }
        }
        // running a for loop to compare elements.

    }

}

