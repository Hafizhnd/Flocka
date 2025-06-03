const express = require('express');
const { getSpaces, getSpaceById } = require('../controllers/spaceController');
const { protect } = require('../controllers/authController'); 

const router = express.Router();

router.use(protect);

router.get('/', getSpaces);

router.get('/:spaceId', getSpaceById);

module.exports = router;