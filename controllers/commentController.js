const Comment = require('../models/Comment');

// Create a new comment
exports.createComment = async (req, res) => {
  try {
    const { thread_id, content } = req.body;
    
    if (!thread_id || !content) {
      return res.status(400).json({
        success: false,
        message: 'Please provide thread_id and content'
      });
    }

    const comment = await Comment.create({
      thread_id,
      user_id: req.user.uid,
      content
    });

    res.status(201).json({
      success: true,
      message: 'Comment created successfully',
      data: comment
    });
  } catch (error) {
    console.error('Error creating comment:', error);

    if (error.message === 'Thread does not exist') {
      return res.status(404).json({
        success: false,
        message: 'Thread not found',
        error: 'The specified thread does not exist'
      });
    }

    if (error.code === 'ER_NO_REFERENCED_ROW_2' || error.code === 'ER_NO_REFERENCED_ROW') {
      return res.status(404).json({
        success: false,
        message: 'Thread not found',
        error: 'The specified thread does not exist'
      });
    }

    res.status(500).json({
      success: false,
      message: 'An error occurred while creating the comment',
      error: error.message
    });
  }
};

// Get all comments for a thread
exports.getComments = async (req, res) => {
  try {
    const { threadId } = req.params;
    const comments = await Comment.getByThreadId(threadId);

    res.json({
      success: true,
      data: comments
    });
  } catch (error) {
    console.error('Error getting comments:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching comments',
      error: error.message
    });
  }
};

// Get a specific comment
exports.getCommentById = async (req, res) => {
  try {
    const { commentId } = req.params;
    const comment = await Comment.getById(commentId);

    if (!comment) {
      return res.status(404).json({
        success: false,
        message: 'Comment not found'
      });
    }

    res.json({
      success: true,
      data: comment
    });
  } catch (error) {
    console.error('Error getting comment:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching the comment',
      error: error.message
    });
  }
};

// Update a comment
exports.updateComment = async (req, res) => {
  try {
    const { commentId } = req.params;
    const { content } = req.body;

    if (!content) {
      return res.status(400).json({
        success: false,
        message: 'Please provide content'
      });
    }

    // Check if comment exists and belongs to user
    const existingComment = await Comment.getById(commentId);
    if (!existingComment) {
      return res.status(404).json({
        success: false,
        message: 'Comment not found'
      });
    }

    if (existingComment.user_id !== req.user.uid) {
      return res.status(403).json({
        success: false,
        message: 'You are not authorized to update this comment'
      });
    }

    const updatedComment = await Comment.update(commentId, { content });

    res.json({
      success: true,
      message: 'Comment updated successfully',
      data: updatedComment
    });
  } catch (error) {
    console.error('Error updating comment:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while updating the comment',
      error: error.message
    });
  }
};

// Delete a comment
exports.deleteComment = async (req, res) => {
  try {
    const { commentId } = req.params;

    // Check if comment exists and belongs to user
    const existingComment = await Comment.getById(commentId);
    if (!existingComment) {
      return res.status(404).json({
        success: false,
        message: 'Comment not found'
      });
    }

    if (existingComment.user_id !== req.user.uid) {
      return res.status(403).json({
        success: false,
        message: 'You are not authorized to delete this comment'
      });
    }

    await Comment.delete(commentId);

    res.json({
      success: true,
      message: 'Comment deleted successfully'
    });
  } catch (error) {
    console.error('Error deleting comment:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while deleting the comment',
      error: error.message
    });
  }
}; 