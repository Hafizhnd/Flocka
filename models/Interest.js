const db = require('../config/db');

class Interest {
  static async create({ name, description, category }) {
    try {
      const interest_id = `interest_${Date.now()}_${Math.floor(Math.random() * 1000)}`;
      
      const [result] = await db.query(
        'INSERT INTO interests (interest_id, name, description, category) VALUES (?, ?, ?, ?)',
        [interest_id, name, description, category]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to create interest');
      }

      return this.getById(interest_id);
    } catch (error) {
      console.error('Error in create interest:', error);
      throw error;
    }
  }

  static async getAll(category = null) {
    try {
      let query = 'SELECT * FROM interests';
      const values = [];

      if (category) {
        query += ' WHERE category = ?';
        values.push(category);
      }

      query += ' ORDER BY name ASC';

      const [interests] = await db.query(query, values);
      return interests;
    } catch (error) {
      console.error('Error in getAll:', error);
      throw error;
    }
  }

  static async getById(interestId) {
    try {
      const [interests] = await db.query(
        'SELECT * FROM interests WHERE interest_id = ?',
        [interestId]
      );
      return interests[0] || null;
    } catch (error) {
      console.error('Error in getById:', error);
      throw error;
    }
  }

  static async getPopular(limit = 10) {
    try {
      const [interests] = await db.query(
        `SELECT 
           i.*,
           COUNT(ui.user_id) as follower_count
         FROM interests i
         LEFT JOIN user_interests ui ON i.interest_id = ui.interest_id
         GROUP BY i.interest_id
         ORDER BY follower_count DESC
         LIMIT ?`,
        [limit]
      );
      return interests;
    } catch (error) {
      console.error('Error in getPopular:', error);
      throw error;
    }
  }

  static async search(query, category = null) {
    try {
      let sqlQuery = `
        SELECT * FROM interests 
        WHERE name LIKE ?
      `;
      const values = [`%${query}%`];

      if (category) {
        sqlQuery += ' AND category = ?';
        values.push(category);
      }

      sqlQuery += ' ORDER BY name ASC';

      const [interests] = await db.query(sqlQuery, values);
      return interests;
    } catch (error) {
      console.error('Error in search:', error);
      throw error;
    }
  }

  static async update(interestId, { name, description, category }) {
    try {
      const updates = [];
      const values = [];

      if (name) {
        updates.push('name = ?');
        values.push(name);
      }
      if (description !== undefined) {
        updates.push('description = ?');
        values.push(description);
      }
      if (category !== undefined) {
        updates.push('category = ?');
        values.push(category);
      }

      if (updates.length === 0) return this.getById(interestId);

      values.push(interestId);
      const [result] = await db.query(
        `UPDATE interests SET ${updates.join(', ')} WHERE interest_id = ?`,
        values
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to update interest');
      }

      return this.getById(interestId);
    } catch (error) {
      console.error('Error in update:', error);
      throw error;
    }
  }

  static async delete(interestId) {
    try {
      // First delete related user_interests records
      await db.query(
        'DELETE FROM user_interests WHERE interest_id = ?',
        [interestId]
      );

      // Then delete the interest
      const [result] = await db.query(
        'DELETE FROM interests WHERE interest_id = ?',
        [interestId]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to delete interest');
      }

      return true;
    } catch (error) {
      console.error('Error in delete:', error);
      throw error;
    }
  }

  static async getCategories() {
    try {
      const [categories] = await db.query(
        'SELECT DISTINCT category FROM interests WHERE category IS NOT NULL'
      );
      return categories.map(c => c.category);
    } catch (error) {
      console.error('Error in getCategories:', error);
      throw error;
    }
  }
}

module.exports = Interest; 