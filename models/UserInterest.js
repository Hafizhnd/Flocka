const db = require('../config/db');

class UserInterest {
  static async add(userId, interestId) {
    try {
      const [result] = await db.query(
        'INSERT INTO user_interests (user_id, interest_id) VALUES (?, ?)',
        [userId, interestId]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to add user interest');
      }

      return true;
    } catch (error) {
      console.error('Error in add:', error);
      throw error;
    }
  }

  static async remove(userId, interestId) {
    try {
      const [result] = await db.query(
        'DELETE FROM user_interests WHERE user_id = ? AND interest_id = ?',
        [userId, interestId]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to remove user interest');
      }

      return true;
    } catch (error) {
      console.error('Error in remove:', error);
      throw error;
    }
  }

  static async hasInterest(userId, interestId) {
    try {
      const [rows] = await db.query(
        'SELECT COUNT(*) as count FROM user_interests WHERE user_id = ? AND interest_id = ?',
        [userId, interestId]
      );
      return rows[0].count > 0;
    } catch (error) {
      console.error('Error in hasInterest:', error);
      throw error;
    }
  }

  static async getUserInterests(userId) {
    try {
      const [interests] = await db.query(
        `SELECT i.* 
         FROM interests i
         JOIN user_interests ui ON i.interest_id = ui.interest_id
         WHERE ui.user_id = ?
         ORDER BY i.name ASC`,
        [userId]
      );
      return interests;
    } catch (error) {
      console.error('Error in getUserInterests:', error);
      throw error;
    }
  }

  static async getInterestUsers(interestId, limit = 10) {
    try {
      const [users] = await db.query(
        `SELECT 
           u.uid,
           u.username,
           u.name,
           u.profession,
           COUNT(ui2.interest_id) as common_interests
         FROM users u
         JOIN user_interests ui ON u.uid = ui.user_id
         LEFT JOIN user_interests ui2 ON u.uid = ui2.user_id
         WHERE ui.interest_id = ?
         GROUP BY u.uid, u.username, u.name, u.profession
         ORDER BY common_interests DESC
         LIMIT ?`,
        [interestId, limit]
      );
      return users;
    } catch (error) {
      console.error('Error in getInterestUsers:', error);
      throw error;
    }
  }

  static async getSuggestions(userId, limit = 5) {
    try {
      // Get suggestions based on interests of users who share the current user's interests
      const [suggestions] = await db.query(
        `SELECT DISTINCT
           i.*,
           COUNT(DISTINCT ui2.user_id) as shared_by
         FROM interests i
         JOIN user_interests ui ON i.interest_id = ui.interest_id
         JOIN user_interests ui2 ON ui.user_id = ui2.user_id
         WHERE ui2.interest_id NOT IN (
           SELECT interest_id 
           FROM user_interests 
           WHERE user_id = ?
         )
         AND ui.user_id IN (
           SELECT DISTINCT ui3.user_id
           FROM user_interests ui3
           WHERE ui3.interest_id IN (
             SELECT interest_id
             FROM user_interests
             WHERE user_id = ?
           )
         )
         GROUP BY i.interest_id
         ORDER BY shared_by DESC
         LIMIT ?`,
        [userId, userId, limit]
      );
      return suggestions;
    } catch (error) {
      console.error('Error in getSuggestions:', error);
      throw error;
    }
  }

  static async getCommonInterests(userId1, userId2) {
    try {
      const [interests] = await db.query(
        `SELECT i.*
         FROM interests i
         JOIN user_interests ui1 ON i.interest_id = ui1.interest_id
         JOIN user_interests ui2 ON i.interest_id = ui2.interest_id
         WHERE ui1.user_id = ? AND ui2.user_id = ?
         ORDER BY i.name ASC`,
        [userId1, userId2]
      );
      return interests;
    } catch (error) {
      console.error('Error in getCommonInterests:', error);
      throw error;
    }
  }

  static async getInterestStats(interestId) {
    try {
      const [stats] = await db.query(
        `SELECT 
           COUNT(DISTINCT ui.user_id) as follower_count,
           (
             SELECT COUNT(DISTINCT ui2.user_id)
             FROM user_interests ui2
             WHERE ui2.user_id IN (
               SELECT user_id
               FROM user_interests
               WHERE interest_id = ?
             )
           ) as network_size
         FROM user_interests ui
         WHERE ui.interest_id = ?`,
        [interestId, interestId]
      );
      return stats[0];
    } catch (error) {
      console.error('Error in getInterestStats:', error);
      throw error;
    }
  }

  static async bulkAdd(userId, interestIds) {
    if (!interestIds || interestIds.length === 0) {
      return true; // Nothing to add
    }
    // Create an array of [userId, interestId] pairs for the query
    const values = interestIds.map(interestId => [userId, interestId]);
    try {
      // Use a single INSERT query to add all pairs
      // The `IGNORE` keyword prevents errors if a user already has an interest
      await db.query(
        'INSERT IGNORE INTO user_interests (user_id, interest_id) VALUES ?',
        [values]
      );
      return true;
    } catch (error) {
      console.error('Error in bulkAdd:', error);
      throw error;
    }
  }
}

module.exports = UserInterest; 