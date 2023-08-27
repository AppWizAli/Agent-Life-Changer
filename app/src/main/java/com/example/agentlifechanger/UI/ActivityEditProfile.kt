package com.example.agentlifechanger.UI

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.agentlifechanger.Constants
import com.example.agentlifechanger.Data.Repo
import com.example.agentlifechanger.Models.ModelFA
import com.example.agentlifechanger.Models.User
import com.example.agentlifechanger.R
import com.example.agentlifechanger.SharedPrefManagar
import com.example.agentlifechanger.Utils
import com.example.agentlifechanger.databinding.ActivityEditProfileBinding
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class ActivityEditProfile : AppCompatActivity() {

    private val db = Firebase.firestore
    private lateinit var mContext: Context
    private lateinit var sharedPrefManager: SharedPrefManagar
    private lateinit var utils: Utils
    private var constants = Constants()
    private var photo = ""
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var dialog: Dialog
    private lateinit var imgProfilePhoto: ImageView

    private val IMAGE_PICKER_REQUEST_CODE = 200
    private var imageURI: Uri? = null

    private lateinit var repo: Repo






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@ActivityEditProfile
        utils = Utils(mContext)
        sharedPrefManager = SharedPrefManagar(mContext)

        repo =Repo(mContext)

        getFAID()

        binding.editPIC.setOnClickListener{
            showPhotoDialog()
        }

        binding.btnProfileedit.setOnClickListener {
            update()
            /*val intent = Intent(this@ActivityEditProfile, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)*/
        }

    }

