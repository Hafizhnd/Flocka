  const UserInterest = require('../models/UserInterest');
  const Interest = require('../models/Interest');

  // Add an interest for current user
  exports.addUserInterest = async (req, res) => {
    try {
      const { interestId } = req.params;

      // Check if interest exists
      const interest = await Interest.getById(interestId);
      if (!interest) {
        return res.status(404).json({
          success: false,
          message: 'Interest not found'
        });
      }

      // Check if user already has this interest
      const hasInterest = await UserInterest.hasInterest(req.user.uid, interestId);
      if (hasInterest) {
        return res.status(400).json({
          success: false,
          message: 'You already have this interest'
        });
      }

      await UserInterest.add(req.user.uid, interestId);

      res.json({
        success: true,
        message: 'Interest added successfully'
      });
    } catch (error) {
      console.error('Error adding user interest:', error);
      res.status(500).json({
        success: false,
        message: 'An error occurred while adding the interest',
        error: error.message
      });
    }
  };

  // Remove an interest from current user
  exports.removeUserInterest = async (req, res) => {
    try {
      const { interestId } = req.params;

      // Check if user has this interest
      const hasInterest = await UserInterest.hasInterest(req.user.uid, interestId);
      if (!hasInterest) {
        return res.status(404).json({
          success: false,
          message: 'Interest not found in your list'
        });
      }

      await UserInterest.remove(req.user.uid, interestId);

      res.json({
        success: true,
        message: 'Interest removed successfully'
      });
    } catch (error) {
      console.error('Error removing user interest:', error);
      res.status(500).json({
        success: false,
        message: 'An error occurred while removing the interest',
        error: error.message
      });
    }
  };

  // Get current user's interests
  exports.getUserInterests = async (req, res) => {
    try {
      const interests = await UserInterest.getUserInterests(req.user.uid);

      res.json({
        success: true,
        data: interests
      });
    } catch (error) {
      console.error('Error getting user interests:', error);
      res.status(500).json({
        success: false,
        message: 'An error occurred while fetching interests',
        error: error.message
      });
    }
  };

  // Get users who share an interest
  exports.getInterestUsers = async (req, res) => {
    try {
      const { interestId } = req.params;
      const { limit = 10 } = req.query;

      // Validate limit
      const parsedLimit = parseInt(limit);
      if (isNaN(parsedLimit) || parsedLimit < 1) {
        return res.status(400).json({
          success: false,
          message: 'Limit must be a positive number'
        });
      }

      // Check if interest exists
      const interest = await Interest.getById(interestId);
      if (!interest) {
        return res.status(404).json({
          success: false,
          message: 'Interest not found'
        });
      }

      const users = await UserInterest.getInterestUsers(interestId, parsedLimit);

      res.json({
        success: true,
        data: users
      });
    } catch (error) {
      console.error('Error getting interest users:', error);
      res.status(500).json({
        success: false,
        message: 'An error occurred while fetching users',
        error: error.message
      });
    }
  };

  // Get suggested interests based on user's current interests
  exports.getSuggestedInterests = async (req, res) => {
    try {
      const { limit = 5 } = req.query;

      // Validate limit
      const parsedLimit = parseInt(limit);
      if (isNaN(parsedLimit) || parsedLimit < 1) {
        return res.status(400).json({
          success: false,
          message: 'Limit must be a positive number'
        });
      }

      const suggestions = await UserInterest.getSuggestions(req.user.uid, parsedLimit);

      res.json({
        success: true,
        data: suggestions
      });
    } catch (error) {
      console.error('Error getting suggested interests:', error);
      res.status(500).json({
        success: false,
        message: 'An error occurred while fetching suggestions',
        error: error.message
      });
    }
  };

  // Get common interests between current user and another user
  exports.getCommonInterests = async (req, res) => {
    try {
      const { userId } = req.params;

      const commonInterests = await UserInterest.getCommonInterests(req.user.uid, userId);

      res.json({
        success: true,
        data: commonInterests
      });
    } catch (error) {
      console.error('Error getting common interests:', error);
      res.status(500).json({
        success: false,
        message: 'An error occurred while fetching common interests',
        error: error.message
      });
    }

    exports.bulkAddUserInterests = async (req, res) => {
      try {
        const userId = req.user.uid; // From 'protect' middleware
        const { interestIds } = req.body; // Expect an array of interest IDs from the client
    
        if (!Array.isArray(interestIds)) {
          return res.status(400).json({
            success: false,
            message: 'Please provide an array of interest IDs.'
          });
        }
    
        await UserInterest.bulkAdd(userId, interestIds);
    
        res.status(201).json({
          success: true,
          message: 'Interests added successfully.'
        });
      } catch (error) {
        console.error('Error bulk adding interests:', error);
        res.status(500).json({
          success: false,
          message: 'An error occurred while adding interests.'
        });
      }
    };
  }; 