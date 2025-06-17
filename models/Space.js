// models/Space.js
const db = require("../config/db");

class Space {
  static async getAll() {
    try {
      const [spaces] = await db.query("SELECT * FROM spaces ORDER BY name ASC");
      return spaces;
    } catch (error) {
      console.error("Error in Space.getAll:", error);
      throw error;
    }
  }

  static async getById(spaceId) {
    try {
      const [spaces] = await db.query(
        "SELECT * FROM spaces WHERE space_id = ?",
        [spaceId]
      );
      return spaces[0] || null;
    } catch (error) {
      console.error("Error in Space.getById:", error);
      throw error;
    }
  }

  static async create(spaceData) {
    try {
      const {
        name,
        location,
        description,
        cost_per_hour,
        opening_time,
        closing_time,
        image,
      } = spaceData;

      const timestamp = Date.now();
      const randomNum = Math.floor(Math.random() * 1000);
      const spaceId = `space_${timestamp}_${randomNum}`;

      const query = `
        INSERT INTO spaces (
          space_id, 
          name, 
          location, 
          description, 
          cost_per_hour, 
          opening_time, 
          closing_time, 
          image,
          created_at
        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, NOW())
      `;

      const values = [
        spaceId,
        name,
        location,
        description || null,
        cost_per_hour || null,
        opening_time || null,
        closing_time || null,
        image || null,
      ];

      const [result] = await db.query(query, values);

      return await this.getById(spaceId);
    } catch (error) {
      console.error("Error in Space.create:", error);
      throw error;
    }
  }

  static async delete(spaceId) {
    try {
      const space = await this.getById(spaceId);

      if (!space) {
        return null;
      }

      const [result] = await db.query("DELETE FROM spaces WHERE space_id = ?", [
        spaceId,
      ]);

      return space;
    } catch (error) {
      console.error("Error in Space.delete:", error);
      throw error;
    }
  }
}

module.exports = Space;
