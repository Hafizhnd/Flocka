const Interest = require('../models/Interest');

// Create a new interest
exports.createInterest = async (req, res) => {
  try {
    const { name, description, category } = req.body;
    
    if (!name) {
      return res.status(400).json({
        success: false,
        message: 'Please provide interest name'
      });
    }

    const interest = await Interest.create({
      name,
      description,
      category
    });

    res.status(201).json({
      success: true,
      message: 'Interest created successfully',
      data: interest
    });
  } catch (error) {
    console.error('Error creating interest:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while creating the interest',
      error: error.message
    });
  }
};

// Get all interests
exports.getInterests = async (req, res) => {
  try {
    const { category } = req.query;
    const interests = await Interest.getAll(category);

    res.json({
      success: true,
      data: interests
    });
  } catch (error) {
    console.error('Error getting interests:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching interests',
      error: error.message
    });
  }
};

// Get popular interests
exports.getPopularInterests = async (req, res) => {
  try {
    const { limit = 10 } = req.query;
    
    // Validate limit
    const parsedLimit = parseInt(limit);
    if (isNaN(parsedLimit) || parsedLimit < 1) {
      return res.status(400).json({
        success: false,
        message: 'Limit must be a positive number'
      });
    }

    const interests = await Interest.getPopular(parsedLimit);

    res.json({
      success: true,
      data: interests
    });
  } catch (error) {
    console.error('Error getting popular interests:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching popular interests',
      error: error.message
    });
  }
};

// Search interests
exports.searchInterests = async (req, res) => {
  try {
    const { query, category } = req.query;
    
    if (!query) {
      return res.status(400).json({
        success: false,
        message: 'Please provide a search query'
      });
    }

    const interests = await Interest.search(query, category);

    res.json({
      success: true,
      data: interests
    });
  } catch (error) {
    console.error('Error searching interests:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while searching interests',
      error: error.message
    });
  }
};

// Get a specific interest
exports.getInterestById = async (req, res) => {
  try {
    const { interestId } = req.params;
    const interest = await Interest.getById(interestId);

    if (!interest) {
      return res.status(404).json({
        success: false,
        message: 'Interest not found'
      });
    }

    res.json({
      success: true,
      data: interest
    });
  } catch (error) {
    console.error('Error getting interest:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching the interest',
      error: error.message
    });
  }
};

// Update an interest
exports.updateInterest = async (req, res) => {
  try {
    const { interestId } = req.params;
    const { name, description, category } = req.body;

    // Check if interest exists
    const interest = await Interest.getById(interestId);
    if (!interest) {
      return res.status(404).json({
        success: false,
        message: 'Interest not found'
      });
    }

    const updatedInterest = await Interest.update(interestId, {
      name,
      description,
      category
    });

    res.json({
      success: true,
      message: 'Interest updated successfully',
      data: updatedInterest
    });
  } catch (error) {
    console.error('Error updating interest:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while updating the interest',
      error: error.message
    });
  }
};

// Delete an interest
exports.deleteInterest = async (req, res) => {
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

    await Interest.delete(interestId);

    res.json({
      success: true,
      message: 'Interest deleted successfully'
    });
  } catch (error) {
    console.error('Error deleting interest:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while deleting the interest',
      error: error.message
    });
  }
}; 