const Community = require('../models/Community');

// Create a new community
exports.createCommunity = async (req, res) => {
  try {
    const { name, description, image } = req.body;
    
    if (!name) {
      return res.status(400).json({
        success: false,
        message: 'Please provide a name for the community'
      });
    }

    const community = await Community.create({
      name,
      description,
      image,
      created_by: req.user.uid
    });

    res.status(201).json({
      success: true,
      message: 'Community created successfully',
      data: community
    });
  } catch (error) {
    console.error('Error creating community:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while creating the community',
      error: error.message
    });
  }
};

// Get all communities
exports.getCommunities = async (req, res) => {
  try {
    const communities = await Community.getAll();

    res.json({
      success: true,
      data: communities
    });
  } catch (error) {
    console.error('Error getting communities:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching communities',
      error: error.message
    });
  }
};

// Get my communities
exports.getMyCommunities = async (req, res) => {
  try {
    const communities = await Community.getByMemberId(req.user.uid);

    res.json({
      success: true,
      data: communities
    });
  } catch (error) {
    console.error('Error getting user communities:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching your communities',
      error: error.message
    });
  }
};

// Get a specific community
exports.getCommunityById = async (req, res) => {
  try {
    const { communityId } = req.params;
    const community = await Community.getById(communityId);

    if (!community) {
      return res.status(404).json({
        success: false,
        message: 'Community not found'
      });
    }

    res.json({
      success: true,
      data: community
    });
  } catch (error) {
    console.error('Error getting community:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching the community',
      error: error.message
    });
  }
};

// Update a community
exports.updateCommunity = async (req, res) => {
  try {
    const { communityId } = req.params;
    const { name, description, image } = req.body;

    // Check if community exists and user is the creator
    const community = await Community.getById(communityId);
    if (!community) {
      return res.status(404).json({
        success: false,
        message: 'Community not found'
      });
    }

    if (community.created_by !== req.user.uid) {
      return res.status(403).json({
        success: false,
        message: 'You are not authorized to update this community'
      });
    }

    const updatedCommunity = await Community.update(communityId, {
      name,
      description,
      image
    });

    res.json({
      success: true,
      message: 'Community updated successfully',
      data: updatedCommunity
    });
  } catch (error) {
    console.error('Error updating community:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while updating the community',
      error: error.message
    });
  }
};

// Delete a community
exports.deleteCommunity = async (req, res) => {
  try {
    const { communityId } = req.params;

    // Check if community exists and user is the creator
    const community = await Community.getById(communityId);
    if (!community) {
      return res.status(404).json({
        success: false,
        message: 'Community not found'
      });
    }

    if (community.created_by !== req.user.uid) {
      return res.status(403).json({
        success: false,
        message: 'You are not authorized to delete this community'
      });
    }

    await Community.delete(communityId);

    res.json({
      success: true,
      message: 'Community deleted successfully'
    });
  } catch (error) {
    console.error('Error deleting community:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while deleting the community',
      error: error.message
    });
  }
};

// Join a community
exports.joinCommunity = async (req, res) => {
  try {
    const { communityId } = req.params;

    // Check if community exists
    const community = await Community.getById(communityId);
    if (!community) {
      return res.status(404).json({
        success: false,
        message: 'Community not found'
      });
    }

    await Community.addMember(communityId, req.user.uid);

    res.json({
      success: true,
      message: 'Successfully joined the community'
    });
  } catch (error) {
    if (error.code === 'ER_DUP_ENTRY') {
      return res.status(400).json({
        success: false,
        message: 'You are already a member of this community'
      });
    }

    console.error('Error joining community:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while joining the community',
      error: error.message
    });
  }
};

// Leave a community
exports.leaveCommunity = async (req, res) => {
  try {
    const { communityId } = req.params;

    // Check if community exists
    const community = await Community.getById(communityId);
    if (!community) {
      return res.status(404).json({
        success: false,
        message: 'Community not found'
      });
    }

    // Don't allow creator to leave their own community
    if (community.created_by === req.user.uid) {
      return res.status(400).json({
        success: false,
        message: 'Community creator cannot leave the community'
      });
    }

    await Community.removeMember(communityId, req.user.uid);

    res.json({
      success: true,
      message: 'Successfully left the community'
    });
  } catch (error) {
    console.error('Error leaving community:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while leaving the community',
      error: error.message
    });
  }
}; 