const express = require('express');
const { 
  addUserInterest,
  removeUserInterest,
  getUserInterests,
  getInterestUsers,
  getSuggestedInterests,
  getCommonInterests,
  bulkAddUserInterests
} = require('../controllers/userInterestController');
const { protect } = require('../controllers/authController');

const router = express.Router();

// All routes require authentication
router.use(protect);

// Add an interest for current user
router.post('/:interestId', addUserInterest);

// Remove an interest from current user
router.delete('/:interestId', removeUserInterest);

// Get current user's interests
router.get('/', getUserInterests);

// Get users who share an interest
router.get('/:interestId/users', getInterestUsers);

// Get suggested interests based on user's current interests
router.get('/suggestions', getSuggestedInterests);

// Get common interests between current user and another user
router.get('/common/:userId', getCommonInterests);

exports.bulkAddUserInterests = async (req, res) => {
  try {
    const userId = req.user.uid; // From the 'protect' middleware
    const { interestIds } = req.body; // We expect an array of IDs from the Android app

    if (!Array.isArray(interestIds)) {
      return res.status(400).json({
        success: false,
        message: 'Please provide an array of interest IDs.'
      });
    }

    // Call the model function to save the data
    await UserInterest.bulkAdd(userId, interestIds);

    res.status(201).json({
      success: true,
      message: 'Interests added successfully.'
    });
  } catch (error) {
    console.error('Error bulk adding user interests:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while adding interests.',
      error: error.message
    });
  }
};

module.exports = router; 