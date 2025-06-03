const Quiz = require('../models/Quiz');

// Create a new quiz
exports.createQuiz = async (req, res) => {
  try {
    const { question, option1, option2, option3, option4, correct_answer, explanation, difficulty } = req.body;
    
    if (!question || !option1 || !option2 || !correct_answer) {
      return res.status(400).json({
        success: false,
        message: 'Please provide question, option1, option2, and correct_answer'
      });
    }

    // Validate correct_answer is a valid option number
    if (![1, 2, 3, 4].includes(correct_answer)) {
      return res.status(400).json({
        success: false,
        message: 'correct_answer must be between 1 and 4'
      });
    }

    // Validate that the correct_answer option exists
    if (correct_answer === 3 && !option3 || correct_answer === 4 && !option4) {
      return res.status(400).json({
        success: false,
        message: 'The correct answer option must exist'
      });
    }

    // Validate difficulty if provided
    if (difficulty && !['easy', 'medium', 'hard'].includes(difficulty)) {
      return res.status(400).json({
        success: false,
        message: 'difficulty must be either easy, medium, or hard'
      });
    }

    const quiz = await Quiz.create({
      question,
      option1,
      option2,
      option3,
      option4,
      correct_answer,
      explanation,
      difficulty
    });

    res.status(201).json({
      success: true,
      message: 'Quiz created successfully',
      data: quiz
    });
  } catch (error) {
    console.error('Error creating quiz:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while creating the quiz',
      error: error.message
    });
  }
};

// Get all quizzes
exports.getQuizzes = async (req, res) => {
  try {
    const quizzes = await Quiz.getAll();

    res.json({
      success: true,
      data: quizzes
    });
  } catch (error) {
    console.error('Error getting quizzes:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching quizzes',
      error: error.message
    });
  }
};

// Get a random quiz
exports.getRandomQuiz = async (req, res) => {
  try {
    const quiz = await Quiz.getRandom();

    if (!quiz) {
      return res.status(404).json({
        success: false,
        message: 'No quizzes available'
      });
    }

    res.json({
      success: true,
      data: quiz
    });
  } catch (error) {
    console.error('Error getting random quiz:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching a random quiz',
      error: error.message
    });
  }
};

// Get quizzes by difficulty
exports.getQuizzesByDifficulty = async (req, res) => {
  try {
    const { level } = req.params;

    // Validate difficulty level
    if (!['easy', 'medium', 'hard'].includes(level)) {
      return res.status(400).json({
        success: false,
        message: 'Difficulty level must be either easy, medium, or hard'
      });
    }

    const quizzes = await Quiz.getByDifficulty(level);

    res.json({
      success: true,
      data: quizzes
    });
  } catch (error) {
    console.error('Error getting quizzes by difficulty:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching quizzes by difficulty',
      error: error.message
    });
  }
};

// Get quiz statistics
exports.getQuizStatistics = async (req, res) => {
  try {
    const statistics = await Quiz.getStatistics();

    res.json({
      success: true,
      data: statistics
    });
  } catch (error) {
    console.error('Error getting quiz statistics:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching quiz statistics',
      error: error.message
    });
  }
};

// Get a specific quiz
exports.getQuizById = async (req, res) => {
  try {
    const { quizId } = req.params;
    const quiz = await Quiz.getById(quizId);

    if (!quiz) {
      return res.status(404).json({
        success: false,
        message: 'Quiz not found'
      });
    }

    res.json({
      success: true,
      data: quiz
    });
  } catch (error) {
    console.error('Error getting quiz:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching the quiz',
      error: error.message
    });
  }
};

// Update a quiz
exports.updateQuiz = async (req, res) => {
  try {
    const { quizId } = req.params;
    const { question, option1, option2, option3, option4, correct_answer, explanation, difficulty } = req.body;

    // Check if quiz exists
    const quiz = await Quiz.getById(quizId);
    if (!quiz) {
      return res.status(404).json({
        success: false,
        message: 'Quiz not found'
      });
    }

    // Validate correct_answer if provided
    if (correct_answer !== undefined) {
      if (![1, 2, 3, 4].includes(correct_answer)) {
        return res.status(400).json({
          success: false,
          message: 'correct_answer must be between 1 and 4'
        });
      }

      // Validate that the correct_answer option exists
      const options = [option1 || quiz.option1, option2 || quiz.option2, option3 || quiz.option3, option4 || quiz.option4];
      if (!options[correct_answer - 1]) {
        return res.status(400).json({
          success: false,
          message: 'The correct answer option must exist'
        });
      }
    }

    // Validate difficulty if provided
    if (difficulty && !['easy', 'medium', 'hard'].includes(difficulty)) {
      return res.status(400).json({
        success: false,
        message: 'difficulty must be either easy, medium, or hard'
      });
    }

    const updatedQuiz = await Quiz.update(quizId, {
      question,
      option1,
      option2,
      option3,
      option4,
      correct_answer,
      explanation,
      difficulty
    });

    res.json({
      success: true,
      message: 'Quiz updated successfully',
      data: updatedQuiz
    });
  } catch (error) {
    console.error('Error updating quiz:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while updating the quiz',
      error: error.message
    });
  }
};

// Delete a quiz
exports.deleteQuiz = async (req, res) => {
  try {
    const { quizId } = req.params;

    // Check if quiz exists
    const quiz = await Quiz.getById(quizId);
    if (!quiz) {
      return res.status(404).json({
        success: false,
        message: 'Quiz not found'
      });
    }

    await Quiz.delete(quizId);

    res.json({
      success: true,
      message: 'Quiz deleted successfully'
    });
  } catch (error) {
    console.error('Error deleting quiz:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while deleting the quiz',
      error: error.message
    });
  }
}; 