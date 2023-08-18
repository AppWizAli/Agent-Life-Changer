package com.example.agentlifechanger.UI

import android.app.Dialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.agentlifechanger.Constants
import com.example.agentlifechanger.R
import com.example.agentlifechanger.SharedPrefManager
import com.example.agentlifechanger.Utils
import com.itextpdf.io.codec.brotli.dec.Dictionary.getData
import kotlinx.coroutines.launch
import java.util.Timer
import kotlin.concurrent.schedule

class ActivitySplash : AppCompatActivity() {

    private lateinit var utils: Utils
    private lateinit var mContext: Context
    private lateinit var constants: Constants
    private lateinit var sharedPrefManager : SharedPrefManager
    private lateinit var dialog : Dialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)


        mContext= this@ActivitySplash;
        utils = Utils(mContext)
        constants= Constants()
        sharedPrefManager = SharedPrefManager(mContext)

        Timer().schedule(1500){

            if(sharedPrefManager.isLoggedIn()==true){
                startActivity(Intent(mContext,MainActivity::class.java))
                finish()
            }
            else if(sharedPrefManager.isLoggedIn()==false) {
                startActivity(Intent(mContext,ActivityLogin::class.java))
                finish()
            }
            else if (sharedPrefManager.logOut().equals(false)){
                startActivity(Intent(mContext,ActivityLogin::class.java))
                finish()
            }

        }




    }

}