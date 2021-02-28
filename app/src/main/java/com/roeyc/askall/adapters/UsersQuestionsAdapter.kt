package com.roeyc.askall.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.roeyc.askall.R
import com.roeyc.askall.databinding.QuestionItemBinding
import com.roeyc.askall.domain.QuestionDataModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class UsersQuestionsAdapter() : RecyclerView.Adapter<UsersQuestionsAdapter.QuestionsViewHolder>() {

    /**
     * The questions that our Adapter will show
     */
    var questionsList: List<QuestionDataModel> = emptyList()
        set(value) {
            field = value
            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int {
        return questionsList.size
    }

    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuestionsViewHolder {
        val withDataBinding: QuestionItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.question_item,
                parent,
                false
        )
        return QuestionsViewHolder(withDataBinding)
    }

    override fun onBindViewHolder(holder: QuestionsViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.question = questionsList[position]
            holder.bind(it.question!!)
        }
    }

    // a global variable which will emit String items
    private val clickSubject = PublishSubject.create<QuestionDataModel>()
    val clickEvent : Observable<QuestionDataModel> = clickSubject

    inner class QuestionsViewHolder(val viewDataBinding: QuestionItemBinding) :RecyclerView.ViewHolder(viewDataBinding.root) {

        init {
            itemView.setOnClickListener {
                clickSubject.onNext(questionsList[layoutPosition])
            }
        }

        fun bind(question: QuestionDataModel) {
            viewDataBinding.questionPreview.text = question.question
        }


    }

}