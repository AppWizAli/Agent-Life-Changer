package com.example.agentlifechanger.UI

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.agentlifechanger.Constants
import com.example.agentlifechanger.Models.User
import com.example.agentlifechanger.R
import com.example.agentlifechanger.SharedPrefManagar
import com.example.agentlifechanger.Utils
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.schedule

class ActivitySplash : AppCompatActivity() {

    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var constants: Constants
    private lateinit var dialog : Dialog
    private lateinit var sharedPrefManager : SharedPrefManagar

    private val db = Firebase.firestore




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mContext = this@ActivitySplash;
        sharedPrefManager = SharedPrefManagar(mContext)
        utils = Utils(mContext)
        constants = Constants()

        getUser()

        Timer().schedule(1500) {
            if (sharedPrefManager.isLoggedIn() == true) {
                startActivity(Intent(mContext, MainActivity::class.java))
                finish()
            } else if (sharedPrefManager.isLoggedIn() == false) {
                startActivity(Intent(mContext, ActivityLogin::class.java))
                finish()
            } else if (sharedPrefManager.logOut().equals(false)) {
                startActivity(Intent(mContext, ActivityLogin::class.java))
                finish()
            }
        }


    }



        fun getUser() {
            utils.startLoadingAnimation()
            lifecycleScope.launch {
                db.collection(constants.INVESTOR_COLLECTION)
                    .get()
                    .addOnCompleteListener { task ->
                        utils.endLoadingAnimation()
                        if (task.isSuccessful) {
                            val list = ArrayList<User>()
                            if (task.result.size() > 0) {
                                for (document in task.result) list.add(
                                    document.toObject(User::class.java).apply { id = document.id })
                                sharedPrefManager.putUserList(list)
                            }
                        } else Toast.makeText(
                            mContext,
                            constants.SOMETHING_WENT_WRONG_MESSAGE,
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener {
                        utils.endLoadingAnimation()
                        Toast.makeText(mContext, it.message + "", Toast.LENGTH_SHORT).show()
                    }
            }
        }



   /* suspend fun setUser(user: User): Task<Void> {
        return InvestorsCollection.document(user.id).set(user)

    }
    suspend fun getUsers(): Task<QuerySnapshot> {
        return InvestorsCollection.get()
    }*/

    }