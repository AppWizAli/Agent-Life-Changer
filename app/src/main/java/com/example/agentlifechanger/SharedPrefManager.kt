package com.example.agentlifechanger

import android.content.Context
import android.content.SharedPreferences
import com.example.agentlifechanger.Models.ModelUser
import com.google.firebase.firestore.auth.User
import com.google.gson.Gson

class SharedPrefManager(context : Context) {

    private val sharedPref: SharedPreferences = context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun saveUser(user: ModelUser) {

        editor.putString("User", Gson().toJson(user))
        editor.commit()
    }

    fun putToken(docID: String) {
        editor.putString("docID", docID)
        editor.commit()
    }
    fun setLogin(isLoggedIn: Boolean = false) {
        editor.putBoolean("isLoggedIn", isLoggedIn)
        editor.commit()
    }

    fun isLoggedIn(): Boolean {
        var isLoggedIn:Boolean=false
        if(sharedPref.getBoolean("isLoggedIn", false).equals(null)) isLoggedIn= false
        else if(sharedPref.getBoolean("isLoggedIn", false)==true) isLoggedIn =true
        return isLoggedIn
    }

    fun saveLoginAuth(user: ModelUser,token:String, loggedIn: Boolean){
        saveUser(user)
        putToken(token)
        setLogin(loggedIn)
    }

    fun getToken(): String {
        return sharedPref.getString("docID", "")!!
    }

    fun logOut(isLoggedOut: Boolean = false) {
        editor.putBoolean("isLoggedIn", isLoggedOut)
        editor.commit()
    }







}