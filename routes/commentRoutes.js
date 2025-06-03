const express = require('express');
const { 
  createComment, 
  getComments, 
  getCommentById, 
  updateComment, 
  deleteComment 
} = require('../controllers/commentController');
const { protect } = require('../controllers/authController');

const router = express.Router();

// All routes require authentication
router.use(protect);

// Create a new comment
router.post('/', createComment);

// Get all comments for a thread
router.get('/thread/:threadId', getComments);

// Get a specific comment
router.get('/:commentId', getCommentById);

// Update a comment
router.put('/:commentId', updateComment);

// Delete a comment
router.delete('/:commentId', deleteComment);

module.exports = router; 