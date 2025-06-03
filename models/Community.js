const db = require('../config/db');

class Community {
  static async create({ name, description, image, created_by }) {
    try {
      const community_id = `community_${Date.now()}_${Math.floor(Math.random() * 1000)}`;
      
      const [result] = await db.query(
        'INSERT INTO communities (community_id, name, description, image, created_by) VALUES (?, ?, ?, ?, ?)',
        [community_id, name, description, image, created_by]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to create community');
      }

      // Add creator as a member
      await this.addMember(community_id, created_by);

      return this.getById(community_id);
    } catch (error) {
      console.error('Error in create community:', error);
      throw error;
    }
  }

  static async getAll() {
    try {
      const [communities] = await db.query(
        `SELECT c.*, u.name as creator_name, u.username as creator_username,
         (SELECT COUNT(*) FROM community_members WHERE community_id = c.community_id) as member_count
         FROM communities c
         JOIN users u ON c.created_by = u.uid
         ORDER BY c.created_at DESC`
      );
      return communities;
    } catch (error) {
      console.error('Error in getAll:', error);
      throw error;
    }
  }

  static async getById(communityId) {
    try {
      const [communities] = await db.query(
        `SELECT c.*, u.name as creator_name, u.username as creator_username,
         (SELECT COUNT(*) FROM community_members WHERE community_id = c.community_id) as member_count
         FROM communities c
         JOIN users u ON c.created_by = u.uid
         WHERE c.community_id = ?`,
        [communityId]
      );
      return communities[0] || null;
    } catch (error) {
      console.error('Error in getById:', error);
      throw error;
    }
  }

  static async getByMemberId(userId) {
    try {
      const [communities] = await db.query(
        `SELECT c.*, u.name as creator_name, u.username as creator_username,
         (SELECT COUNT(*) FROM community_members WHERE community_id = c.community_id) as member_count
         FROM communities c
         JOIN community_members cm ON c.community_id = cm.community_id
         JOIN users u ON c.created_by = u.uid
         WHERE cm.user_id = ?
         ORDER BY cm.joined_at DESC`,
        [userId]
      );
      return communities;
    } catch (error) {
      console.error('Error in getByMemberId:', error);
      throw error;
    }
  }

  static async update(communityId, { name, description, image }) {
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
      if (image !== undefined) {
        updates.push('image = ?');
        values.push(image);
      }

      if (updates.length === 0) return this.getById(communityId);

      values.push(communityId);
      const [result] = await db.query(
        `UPDATE communities SET ${updates.join(', ')} WHERE community_id = ?`,
        values
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to update community');
      }

      return this.getById(communityId);
    } catch (error) {
      console.error('Error in update:', error);
      throw error;
    }
  }

  static async delete(communityId) {
    try {
      // Delete all community members first
      await db.query('DELETE FROM community_members WHERE community_id = ?', [communityId]);
      
      // Then delete the community
      const [result] = await db.query(
        'DELETE FROM communities WHERE community_id = ?',
        [communityId]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to delete community');
      }

      return true;
    } catch (error) {
      console.error('Error in delete:', error);
      throw error;
    }
  }

  static async addMember(communityId, userId) {
    try {
      const [result] = await db.query(
        'INSERT INTO community_members (community_id, user_id) VALUES (?, ?)',
        [communityId, userId]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to add member to community');
      }

      return true;
    } catch (error) {
      console.error('Error in addMember:', error);
      throw error;
    }
  }

  static async removeMember(communityId, userId) {
    try {
      const [result] = await db.query(
        'DELETE FROM community_members WHERE community_id = ? AND user_id = ?',
        [communityId, userId]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to remove member from community');
      }

      return true;
    } catch (error) {
      console.error('Error in removeMember:', error);
      throw error;
    }
  }
}

module.exports = Community; 