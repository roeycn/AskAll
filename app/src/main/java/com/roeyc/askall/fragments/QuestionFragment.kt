package com.roeyc.askall.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.roeyc.askall.R
import com.roeyc.askall.databinding.FragmentQuestionBinding
import com.roeyc.askall.domain.QuestionDataModel
import com.roeyc.askall.viewmodels.QuestionViewModel
import com.roeyc.stockapp.dialogs.APPDialog
import com.roeyc.stockapp.dialogs.AppDialogItem
import kotlinx.android.synthetic.main.fragment_question.*

/**
 * A simple [Fragment] subclass.
 * Use the [QuestionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuestionFragment : Fragment() {

    private lateinit var binding: FragmentQuestionBinding
    lateinit var bardataset: BarDataSet
    // Use the 'by activityViewModels()' Kotlin property delegate from the fragment-ktx artifact
    val sharedViewModel: QuestionViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentQuestionBinding.inflate(layoutInflater)
        auth = Firebase.auth
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = sharedViewModel
        initView()
        initListeners()
        initObservables()
        initBarChart(binding.viewModel!!.questionData.value!!)
    }

    private fun initView() {
        //check if user is signed in
        if (auth.currentUser != null) {
            // if yes - check if he already voted
            if (binding.viewModel!!.checkIfUserAlreadyVoted(auth.currentUser!!.uid)) {
                binding.firstButton.isEnabled = false
                binding.secondButton.isEnabled = false
                binding.thanksForVoting.visibility = View.VISIBLE
            }
        }
    }

    private fun initObservables() {
        binding.viewModel!!.showDialogParamsLiveData.observe(viewLifecycleOwner, Observer {
            showAppDialog(it)
        })
    }

    private fun showAppDialog(appDialogItem: AppDialogItem, forceShow: Boolean = false) {
        APPDialog.show(childFragmentManager, appDialogItem, forceShow)
    }

    private fun initListeners() {
        first_button?.setOnClickListener {
            // check if user is logged in
            if (binding.viewModel!!.auth.currentUser != null) {
                binding.viewModel!!.setUserVote(true, binding.question.text.toString())
                binding.firstButton.isEnabled = false
                binding.secondButton.isEnabled = false
                binding.thanksForVoting.visibility = View.VISIBLE
                updateUserVote("a")
            } else {
                binding.viewModel!!.showPleaseLoginInOrderToVoteDialog()
            }
        }

        second_button?.setOnClickListener {
            // check if user is logged in
            if (binding.viewModel!!.auth.currentUser != null) {
                binding.viewModel!!.setUserVote(false, binding.question.text.toString())
                binding.firstButton.isEnabled = false
                binding.secondButton.isEnabled = false
                binding.thanksForVoting.visibility = View.VISIBLE
                updateUserVote("b")
            } else {
                binding.viewModel!!.showPleaseLoginInOrderToVoteDialog()
            }
        }
    }

    // update locally that the user vote (instead of go to the server and get all data again)
    private fun updateUserVote(s: String) {
        if (s.equals("a")) {
            binding.viewModel!!.questionData.value!!.usersVotedA.add(auth.currentUser!!.uid)
        } else if (s.equals("b")) {
            binding.viewModel!!.questionData.value!!.usersVotedB.add(auth.currentUser!!.uid)
        }
        initBarChart(binding.viewModel!!.questionData.value!!)

    }

    private fun initBarChart(questionData: QuestionDataModel) {

        val labels = arrayListOf<String>()
        val barEntries: ArrayList<BarEntry> = ArrayList()

        labels.add("users voted: " + questionData.first_answer)
        barEntries.add(BarEntry(questionData.usersVotedA.size.toFloat(), 0))
        labels.add("users voted: " + questionData.second_answer)
        barEntries.add(BarEntry(questionData.usersVotedB.size.toFloat(), 1))

        bardataset = BarDataSet(barEntries, "Cells")

        val colors = intArrayOf(Color.BLUE, Color.YELLOW)
        bardataset.setColors(colors)
        bardataset.setValueTextColor(Color.BLACK)

        val data = BarData(labels, bardataset)

        barChart.data = data // set the data and list of labels into chart
        barChart.animateY(2000)

        barChart.setDrawBarShadow(true)
        barChart.setDrawValueAboveBar(false)

        barChart.axisLeft.setAxisMinValue(0.0F)
        barChart.axisRight.setAxisMinValue(0.0F)

        binding.numberOfVotesA.text = getString(R.string.votes, questionData.usersVotedA.size.toString())
        binding.numberOfVotesB.text = getString(R.string.votes, questionData.usersVotedB.size.toString())
    }



}