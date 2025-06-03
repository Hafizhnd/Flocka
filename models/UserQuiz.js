const db = require('../config/db');
const Quiz = require('./Quiz');

class UserQuiz {
  static async submitAnswer(userId, quizId, answerGiven) {
    try {
      // Get the quiz to check the correct answer
      const quiz = await Quiz.getById(quizId);
      if (!quiz) {
        throw new Error('Quiz not found');
      }

      const isCorrect = quiz.correct_answer === answerGiven;
      const userQuizId = `user_quiz_${Date.now()}_${Math.floor(Math.random() * 1000)}`;

      const [result] = await db.query(
        `INSERT INTO user_quizzes (user_quiz_id, user_id, quiz_id, answer_given, is_correct) 
         VALUES (?, ?, ?, ?, ?)`,
        [userQuizId, userId, quizId, answerGiven, isCorrect]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to submit quiz answer');
      }

      return {
        isCorrect,
        explanation: quiz.explanation,
        correctAnswer: quiz.correct_answer,
        userAnswer: answerGiven
      };
    } catch (error) {
      console.error('Error in submitAnswer:', error);
      throw error;
    }
  }

  static async getHistory(userId) {
    try {
      const [history] = await db.query(
        `SELECT 
           uq.*,
           q.question,
           q.option1,
           q.option2,
           q.option3,
           q.option4,
           q.correct_answer,
           q.explanation,
           q.difficulty
         FROM user_quizzes uq
         JOIN quizzes q ON uq.quiz_id = q.quiz_id
         WHERE uq.user_id = ?
         ORDER BY uq.completed_at DESC`,
        [userId]
      );
      return history;
    } catch (error) {
      console.error('Error in getHistory:', error);
      throw error;
    }
  }

  static async getStats(userId) {
    try {
      const [overallStats] = await db.query(
        `SELECT 
           COUNT(*) as total_attempts,
           SUM(is_correct) as correct_answers,
           ROUND(AVG(is_correct) * 100, 2) as accuracy_percentage
         FROM user_quizzes
         WHERE user_id = ?`,
        [userId]
      );

      const [difficultyStats] = await db.query(
        `SELECT 
           q.difficulty,
           COUNT(*) as attempts,
           SUM(uq.is_correct) as correct_answers,
           ROUND(AVG(uq.is_correct) * 100, 2) as accuracy_percentage
         FROM user_quizzes uq
         JOIN quizzes q ON uq.quiz_id = q.quiz_id
         WHERE uq.user_id = ? AND q.difficulty IS NOT NULL
         GROUP BY q.difficulty`,
        [userId]
      );

      const [streakInfo] = await db.query(
        `SELECT 
           COUNT(*) as current_streak
         FROM (
           SELECT 
             DATE(completed_at) as quiz_date,
             MAX(is_correct) as daily_success
           FROM user_quizzes
           WHERE user_id = ?
           GROUP BY DATE(completed_at)
           ORDER BY quiz_date DESC
         ) daily_results
         WHERE daily_success = 1`,
        [userId]
      );

      return {
        overall: overallStats[0],
        by_difficulty: difficultyStats,
        current_streak: streakInfo[0].current_streak
      };
    } catch (error) {
      console.error('Error in getStats:', error);
      throw error;
    }
  }

  static async getLeaderboard(limit = 10, difficulty = null) {
    try {
      let query = `
        SELECT 
          u.username,
          COUNT(DISTINCT uq.quiz_id) as quizzes_taken,
          SUM(uq.is_correct) as correct_answers,
          ROUND(AVG(uq.is_correct) * 100, 2) as accuracy_percentage
        FROM user_quizzes uq
        JOIN users u ON uq.user_id = u.uid
      `;

      const values = [];
      if (difficulty) {
        query += ` JOIN quizzes q ON uq.quiz_id = q.quiz_id WHERE q.difficulty = ?`;
        values.push(difficulty);
      }

      query += `
        GROUP BY u.uid, u.username
        ORDER BY correct_answers DESC, accuracy_percentage DESC
        LIMIT ?
      `;
      values.push(limit);

      const [leaderboard] = await db.query(query, values);
      return leaderboard;
    } catch (error) {
      console.error('Error in getLeaderboard:', error);
      throw error;
    }
  }

  static async getProgress(userId, difficulty = null) {
    try {
      let query = `
        SELECT 
          COUNT(DISTINCT q.quiz_id) as total_quizzes,
          COUNT(DISTINCT CASE WHEN uq.is_correct = 1 THEN q.quiz_id END) as completed_quizzes,
          ROUND((COUNT(DISTINCT CASE WHEN uq.is_correct = 1 THEN q.quiz_id END) / 
                 COUNT(DISTINCT q.quiz_id)) * 100, 2) as completion_percentage
        FROM quizzes q
        LEFT JOIN user_quizzes uq ON q.quiz_id = uq.quiz_id AND uq.user_id = ?
      `;

      const values = [userId];
      if (difficulty) {
        query += ` WHERE q.difficulty = ?`;
        values.push(difficulty);
      }

      const [progress] = await db.query(query, values);

      // Get recent activity
      const [recentActivity] = await db.query(
        `SELECT 
           uq.*,
           q.question,
           q.difficulty
         FROM user_quizzes uq
         JOIN quizzes q ON uq.quiz_id = q.quiz_id
         WHERE uq.user_id = ?
         ${difficulty ? 'AND q.difficulty = ?' : ''}
         ORDER BY uq.completed_at DESC
         LIMIT 5`,
        difficulty ? [userId, difficulty] : [userId]
      );

      return {
        progress: progress[0],
        recent_activity: recentActivity
      };
    } catch (error) {
      console.error('Error in getProgress:', error);
      throw error;
    }
  }

  static async resetHistory(userId) {
    try {
      const [result] = await db.query(
        'DELETE FROM user_quizzes WHERE user_id = ?',
        [userId]
      );

      return result.affectedRows > 0;
    } catch (error) {
      console.error('Error in resetHistory:', error);
      throw error;
    }
  }
}

module.exports = UserQuiz; 