const Thread = require('../models/Thread');
const Community = require('../models/Community');

// Create a new thread
exports.createThread = async (req, res) => {
  try {
    const { community_id, title, content } = req.body;
    
    if (!community_id || !title || !content) {
      return res.status(400).json({
        success: false,
        message: 'Please provide community_id, title, and content'
      });
    }

    // Verify community exists and user is a member
    const community = await Community.getById(community_id);
    if (!community) {
      return res.status(404).json({
        success: false,
        message: 'Community not found'
      });
    }

    const thread = await Thread.create({
      community_id,
      user_id: req.user.uid,
      title,
      content
    });

    res.status(201).json({
      success: true,
      message: 'Thread created successfully',
      data: thread
    });
  } catch (error) {
    console.error('Error creating thread:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while creating the thread',
      error: error.message
    });
  }
};

// Get all threads
exports.getThreads = async (req, res) => {
  try {
    const threads = await Thread.getAll();

    res.json({
      success: true,
      data: threads
    });
  } catch (error) {
    console.error('Error getting threads:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching threads',
      error: error.message
    });
  }
};

// Get my threads
exports.getMyThreads = async (req, res) => {
  try {
    const threads = await Thread.getByUserId(req.user.uid);

    res.json({
      success: true,
      data: threads
    });
  } catch (error) {
    console.error('Error getting user threads:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching your threads',
      error: error.message
    });
  }
};

// Get threads by community
exports.getCommunityThreads = async (req, res) => {
  try {
    const { communityId } = req.params;
    const threads = await Thread.getByCommunityId(communityId);

    res.json({
      success: true,
      data: threads
    });
  } catch (error) {
    console.error('Error getting community threads:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching community threads',
      error: error.message
    });
  }
};

// Get a specific thread
exports.getThreadById = async (req, res) => {
  try {
    const { threadId } = req.params;
    const thread = await Thread.getById(threadId);

    if (!thread) {
      return res.status(404).json({
        success: false,
        message: 'Thread not found'
      });
    }

    res.json({
      success: true,
      data: thread
    });
  } catch (error) {
    console.error('Error getting thread:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching the thread',
      error: error.message
    });
  }
};

// Update a thread
exports.updateThread = async (req, res) => {
  try {
    const { threadId } = req.params;
    const { title, content } = req.body;

    if (!title && !content) {
      return res.status(400).json({
        success: false,
        message: 'Please provide title or content to update'
      });
    }

    // Check if thread exists and user is the creator
    const thread = await Thread.getById(threadId);
    if (!thread) {
      return res.status(404).json({
        success: false,
        message: 'Thread not found'
      });
    }

    if (thread.user_id !== req.user.uid) {
      return res.status(403).json({
        success: false,
        message: 'You are not authorized to update this thread'
      });
    }

    const updatedThread = await Thread.update(threadId, {
      title,
      content
    });

    res.json({
      success: true,
      message: 'Thread updated successfully',
      data: updatedThread
    });
  } catch (error) {
    console.error('Error updating thread:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while updating the thread',
      error: error.message
    });
  }
};

// Delete a thread
exports.deleteThread = async (req, res) => {
  try {
    const { threadId } = req.params;

    // Check if thread exists and user is the creator
    const thread = await Thread.getById(threadId);
    if (!thread) {
      return res.status(404).json({
        success: false,
        message: 'Thread not found'
      });
    }

    if (thread.user_id !== req.user.uid) {
      return res.status(403).json({
        success: false,
        message: 'You are not authorized to delete this thread'
      });
    }

    await Thread.delete(threadId);

    res.json({
      success: true,
      message: 'Thread deleted successfully'
    });
  } catch (error) {
    console.error('Error deleting thread:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while deleting the thread',
      error: error.message
    });
  }
}; 