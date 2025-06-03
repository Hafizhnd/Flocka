const express = require('express');
const { 
  createThread,
  getThreads,
  getThreadById,
  updateThread,
  deleteThread,
  getCommunityThreads,
  getMyThreads
} = require('../controllers/threadController');
const { protect } = require('../controllers/authController');

const router = express.Router();

// All routes require authentication
router.use(protect);

// Create a new thread
router.post('/', createThread);

// Get all threads
router.get('/', getThreads);

// Get my threads
router.get('/my-threads', getMyThreads);

// Get threads by community
router.get('/community/:communityId', getCommunityThreads);

// Get a specific thread
router.get('/:threadId', getThreadById);

// Update a thread (only by creator)
router.put('/:threadId', updateThread);

// Delete a thread (only by creator)
router.delete('/:threadId', deleteThread);

module.exports = router; 