const express = require('express');
const { 
  createInterest,
  getInterests,
  getInterestById,
  updateInterest,
  deleteInterest,
  getPopularInterests,
  searchInterests
} = require('../controllers/interestController');
const { protect } = require('../controllers/authController');

const router = express.Router();

// All routes require authentication
router.use(protect);

// Create a new interest
router.post('/', createInterest);

// Get all interests
router.get('/', getInterests);

// Get popular interests
router.get('/popular', getPopularInterests);

// Search interests
router.get('/search', searchInterests);

// Get a specific interest
router.get('/:interestId', getInterestById);

// Update an interest
router.put('/:interestId', updateInterest);

// Delete an interest
router.delete('/:interestId', deleteInterest);

module.exports = router; 