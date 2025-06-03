const express = require('express');
const { createOrder, getMyOrders } = require('../controllers/orderController');
const { protect } = require('../controllers/authController');

const router = express.Router();

router.use(protect);

router.post('/', createOrder);    
router.get('/my', getMyOrders);     

module.exports = router;