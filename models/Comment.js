const db = require('../config/db');

class Comment {
  static async verifyThreadExists(threadId) {
    try {
      const [threads] = await db.query(
        'SELECT thread_id FROM threads WHERE thread_id = ?',
        [threadId]
      );
      return threads.length > 0;
    } catch (error) {
      console.error('Error in verifyThreadExists:', error);
      throw error;
    }
  }

  static async create({ thread_id, user_id, content }) {
    try {
      // Verify thread exists
      const threadExists = await this.verifyThreadExists(thread_id);
      if (!threadExists) {
        throw new Error('Thread does not exist');
      }

      const comment_id = `comment_${Date.now()}_${Math.floor(Math.random() * 1000)}`;
      
      const [result] = await db.query(
        'INSERT INTO comments (comment_id, thread_id, user_id, content) VALUES (?, ?, ?, ?)',
        [comment_id, thread_id, user_id, content]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to create comment');
      }

      return this.getById(comment_id);
    } catch (error) {
      console.error('Error in create comment:', error);
      throw error;
    }
  }

  static async getByThreadId(threadId) {
    try {
      const [comments] = await db.query(
        `SELECT c.*, u.name as author_name, u.username as author_username 
         FROM comments c 
         JOIN users u ON c.user_id = u.uid 
         WHERE c.thread_id = ? 
         ORDER BY c.created_at DESC`,
        [threadId]
      );
      return comments;
    } catch (error) {
      console.error('Error in getByThreadId:', error);
      throw error;
    }
  }

  static async getById(commentId) {
    try {
      const [comments] = await db.query(
        `SELECT c.*, u.name as author_name, u.username as author_username 
         FROM comments c 
         JOIN users u ON c.user_id = u.uid 
         WHERE c.comment_id = ?`,
        [commentId]
      );
      return comments[0] || null;
    } catch (error) {
      console.error('Error in getById:', error);
      throw error;
    }
  }

  static async update(commentId, { content }) {
    try {
      const [result] = await db.query(
        'UPDATE comments SET content = ? WHERE comment_id = ?',
        [content, commentId]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to update comment');
      }

      return this.getById(commentId);
    } catch (error) {
      console.error('Error in update:', error);
      throw error;
    }
  }

  static async delete(commentId) {
    try {
      const [result] = await db.query(
        'DELETE FROM comments WHERE comment_id = ?',
        [commentId]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to delete comment');
      }

      return true;
    } catch (error) {
      console.error('Error in delete:', error);
      throw error;
    }
  }
}

module.exports = Comment; 