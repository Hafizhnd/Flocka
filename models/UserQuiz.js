const db = require('../config/db');

class UserQuiz {
  static async createAttempt({ user_id, quiz_id, answer_given, is_correct }) {
    try {
      const user_quiz_id = `uq_${Date.now()}_${Math.floor(Math.random() * 1000)}`;
      const [result] = await db.query(
        'INSERT INTO user_quizzes (user_quiz_id, user_id, quiz_id, answer_given, is_correct, completed_at) VALUES (?, ?, ?, ?, ?, NOW())',
        [user_quiz_id, user_id, quiz_id, answer_given, is_correct]
      );
      if (!result || !result.affectedRows) {
        throw new Error('Failed to log quiz attempt');
      }
      return { user_quiz_id, user_id, quiz_id, answer_given, is_correct };
    } catch (error) {
      console.error('Error in UserQuiz.createAttempt:', error);
      throw error;
    }
  }

  static async hasCompletedQuizToday(userId, quizId) {
    try {
      const today = new Date().toISOString().slice(0, 10);
      const [rows] = await db.query(
        'SELECT COUNT(*) as count FROM user_quizzes WHERE user_id = ? AND quiz_id = ? AND DATE(completed_at) = ?',
        [userId, quizId, today]
      );
      return rows[0].count > 0;
    } catch (error) {
      console.error('Error in UserQuiz.hasCompletedQuizToday:', error);
      throw error;
    }
  }
}

module.exports = UserQuiz;