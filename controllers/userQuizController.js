const UserQuiz = require('../models/UserQuiz');
const Quiz = require('../models/Quiz');

// Submit an answer for a quiz
exports.submitQuizAnswer = async (req, res) => {
  try {
    const { quizId } = req.params;
    const { answer_given } = req.body;
    
    if (!answer_given) {
      return res.status(400).json({
        success: false,
        message: 'Please provide an answer'
      });
    }

    // Validate answer is a number between 1 and 4
    if (![1, 2, 3, 4].includes(answer_given)) {
      return res.status(400).json({
        success: false,
        message: 'Answer must be between 1 and 4'
      });
    }

    const result = await UserQuiz.submitAnswer(req.user.uid, quizId, answer_given);

    res.json({
      success: true,
      message: result.isCorrect ? 'Correct answer!' : 'Incorrect answer',
      data: result
    });
  } catch (error) {
    console.error('Error submitting quiz answer:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while submitting the answer',
      error: error.message
    });
  }
};

// Get user's quiz history
exports.getUserQuizHistory = async (req, res) => {
  try {
    const history = await UserQuiz.getHistory(req.user.uid);

    res.json({
      success: true,
      data: history
    });
  } catch (error) {
    console.error('Error getting quiz history:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching quiz history',
      error: error.message
    });
  }
};

// Get user's quiz statistics
exports.getUserQuizStats = async (req, res) => {
  try {
    const stats = await UserQuiz.getStats(req.user.uid);

    res.json({
      success: true,
      data: stats
    });
  } catch (error) {
    console.error('Error getting quiz stats:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching quiz statistics',
      error: error.message
    });
  }
};

// Get quiz leaderboard
exports.getLeaderboard = async (req, res) => {
  try {
    const { limit = 10, difficulty } = req.query;

    // Validate limit
    const parsedLimit = parseInt(limit);
    if (isNaN(parsedLimit) || parsedLimit < 1) {
      return res.status(400).json({
        success: false,
        message: 'Limit must be a positive number'
      });
    }

    // Validate difficulty if provided
    if (difficulty && !['easy', 'medium', 'hard'].includes(difficulty)) {
      return res.status(400).json({
        success: false,
        message: 'Difficulty must be either easy, medium, or hard'
      });
    }

    const leaderboard = await UserQuiz.getLeaderboard(parsedLimit, difficulty);

    res.json({
      success: true,
      data: leaderboard
    });
  } catch (error) {
    console.error('Error getting leaderboard:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching leaderboard',
      error: error.message
    });
  }
};

// Get user's progress in quizzes
exports.getQuizProgress = async (req, res) => {
  try {
    const { difficulty } = req.query;

    // Validate difficulty if provided
    if (difficulty && !['easy', 'medium', 'hard'].includes(difficulty)) {
      return res.status(400).json({
        success: false,
        message: 'Difficulty must be either easy, medium, or hard'
      });
    }

    const progress = await UserQuiz.getProgress(req.user.uid, difficulty);

    res.json({
      success: true,
      data: progress
    });
  } catch (error) {
    console.error('Error getting quiz progress:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching quiz progress',
      error: error.message
    });
  }
};

// Reset user's quiz history
exports.resetUserQuizHistory = async (req, res) => {
  try {
    await UserQuiz.resetHistory(req.user.uid);

    res.json({
      success: true,
      message: 'Quiz history reset successfully'
    });
  } catch (error) {
    console.error('Error resetting quiz history:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while resetting quiz history',
      error: error.message
    });
  }
}; 