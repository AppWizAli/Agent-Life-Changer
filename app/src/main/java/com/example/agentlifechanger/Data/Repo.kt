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


    suspend fun setUser(user: User): Task<Void> {
        return InvestorsCollection.document(user.id).set(user)
    }

     fun getUsers(): Task<QuerySnapshot> {
        return InvestorsCollection.get()
    }

    fun uploadPhoto(imageUri: Uri, type:String): UploadTask {
        return storageRef.child(    type+"/"+sharedPrefManager.getToken()).putFile(imageUri)
    }

    fun updateUserPhoto(user: ModelFA): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        FACollection.document(sharedPrefManager.getToken()).set(user).addOnSuccessListener { documents ->
            result.value =true
        }.addOnFailureListener {
            result.value = false
        }
        return result
    }

//////////////////////////////////////////////////////////////////////////////////
    fun updateFAFirstName(id: String, firstName: String ): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val faUpdate = mapOf(
            "firstName" to firstName,
        )
        FACollection.document(id).update(faUpdate)
            .addOnSuccessListener {
                result.value = true
            }.addOnFailureListener {
                result.value = false
            }

        return result
    }

    fun updateFALastName(id: String,lastName: String ): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val faUpdate = mapOf(
            "lastName" to lastName,
        )
        FACollection.document(id).update(faUpdate)
            .addOnSuccessListener {
                result.value = true
            }.addOnFailureListener {
                result.value = false
            }

        return result
    }
    fun updateFADesignation(id: String,designation: String): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val faUpdate = mapOf(
            "designantion" to designation,
        )
        FACollection.document(id).update(faUpdate)
            .addOnSuccessListener {
                result.value = true
            }.addOnFailureListener {
                result.value = false
            }

        return result
    }
    fun updateFACNIC(id: String, cnic: String ): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val faUpdate = mapOf(
            "cnic" to cnic,
        )
        FACollection.document(id).update(faUpdate)
            .addOnSuccessListener {
                result.value = true
            }.addOnFailureListener {
                result.value = false
            }

        return result
    }
    fun updateFAPassword(id: String,password: String): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val faUpdate = mapOf(
            "password" to password
        )
        FACollection.document(id).update(faUpdate)
            .addOnSuccessListener {
                result.value = true
            }.addOnFailureListener {
                result.value = false
            }

        return result
    }
    fun updateFAAddress(id: String,address : String): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val faUpdate = mapOf(
            "address" to address,
        )
        FACollection.document(id).update(faUpdate)
            .addOnSuccessListener {
                result.value = true
            }.addOnFailureListener {
                result.value = false
            }

        return result
    }
    fun updateFAPhone(id: String,phone : String): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val faUpdate = mapOf(
            "phone" to phone,
        )
        FACollection.document(id).update(faUpdate)
            .addOnSuccessListener {
                result.value = true
            }.addOnFailureListener {
                result.value = false
            }

        return result
    }




}