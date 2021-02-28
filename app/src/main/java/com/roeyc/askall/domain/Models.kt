package com.roeyc.askall.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.sql.Timestamp

/**
 * Domain objects are plain Kotlin data classes that represent the things in our app. These are the
 * objects that should be displayed on screen, or manipulated by the app.
 *
 * @see database for objects that are mapped to the database
 * @see network for objects that parse or prepare network calls
 */

/**
 * Question represent a question data
 */
@Parcelize
data class QuestionDataModel(
        var uid: String = "",
        var timestamp: String = "",
        var name: String = "",
        var sex: String = "",
        var age: Int = 0,
        var question: String = "",
        var first_answer: String = "",
        var second_answer: String = "",
        var usersVotedA: ArrayList<String> = ArrayList(),
        var usersVotedB: ArrayList<String> = ArrayList()
        ) : Parcelable {
}

data class QustionDocument (
    var questions: List<QuestionDataModel>
) {
}
