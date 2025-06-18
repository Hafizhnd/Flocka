package com.example.flocka.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.flocka.data.model.QuizQuestion
import com.example.flocka.data.model.QuizResultResponseData

@Entity(tableName = "quiz_questions")
data class QuizEntity(
    @PrimaryKey
    val quizId: String,
    val questionText: String,
    val option1: String,
    val option2: String,
    val option3: String?,
    val option4: String?,
    val correctAnswer: Int,
    val fetchedAt: Long = System.currentTimeMillis(),
    val isUsed: Boolean = false,
    val isSynced: Boolean = true
) {
    fun toQuizQuestion(): QuizQuestion {
        return QuizQuestion(
            quizId = quizId,
            questionText = questionText,
            option1 = option1,
            option2 = option2,
            option3 = option3,
            option4 = option4,
            correctAnswer = correctAnswer
        )
    }

    companion object {
        fun fromQuizQuestion(quizQuestion: QuizQuestion, isSynced: Boolean = true): QuizEntity {
            return QuizEntity(
                quizId = quizQuestion.quizId,
                questionText = quizQuestion.questionText,
                option1 = quizQuestion.option1,
                option2 = quizQuestion.option2,
                option3 = quizQuestion.option3,
                option4 = quizQuestion.option4,
                correctAnswer = quizQuestion.correctAnswer,
                isSynced = isSynced
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
    val answerGiven: Int,
    val isSynced: Boolean = true
) {
    fun toQuizResultResponseData(): QuizResultResponseData {
        return QuizResultResponseData(
            quizId = quizId,
            isCorrect = isCorrect,
            correctAnswerText = correctAnswerText,
            userStreak = userStreak
        )
    }

    companion object {
        fun fromQuizResultResponseData(
            result: QuizResultResponseData,
            answerGiven: Int,
            isSynced: Boolean = true
        ): QuizResultEntity {
            return QuizResultEntity(
                quizId = result.quizId,
                isCorrect = result.isCorrect,
                correctAnswerText = result.correctAnswerText,
                userStreak = result.userStreak,
                answerGiven = answerGiven,
                isSynced = isSynced
            )
        }
    }
}