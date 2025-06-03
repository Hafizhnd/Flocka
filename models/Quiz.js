const db = require('../config/db');

class Quiz {
  static async getById(quizId) {
    try {
      const [quizzes] = await db.query('SELECT * FROM quizzes WHERE quiz_id = ?', [quizId]);
      return quizzes[0] || null;
    } catch (error) {
      console.error('Error in Quiz.getById:', error);
      throw error;
    }
  }

  static async getRandomQuestion() {
    try {
      const [quizzes] = await db.query(
        'SELECT quiz_id, question, option1, option2, option3, option4 FROM quizzes ORDER BY RAND() LIMIT 1'
      );
      return quizzes[0] || null;
    } catch (error) {
      console.error('Error in Quiz.getRandomQuestion:', error);
      throw error;
    }
  }
}

module.exports = Quiz;