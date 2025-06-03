const db = require('../config/db');

class Quiz {
  static async create({ question, option1, option2, option3, option4, correct_answer, explanation, difficulty }) {
    try {
      const quiz_id = `quiz_${Date.now()}_${Math.floor(Math.random() * 1000)}`;
      
      const [result] = await db.query(
        'INSERT INTO quizzes (quiz_id, question, option1, option2, option3, option4, correct_answer, explanation, difficulty) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)',
        [quiz_id, question, option1, option2, option3, option4, correct_answer, explanation, difficulty]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to create quiz');
      }

      return this.getById(quiz_id);
    } catch (error) {
      console.error('Error in create quiz:', error);
      throw error;
    }
  }

  static async getAll() {
    try {
      const [quizzes] = await db.query(
        'SELECT * FROM quizzes ORDER BY created_at DESC'
      );
      return quizzes;
    } catch (error) {
      console.error('Error in getAll:', error);
      throw error;
    }
  }

  static async getById(quizId) {
    try {
      const [quizzes] = await db.query(
        'SELECT * FROM quizzes WHERE quiz_id = ?',
        [quizId]
      );
      return quizzes[0] || null;
    } catch (error) {
      console.error('Error in getById:', error);
      throw error;
    }
  }

  static async getRandom() {
    try {
      const [quizzes] = await db.query(
        'SELECT * FROM quizzes ORDER BY RAND() LIMIT 1'
      );
      return quizzes[0] || null;
    } catch (error) {
      console.error('Error in getRandom:', error);
      throw error;
    }
  }

  static async getByDifficulty(difficulty) {
    try {
      const [quizzes] = await db.query(
        'SELECT * FROM quizzes WHERE difficulty = ? ORDER BY created_at DESC',
        [difficulty]
      );
      return quizzes;
    } catch (error) {
      console.error('Error in getByDifficulty:', error);
      throw error;
    }
  }

  static async getStatistics() {
    try {
      // Get total number of quizzes
      const [totalResult] = await db.query('SELECT COUNT(*) as total FROM quizzes');
      const total = totalResult[0].total;

      // Get completion statistics
      const [completionStats] = await db.query(`
        SELECT 
          COUNT(DISTINCT user_id) as total_participants,
          COUNT(CASE WHEN is_correct = 1 THEN 1 END) as correct_answers,
          COUNT(*) as total_attempts,
          ROUND(AVG(CASE WHEN is_correct = 1 THEN 100 ELSE 0 END), 2) as average_score
        FROM user_quizzes
      `);

      // Get difficulty distribution
      const [difficultyStats] = await db.query(`
        SELECT 
          difficulty,
          COUNT(*) as count,
          ROUND((COUNT(*) / ${total}) * 100, 2) as percentage
        FROM quizzes
        WHERE difficulty IS NOT NULL
        GROUP BY difficulty
      `);

      return {
        total_quizzes: total,
        completion_stats: completionStats[0],
        difficulty_distribution: difficultyStats
      };
    } catch (error) {
      console.error('Error in getStatistics:', error);
      throw error;
    }
  }

  static async update(quizId, { question, option1, option2, option3, option4, correct_answer, explanation, difficulty }) {
    try {
      const updates = [];
      const values = [];

      if (question) {
        updates.push('question = ?');
        values.push(question);
      }
      if (option1) {
        updates.push('option1 = ?');
        values.push(option1);
      }
      if (option2) {
        updates.push('option2 = ?');
        values.push(option2);
      }
      if (option3 !== undefined) {
        updates.push('option3 = ?');
        values.push(option3);
      }
      if (option4 !== undefined) {
        updates.push('option4 = ?');
        values.push(option4);
      }
      if (correct_answer) {
        updates.push('correct_answer = ?');
        values.push(correct_answer);
      }
      if (explanation !== undefined) {
        updates.push('explanation = ?');
        values.push(explanation);
      }
      if (difficulty) {
        updates.push('difficulty = ?');
        values.push(difficulty);
      }

      if (updates.length === 0) return this.getById(quizId);

      values.push(quizId);
      const [result] = await db.query(
        `UPDATE quizzes SET ${updates.join(', ')} WHERE quiz_id = ?`,
        values
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to update quiz');
      }

      return this.getById(quizId);
    } catch (error) {
      console.error('Error in update:', error);
      throw error;
    }
  }

  static async delete(quizId) {
    try {
      // First delete related user_quizzes records
      await db.query(
        'DELETE FROM user_quizzes WHERE quiz_id = ?',
        [quizId]
      );

      // Then delete the quiz
      const [result] = await db.query(
        'DELETE FROM quizzes WHERE quiz_id = ?',
        [quizId]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to delete quiz');
      }

      return true;
    } catch (error) {
      console.error('Error in delete:', error);
      throw error;
    }
  }

  static async submitQuizAnswer(userId, quizId, answerGiven) {
    try {
      const quiz = await this.getById(quizId);
      if (!quiz) {
        throw new Error('Quiz not found');
      }

      const isCorrect = quiz.correct_answer === answerGiven;
      const userQuizId = `user_quiz_${Date.now()}_${Math.floor(Math.random() * 1000)}`;

      await db.query(
        `INSERT INTO user_quizzes (user_quiz_id, user_id, quiz_id, answer_given, is_correct) 
         VALUES (?, ?, ?, ?, ?)`,
        [userQuizId, userId, quizId, answerGiven, isCorrect]
      );

      return {
        isCorrect,
        explanation: quiz.explanation,
        correctAnswer: quiz.correct_answer
      };
    } catch (error) {
      console.error('Error in submitQuizAnswer:', error.message);
      throw error;
    }
  }

  static async getUserQuizHistory(userId) {
    try {
      const [rows] = await db.query(
        `SELECT uq.*, q.question, q.correct_answer, q.explanation 
         FROM user_quizzes uq 
         JOIN quizzes q ON uq.quiz_id = q.quiz_id 
         WHERE uq.user_id = ? 
         ORDER BY uq.completed_at DESC`,
        [userId]
      );
      return rows;
    } catch (error) {
      console.error('Error in getUserQuizHistory:', error.message);
      throw error;
    }
  }

  static async getUserQuizStats(userId) {
    try {
      const [rows] = await db.query(
        `SELECT 
           COUNT(*) as total_attempts,
           SUM(is_correct) as correct_answers,
           (SUM(is_correct) / COUNT(*) * 100) as accuracy_percentage
         FROM user_quizzes 
         WHERE user_id = ?`,
        [userId]
      );
      return rows[0];
    } catch (error) {
      console.error('Error in getUserQuizStats:', error.message);
      throw error;
    }
  }
}

module.exports = Quiz; 