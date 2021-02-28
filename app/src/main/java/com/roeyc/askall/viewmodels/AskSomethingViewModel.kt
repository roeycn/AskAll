package com.roeyc.askall.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.roeyc.askall.R
import com.roeyc.askall.domain.QuestionDataModel

class AskSomethingViewModel(application: Application) : BaseViewModel(application) {

    private val TAG = AskSomethingViewModel::class.java.simpleName
    private var firestore = Firebase.firestore
    private var auth : FirebaseAuth
    private val user : FirebaseUser

    private val _sendQuestionFailed = MutableLiveData<Boolean>()
    val sendQuestionFailed : LiveData<Boolean>
        get() = _sendQuestionFailed

    private val _sendQuestionSucceded = MutableLiveData<Boolean>()
    val sendQuestionSucceded : LiveData<Boolean>
        get() = _sendQuestionSucceded

    init {
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser!!
    }

    fun sendQuestionFailedComplete() {
        _sendQuestionFailed.value = null
    }

    fun sendQuestionSuccededComplete() {
        _sendQuestionSucceded.value = null
    }

    fun sendQuestion(questionDataModel: QuestionDataModel) {
        var userQuestion = firestore.collection("Questions").document(questionDataModel.question)
        var usersVoted: ArrayList<String> = ArrayList()

        val questionFields = hashMapOf(
            "uid" to user.uid,
            "timestamp" to FieldValue.serverTimestamp(),
            "name" to questionDataModel.name,
            "sex" to questionDataModel.sex,
            "age" to questionDataModel.age,
            "first_answer" to questionDataModel.first_answer,
            "second_answer" to questionDataModel.second_answer,
            "usersVotedA" to   usersVoted,
            "usersVotedB" to usersVoted
        )

        var userLastRequest = firestore.collection("Users").document(user.uid)
        val timeStep = hashMapOf(
                "lastRequest" to FieldValue.serverTimestamp()
        )

        firestore.runBatch { batch ->
            batch.set(
                userQuestion,
                questionFields,
                SetOptions.merge()
            )
            batch.set(
                 userLastRequest,
                 timeStep
            )
        }.addOnSuccessListener {
            Log.d(TAG, "Update DocumentSnapshot succeed")
            _sendQuestionSucceded.value = true
        }.addOnFailureListener{
            Log.e(TAG, "Update DocumentSnapshot failed")
            _sendQuestionFailed.value = true
        }


    }
}