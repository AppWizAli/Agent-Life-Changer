package com.example.agentlifechanger.Data

import android.content.Context
import com.example.agentlifechanger.Adapters.AdapterClient
import com.example.agentlifechanger.Constants
import com.example.agentlifechanger.Models.User
import com.example.agentlifechanger.SharedPrefManagar
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class Repo (val context: Context) {

    private var constants: Constants = Constants()

    private var mContext: Context = context

    private val db = Firebase.firestore

    private var InvestorsCollection = db.collection(constants.INVESTOR_COLLECTION)


    private var sharedPrefManager = SharedPrefManagar(mContext)



    fun getInvestorsAdapter(
        fromActivity: String,
        listener: AdapterClient.OnItemClickListener
    ): AdapterClient {
        return AdapterClient(
            fromActivity,
            sharedPrefManager.getUsersList().filter { it.fa_id.isNullOrEmpty() },
            listener
        )
    }


    suspend fun setUser(user: User): Task<Void> {
        return InvestorsCollection.document(user.id).set(user)
    }

     fun getUsers(): Task<QuerySnapshot> {
        return InvestorsCollection.get()
    }


}