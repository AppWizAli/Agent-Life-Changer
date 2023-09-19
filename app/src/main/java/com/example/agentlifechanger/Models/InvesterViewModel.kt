package com.example.agentlifechanger.Models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.agentlifechanger.Constants
import com.example.agentlifechanger.Data.Repo
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot
import com.google.mlkit.common.sdkinternal.SharedPrefManager

class InvesterViewModel(context: Application) : AndroidViewModel(context) {

    private val userRepo = Repo(context)
    private val sharedPrefManager = SharedPrefManager(context)

    private var constants= Constants()



    suspend fun getInvestors():Task<QuerySnapshot>
    {
        return  userRepo.getInvestors()
    }
    suspend fun getInvestement():Task<QuerySnapshot>
    {
        return  userRepo.getInvestement()
    }
}