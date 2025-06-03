const db = require('../config/db');

class Event {
  static async create({ name, organizer, description, event_date, start_time, end_time, location, image, cost }) {
    try {
      const event_id = `event_${Date.now()}_${Math.floor(Math.random() * 1000)}`;
      
      const [result] = await db.query(
        'INSERT INTO events (event_id, name, organizer, description, event_date, start_time, end_time, location, image, cost) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)',
        [event_id, name, organizer, description, event_date, start_time, end_time, location, image, cost]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to create event');
      }

      return this.getById(event_id);
    } catch (error) {
      console.error('Error in create event:', error);
      throw error;
    }
  }

  static async getAll() {
    try {
        console.log("EVENT MODEL: GetAll - RUNNING EXTREMELY SIMPLIFIED TEST QUERY.");
        const [events] = await db.query('SELECT event_id, name FROM events LIMIT 3;');
        console.log("EVENT MODEL: GetAll - SIMPLIFIED TEST QUERY RESULT:", events);
        return events;
    } catch (error) {
        console.error('EVENT MODEL: GetAll - Error in SIMPLIFIED TEST QUERY:', error);
        throw error;
    }
}

  static async getById(eventId) {
    try {
      const [events] = await db.query(
        `SELECT e.*, u.name as organizer_name, u.username as organizer_username,
         (SELECT COUNT(*) FROM orders WHERE item_type = 'event' AND item_id = e.event_id) as participant_count
         FROM events e
         JOIN users u ON e.organizer = u.uid
         WHERE e.event_id = ?`,
        [eventId]
      );
      return events[0] || null;
    } catch (error) {
      console.error('Error in getById:', error);
      throw error;
    }
  }

  static async getByOrganizer(organizerId) {
    try {
      const [events] = await db.query(
        `SELECT e.*, u.name as organizer_name, u.username as organizer_username,
         (SELECT COUNT(*) FROM orders WHERE item_type = 'event' AND item_id = e.event_id) as participant_count
         FROM events e
         JOIN users u ON e.organizer = u.uid
         WHERE e.organizer = ?
         ORDER BY e.start_time ASC`,
        [organizerId]
      );
      return events;
    } catch (error) {
      console.error('Error in getByOrganizer:', error);
      throw error;
    }
  }

  static async getUpcoming() { // For testing the /upcoming route with basic data
    try {
      console.log("EVENT MODEL: GetUpcoming - Running Test Query with Essential Fields + Dummies.");
      const [events] = await db.query(
          'SELECT event_id, name, organizer, description, event_date, start_time, end_time, location, image, cost, created_at, ' +
          ' "Test Organizer" as organizer_name, "test_org_user" as organizer_username, 0 as participant_count ' +
          'FROM events;'
      );
      console.log("EVENT MODEL: GetUpcoming - Test Query with Dummies Result:", events);
      return events;
    } catch (error) {
      console.error('Error in getUpcoming (full query):', error);
      throw error;
    }
  }

  static async getPast() {
    try {
      const [events] = await db.query(
        `SELECT e.*, u.name as organizer_name, u.username as organizer_username,
         (SELECT COUNT(*) FROM orders WHERE item_type = 'event' AND item_id = e.event_id) as participant_count
         FROM events e
         JOIN users u ON e.organizer = u.uid
         WHERE e.start_time < NOW()
         ORDER BY e.start_time DESC`
      );
      return events;
    } catch (error) {
      console.error('Error in getPast:', error);
      throw error;
    }
  }

  static async update(eventId, { name, description, event_date, start_time, end_time, location, image, cost }) {
    try {
      const updates = [];
      const values = [];

      if (name !== undefined) { updates.push('name = ?'); values.push(name); }
      if (description !== undefined) { updates.push('description = ?'); values.push(description); }
      if (event_date !== undefined) { updates.push('event_date = ?'); values.push(event_date); } 
      if (start_time !== undefined) { updates.push('start_time = ?'); values.push(start_time); } 
      if (end_time !== undefined) { updates.push('end_time = ?'); values.push(end_time); }     
      if (location !== undefined) { updates.push('location = ?'); values.push(location); }
      if (image !== undefined) { updates.push('image = ?'); values.push(image); }
      if (cost !== undefined) { updates.push('cost = ?'); values.push(cost); }               

      if (updates.length === 0) return this.getById(eventId);

      values.push(eventId);
      const [result] = await db.query(
        `UPDATE events SET ${updates.join(', ')} WHERE event_id = ?`,
        values
      );

      return this.getById(eventId);
    } catch (error) {
      console.error('Error in update event:', error);
      throw error;
    }
  }

  static async delete(eventId) {
    try {
      // Delete all related orders first
      await db.query('DELETE FROM orders WHERE item_type = "event" AND item_id = ?', [eventId]);
      
      // Then delete the event
      const [result] = await db.query(
        'DELETE FROM events WHERE event_id = ?',
        [eventId]
      );

      if (!result || !result.affectedRows) {
        throw new Error('Failed to delete event');
      }

      return true;
    } catch (error) {
      console.error('Error in delete:', error);
      throw error;
    }
  }
}

module.exports = Event; 