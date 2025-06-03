const express = require('express');
const { getQuizQuestion, submitQuizAnswer } = require('../controllers/quizController');
const { protect } = require('../controllers/authController');

const router = express.Router();

router.use(protect);

router.get('/question', getQuizQuestion);

router.post('/submit', submitQuizAnswer);

module.exports = router;