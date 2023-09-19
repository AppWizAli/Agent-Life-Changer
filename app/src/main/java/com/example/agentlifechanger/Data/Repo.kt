package com.example.agentlifechanger.Data

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.agentlifechanger.Adapters.AdapterClient
import com.example.agentlifechanger.Constants
import com.example.agentlifechanger.Models.ModelFA
import com.example.agentlifechanger.Models.User
import com.example.agentlifechanger.SharedPrefManagar
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage

class Repo (val context: Context) {

    private var constants: Constants = Constants()

    private var mContext: Context = context

    private val db = Firebase.firestore

    private var InvestorsCollection = db.collection(constants.INVESTOR_COLLECTION)
    private var InvestmentCollection = db.collection(constants.INVESTMENT_COLLECTION)
    private var FACollection = db.collection(constants.FA_COLLECTION)
    private val TransactionsCollection = db.collection(constants.TRANSACTION_COLLECTION)


    private val firebaseStorage = Firebase.storage
    private var sharedPrefManager = SharedPrefManagar(mContext)
    private val storageRef = firebaseStorage.reference




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

     fun getInvestors(): Task<QuerySnapshot> {
        return InvestorsCollection.get()
    }
    fun getInvestement(): Task<QuerySnapshot> {
        return InvestmentCollection.get()
    }

    fun uploadPhoto(imageUri: Uri, type:String): UploadTask {
        return storageRef.child(    type+"/"+sharedPrefManager.getToken()).putFile(imageUri)
    }

    fun updateFA(modelFA: ModelFA): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        FACollection.document(sharedPrefManager.getToken()).set(modelFA).addOnSuccessListener { documents ->
            result.value =true
        }.addOnFailureListener {
            result.value = false
        }
        return result
    }


}