// models/Space.js
const db = require('../config/db');

class Space {
  // Method to get all spaces
  static async getAll() {
    try {
      const [spaces] = await db.query('SELECT * FROM spaces ORDER BY name ASC');
      return spaces;
    } catch (error) {
      console.error('Error in Space.getAll:', error);
      throw error;
    }
  }

  // Method to get a single space by its ID
  static async getById(spaceId) {
    try {
      const [spaces] = await db.query('SELECT * FROM spaces WHERE space_id = ?', [spaceId]);
      return spaces[0] || null;
    } catch (error) {
      console.error('Error in Space.getById:', error);
      throw error;
    }
  }

}

module.exports = Space;