package com.example.flocka.data.local.dao

import androidx.room.*
import com.example.flocka.data.local.entity.QuizEntity
import com.example.flocka.data.local.entity.QuizResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizDao {
    @Query("SELECT * FROM quiz_questions WHERE isUsed = 0 ORDER BY fetchedAt DESC LIMIT 1")
    suspend fun getUnusedQuizQuestion(): QuizEntity?

    @Query("SELECT * FROM quiz_questions WHERE quizId = :quizId")
    suspend fun getQuizById(quizId: String): QuizEntity?

    @Query("SELECT * FROM quiz_questions ORDER BY fetchedAt DESC")
    fun getAllQuizQuestions(): Flow<List<QuizEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizQuestion(quiz: QuizEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizQuestions(quizzes: List<QuizEntity>)

    @Query("UPDATE quiz_questions SET isUsed = 1 WHERE quizId = :quizId")
    suspend fun markQuizAsUsed(quizId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuizResult(result: QuizResultEntity)

    @Query("SELECT * FROM quiz_results WHERE quizId = :quizId")
    suspend fun getQuizResult(quizId: String): QuizResultEntity?

    @Query("SELECT * FROM quiz_results ORDER BY answeredAt DESC")
    fun getAllQuizResults(): Flow<List<QuizResultEntity>>

    @Query("DELETE FROM quiz_questions WHERE fetchedAt < :cutoffTime AND isUsed = 1")
    suspend fun deleteOldQuizQuestions(cutoffTime: Long)

    @Query("DELETE FROM quiz_results WHERE answeredAt < :cutoffTime")
    suspend fun deleteOldQuizResults(cutoffTime: Long)

    @Query("DELETE FROM quiz_questions")
    suspend fun deleteAllQuizQuestions()

    @Query("DELETE FROM quiz_results")
    suspend fun deleteAllQuizResults()
}