const Quiz = require('../models/Quiz');
const UserQuiz = require('../models/UserQuiz');
const User = require('../models/User'); 

exports.getQuizQuestion = async (req, res) => {
  try {
    const question = await Quiz.getRandomQuestion();
    if (!question) {
      return res.status(404).json({ success: false, message: 'No quiz questions available at the moment.' });
    }
    res.json({ success: true, data: question });
  } catch (error) {
    res.status(500).json({ success: false, message: 'Failed to fetch quiz question.', error: error.message });
  }
};

exports.submitQuizAnswer = async (req, res) => {
  try {
    const { quiz_id, answer_given } = req.body; 
    const user_id = req.user.uid;

    if (!quiz_id || answer_given === undefined) {
      return res.status(400).json({ success: false, message: 'Quiz ID and answer are required.' });
    }

    const quiz = await Quiz.getById(quiz_id);
    if (!quiz) {
      return res.status(404).json({ success: false, message: 'Quiz not found.' });
    }

    const is_correct = parseInt(answer_given) === parseInt(quiz.correct_answer);
    let correctAnswerText = '';
    if (quiz.correct_answer === 1) correctAnswerText = quiz.option1;
    else if (quiz.correct_answer === 2) correctAnswerText = quiz.option2;
    else if (quiz.correct_answer === 3) correctAnswerText = quiz.option3;
    else if (quiz.correct_answer === 4) correctAnswerText = quiz.option4;

    await UserQuiz.createAttempt({
      user_id,
      quiz_id,
      answer_given: answer_given.toString(),
      is_correct
    });

    const streakInfo = await User.updateStreak(user_id);

    res.json({
      success: true,
      data: {
        quiz_id: quiz.quiz_id,
        is_correct: is_correct,
        correct_answer_text: correctAnswerText,
        user_streak: streakInfo.current_streak
      }
    });

  } catch (error) {
    console.error("Error submitting quiz answer:", error)
    res.status(500).json({ success: false, message: 'Failed to submit quiz answer.', error: error.message });
  }
};