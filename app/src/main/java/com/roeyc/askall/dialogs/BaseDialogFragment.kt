package com.roeyc.stockapp.dialogs

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment


open class BaseDialogFragment : DialogFragment() {

    private val TAG = BaseDialogFragment::class.java.simpleName

    protected var mIsFragmentPaused = true
    protected var isLandscape = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i(TAG,"onCreate")
        isLandscape =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG,"onCreate")
        mIsFragmentPaused = true
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG,"onResume")
        mIsFragmentPaused = false
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG,"onStop")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG,"onStart")
    }

    override fun onDestroy() {
        Log.i(TAG,"onDestroy")
        super.onDestroy()
    }


}