package com.example.agentlifechanger.UI

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.agentlifechanger.Adapters.AdapterClient
import com.example.agentlifechanger.Constants
import com.example.agentlifechanger.Data.Repo
import com.example.agentlifechanger.Models.ModelFA
import com.example.agentlifechanger.Models.User
import com.example.agentlifechanger.R
import com.example.agentlifechanger.SharedPrefManagar
import com.example.agentlifechanger.Utils
import com.example.agentlifechanger.databinding.ActivityMainBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.Locale


class MainActivity : AppCompatActivity() , AdapterClient.OnItemClickListener{

    private lateinit var rvInvestors: RecyclerView
    private var originalFAList: List<User> = emptyList()
    private var originallist: List<User> = emptyList()
    private lateinit var adapter: AdapterClient
    private lateinit var repo: Repo


    private lateinit var mContext: Context
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefManager: SharedPrefManagar
    private lateinit var utils: Utils
    private var constants = Constants()
    private lateinit var dialog: BottomSheetDialog
    private val db = Firebase.firestore

    private lateinit var modelFA: ModelFA
    private lateinit var userArraylist: ArrayList<User>
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

        userArraylist = arrayListOf()

        myAdapter = AdapterClient(id, userArraylist,this)

        recyclerView.adapter = myAdapter

        getFAID()


        binding.fbAddClient.setOnClickListener {
            showClientDialog()
        }
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
            /*startActivity(Intent(this@MainActivity, ActivityEditFA::class.java).apply {
                putExtra("FA", modelFA.toString())
            })*/
        }

    }

    private fun EventChangeListener() {
        db.collection(constants.INVESTOR_COLLECTION).whereEqualTo(constants.INVESTOR_FA_ID,id)
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


    private fun getFAID() {
        db.collection(constants.FA_COLLECTION).whereEqualTo(FieldPath.documentId(),sharedPrefManager.getToken())
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    utils.startLoadingAnimation()
                    snapshot.documents?.forEach { document ->
                        id =document.getString("id").toString()
                        photo = document.getString("photo").toString()
                        binding.tvInvestorName.setText(document.getString("firstName")+""+document.getString("lastName"))
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
    fun getData() {

        binding.rvClients.adapter =AdapterClient(id, userArraylist,this)

    }


    override fun onItemClick(user: User) {
    }

    override fun onAssignClick(user: User) {
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

    }

    override fun onRemoveClick(user: User) {
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
    }


    fun showClientDialog() {
        dialog = BottomSheetDialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.bottom_sheet_investors)
        rvInvestors = dialog.findViewById<RecyclerView>(R.id.rvInvestors) as RecyclerView
        rvInvestors.layoutManager = LinearLayoutManager(mContext)
        rvInvestors.adapter =
            repo.getInvestorsAdapter(constants.FROM_UN_ASSIGNED_FA, this@MainActivity)
        dialog.show()
        val svFadetail = dialog.findViewById<androidx.appcompat.widget.SearchView>(R.id.svFadetail)
        svFadetail?.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filter(newText)
                return false
            }
        })


    }


    private fun filter(text: String) {
        val filteredList = ArrayList<User>()
        if (text.isEmpty() || text.isBlank()) {
            rvInvestors.adapter = AdapterClient(
                constants.FROM_UN_ASSIGNED_FA,
                originallist,
                this@MainActivity
            )
        } else {
            for (user in originallist) {
                if (user.firstName.toLowerCase(Locale.getDefault())
                        .contains(text.toLowerCase(Locale.getDefault()))
                ) {
                    filteredList.add(user)
                }
            }

            if (filteredList.isEmpty()) {
                Toast.makeText(mContext, "No Data Found..", Toast.LENGTH_SHORT).show()
            } else {
                rvInvestors.adapter = AdapterClient(
                    constants.FROM_UN_ASSIGNED_FA,
                    filteredList,
                    this@MainActivity
                )
            }
        }
    }

    private fun filterclients(text: String) {
        // creating a new array list to filter our data.
        val filteredlist = ArrayList<User>()
        if (text.isEmpty() || text.equals("")) {
            binding.rvClients.adapter =
                AdapterClient(constants.FROM_ASSIGNED_FA, originalFAList, this@MainActivity)

        } else {
            for (user in originalFAList) {

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

