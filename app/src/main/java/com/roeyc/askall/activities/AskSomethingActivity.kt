package com.roeyc.askall.activities

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.roeyc.askall.R
import com.roeyc.askall.databinding.ActivityAskSomethingBinding
import com.roeyc.askall.domain.QuestionDataModel
import com.roeyc.askall.viewmodels.AskSomethingViewModel
import kotlinx.android.synthetic.main.activity_ask_something.*
import kotlinx.android.synthetic.main.activity_ask_something_content_scrolling.*


class AskSomethingActivity : AppCompatActivity() {

    lateinit var viewModel: AskSomethingViewModel
    private lateinit var binding: ActivityAskSomethingBinding
    var questionData =  QuestionDataModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ask_something)
        viewModel = ViewModelProvider(this).get(AskSomethingViewModel::class.java)
        binding = ActivityAskSomethingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar_layout.title = title

        number_picker.minValue = 0
        number_picker.maxValue = 120

        number_picker.setOnValueChangedListener { picker, oldVal, newVal ->
            questionData.age = newVal
        }

        questionData.sex = "male"

        //val displayName = SharedPref.read(SharedPref.SIGN_IN, SharedPref.DISPLAY_NAME)
        //binding.textView1.text = displayName

        initObservables()
        initListeners()
    }

    private fun initObservables() {
        viewModel.sendQuestionFailed.observe(this, Observer {
            it?.let {
                 MaterialAlertDialogBuilder(this)
                .setTitle(R.string.send_your_question_failed_dialog_title)
                .setMessage(R.string.send_your_question_failed_dialog_subtitle)
                .setNeutralButton("OK") { dialog, which ->
                    finish()
                }
                .show()
            }
        })

        viewModel.sendQuestionSucceded.observe(this, Observer {
            it?.let {
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.send_your_question_succeded_dialog_title)
                    .setNeutralButton("OK") { dialog, which ->
                        finish()
                    }
                    .show()
            }
        })
    }

    private fun initListeners() {

        fab.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.send_your_question_info_dailog_title)
                    .setMessage(R.string.send_your_question_info_dailog_subtitle)
                    .setNeutralButton("OK") { dialog, which ->
                        // dialog will be closed
                    }
                    .show()
        }

        send_question_button.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.send_your_question_dailog_title)
                .setMessage(R.string.send_your_question_dailog_subtitle)
                .setPositiveButton("YES") { dialog, which ->
                    viewModel.sendQuestion(questionData)
                    viewModel.sendQuestionSuccededComplete()
                    viewModel.sendQuestionFailedComplete()
                }.setNegativeButton("No") { dialog, which ->
                    // dialog will be closed
                }
                .show()
        }

        val submitQuestionTextWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                questionData.name = edit_name.text.toString().trim()
                questionData.question = edit_question.text.toString().trim()
                questionData.first_answer = edit_first_answer.text.toString().trim()
                questionData.second_answer = edit_second_answer.text.toString().trim()

                send_question_button.isEnabled = (!questionData.name.trim().isEmpty() && !questionData.question.trim().isEmpty()
                        && !questionData.first_answer.trim().isEmpty() && !questionData.second_answer.trim().isEmpty())

            }

        }

        edit_name.addTextChangedListener(submitQuestionTextWatcher)
        edit_question.addTextChangedListener(submitQuestionTextWatcher)
        edit_first_answer.addTextChangedListener(submitQuestionTextWatcher)
        edit_second_answer.addTextChangedListener(submitQuestionTextWatcher)
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Is the button now checked?
            val checked = view.isChecked

            // Check which radio button was clicked
            when (view.getId()) {
                R.id.radio_male ->
                    if (checked) {
                        questionData.sex = "male"
                    }
                R.id.radio_female ->
                    if (checked) {
                        questionData.sex = "female"
                    }
            }
        }
    }



}