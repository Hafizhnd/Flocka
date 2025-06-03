const db = require('../config/db');

class Streak {
  static async createStreak(userId) {
    try {
      if (!userId) {
        throw new Error('User ID is required');
      }

      const streakId = `streak_${Date.now()}_${Math.floor(Math.random() * 1000)}`;
      const today = new Date().toISOString().split('T')[0];

      await db.query(
        `INSERT INTO streaks (streak_id, user_id, current_streak, longest_streak, last_updated) 
         VALUES (?, ?, ?, ?, ?)`,
        [streakId, userId, 1, 1, today]
      );

      return this.getStreakByUserId(userId);
    } catch (error) {
      console.error('Error in createStreak:', error.message);
      throw error;
    }
  }

  static async getStreakById(streakId) {
    try {
      const [rows] = await db.query('SELECT * FROM streaks WHERE streak_id = ?', [streakId]);
      return rows[0] || null;
    } catch (error) {
      console.error('Error in getStreakById:', error.message);
      throw error;
    }
  }

  static async getStreakByUserId(userId) {
    try {
      const [rows] = await db.query('SELECT * FROM streaks WHERE user_id = ?', [userId]);
      return rows[0] || null;
    } catch (error) {
      console.error('Error in getStreakByUserId:', error.message);
      throw error;
    }
  }

  static async updateStreak(userId) {
    try {
      const streak = await this.getStreakByUserId(userId);
      if (!streak) {
        return this.createStreak(userId);
      }

      const today = new Date().toISOString().split('T')[0];
      const lastUpdated = new Date(streak.last_updated).toISOString().split('T')[0];

      if (today === lastUpdated) {
        return streak;
      }

      const yesterday = new Date(Date.now() - 86400000).toISOString().split('T')[0];
      let currentStreak = streak.current_streak;
      let longestStreak = streak.longest_streak;

      if (lastUpdated === yesterday) {
        currentStreak += 1;
        if (currentStreak > longestStreak) {
          longestStreak = currentStreak;
        }
      } else {
        currentStreak = 1;
      }

      await db.query(
        `UPDATE streaks 
         SET current_streak = ?, longest_streak = ?, last_updated = ? 
         WHERE user_id = ?`,
        [currentStreak, longestStreak, today, userId]
      );

      return this.getStreakByUserId(userId);
    } catch (error) {
      console.error('Error in updateStreak:', error.message);
      throw error;
    }
  }

  static async resetStreak(userId) {
    try {
      const today = new Date().toISOString().split('T')[0];
      await db.query(
        `UPDATE streaks 
         SET current_streak = 1, last_updated = ? 
         WHERE user_id = ?`,
        [today, userId]
      );
      return this.getStreakByUserId(userId);
    } catch (error) {
      console.error('Error in resetStreak:', error.message);
      throw error;
    }
  }

  static async deleteStreak(userId) {
    try {
      await db.query('DELETE FROM streaks WHERE user_id = ?', [userId]);
      return true;
    } catch (error) {
      console.error('Error in deleteStreak:', error.message);
      throw error;
    }
  }

  static async getTopStreaks(limit = 10) {
    try {
      const [rows] = await db.query(
        `SELECT s.*, u.name, u.username 
         FROM streaks s 
         JOIN users u ON s.user_id = u.uid 
         ORDER BY s.current_streak DESC 
         LIMIT ?`,
        [limit]
      );
      return rows;
    } catch (error) {
      console.error('Error in getTopStreaks:', error.message);
      throw error;
    }
  }
}

module.exports = Streak; 