package com.roeyc.askall.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.roeyc.askall.R
import com.roeyc.askall.databinding.ActivityQuestionBinding
import com.roeyc.askall.domain.QuestionDataModel
import com.roeyc.askall.fragments.QuestionFragment
import com.roeyc.askall.fragments.UserInfoFragment
import com.roeyc.askall.viewmodels.QuestionViewModel
import kotlinx.android.synthetic.main.activity_question.*

class QuestionActivity : AppCompatActivity() {

    val questionFragment = QuestionFragment()
    val userInfoFragment = UserInfoFragment()
    private lateinit var binding: ActivityQuestionBinding

    private val viewModel: QuestionViewModel by lazy {
        val activity = requireNotNull(this) {
            "You can only access the viewModel after onActivityCreated()"
        }
        val questionData = intent.getParcelableExtra<QuestionDataModel>("QuestionDataModel")

        ViewModelProviders.of(this, QuestionViewModel.QuestionViewModelFactory(questionData!!, activity.application))
            .get(QuestionViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)
        binding = ActivityQuestionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setCurrentFragment(questionFragment)
        initListeners()
        viewModel.initQuestionData(intent.getParcelableExtra<QuestionDataModel>("QuestionDataModel")!!)
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment_container, fragment)
            commit()
        }

    private fun initListeners() {
        bottom_navigation.setOnNavigationItemSelectedListener {  item ->
            when (item.itemId) {
                R.id.action_question -> {
                    setCurrentFragment(questionFragment)
                }
                R.id.action_user_info -> {
                    setCurrentFragment(userInfoFragment)
                }
            }
            true
        }
    }


}