package com.enfotrix.falifechanger.Models

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.enfotrix.falifechanger.Adapters.FaAccountsAdapter
import com.enfotrix.falifechanger.Constants
import com.enfotrix.falifechanger.Data.Repo
import com.enfotrix.falifechanger.SharedPrefManagar
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask

class FAViewModel(context: Application) : AndroidViewModel(context) {

    private var constants= Constants()
    private val db = Firebase.firestore
    private var FACollection = db.collection(constants.FA_COLLECTION)


    private val userRepo = Repo(context)
    private val sharedPrefManager = SharedPrefManagar(context)


    private var context = context

     fun updateFA(modelFA: ModelFA): LiveData<Boolean> {
        return userRepo.updateFA(modelFA)
    }


    suspend fun uploadPhoto(imageUri: Uri, type:String): UploadTask {
        return userRepo.uploadPhoto(imageUri, type)
    }
    suspend fun addFAAccount(bankAccount: ModelBankAccount): LiveData<Boolean> {
        return userRepo.registerBankAccount(bankAccount)
    }
    suspend fun getAccounts(): Task<QuerySnapshot> {
        return userRepo.getAccounts()
    }   suspend fun getAgentProfit():Task<QuerySnapshot>
    {
        return  userRepo.getAgentProfit()
    }
    fun getFaAccountsAdapter(fromActivity:String, listener: FaAccountsAdapter.OnItemClickListener ): FaAccountsAdapter {
        return FaAccountsAdapter(fromActivity,sharedPrefManager.getFaBankList(), listener)
    }
    suspend fun getFaIncome():Task<QuerySnapshot>
    {
        return  userRepo.getAgentProfit()
    }
}