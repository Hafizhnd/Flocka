const express = require('express');
const { 
  createTodo,
  getTodos,
  getTodoById,
  updateTodo,
  deleteTodo,
  toggleTodoStatus,
  getTodosByDate,
  getCompletedTodos,
  getPendingTodos
} = require('../controllers/todoController');
const { protect } = require('../controllers/authController');

const router = express.Router();

// All routes require authentication
router.use(protect);

// Create a new todo
router.post('/', createTodo);

// Get all todos for current user
router.get('/', getTodos);

// Get todos by date
router.get('/date/:date', getTodosByDate);

// Get completed todos
router.get('/completed', getCompletedTodos);

// Get pending todos
router.get('/pending', getPendingTodos);

// Get a specific todo
router.get('/:todoId', getTodoById);

// Update a todo
router.put('/:todoId', updateTodo);

// Toggle todo status (complete/incomplete)
router.patch('/:todoId/toggle', toggleTodoStatus);

// Delete a todo
router.delete('/:todoId', deleteTodo);

module.exports = router; 