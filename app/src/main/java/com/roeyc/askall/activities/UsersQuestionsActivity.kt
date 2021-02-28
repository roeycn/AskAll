package com.roeyc.askall.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.roeyc.askall.R
import com.roeyc.askall.adapters.UsersQuestionsAdapter
import com.roeyc.askall.databinding.ActivityUsersQuestionsBinding
import com.roeyc.askall.viewmodels.UsersQuestionsViewModel
import io.reactivex.disposables.Disposable

class UsersQuestionsActivity : AppCompatActivity() {

    private val TAG = UsersQuestionsActivity::class.java.simpleName
    private var subscribe: Disposable? = null

    lateinit var viewModel: UsersQuestionsViewModel
    // Simple one-liner ViewBinding - https://medium.com/@Zhuinden/simple-one-liner-viewbinding-in-fragments-and-activities-with-kotlin-961430c6c07c
    private lateinit var binding: ActivityUsersQuestionsBinding
    private var usersQuestionsAdapter: UsersQuestionsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this).get(UsersQuestionsViewModel::class.java)

        setContentView(R.layout.activity_users_questions)
        binding = ActivityUsersQuestionsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        usersQuestionsAdapter = UsersQuestionsAdapter()
        binding.usersQuestionsRecyclerview.layoutManager = LinearLayoutManager(this)
        binding.usersQuestionsRecyclerview.adapter = usersQuestionsAdapter

        viewModel.totalUsersVotes.observe(this, Observer {
            Log.d(TAG, it.toString())
            usersQuestionsAdapter!!.questionsList = it
            binding.loadingSpinner.visibility = View.INVISIBLE //check how to do it via bindingAdapters goneIfNotNull
        })

        setItemClick()
    }

    private fun setItemClick() {
        subscribe = usersQuestionsAdapter!!.clickEvent.subscribe({

            val intent = Intent(this, QuestionActivity::class.java)
            intent.putExtra("QuestionDataModel", it)
            startActivity(intent)
        //       Toast.makeText(this, "Clicked on $it", Toast.LENGTH_LONG).show()
        //  FragmentTransaction transaction =  getSupportFragmentManager().b
        //   findNavController(binding.root.id).navigate(UsersQuestionsActivityDirections.actionUsersQuestionsActivityToQuestionFragment())
        // add item fragment with layout
        // use the nav_graph.xml https://developer.android.com/guide/navigation/navigation-getting-started
        })

    }
}