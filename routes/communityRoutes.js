const express = require('express');
const { 
  createCommunity,
  getCommunities,
  getCommunityById,
  updateCommunity,
  deleteCommunity,
  joinCommunity,
  leaveCommunity,
  getMyCommunities
} = require('../controllers/communityController');
const { protect } = require('../controllers/authController');

const router = express.Router();

// All routes require authentication
router.use(protect);

// Create a new community
router.post('/', createCommunity);

// Get all communities
router.get('/', getCommunities);

// Get my communities (communities I'm a member of)
router.get('/my-communities', getMyCommunities);

// Get a specific community
router.get('/:communityId', getCommunityById);

// Update a community (only by creator)
router.put('/:communityId', updateCommunity);

// Delete a community (only by creator)
router.delete('/:communityId', deleteCommunity);

// Join a community
router.post('/:communityId/join', joinCommunity);

// Leave a community
router.post('/:communityId/leave', leaveCommunity);

module.exports = router; 