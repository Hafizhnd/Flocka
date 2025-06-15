package com.example.flocka.data.local.entity

import androidx.room.PrimaryKey
import com.example.flocka.data.local.entity.QuizEntity.Entity
import com.example.flocka.data.model.QuizQuestion

@Entity(tableName = "quiz_questions")
data class QuizEntity(
    @PrimaryKey
    val quizId: String,
    val questionText: String,
    val option1: String,
    val option2: String,
    val option3: String?,
    val option4: String?,
    val fetchedAt: Long = System.currentTimeMillis(),
    val isUsed: Boolean = false
) {
    annotation class Entity(val tableName: String)

    fun toQuizQuestion(): QuizQuestion {
        return QuizQuestion(
            quizId = quizId,
            questionText = questionText,
            option1 = option1,
            option2 = option2,
            option3 = option3,
            option4 = option4
        )
    }

    companion object {
        fun fromQuizQuestion(quizQuestion: QuizQuestion): QuizEntity {
            return QuizEntity(
                quizId = quizQuestion.quizId,
                questionText = quizQuestion.questionText,
                option1 = quizQuestion.option1,
                option2 = quizQuestion.option2,
                option3 = quizQuestion.option3,
                option4 = quizQuestion.option4
            )
        }
    }
}

@Entity(tableName = "quiz_results")
data class QuizResultEntity(
    @PrimaryKey
    val quizId: String,
    val isCorrect: Boolean,
    val correctAnswerText: String?,
    val userStreak: Int,
    val answeredAt: Long = System.currentTimeMillis(),
    val answerGiven: Int
)