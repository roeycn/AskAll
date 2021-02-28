package com.roeyc.askall.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.roeyc.askall.AskAll
import com.roeyc.askall.common.SingleLiveEvent
import com.roeyc.stockapp.dialogs.AppDialogItem

abstract class BaseViewModel(application: Application) : AndroidViewModel(application), IBaseViewModel {

    override val showDialogParamsLiveData = SingleLiveEvent<AppDialogItem>()

    override val hideDialogLiveData = MutableLiveData<Unit>()

    override val showLoadingDialogLiveData = MutableLiveData<Boolean>()

    override val hideSpecificDialogLiveData = MutableLiveData<String>()


    override fun observeShowLoadingDialogLiveData(lifecycle: Lifecycle, observer: (Boolean) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun observeShowDialogLiveData(lifecycle: Lifecycle, observer: (AppDialogItem) -> Unit) {
        showDialogParamsLiveData.observe({lifecycle}) {
            it.let(observer)
        }
    }

    protected fun getApplicationContext(): Context {
        return getApplication<AskAll>()
    }

    protected fun getString(stringId: Int): String {
        return getApplicationContext().getString(stringId)
    }


}