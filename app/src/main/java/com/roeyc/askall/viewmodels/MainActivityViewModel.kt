package com.roeyc.askall.viewmodels

import android.app.Application
import android.util.Log
import com.roeyc.askall.R
import com.roeyc.stockapp.dialogs.AppDialogItem


class MainActivityViewModel(application: Application) : BaseViewModel(application) {

    private val TAG = MainActivityViewModel::class.java.simpleName

    fun showPleaseLoginDialog() {
        Log.i(TAG, "show Please Login in order to send your question Dialog")
        showDialogParamsLiveData.value  = AppDialogItem.Builder()
            .setTitleText(getString(R.string.dialog_info_please_login_title))
            .setMessageText(getString(R.string.dialog_info_please_login_in_order_to_send_your_question))
            .setPositiveButtonText(getString(R.string.dialog_positive_button_text_ok))
            .setStyle(AppDialogItem.DialogStyle.InfoDialog)
            .build()
    }
}

