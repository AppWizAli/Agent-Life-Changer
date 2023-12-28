package com.enfotrix.falifechanger.UI

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enfotrix.falifechanger.Adapters.AdapterClient
import com.enfotrix.falifechanger.Constants
import com.enfotrix.falifechanger.Data.Repo
import com.enfotrix.falifechanger.Models.FAViewModel
import com.enfotrix.falifechanger.Models.InvesterViewModel
import com.enfotrix.falifechanger.Models.AgentTransactionModel
import com.enfotrix.falifechanger.Models.InvestmentModel
import com.enfotrix.falifechanger.Models.ModelFA
import com.enfotrix.falifechanger.Models.User
import com.enfotrix.falifechanger.R
import com.enfotrix.falifechanger.SharedPrefManagar
import com.enfotrix.falifechanger.Utils
import com.enfotrix.falifechanger.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var repo: Repo

    private lateinit var mContext: Context
    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPrefManager: SharedPrefManagar
    private lateinit var utils: Utils
    private var constants = Constants()
    private val db = Firebase.firestore
    private lateinit var modelFA: ModelFA

    private lateinit var userArraylist: ArrayList<User>
    private lateinit var investorIDArraylist: ArrayList<InvestmentModel>
    private lateinit var balanceArraylist: ArrayList<Int>
    private lateinit var faProfitModel: AgentTransactionModel

    private val investerViewModel: InvesterViewModel by viewModels()
    private val faViewModel: FAViewModel by viewModels()
    private lateinit var appUpdateManager: AppUpdateManager

    private lateinit var recyclerView: RecyclerView
    private lateinit var myAdapter: AdapterClient

    private var id = ""
    private var photo = ""


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this@MainActivity
        utils = Utils(mContext)
        sharedPrefManager = SharedPrefManagar(mContext)

        modelFA = ModelFA()
        repo = Repo(mContext)
        appUpdateManager= AppUpdateManagerFactory.create(this@MainActivity)


        checkUpdates()


        faProfitModel = AgentTransactionModel()
        recyclerView = findViewById(R.id.rvClients)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        investorIDArraylist = arrayListOf()
        userArraylist = arrayListOf()
        balanceArraylist = arrayListOf()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }



    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkUpdates()
    {
        var appUpdateInfoTask=appUpdateManager.appUpdateInfo
        appUpdateInfoTask.addOnSuccessListener { appApdateInfp->


            if(appApdateInfp.updateAvailability()== UpdateAvailability.UPDATE_AVAILABLE
                && appApdateInfp.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE))
            {
                appUpdateManager.startUpdateFlowForResult(
                    appApdateInfp,activityResultLauncher, AppUpdateOptions.newBuilder(AppUpdateType.FLEXIBLE)
                        .build()
                )
            }

        }


        appUpdateManager.registerListener(listener)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private  val listener= InstallStateUpdatedListener{ state->

        if(state.installStatus()== InstallStatus.DOWNLOADED)
        {
            popupSnackbarForCompleteUpdate()
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun popupSnackbarForCompleteUpdate()
    {
        Snackbar.make(
            findViewById(R.id.nav_view),
            "Ap updae has just been downloaded",
            Snackbar.LENGTH_INDEFINITE
        )
            .apply {
                setAction("Install"){appUpdateManager.completeUpdate()}
                    .setActionTextColor(getColor(android.R.color.holo_blue_dark))
                show()
            }
    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDestroy() {
        super.onDestroy()
        appUpdateManager.unregisterListener(listener)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onResume() {
        super.onResume()
        appUpdateManager.appUpdateInfo.addOnSuccessListener {appUpdateInfo->

            if(appUpdateInfo.installStatus()== InstallStatus.DOWNLOADED)
            {
                popupSnackbarForCompleteUpdate()
            }


        }
    }

    private  val activityResultLauncher=registerForActivityResult(
        ActivityResultContracts.StartIntentSenderForResult()
    )
    {result: ActivityResult ->
        if(result.resultCode!= RESULT_OK)
        {
            Toast.makeText(this@MainActivity, "Error", Toast.LENGTH_SHORT).show()
        }

    }


}
