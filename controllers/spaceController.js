
const Space = require('../models/Space');

exports.getSpaces = async (req, res) => {
  try {
    const spaces = await Space.getAll();
    res.json({
      success: true,
      data: spaces
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching spaces',
      error: error.message
    });
  }
};

// Get a specific space by ID
exports.getSpaceById = async (req, res) => {
  try {
    const { spaceId } = req.params;
    const space = await Space.getById(spaceId);

    if (!space) {
      return res.status(404).json({ 
        success: false,
        message: 'Space not found'
      });
    }
    res.json({
      success: true,
      data: space
    });
  } catch (error) {
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching the space',
      error: error.message
    });
  }
};