package com.roeyc.askall.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

abstract class BaseViewModel(application: Application) : AndroidViewModel(application), IBaseViewModel {

    override val hideDialogLiveData = MutableLiveData<Unit>()

    override val showLoadingDialogLiveData = MutableLiveData<Boolean>()

    override val hideSpecificDialogLiveData = MutableLiveData<String>()


    override fun observeShowLoadingDialogLiveData(lifecycle: Lifecycle, observer: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

}