package com.roeyc.askall.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.roeyc.askall.R
import com.roeyc.askall.domain.QuestionDataModel
import com.roeyc.stockapp.dialogs.AppDialogItem

class QuestionViewModel(questionData: QuestionDataModel, application: Application) : BaseViewModel(application) {

    private val TAG = QuestionViewModel::class.java.simpleName
    private var firestore = Firebase.firestore
    var auth : FirebaseAuth
    lateinit var user : FirebaseUser

    private var _questionData = MutableLiveData<QuestionDataModel>()
    val questionData: LiveData<QuestionDataModel>
        get() = _questionData

    private var _firstButton = MutableLiveData<Boolean>()
    val firstButton: LiveData<Boolean>
        get() = _firstButton

    private var _secondButton = MutableLiveData<Boolean>()
    val secondButton: LiveData<Boolean>
        get() = _secondButton

    init {
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            user = auth.currentUser!!
        }
        _questionData.value = questionData
    }

    fun firstButtonClicked() {
        _firstButton.value = true
    }

    fun secondButtonClicked() {
        _secondButton.value = true
    }

    fun onButtonPressedComplete() {
        _firstButton.value = null
        _secondButton.value = null
    }

    fun initQuestionData(questionDataModel: QuestionDataModel) {
        _questionData.value = questionDataModel
    }

    fun setUserVote(firstButton: Boolean, document: String) {

        val addUserToArrayMap: MutableMap<String, Any> = HashMap()

        if (firstButton) {
            addUserToArrayMap["usersVotedA"] = FieldValue.arrayUnion(user!!.uid)
        } else {
            addUserToArrayMap["usersVotedB"] = FieldValue.arrayUnion(user!!.uid)
        }

        firestore.collection("Questions").document(document)
            .update(addUserToArrayMap)
            .addOnSuccessListener {
            Log.d(TAG, "update succeed")
        }.addOnFailureListener{
            Log.d(TAG, "update failed: {}" + it)
        }

    }

    fun checkIfUserAlreadyVoted(userId: String) : Boolean {
        for (id in questionData.value!!.usersVotedA) {
            if (id.equals(userId)) {
                return true
            }
        }

        for (id in questionData.value!!.usersVotedB) {
            if (id.equals(userId)) {
                return true
            }
        }
        return false
    }

    fun showPleaseLoginInOrderToVoteDialog() {
        Log.i(TAG, "show Please Login in order to vote Dialog")
        showDialogParamsLiveData.value  = AppDialogItem.Builder()
            .setTitleText(getString(R.string.dialog_info_please_login_title))
            .setMessageText(getString(R.string.dialog_info_please_login_in_order_to_vote))
            .setPositiveButtonText(getString(R.string.dialog_positive_button_text_ok))
            .setStyle(AppDialogItem.DialogStyle.InfoDialog)
            .build()
    }

    /**
     * Factory for constructing StockInfoViewModel with parameter
     */
    class QuestionViewModelFactory(val questionData: QuestionDataModel, val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(QuestionViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return QuestionViewModel(questionData , app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }


}