const express = require('express');
const { 
  createEvent,
  getEvents,
  getEventById,
  updateEvent,
  deleteEvent,
  getMyEvents,
  getUpcomingEvents,
  getPastEvents
} = require('../controllers/eventController');

const { protect } = require('../controllers/authController');

const router = express.Router();

router.use((req, res, next) => {
  console.log(`EVENT_ROUTES.JS: Received request. Method: ${req.method}, Path: ${req.path}, Params: ${JSON.stringify(req.params)}`);
  next();
});

// All routes require authentication
router.use(protect);

// Create a new event
router.post('/', createEvent);

// Get all events
router.get('/', getEvents);

// Get my events (events I'm organizing)
router.get('/my-events', getMyEvents);

// Get upcoming events
router.get('/upcoming', getUpcomingEvents);

// Get past events
router.get('/past', getPastEvents);

// Get a specific event
router.get('/:eventId', getEventById);

// Update an event (only by organizer)
router.put('/:eventId', updateEvent);

// Delete an event (only by organizer)
router.delete('/:eventId', deleteEvent);

module.exports = router; 