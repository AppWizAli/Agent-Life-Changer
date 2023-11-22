package com.enfotrix.falifechanger.Fragments

import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.enfotrix.falifechanger.Adapters.AdapterClient
import com.enfotrix.falifechanger.Constants
import com.enfotrix.falifechanger.Data.Repo
import com.enfotrix.falifechanger.Models.FAViewModel
import com.enfotrix.falifechanger.Models.AgentTransactionModel
import com.enfotrix.falifechanger.Models.InvesterViewModel
import com.enfotrix.falifechanger.Models.InvestmentModel
import com.enfotrix.falifechanger.Models.ModelFA
import com.enfotrix.falifechanger.Models.Notificaion
import com.enfotrix.falifechanger.Models.NotificationData
import com.enfotrix.falifechanger.Models.User
import com.enfotrix.falifechanger.R
import com.enfotrix.falifechanger.SharedPrefManagar
import com.enfotrix.falifechanger.UI.ActivityInvestorDetail
import com.enfotrix.falifechanger.Utils
import com.enfotrix.falifechanger.api.ApiUtilities
import com.enfotrix.falifechanger.databinding.FragmentHomeBinding
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale

class FragmentHome : Fragment() ,AdapterClient.OnItemClickListener{

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!


    private lateinit var repo: Repo

    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManagar
    private lateinit var utils: Utils
    private var constants = Constants()
    private val db = Firebase.firestore
    private lateinit var modelFA: ModelFA

    private lateinit var userArraylist: ArrayList<User>
    private lateinit var investorIDArraylist: ArrayList<InvestmentModel>
    private lateinit var balanceArraylist: ArrayList<Int>
    private lateinit var faProfitModel: AgentTransactionModel

    private val investerViewModel: InvesterViewModel by viewModels()
    private val faViewModel: FAViewModel by viewModels()



    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: AdapterClient

    private var id = ""
    private var photo = ""




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        mContext = requireContext()
        utils = Utils(mContext)
        constants = Constants()
        sharedPrefManager = SharedPrefManagar(mContext)

        mContext = requireContext()
        utils = Utils(mContext)
        sharedPrefManager = SharedPrefManagar(mContext)

        modelFA = ModelFA()
        repo = Repo(mContext)


        faProfitModel = AgentTransactionModel()
        recyclerView = binding.rvClients
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
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
            this,
            viewLifecycleOwner.lifecycleScope,
            investerViewModel
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
binding.tvInvestorName.setOnClickListener {
    FirebaseMessaging.getInstance().token
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val fcmToken = task.result // Retrieve FCM token

                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain" // Set the MIME type for plain text
                    putExtra(Intent.EXTRA_SUBJECT, "FCM Token") // Set subject if necessary
                    putExtra(Intent.EXTRA_TEXT, fcmToken) // Include FCM token as text
                }


                    startActivity(Intent.createChooser(shareIntent, "Share FCM Token")) // Display the app chooser

            } else {
                // Handle task failure if necessary
            }
        }

            }

        binding.withdraw.setOnClickListener {
            FirebaseMessaging.getInstance().token
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Get the device token
                        val token = task.result

                        val notification = Notificaion("dafEQhatRECfj03CAAsJgm:APA91bEvWC3B9Il1gbsMilRUfRmB7a2r76tmRDb_2x_D6hHOuNGv5Eyz9ARq5dtk9pSy3fjozfMAmFp2jopqRHOpylKxNZhvg-jNP3Kp8t3VCBHrZBWom6uUtaSW_cd4ybT4hP0ZsypO", NotificationData("Withdraw Request", "Body work title"))
                        ApiUtilities.api.sendNotification(notification).enqueue(object : Callback<Notification> {
                            override fun onResponse(call: Call<Notification>, response: Response<Notification>) {
                                if (response.isSuccessful) {
                                    // Handle successful response here
                                    // For example, log success message or perform actions on success
                                    Toast.makeText(mContext, "Notification sent successfully!", Toast.LENGTH_SHORT).show()
                                } else {
                                    // Handle unsuccessful response here
                                    // For example, log failure message or handle errors
                                    Toast.makeText(mContext, "Failed to send notification. Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onFailure(call: Call<Notification>, t: Throwable) {
                                // Handle failure (exception) here
                                // For example, log the exception or perform actions on failure
                                Toast.makeText(mContext, "Failed to send notification. Exception: ${t.message}", Toast.LENGTH_SHORT).show()
                            }
                        })
                    } else {
                        Toast.makeText(mContext, "Something went wrong while fetching token", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        return root
    }


    private fun filterclients(text: String) {
        // creating a new array list to filter our data.
        val filteredlist = ArrayList<User>()
        if (text.isEmpty() || text.equals("")) {
            binding.rvClients.adapter =
                AdapterClient(constants.FROM_ASSIGNED_FA, sharedPrefManager.getAssignedInvestor(), this,
                    viewLifecycleOwner.lifecycleScope,
                    investerViewModel)

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
                    this,
                    viewLifecycleOwner.lifecycleScope,
                    investerViewModel
                )

            }
        }

    }
    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.top_right_investor_menue, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when(item.itemId)
        {
            R.id.top_inestment -> {

                true

            }
            R.id.top_withdraw -> {

                true

            }
            else -> return super.onOptionsItemSelected(item)
        }


    }
    fun getData() {
        utils.startLoadingAnimation()
        lifecycleScope.launch {
            investerViewModel.getInvestors()
                .addOnCompleteListener { task ->
                    utils.endLoadingAnimation()
                    binding.tvProfit.text=sharedPrefManager.getProfit().newBalance
                    binding.ramarks .text=sharedPrefManager.getProfit().remarks
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
        startActivity(
            Intent(mContext,
                ActivityInvestorDetail::class.java).putExtra("Investor", Gson().toJson(user)))
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

        var faProfitModel=AgentTransactionModel()
        lifecycleScope.launch {
            faViewModel.getAgentProfit().addOnCompleteListener { task->
                if(task.isSuccessful)
                {
                    if (task.result.size()>0)
                    {
                        for (document in task.result) {
                            if(document.toObject(AgentTransactionModel::class.java).fa_id == sharedPrefManager.getToken()&&document.toObject(AgentTransactionModel::class.java).type == constants.PROFIT_TYPE)
                            {
                                faProfitModel=document.toObject(AgentTransactionModel::class.java)
                                break
                            }
                        }
                        sharedPrefManager.putFaProfit( faProfitModel)
                    }
                }
            }
        }


    }







}