//    toas?
    private fun update(){

        val editedFirstName = binding.etFirstName.editText?.text.toString()
        val editedLastName = binding.etLastName.editText?.text.toString()
        val editedDesignation = binding.etDesignation.editText?.text.toString()
        val editedCNIC = binding.etCNIC.editText?.text.toString()
        val editedpassword = binding.etpassword.editText?.text.toString()
        val editedaddress = binding.etAddress.editText?.text.toString()

        if (TextUtils.isEmpty(editedFirstName)){
        }
        else if (!TextUtils.isEmpty(editedFirstName)){
            lifecycleScope.launch {
                val isSuccessLiveData = repo.updateFAFirstName(
                    sharedPrefManager.getToken(),
                    editedFirstName,
                )
                isSuccessLiveData.observe(this@ActivityEditProfile) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(this@ActivityEditProfile,constants.UPDATE_SUCCESSFULLY, Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this@ActivityEditProfile, "Failed to update FA details", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

        if (TextUtils.isEmpty(editedLastName)){}
        else if(!TextUtils.isEmpty(editedLastName)){
            lifecycleScope.launch {
                val isSuccessLiveData = repo.updateFALastName(
                    sharedPrefManager.getToken(),
                    editedLastName,
                )
                isSuccessLiveData.observe(this@ActivityEditProfile) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(this@ActivityEditProfile,constants.UPDATE_SUCCESSFULLY, Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this@ActivityEditProfile, "Failed to update FA details", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        if (TextUtils.isEmpty(editedDesignation))
        else if(!TextUtils.isEmpty(editedDesignation)){
            lifecycleScope.launch {
                val isSuccessLiveData = repo.updateFADesignation(
                    sharedPrefManager.getToken(),
                    editedDesignation,
                )
                isSuccessLiveData.observe(this@ActivityEditProfile) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(this@ActivityEditProfile,constants.UPDATE_SUCCESSFULLY, Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this@ActivityEditProfile, "Failed to update FA details", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        if (TextUtils.isEmpty(editedCNIC))
        else if(!TextUtils.isEmpty(editedCNIC)){
            lifecycleScope.launch {
                val isSuccessLiveData = repo.updateFACNIC(
                    sharedPrefManager.getToken(),
                    editedCNIC,
                )
                isSuccessLiveData.observe(this@ActivityEditProfile) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(this@ActivityEditProfile,constants.UPDATE_SUCCESSFULLY, Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this@ActivityEditProfile, "Failed to update FA details", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        if (TextUtils.isEmpty(editedpassword))
        else if(!TextUtils.isEmpty(editedpassword)){
            lifecycleScope.launch {
                val isSuccessLiveData = repo.updateFAPassword(
                    sharedPrefManager.getToken(),
                    editedpassword,
                )
                isSuccessLiveData.observe(this@ActivityEditProfile) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(this@ActivityEditProfile,constants.UPDATE_SUCCESSFULLY, Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this@ActivityEditProfile, "Failed to update FA details", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        if (TextUtils.isEmpty(editedaddress))
        else if(!TextUtils.isEmpty(editedaddress)){
            lifecycleScope.launch {
                val isSuccessLiveData = repo.updateFAAddress(
                    sharedPrefManager.getToken(),
                    editedaddress,
                )
                isSuccessLiveData.observe(this@ActivityEditProfile) { isSuccess ->
                    if (isSuccess) {
                        Toast.makeText(this@ActivityEditProfile,constants.UPDATE_SUCCESSFULLY, Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this@ActivityEditProfile, "Failed to update FA details", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    }

    private fun getFAID() {
        db.collection(constants.FA_COLLECTION).whereEqualTo(FieldPath.documentId(),sharedPrefManager.getToken())
            .addSnapshotListener { snapshot, e ->
                if (snapshot != null) {
                    utils.startLoadingAnimation()
                    snapshot.documents?.forEach { document ->
                        photo = document.getString("photo").toString()
                        Glide.with(mContext)
                            .load(photo)
                            .centerCrop()
                            .placeholder(R.drawable.ic_launcher_background) // Placeholder image while loading
                            .into(binding.imgView)
                        utils.endLoadingAnimation()
                    }
                }
            }

    }

    fun showPhotoDialog() {

        dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setContentView(R.layout.dialog_profile_photo_upload)

        imgProfilePhoto = dialog.findViewById<ImageView>(R.id.imgProfilePhoto)
        val tvSelect = dialog.findViewById<TextView>(R.id.tvSelect)
        val btnUplodProfile = dialog.findViewById<Button>(R.id.btnUplodProfile)

        tvSelect.setOnClickListener {
            resultLauncher.launch("image/*")
        }

        btnUplodProfile.setOnClickListener {

            lifecycleScope.launch {
                if (imageURI != null) addUserPhoto(imageURI!!, "AdvisorProfilePhoto")
                else Toast.makeText(mContext, "Please Select Image", Toast.LENGTH_SHORT).show()
            }


        }

        dialog.show()
    }


    private var resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()){
        imageURI = it
        imgProfilePhoto.setImageURI(imageURI)

    }


    fun addUserPhoto(imageUri: Uri, type: String) {

        /* utils.startLoadingAnimation()
         userViewModel.uploadPhotoRefrence(imageUri,type)
             .downloadUrl.addOnSuccessListener {uri ->
                 var user:User=sharedPrefManager.getUser()
                 user.photo= uri.toString()
                 lifecycleScope.launch {

                     userViewModel.updateUser(user).observe(this@ActivityUserDetails) {
                         utils.endLoadingAnimation()
                         if (it == true) {
                             sharedPrefManager.saveUser(user)
                             sharedPrefManager.putUserPhoto(true)
                             Toast.makeText(mContext, "Profile Photo Updated", Toast.LENGTH_SHORT).show()
                             dialog.dismiss()
                             checkData()
                         }
                         else Toast.makeText(mContext, constants.SOMETHING_WENT_WRONG_MESSAGE, Toast.LENGTH_SHORT).show()

                     }
                 }
         }*/
        utils.startLoadingAnimation()
        repo.uploadPhoto(imageUri, type)
            .addOnSuccessListener { taskSnapshot ->
                taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
                    var modelFA: ModelFA = sharedPrefManager.getUser()
                    modelFA.photo = uri.toString()

                        repo.updateUserPhoto(modelFA)

                    if(repo.updateUserPhoto(modelFA).equals(false)){
                        utils.endLoadingAnimation()
                        Toast.makeText(mContext,
                            constants.SOMETHING_WENT_WRONG_MESSAGE,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else{
                        utils.endLoadingAnimation()
                        sharedPrefManager.saveUser(modelFA)
                        utils.endLoadingAnimation()
                        Toast.makeText(
                            mContext,
                            "Profile Photo Updated",
                            Toast.LENGTH_SHORT
                        ).show()
                        dialog.dismiss()

                    }
                }
                    .addOnFailureListener { exception ->
                        Toast.makeText(mContext, exception.message + "", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(mContext, "Failed to upload profile pic", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(mContext,MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK))

    }



}