const express = require('express');
const { 
  submitQuizAnswer,
  getUserQuizHistory,
  getUserQuizStats,
  getLeaderboard,
  getQuizProgress,
  resetUserQuizHistory
} = require('../controllers/userQuizController');
const { protect } = require('../controllers/authController');

const router = express.Router();

// All routes require authentication
router.use(protect);

// Submit an answer for a quiz
router.post('/:quizId/submit', submitQuizAnswer);

// Get user's quiz history
router.get('/history', getUserQuizHistory);

// Get user's quiz statistics
router.get('/stats', getUserQuizStats);

// Get quiz leaderboard
router.get('/leaderboard', getLeaderboard);

// Get user's progress in quizzes
router.get('/progress', getQuizProgress);

// Reset user's quiz history (for testing/development)
router.delete('/reset', resetUserQuizHistory);

module.exports = router; 