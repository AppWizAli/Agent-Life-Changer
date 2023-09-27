package com.enfotrix.lifechanger.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.agentlifechanger.Constants
import com.example.agentlifechanger.Data.Repo
import com.example.agentlifechanger.Models.FaProfitModel
import com.example.agentlifechanger.Models.TransactionModel
import com.example.agentlifechanger.SharedPrefManagar
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

class FaProfitViewModel(context: Application) : AndroidViewModel(context) {

    private val userRepo = Repo(context)
    private val sharedPrefManager = SharedPrefManagar(context)

    private var constants= Constants()

/*
    suspend fun addInvestment(faProfitModel: FaProfitModel): LiveData<Boolean> {
        return userRepo.addProfit(faProfitModel)
    }
    suspend fun getProfitTax(token: String): Task<QuerySnapshot> {
        return userRepo.getProfit(token)
    }

    suspend fun getWithdrawsReq(token: String): Task<QuerySnapshot> {
        return userRepo.getTransactionReq(token,constants.TRANSACTION_TYPE_WITHDRAW)
    }
    suspend fun getInvestmentReq(token: String): Task<QuerySnapshot> {
        return userRepo.getTransactionReq(token,constants.TRANSACTION_TYPE_INVESTMENT)
    }
    suspend fun addTransactionReq(transactionModel: TransactionModel): LiveData<Boolean> {
        return userRepo.addTransactionReq(transactionModel)
    }
    fun getProfitAdapter( from:String): TransactionsAdapter {
        //return ProfitTaxAdapter(from,sharedPrefManager.getProfitTaxList().filter{ it.type.equals(constants.PROFIT_TYPE) }.sortedByDescending { it.createdAt })
        return TransactionsAdapter(from,sharedPrefManager.getProfitList().filter{ it.status.equals(constants.TRANSACTION_STATUS_APPROVED) }.sortedByDescending { it.createdAt })
    }
    fun getTaxAdapter( from:String): TransactionsAdapter {
        //return ProfitTaxAdapter(from,sharedPrefManager.getProfitTaxList().filter{ it.type.equals(constants.TAX_TYPE) }.sortedByDescending { it.createdAt })
        return TransactionsAdapter(from,sharedPrefManager.getProfitList().filter{ it.status.equals(constants.TRANSACTION_STATUS_APPROVED) }.sortedByDescending { it.createdAt })

    }
    fun getPendingWithdrawReqAdapter( from:String): TransactionsAdapter {
        return TransactionsAdapter(from,sharedPrefManager.getWithdrawReqList().filter{ it.status.equals(constants.TRANSACTION_STATUS_PENDING) }.sortedByDescending { it.createdAt })
    }
    fun getPendingInvestmentReqAdapter( from:String): TransactionsAdapter {
        return TransactionsAdapter(from,sharedPrefManager.getInvestmentReqList().filter{ it.status.equals(constants.TRANSACTION_STATUS_PENDING) }.sortedByDescending { it.createdAt })
    }
    fun getApprovedWithdrawReqAdapter( from:String): TransactionsAdapter {
        return TransactionsAdapter(from,sharedPrefManager.getWithdrawReqList().filter{ it.status.equals(constants.TRANSACTION_STATUS_APPROVED) }.sortedByDescending { it.createdAt })
    }

    fun getApprovedInvestmentReqAdapter( from:String): TransactionsAdapter {
        return TransactionsAdapter(from,sharedPrefManager.getInvestmentReqList().filter{ it.status.equals(constants.TRANSACTION_STATUS_APPROVED) }.sortedByDescending { it.createdAt })
    }*/

    suspend fun addTransactionReq(transactionModel: TransactionModel): LiveData<Boolean> {
        return userRepo.addTransactionReq(transactionModel)
    }
}