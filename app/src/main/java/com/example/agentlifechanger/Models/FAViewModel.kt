package com.example.agentlifechanger.Models

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.agentlifechanger.Constants
import com.example.agentlifechanger.Data.Repo
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.mlkit.common.sdkinternal.SharedPrefManager

class FAViewModel(context: Application) : AndroidViewModel(context) {

    private var constants= Constants()
    private val db = Firebase.firestore
    private var FACollection = db.collection(constants.FA_COLLECTION)


    private val userRepo = Repo(context)
    private val sharedPrefManager = SharedPrefManager(context)


    private var context = context

    suspend fun updateFA(modelFA: ModelFA): LiveData<Boolean> {
        return userRepo.updateFA(modelFA)
    }


    suspend fun uploadPhoto(imageUri: Uri, type:String): UploadTask {
        return userRepo.uploadPhoto(imageUri, type)
    }

}