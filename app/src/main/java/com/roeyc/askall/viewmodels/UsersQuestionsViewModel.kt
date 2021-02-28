package com.roeyc.askall.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.roeyc.askall.domain.QuestionDataModel
import java.sql.Timestamp

class UsersQuestionsViewModel(application: Application) : BaseViewModel(application) {

    private val TAG = UsersQuestionsViewModel::class.java.simpleName
    private var firestore = Firebase.firestore
 //   private var auth: FirebaseAuth
 //   private val user : FirebaseUser

//    private var _totalUsersVotes = MutableLiveData<HashMap<String, Map<String, Any>>>()
//    val totalUsersVotes: LiveData<HashMap<String, Map<String, Any>>>
//        get() = _totalUsersVotes

    private var _totalUsersVotes = MutableLiveData<ArrayList<QuestionDataModel>>()
    val totalUsersVotes: LiveData<ArrayList<QuestionDataModel>>
        get() = _totalUsersVotes


    init {
    //    auth = FirebaseAuth.getInstance()
    //    user = auth.currentUser!!
        getQuestions()
    }


    fun getQuestions() {
        var response: ArrayList<QuestionDataModel> = ArrayList()

        firestore.collection("Questions")
                  .get()
                  .addOnSuccessListener {
                      documents ->
                      for (document in documents) {

                          var questionData =  QuestionDataModel()
                          questionData.uid = document.data.get("uid").toString()
                          questionData.timestamp = document.data.get("timestamp").toString()
                          questionData.name = document.data.get("name").toString()
                          questionData.sex = document.data.get("sex").toString()
                          questionData.age = document.data.get("age").toString().toInt()
                          questionData.question = document.id
                          questionData.first_answer = document.data.get("first_answer").toString()
                          questionData.second_answer = document.data.get("second_answer").toString()
                          questionData.usersVotedA = document.data.get("usersVotedA")!! as ArrayList<String>
                          questionData.usersVotedB = document.data.get("usersVotedB")!! as ArrayList<String>
                          response.add(questionData)

                      }

                      _totalUsersVotes.value = response

                      Log.d(TAG, "Success getting documents.")
                  }.addOnFailureListener { exception ->
                      Log.d(TAG, "Failure getting documents.")
                }
    }

    /**
     * Factory for constructing StockViewModel with parameter
     */
    class UsersQuestionsViewModelFactory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UsersQuestionsViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UsersQuestionsViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}