const express = require('express');
const { 
  createQuiz,
  getQuizzes,
  getQuizById,
  updateQuiz,
  deleteQuiz,
  getRandomQuiz,
  getQuizzesByDifficulty,
  getQuizStatistics
} = require('../controllers/quizController');
const { protect } = require('../controllers/authController');

const router = express.Router();

// All routes require authentication
router.use(protect);

// Create a new quiz
router.post('/', createQuiz);

// Get all quizzes
router.get('/', getQuizzes);

// Get a random quiz
router.get('/random', getRandomQuiz);

// Get quizzes by difficulty
router.get('/difficulty/:level', getQuizzesByDifficulty);

// Get quiz statistics (completion rate, average score)
router.get('/statistics', getQuizStatistics);

// Get a specific quiz
router.get('/:quizId', getQuizById);

// Update a quiz
router.put('/:quizId', updateQuiz);

// Delete a quiz
router.delete('/:quizId', deleteQuiz);

module.exports = router; 