const db = require('../config/db');

class Thread {
  static async create({ community_id, user_id, title, content }) {
    try {
      const thread_id = `thread_${Date.now()}_${Math.floor(Math.random() * 1000)}`;
      
      const [result] = await db.query(
        'INSERT INTO threads (thread_id, community_id, user_id, title, content) VALUES (?, ?, ?, ?, ?)',
        [thread_id, community_id, user_id, title, content]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to create thread');
      }

      return this.getById(thread_id);
    } catch (error) {
      console.error('Error in create thread:', error);
      throw error;
    }
  }

  static async getAll() {
    try {
      const [threads] = await db.query(
        `SELECT t.*, u.name as author_name, u.username as author_username,
         c.name as community_name,
         (SELECT COUNT(*) FROM comments WHERE thread_id = t.thread_id) as comment_count
         FROM threads t
         JOIN users u ON t.user_id = u.uid
         JOIN communities c ON t.community_id = c.community_id
         ORDER BY t.created_at DESC`
      );
      return threads;
    } catch (error) {
      console.error('Error in getAll:', error);
      throw error;
    }
  }

  static async getById(threadId) {
    try {
      const [threads] = await db.query(
        `SELECT t.*, u.name as author_name, u.username as author_username,
         c.name as community_name,
         (SELECT COUNT(*) FROM comments WHERE thread_id = t.thread_id) as comment_count
         FROM threads t
         JOIN users u ON t.user_id = u.uid
         JOIN communities c ON t.community_id = c.community_id
         WHERE t.thread_id = ?`,
        [threadId]
      );
      return threads[0] || null;
    } catch (error) {
      console.error('Error in getById:', error);
      throw error;
    }
  }

  static async getByUserId(userId) {
    try {
      const [threads] = await db.query(
        `SELECT t.*, u.name as author_name, u.username as author_username,
         c.name as community_name,
         (SELECT COUNT(*) FROM comments WHERE thread_id = t.thread_id) as comment_count
         FROM threads t
         JOIN users u ON t.user_id = u.uid
         JOIN communities c ON t.community_id = c.community_id
         WHERE t.user_id = ?
         ORDER BY t.created_at DESC`,
        [userId]
      );
      return threads;
    } catch (error) {
      console.error('Error in getByUserId:', error);
      throw error;
    }
  }

  static async getByCommunityId(communityId) {
    try {
      const [threads] = await db.query(
        `SELECT t.*, u.name as author_name, u.username as author_username,
         c.name as community_name,
         (SELECT COUNT(*) FROM comments WHERE thread_id = t.thread_id) as comment_count
         FROM threads t
         JOIN users u ON t.user_id = u.uid
         JOIN communities c ON t.community_id = c.community_id
         WHERE t.community_id = ?
         ORDER BY t.created_at DESC`,
        [communityId]
      );
      return threads;
    } catch (error) {
      console.error('Error in getByCommunityId:', error);
      throw error;
    }
  }

  static async update(threadId, { title, content }) {
    try {
      const updates = [];
      const values = [];

      if (title) {
        updates.push('title = ?');
        values.push(title);
      }
      if (content !== undefined) {
        updates.push('content = ?');
        values.push(content);
      }

      if (updates.length === 0) return this.getById(threadId);

      values.push(threadId);
      const [result] = await db.query(
        `UPDATE threads SET ${updates.join(', ')}, updated_at = CURRENT_TIMESTAMP WHERE thread_id = ?`,
        values
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to update thread');
      }

      return this.getById(threadId);
    } catch (error) {
      console.error('Error in update:', error);
      throw error;
    }
  }

  static async delete(threadId) {
    try {
      // Delete all comments first
      await db.query('DELETE FROM comments WHERE thread_id = ?', [threadId]);
      
      // Then delete the thread
      const [result] = await db.query(
        'DELETE FROM threads WHERE thread_id = ?',
        [threadId]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to delete thread');
      }

      return true;
    } catch (error) {
      console.error('Error in delete:', error);
      throw error;
    }
  }
}

module.exports = Thread; 