const db = require('../config/db');
const bcrypt = require('bcryptjs');

class User {
  static async createUser({ username, email, password }) {
    try {
      // Validasi input
      if ( !username || !email || !password) {
        throw new Error('All fields are required');
      }

      // Cek apakah email atau username sudah terdaftar
      const existingUser = await this.getUserByEmail(email) || await this.getUserByUsername(username);
      if (existingUser) {
        throw new Error('Email or username already registered');
      }

      const hashedPassword = await bcrypt.hash(password, 10);
      const uid = `user_${Date.now()}_${Math.floor(Math.random() * 1000)}`;

      const [result] = await db.query(
        'INSERT INTO users (uid, username, email, password) VALUES (?, ?, ?, ?)',
        [uid, username, email, hashedPassword]
      );
      
      if (!result || !result.affectedRows) {
        throw new Error('Failed to insert user into database');
      }

      return this.getSafeUserByUid(uid);
    } catch (error) {
      console.error('Error in createUser:', error);
      console.error('Error details:', {
        message: error.message,
        code: error.code,
        sqlMessage: error.sqlMessage
      });
      throw error;
    }
  }

  static async updateUser(uid, { name, profession, gender, age, bio }) {
    try {
      const fieldsToUpdate = {};
      if (name !== undefined) fieldsToUpdate.name = name;
      if (profession !== undefined) fieldsToUpdate.profession = profession;
      if (gender !== undefined) fieldsToUpdate.gender = gender;
      if (age !== undefined) fieldsToUpdate.age = age;
      if (bio !== undefined) fieldsToUpdate.bio = bio;
      
      if (Object.keys(fieldsToUpdate).length === 0) {
         if (name === undefined && profession === undefined && gender === undefined && age === undefined && bio === undefined) {
            throw new Error('No fields to update');
        }
      }

      const setClause = Object.keys(fieldsToUpdate).map(key => `${key} = ?`).join(', ');
      const values = Object.values(fieldsToUpdate);

      const [result] = await db.query(
        `UPDATE users SET ${setClause} WHERE uid = ?`,
        [...values, uid]
      );

      if (result.affectedRows === 0 && Object.keys(fieldsToUpdate).length > 0) {
        throw new Error('User not found or no new data to update.');
      }

      return this.getSafeUserByUid(uid);
    } catch (error) {
      console.error('Error in updateUser:', error.message);
      throw error;
    }
  }

  static async getUserByEmail(email) {
    try {
      const [rows] = await db.query('SELECT * FROM users WHERE email = ?', [email]);
      return rows[0] || null;
    } catch (error) {
      console.error('Error in getUserByEmail:', error.message);
      throw error;
    }
  }

  static async getUserByUsername(username) {
    try {
      const [rows] = await db.query('SELECT * FROM users WHERE username = ?', [username]);
      return rows[0] || null;
    } catch (error) {
      console.error('Error in getUserByUsername:', error.message);
      throw error;
    }
  }

  static async getUserByUid(uid) {
    try {
      const [rows] = await db.query('SELECT * FROM users WHERE uid = ?', [uid]);
      return rows[0] || null;
    } catch (error) {
      console.error('Error in getUserByUid:', error.message);
      throw error;
    }
  }

  static async getSafeUserByUid(uid) {
    try {
      const user = await this.getUserByUid(uid);
      if (!user) return null;
      
      const { password, ...userWithoutPassword } = user;
      return userWithoutPassword;
    } catch (error) {
      console.error('Error in getSafeUserByUid:', error.message);
      throw error;
    }
  }

  static async updateProfileImageUrl(uid, imageUrl) {
    try {
        await db.query(
            'UPDATE users SET profile_image_url = ? WHERE uid = ?',
            [imageUrl, uid]
        );
        return true;
    } catch (error) {
        console.error('Error updating profile image URL in DB:', error);
        throw error;
    }
  }

  static async updateStreak(userId) {
    try {
      const [rows] = await db.query(
        'SELECT current_streak, last_quiz_completed_date FROM users WHERE uid = ?',
        [userId]
      );
      if (!rows.length) {
        throw new Error('User not found for streak update');
      }

      const userStreakData = rows[0];
      let currentStreak = userStreakData.current_streak;
      const lastCompletedDateStr = userStreakData.last_quiz_completed_date 
        ? new Date(userStreakData.last_quiz_completed_date).toISOString().slice(0,10) 
        : null;

      const today = new Date();
      const todayStr = today.toISOString().slice(0, 10);
      
      if (lastCompletedDateStr !== todayStr) {
        const yesterday = new Date(today);
        yesterday.setDate(today.getDate() - 1);
        const yesterdayStr = yesterday.toISOString().slice(0, 10);

        if (lastCompletedDateStr === yesterdayStr) {
          currentStreak++;
        } else {
          currentStreak = 1;
        }
        
        await db.query(
          'UPDATE users SET current_streak = ?, last_quiz_completed_date = ? WHERE uid = ?',
          [currentStreak, todayStr, userId]
        );
      }

      const updatedUser = await this.getSafeUserByUid(userId); 
      return {
        current_streak: updatedUser.current_streak, 
        last_quiz_completed_date: updatedUser.last_quiz_completed_date
      };

    } catch (error) {
      console.error('Error updating streak:', error);
      throw error;
    }
  }
}

module.exports = User;