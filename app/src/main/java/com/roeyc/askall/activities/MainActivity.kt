package com.roeyc.askall.activities

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.transition.Fade
import android.view.View
import android.view.Window
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.roeyc.askall.NavigationIconClickListener
import com.roeyc.askall.R
import com.roeyc.askall.databinding.ActivityMainBinding
import com.roeyc.askall.utils.SharedPref
import com.roeyc.askall.viewmodels.MainActivityViewModel
import com.roeyc.stockapp.dialogs.APPDialog
import com.roeyc.stockapp.dialogs.AppDialogItem


class MainActivity : AppCompatActivity() {

    // using android  https://developer.android.com/topic/libraries/view-binding
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainActivityViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
       // setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root

        // https://heartbeat.fritz.ai/implementing-activity-and-element-transition-animations-in-android-5e2a2ba19f2f
        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
            // set set the transition to be shown when the user enters this activity
        //    enterTransition = Fade()
            // set the transition to be shown when the user leaves this activity
            exitTransition = Fade()
        }

        setContentView(view)
        init()
    }

    private fun init() {
        initSharedPref()
        initMenu()
        initFirebase()
        initSubscriptions()
        initObservables()
    }

    private fun initObservables() {
        viewModel.observeShowDialogLiveData(lifecycle) {
            showAppDialog(it)
        }
    }

    private fun showAppDialog(appDialogItem: AppDialogItem, forceShow: Boolean = false) {
        APPDialog.show(this.supportFragmentManager, appDialogItem, forceShow)
    }

    private fun initSharedPref() {
        SharedPref.init(applicationContext)
    }

    private fun initMenu() {
        setSupportActionBar(binding.appBar)
        // handling the open/close manu button
        binding.appBar.setNavigationOnClickListener(
            NavigationIconClickListener(this, binding.grid, AccelerateDecelerateInterpolator(),
                ContextCompat.getDrawable(this, R.drawable.ic_menu_black), // Menu open icon
                ContextCompat.getDrawable(this, R.drawable.ic_close_black))
        )
    }

    private fun initFirebase() {
        // Initialize Firebase Auth
        auth = Firebase.auth
        // https://firebase.google.com/docs/database/android/offline-capabilities
    }

    private fun initSubscriptions() {
        binding.signinButton.setOnClickListener() {
            signIn()
        }

        binding.signoutButton.setOnClickListener()
        {
            signOut()
        }

        binding.seeQuestionsButton.setOnClickListener()
        {
            val intent = Intent(this, UsersQuestionsActivity::class.java)
            startActivity(intent)
        }

        binding.askSomethingButton.setOnClickListener()
        {
            if (auth.currentUser != null) {
                // User is signed in.
                val intent = Intent(this, AskSomethingActivity::class.java)
               // startActivity(intent)
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
            }else {
                // user is NOT signed in.
                viewModel.showPleaseLoginDialog()
            }
        }
    }



    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {

        SharedPref.write(SharedPref.SIGN_IN, user?.displayName);//save string in shared preference.

        if (user != null) {
            binding.hello.text = getString(R.string.sign_in_signed_user, user.displayName)
            binding.signinButton.visibility = View.INVISIBLE
            binding.signoutButton.visibility = View.VISIBLE
        } else {
            binding.hello.setText(R.string.sign_in)
            binding.signinButton.visibility = View.VISIBLE
            binding.signoutButton.visibility = View.INVISIBLE
        }
    }

    private fun signIn() {
        // with firebaseui
        val providers = arrayListOf(
            AuthUI.IdpConfig.GoogleBuilder().build(),
            AuthUI.IdpConfig.EmailBuilder().build()
        )

        startActivityForResult(AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setIsSmartLockEnabled(false)
            .setTheme(R.style.LoginTheme)
            .build(),
            1822
        )

    }

    private fun signOut() {
        AuthUI.getInstance()
            .signOut(this)
            .addOnCompleteListener {
                Toast.makeText(this,"signed out successfully" , Toast.LENGTH_LONG).show()
            }
        updateUI(null)
    }

}