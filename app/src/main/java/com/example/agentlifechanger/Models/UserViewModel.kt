package com.enfotrix.lifechanger.Models

import android.app.Application
import android.content.Context
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.agentlifechanger.Adapters.InvestorAdapter
import com.example.agentlifechanger.Constants
import com.example.agentlifechanger.SharedPrefManager
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask


class UserViewModel(context: Application) : AndroidViewModel(context) {

    private val sharedPrefManager = SharedPrefManager(context)
    private var constants = Constants()

    private var context = context

    /*suspend fun registerUser(user: User): LiveData<Pair<Boolean, String>> {
        return userRepo.registerUser(user)
    }*/

    //suspend fun uploadProfilePic(uri: Uri): LiveData<Uri> = userRepo.uploadProfilePic(uri)


    /*suspend fun isInvestorExist(CNIC: String): MutableLiveData<Pair<Boolean, String>> {

            val result = MutableLiveData<Pair<Boolean, String>>()

            userRepo.isInvestorExist(CNIC)
                 .addOnSuccessListener {
                     var user:User?=null
                     for (document in it) user = document.toObject<User>()
                     result.postValue(Pair( true,"gdfgfsdgdfg"))
                     if(user?.status.equals(INVESTOR_STATUS_ACTIVE)) result.postValue(Pair(true, INVESTOR_CNIC_EXIST))
                     else if(user?.status.equals(INVESTOR_STATUS_BLOCKED))result.postValue(Pair(false, INVESTOR_CNIC_BLOCKED))
                     else if(it==null)result.postValue(Pair(false, INVESTOR_CNIC_NOT_EXIST))
                 }
                .addOnFailureListener{
                    result.postValue(Pair( true,"aaaaaaaaa"))

                }
            return result
        }*/


    fun getAssignedInvestorsAdapter(
        fa_ID: String,
        fromActivity: String,
        listener: Context
    ): InvestorAdapter {
        return InvestorAdapter(
            fromActivity,
            sharedPrefManager.getUsersList().filter { it.fa_id.equals(fa_ID) },
            listener
        )
    }


    /*
        fun getInvestorAccountsAdapter( fromActivity:String,listener: InvestorAccountsAdapter.OnItemClickListener): InvestorAccountsAdapter {
            return InvestorAccountsAdapter(fromActivity,sharedPrefManager.getInvestorBankList(), listener)
        }
        fun getAdminAccountsAdapter( fromActivity:String,listener: InvestorAccountsAdapter.OnItemClickListener): InvestorAccountsAdapter {
            return InvestorAccountsAdapter(fromActivity,sharedPrefManager.getAdminBankList(), listener)
        }*/


    //suspend fun getBalance(): String = userRepo.getBalance()

    /*suspend fun updateUser(
        firstName: String,
        lastName: String,
        address: String,
        mobileNumber: String
    ): LiveData<Boolean> {
        return userRepo.updateUser(firstName, lastName, address, mobileNumber)
    }*/




    }
