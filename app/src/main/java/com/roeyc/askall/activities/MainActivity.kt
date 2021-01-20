package com.roeyc.askall.activities


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.roeyc.askall.NavigationIconClickListener
import com.roeyc.askall.R
import com.roeyc.askall.databinding.ActivityMainBinding
import com.roeyc.askall.viewmodels.MainActivityViewModel

class MainActivity : AppCompatActivity() {

    // using android  https://developer.android.com/topic/libraries/view-binding
    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainActivityViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
       // setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setMenu()
        // Initialize Firebase Auth
        auth = Firebase.auth

        binding.loginButton.setOnClickListener() {
            signIn()
        }

        binding.logoutButton.setOnClickListener()
        {
            signOut()
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            binding.hello.text = getString(R.string.sign_in_signed_user, user.displayName) //"Welcome: " + user.displayName
        } else {
            binding.hello.setText(R.string.sign_in)
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

    private fun setMenu() {
        setSupportActionBar(binding.appBar)
        // handling the open/close manu button
        binding.appBar.setNavigationOnClickListener(
            NavigationIconClickListener(this, binding.grid, AccelerateDecelerateInterpolator(),
                ContextCompat.getDrawable(this, R.drawable.ic_menu_black), // Menu open icon
                ContextCompat.getDrawable(this, R.drawable.ic_close_black))
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
       super.onCreateOptionsMenu(menu)
       // menuInflater.inflate(R.layout.menu_backdrop, menu)
      // menuInflater.inflate(R.menu.menu_backdrop, menu)
        return true
    }
}