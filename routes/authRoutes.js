const express = require('express');
const { 
  register, 
  login, 
  logout, 
  protect, 
  updateProfile, 
  uploadMiddleware,
  updateProfilePicture 
} = require('../controllers/authController');

const router = express.Router();

// Public routes
router.post('/register', register);
router.post('/login', login);

// Protected routes
router.post('/logout', protect, logout);

// Test protected route
router.get('/me', protect, (req, res) => {
  res.json({
    success: true,
    data: {
      user: req.user
    }
  });
});

router.put('/me', protect, updateProfile); 

router.put('/me/picture', protect, uploadMiddleware, updateProfilePicture);

module.exports = router;