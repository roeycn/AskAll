package com.roeyc.askall

import android.app.Application
import android.util.Log
import com.roeyc.askall.viewmodels.MainActivityViewModel

class AskAll : Application() {

    private val TAG = AskAll::class.java.simpleName

    fun MyApp() {
        // this method fires only once per application start.
        // getApplicationContext returns null here
        Log.i(TAG, "Constructor fired")
    }

    override fun onCreate() {
        super.onCreate()

        // this method fires once as well as constructor
        // but also application has context here

        Log.i(TAG, "onCreate fired");

    }
}