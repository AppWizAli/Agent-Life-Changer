package com.enfotrix.lifechanger.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.enfotrix.falifechanger.Adapters.FATransactionsAdapter
import com.enfotrix.falifechanger.Constants
import com.enfotrix.falifechanger.Data.Repo
import com.enfotrix.falifechanger.Models.AgentTransactionModel
import com.enfotrix.falifechanger.Models.AgentWithdrawModel
import com.enfotrix.falifechanger.SharedPrefManagar
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class FaProfitViewModel(context: Application) : AndroidViewModel(context) {

    private val userRepo = Repo(context)
    private val sharedPrefManager = SharedPrefManagar(context)

    private var constants= Constants()

    suspend fun addTransactionReq(transactionModel: AgentWithdrawModel): LiveData<Boolean> {
        return userRepo.addTransactionReq(transactionModel)
    } suspend fun addAgentTransactionReq(transactionModel: AgentTransactionModel): LiveData<Boolean> {
        return userRepo.addAgentTransactionReq(transactionModel)
    }


    suspend fun getAgentWithdraw(): Task<QuerySnapshot> {
        return userRepo.getAgentWithdraw()
    }
    fun getApprovedWithdrawReqAdapter( from:String): FATransactionsAdapter {
        return FATransactionsAdapter(from,sharedPrefManager.getWithdrawReqList().filter{ it.status.equals(constants.TRANSACTION_STATUS_APPROVED) }.sortedByDescending { it.withdrawApprovedDate })
    }    fun getPendingWithdrawReqAdapter( from:String): FATransactionsAdapter {
        return FATransactionsAdapter(from,sharedPrefManager.getWithdrawReqList().filter{ it.status.equals(constants.TRANSACTION_STATUS_PENDING) }.sortedByDescending { it.lastWithdrawReqDate })
    }
    suspend fun getAgentTransactionReq(token: String): Task<QuerySnapshot> {
        return userRepo.getAgentTransactionReq(token)
    }
    suspend fun getAfentTransactions(): Task<QuerySnapshot> {
        return userRepo.getAfentTransactions()
    }
}