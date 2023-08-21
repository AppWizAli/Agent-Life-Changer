package com.example.agentlifechanger.UI

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.agentlifechanger.Constants
import com.example.agentlifechanger.Models.ModelUser
import com.example.agentlifechanger.R
import com.example.agentlifechanger.SharedPrefManager
import com.example.agentlifechanger.Utils
import com.example.agentlifechanger.databinding.ActivityLoginBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ActivityLogin : AppCompatActivity() {

    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var binding : ActivityLoginBinding
    private lateinit var modelUser: ModelUser
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager

    private lateinit var dialog : Dialog

    private var db= Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setContentView(binding.root)
        mContext=this@ActivityLogin
        supportActionBar?.hide()
        utils = Utils(mContext)
        constants= Constants()
        sharedPrefManager = SharedPrefManager(mContext)

        binding.btnSignIn.setOnClickListener {
            if(TextUtils.isEmpty(binding.etCNIC.editText?.text.toString())){
                binding.etCNIC.setError("Enter CNIC")
            }
            else if (binding.etCNIC.editText?.text.toString().length < 13){
                binding.etCNIC.setError("Invalid CNIC")
            }
            else{
                checkCNIC(binding.etCNIC.editText?.text.toString())
            }
        }

    }

    private fun checkCNIC(cnic: String) {
        utils.startLoadingAnimation()
        db.collection(constants.FA_COLLECTION).whereEqualTo(constants.FA_CNIC,cnic)
            .get()
            .addOnCompleteListener{
                if(it.isSuccessful){
                    showDialogPin()
                }else{
                    Toast.makeText(mContext, constants.INVESTOR_CNIC, Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener{
                Toast.makeText(mContext, it.message+"", Toast.LENGTH_SHORT).show()
            }
    }

    fun showDialogPin() {

        dialog = Dialog (mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_set_pin)
        val etPin1 = dialog.findViewById<EditText>(R.id.etPin1)
        val etPin2 = dialog.findViewById<EditText>(R.id.etPin2)
        val etPin3 = dialog.findViewById<EditText>(R.id.etPin3)
        val etPin4 = dialog.findViewById<EditText>(R.id.etPin4)
        val etPin5 = dialog.findViewById<EditText>(R.id.etPin5)
        val etPin6 = dialog.findViewById<EditText>(R.id.etPin6)
        val tvClearAll = dialog.findViewById<TextView>(R.id.tvClearAll)
        val tvHeader = dialog.findViewById<TextView>(R.id.tvHeader)
        val btnSetPin = dialog.findViewById<Button>(R.id.btnSetPin)

        tvHeader.setText("Enter your Pin to Login !")
        btnSetPin.setText("Login")
        etPin1.requestFocus();
        utils.moveFocus( listOf(etPin1, etPin2, etPin3, etPin4, etPin5, etPin6))

        tvClearAll.setOnClickListener{
            utils.clearAll( listOf(etPin1, etPin2, etPin3, etPin4, etPin5, etPin6))
            etPin1.requestFocus();

        }
        btnSetPin.setOnClickListener {
            if(!utils.checkEmpty( listOf(etPin1, etPin2, etPin3, etPin4, etPin5, etPin6))){
                var pin : String =  utils.getPIN( listOf(etPin1, etPin2, etPin3, etPin4, etPin5, etPin6))
                login(binding.etCNIC.editText?.text.toString(),pin)
            }
            else Toast.makeText(mContext, "Please enter 6 Digits Pin!", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
    }

    fun login(cnic: String, pin: String) {

        utils.startLoadingAnimation()
        db.collection(constants.FA_COLLECTION).whereEqualTo(constants.FA_PIN,pin)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    utils.endLoadingAnimation()
                    if (task.result.size() > 0) {


                        var modelUser: ModelUser? = null
                        for (document in task.result) {
                            modelUser = document.toObject(ModelUser::class.java)
                            modelUser.id = document.id
                        }

                        //Toast.makeText(mContext, pin+" "+modelUser?.pin, Toast.LENGTH_SHORT).show()
                        if (modelUser?.pin.equals(pin)) {


                            if (modelUser != null) {
                                sharedPrefManager.saveLoginAuth(modelUser, modelUser.id, true)
                            }

                            //Toast.makeText(mContext, "Login Successfull", Toast.LENGTH_SHORT).show()

                            startActivity(
                                Intent(
                                    mContext,
                                    MainActivity::class.java
                                ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            )
                            finish()

                        } else Toast.makeText(mContext, "Incorrect PIN", Toast.LENGTH_SHORT).show()

                    } else Toast.makeText(mContext, "CNIC Incorrect", Toast.LENGTH_LONG).show()

                }

            }
    }


}