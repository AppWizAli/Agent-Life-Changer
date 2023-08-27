package com.example.agentlifechanger

import android.content.Context
import android.content.SharedPreferences
import com.example.agentlifechanger.Models.ModelFA
import com.example.agentlifechanger.Models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class SharedPrefManagar (context: Context ){

    private val sharedPref: SharedPreferences = context.getSharedPreferences("myAppPrefs", Context.MODE_PRIVATE)

    private val editor: SharedPreferences.Editor = sharedPref.edit()

    fun saveUser(user: ModelFA) {
        editor.putString("com.example.agentlifechanger.Models.User", Gson().toJson(user))
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

    fun saveLoginAuth(user: ModelFA, token:String, loggedIn: Boolean){
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
    fun putUserList(list: List<User>) {
        editor.putString("ListUser", Gson().toJson(list))
        editor.commit()
    }

    fun getUser(): ModelFA {

        val json = sharedPref.getString("com.example.agentlifechanger.Models.User", "") ?: ""
        return Gson().fromJson(json, ModelFA::class.java)

    }


    fun getUsersList(): List<User>{

        val json = sharedPref.getString("ListUser", "") ?: ""
        val type: Type = object : TypeToken<List<User?>?>() {}.getType()
        //return Gson().fromJson(json, type)
        return if (!json.isNullOrEmpty()) {
            Gson().fromJson(json, type) ?: emptyList()
        } else {
            emptyList()
        }
    }


}