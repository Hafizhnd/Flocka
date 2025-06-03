// models/Order.js
const db = require('../config/db');

class Order {
  static async create({
    user_id, item_type, item_id, item_name, item_image,
    booked_start_datetime, booked_end_datetime, 
    quantity, amount_paid, currency, order_status
  }) {
    try {
      const order_id = `order_${Date.now()}_${Math.floor(Math.random() * 1000)}`;
      const sql = `INSERT INTO orders (
                    order_id, user_id, item_type, item_id, item_name, item_image,
                    booked_start_datetime, booked_end_datetime, quantity, amount_paid, currency, order_status
                 ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)`;
      
      const [result] = await db.query(sql, [
        order_id, user_id, item_type, item_id, item_name, item_image,
        booked_start_datetime, booked_end_datetime, 
        quantity, amount_paid, currency, order_status
      ]);

      if (!result || !result.affectedRows) {
        throw new Error('Failed to create order');
      }
      return this.getById(order_id);
    } catch (error) {
      console.error('Error in Order.create:', error);
      throw error;
    }
  }

  static async getById(orderId) {
    try {
      const [rows] = await db.query('SELECT * FROM orders WHERE order_id = ?', [orderId]);
      return rows[0] || null;
    } catch (error) {
      console.error('Error in Order.getById:', error);
      throw error;
    }
  }

  static async getByUserId(userId, pStatus = null) {
    try {
      let sql = 'SELECT * FROM orders WHERE user_id = ?';
      const params = [userId];

      if (pStatus === 'active') {
        sql += ' AND order_status = "active" AND (booked_end_datetime IS NULL OR booked_end_datetime >= NOW())';
      } else if (pStatus === 'archived') {   
        sql += ' AND (order_status IN ("completed", "archived") OR (order_status = "active" AND booked_end_datetime < NOW()))';
      }

      sql += ' ORDER BY booked_start_datetime DESC';
      
      const [rows] = await db.query(sql, params);
      return rows;
    } catch (error) {
      console.error('Error in Order.getByUserId:', error);
      throw error;
    }
  }
}

module.exports = Order;