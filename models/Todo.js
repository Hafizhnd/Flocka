const db = require('../config/db');

class Todo {
  static async create({ user_id, task_title, task_description, start_time, end_time, date }) {
    try {
      const todo_id = `todo_${Date.now()}_${Math.floor(Math.random() * 1000)}`;
      
      const [result] = await db.query(
        'INSERT INTO todos (todo_id, user_id, task_title, task_description, start_time, end_time, date) VALUES (?, ?, ?, ?, ?, ?, ?)',
        [todo_id, user_id, task_title, task_description, start_time, end_time, date]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to create todo');
      }

      return this.getById(todo_id);
    } catch (error) {
      console.error('Error in create todo:', error);
      throw error;
    }
  }

  static async getByUserId(userId) {
    try {
        const [todos] = await db.query(
        `SELECT * FROM todos 
         WHERE user_id = ? 
         ORDER BY 
           CASE 
             WHEN date IS NULL THEN 1 
             ELSE 0 
           END,
           date ASC,
           CASE 
             WHEN start_time IS NULL THEN 1  -- Changed from time to start_time
             ELSE 0 
           END,
           start_time ASC`, 
        [userId]
      );
      return todos;
    } catch (error) {
      console.error('Error in getByUserId:', error);
      throw error;
    }
  }

  static async getById(todoId) {
    try {
      const [todos] = await db.query(
        'SELECT * FROM todos WHERE todo_id = ?',
        [todoId]
      );
      return todos[0] || null;
    } catch (error) {
      console.error('Error in getById:', error);
      throw error;
    }
  }

  static async getByDate(userId, date) {
    try {
      const [todos] = await db.query(
        `SELECT * FROM todos 
         WHERE user_id = ? AND date = ?
         ORDER BY 
           CASE 
             WHEN start_time IS NULL THEN 1  // Changed from time to start_time
             ELSE 0 
           END,
           start_time ASC`,
        [userId, date]
      );
      return todos;
    } catch (error) {
      console.error('Error in getByDate:', error);
      throw error;
    }
  }

  static async getByStatus(userId, isDone) {
    try {
      const [todos] = await db.query(
        `SELECT * FROM todos 
         WHERE user_id = ? AND is_done = ?
         ORDER BY 
           CASE 
             WHEN date IS NULL THEN 1 
             ELSE 0 
           END,
           date ASC,
           CASE 
             WHEN start_time IS NULL THEN 1 
             ELSE 0 
           END,
           start_time ASC`,
        [userId, isDone]
      );
      return todos;
    } catch (error) {
      console.error('Error in getByStatus:', error);
      throw error;
    }
  }

  static async update(todoId, { task_title, task_description, start_time, end_time, date }) {
    try {
      const updates = [];
      const values = [];

      if (task_title !== undefined) { 
        updates.push('task_title = ?');
        values.push(task_title);
      }
      if (task_description !== undefined) {
        updates.push('task_description = ?');
        values.push(task_description);
      }
      // Handle new time fields
      if (start_time !== undefined) {
        updates.push('start_time = ?');
        values.push(start_time);
      }
      if (end_time !== undefined) {
        updates.push('end_time = ?');
        values.push(end_time);
      }
      if (date !== undefined) {
        updates.push('date = ?');
        values.push(date);
      }

      if (updates.length === 0) return this.getById(todoId); 

      values.push(todoId);
      const [result] = await db.query(
        `UPDATE todos SET ${updates.join(', ')}, updated_at = CURRENT_TIMESTAMP WHERE todo_id = ?`,
        values
      );

      // if (!result || !result.affectedRows) {
      //   throw new Error('Failed to update todo');
      // }

      return this.getById(todoId);
    } catch (error) {
      console.error('Error in update:', error);
      throw error;
    }
  }

  static async toggleStatus(todoId) {
    try {
      const [result] = await db.query(
        'UPDATE todos SET is_done = NOT is_done, updated_at = CURRENT_TIMESTAMP WHERE todo_id = ?',
        [todoId]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to toggle todo status');
      }

      return this.getById(todoId);
    } catch (error) {
      console.error('Error in toggleStatus:', error);
      throw error;
    }
  }

  static async delete(todoId) {
    try {
      const [result] = await db.query(
        'DELETE FROM todos WHERE todo_id = ?',
        [todoId]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to delete todo');
      }

      return true;
    } catch (error) {
      console.error('Error in delete:', error);
      throw error;
    }
  }
}

module.exports = Todo; 