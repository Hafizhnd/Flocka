const Event = require('../models/Event');

// Create a new event
exports.createEvent = async (req, res) => {
  try {
    const { name, description, event_date, start_time, end_time, location, image, cost } = req.body;
    
    if (!name || !event_date || !start_time || !end_time || !location) {
      return res.status(400).json({
        success: false,
        message: 'Please provide name, event_date, start_time, end_time, and location'
      });
    }

    if (event_date && !/^\d{4}-\d{2}-\d{2}$/.test(event_date)) {
      return res.status(400).json({ success: false, message: 'Event date format should be YYYY-MM-DD' });
  }

    const startDateObj = new Date(start_time); 
    const endDateObj = new Date(end_time);   
    const eventDateObj = new Date(event_date);

    if (endDateObj <= startDateObj) {
      return res.status(400).json({
        success: false,
        message: 'End time must be after start time'
      });
    }

    if (cost && (isNaN(parseFloat(cost)) || parseFloat(cost) < 0)) {
      return res.status(400).json({ success: false, message: 'Cost must be a non-negative number.' });
  }

    const event = await Event.create({
      name,
      organizer: req.user.uid, 
      description,
      event_date,
      start_time,
      end_time,  
      location,
      image,
      cost: cost || 0.00
    });

    res.status(201).json({
      success: true,
      message: 'Event created successfully',
      data: event
    });
  } catch (error) {
    console.error('Error creating event:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while creating the event',
      error: error.message
    });
  }
};

exports.updateEvent = async (req, res) => {
  try {
    const { eventId } = req.params;
    const { name, description, event_date, start_time, end_time, location, image, cost } = req.body;

    const event = await Event.getById(eventId);
    if (!event) {  }
    if (event.organizer !== req.user.uid) {  }

    if (event_date && !/^\d{4}-\d{2}-\d{2}$/.test(event_date)) { 
        return res.status(400).json({ success: false, message: 'Event date format should be YYYY-MM-DD' });
     }
    if (start_time && end_time) {
        const startDateObj = new Date(start_time);
        const endDateObj = new Date(end_time);
        if (endDateObj <= startDateObj) {
            return res.status(400).json({ success: false, message: 'End time must be after start time' });
        }
    }
    if (cost && (isNaN(parseFloat(cost)) || parseFloat(cost) < 0)) {
        return res.status(400).json({ success: false, message: 'Cost must be a non-negative number.' });
    }


    const updatedEventData = { name, description, event_date, start_time, end_time, location, image, cost };
    Object.keys(updatedEventData).forEach(key => updatedEventData[key] === undefined && delete updatedEventData[key]);


    const updatedEvent = await Event.update(eventId, updatedEventData);

    res.json({
      success: true,
      message: 'Event updated successfully',
      data: updatedEvent
    });
  } catch (error) {
    console.error('Error updating event:', error);
    res.status(500).json({ 
      success: false,
      message: 'An error occurred while updating the event',
      error: error.message
     });
  }
};

exports.getEventById = async (req, res) => {
  try {
    const { eventId } = req.params;
    // --- LOG 1: Verify the eventId received by the controller ---
    console.log(`EVENT CONTROLLER: getEventById received eventId from req.params: "${eventId}"`); 
    
    const event = await Event.getById(eventId);

    if (!event) {
      // --- LOG 2: Confirm that Event.getById returned null ---
      console.log(`EVENT CONTROLLER: Event.getById returned null for eventId: "${eventId}"`); 
      return res.status(404).json({
        success: false,
        message: 'Event not found'
      });
    }

    // This part is only reached if 'event' is found
    console.log(`EVENT CONTROLLER: Event found for eventId: "${eventId}", Data:`, event);
    res.json({
      success: true,
      data: event
    });
  } catch (error) {
    console.error(`EVENT CONTROLLER: Error in getEventById for eventId: "${eventId}"`, error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching the event',
      error: error.message
    });
  }
};

exports.getEvents = async (req, res) => {
  try {
    const events = await Event.getAll();

    res.json({
      success: true,
      data: events
    });
  } catch (error) {
    console.error('Error getting events:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching events',
      error: error.message
    });
  }
};

// Get my events
exports.getMyEvents = async (req, res) => {
  try {
    const events = await Event.getByOrganizer(req.user.uid);

    res.json({
      success: true,
      data: events
    });
  } catch (error) {
    console.error('Error getting user events:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching your events',
      error: error.message
    });
  }
};

// Get upcoming events
exports.getUpcomingEvents = async (req, res) => {
  try {
    const events = await Event.getUpcoming();

    res.json({
      success: true,
      data: events
    });
  } catch (error) {
    console.error('Error getting upcoming events:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching upcoming events',
      error: error.message
    });
  }
};

// Get past events
exports.getPastEvents = async (req, res) => {
  try {
    const events = await Event.getPast();

    res.json({
      success: true,
      data: events
    });
  } catch (error) {
    console.error('Error getting past events:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching past events',
      error: error.message
    });
  }
};

// Get a specific event
exports.getEventById = async (req, res) => {
  try {
    const { eventId } = req.params;
    const event = await Event.getById(eventId);

    if (!event) {
      return res.status(404).json({
        success: false,
        message: 'Event not found'
      });
    }

    res.json({
      success: true,
      data: event
    });
  } catch (error) {
    console.error('Error getting event:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching the event',
      error: error.message
    });
  }
};

// Delete an event
exports.deleteEvent = async (req, res) => {
  try {
    const { eventId } = req.params;

    // Check if event exists and user is the organizer
    const event = await Event.getById(eventId);
    if (!event) {
      return res.status(404).json({
        success: false,
        message: 'Event not found'
      });
    }

    if (event.organizer !== req.user.uid) {
      return res.status(403).json({
        success: false,
        message: 'You are not authorized to delete this event'
      });
    }

    await Event.delete(eventId);

    res.json({
      success: true,
      message: 'Event deleted successfully'
    });
  } catch (error) {
    console.error('Error deleting event:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while deleting the event',
      error: error.message
    });
  }
}; 