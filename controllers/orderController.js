const Order = require('../models/Order');
const Event = require('../models/Event');
const Space = require('../models/Space'); 

exports.createOrder = async (req, res) => {
  try {
    const user_id = req.user.uid;
    const { 
        item_type, 
        item_id,
        quantity, 
        amount_paid,
        currency,
        booked_start_datetime,
        booked_end_datetime   
    } = req.body;

    if (!item_type || !item_id || !booked_start_datetime || amount_paid === undefined || quantity === undefined) {
      return res.status(400).json({ success: false, message: 'Missing required fields for order.' });
    }

    let itemName = "Unknown Item";
    let itemImage = null;
    let calculatedAmount = parseFloat(amount_paid);

    if (item_type === 'event') {
        const event = await Event.getById(item_id);
        if (!event) return res.status(404).json({ success: false, message: 'Event not found for order.' });
        itemName = event.name;
        itemImage = event.image;
        if (event.cost !== null && parseFloat(event.cost) !== calculatedAmount && quantity === 1) {
            console.warn(`Order amount ${calculatedAmount} for event ${item_id} does not match event cost ${event.cost}`);
        }
    } else if (item_type === 'space') {
        const space = await Space.getById(item_id);
        if (!space) return res.status(404).json({ success: false, message: 'Space not found for order.' });
        itemName = space.name;
        itemImage = space.image;
    } else {
        return res.status(400).json({ success: false, message: 'Invalid item type for order.' });
    }

    const orderData = {
        user_id,
        item_type,
        item_id,
        item_name: itemName,
        item_image: itemImage,
        booked_start_datetime,
        booked_end_datetime, 
        quantity: parseInt(quantity) || 1,
        amount_paid: calculatedAmount,
        currency: currency || 'IDR',
        order_status: 'active'
    };
    
    const newOrder = await Order.create(orderData);

    res.status(201).json({ 
        success: true, 
        message: `Your booking for ${itemName} is confirmed!`, 
        data: newOrder 
    });

  } catch (error) {
    console.error("Error in createOrder controller:", error);
    res.status(500).json({ success: false, message: 'Failed to process your booking.', error: error.message });
  }
};

exports.getMyOrders = async (req, res) => {
  try {
    const user_id = req.user.uid;
    const { status } = req.query; 
    
    const orders = await Order.getByUserId(user_id, status);
    res.json({ success: true, data: orders });
  } catch (error) {
    console.error("Error fetching user orders:", error);
    res.status(500).json({ success: false, message: 'Failed to fetch your orders.', error: error.message });
  }
};