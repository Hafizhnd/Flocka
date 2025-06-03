const express = require('express');
const path = require('path');
const cors = require('cors');
const authRoutes = require('./routes/authRoutes');
const commentRoutes = require('./routes/commentRoutes');
const communityRoutes = require('./routes/communityRoutes');
const threadRoutes = require('./routes/threadRoutes');
const eventRoutes = require('./routes/eventRoutes');
const spaceRoutes = require('./routes/spaceRoutes');
const todoRoutes = require('./routes/todoRoutes');
const quizRoutes = require('./routes/quizRoutes');
const userQuizRoutes = require('./routes/userQuizRoutes');
const interestRoutes = require('./routes/interestRoutes');
const userInterestRoutes = require('./routes/userInterestRoutes');
const orderRoutes = require('./routes/orderRoutes');

const app = express();

// Middleware
app.use(cors());
app.use(express.json());

app.use('/public', express.static(path.join(__dirname, 'public')));
app.use('/api/events', (req, res, next) => {
  console.log(`APP.JS: Request to /api/events. Method: ${req.method}, Path: ${req.originalUrl}`);
  next();
}, eventRoutes);

// Root Route
app.get('/', (req, res) => {
  res.status(200).json({
    success: true,
    message: 'Welcome to the API Service',
    documentation: {
      auth: '/api/auth',
      communities: '/api/communities',
      threads: '/api/threads',
      comments: '/api/comments',
      events: '/api/events',
      spaces: '/api/spaces',
      todos: '/api/todos',
      quizzes: '/api/quizzes',
      userQuizzes: '/api/user-quizzes',
      interests: '/api/interests',
      userInterests: '/api/user-interests',
      orders: '/api/orders',
      status: '/api/health'
    }
  });
});

// Health Check Route
app.get('/api/health', (req, res) => {
  res.status(200).json({
    status: 'OK',
    timestamp: new Date().toISOString()
  });
});

// Routes
app.use('/api/auth', authRoutes);
app.use('/api/comments', commentRoutes);
app.use('/api/communities', communityRoutes);
app.use('/api/threads', threadRoutes);
app.use('/api/events', eventRoutes);
app.use('/api/spaces', spaceRoutes);
app.use('/api/todos', todoRoutes);
app.use('/api/quizzes', quizRoutes);
app.use('/api/user-quizzes', userQuizRoutes);
app.use('/api/interests', interestRoutes);
app.use('/api/user-interests', userInterestRoutes);
app.use('/api/orders', orderRoutes);

// 404 Handler
app.use((req, res, next) => {
  res.status(404).json({
    success: false,
    error: 'Endpoint not found'
  });
});

// Error handling
app.use((err, req, res, next) => {
  console.error(err.stack);
  res.status(500).json({ 
    success: false, 
    error: 'Internal server error',
    message: process.env.NODE_ENV === 'development' ? err.message : undefined
  });
});

module.exports = app;