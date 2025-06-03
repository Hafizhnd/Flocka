const Todo = require('../models/Todo');

// Create a new todo
exports.createTodo = async (req, res) => {
  try {
    const { task_title, task_description, start_time, end_time, date } = req.body;
    
    if (!task_title) {
      return res.status(400).json({
        success: false,
        message: 'Please provide task_title'
      });
    }

    // Validate date format if provided
    if (date && !/^\d{4}-\d{2}-\d{2}$/.test(date)) {
      return res.status(400).json({
        success: false,
        message: 'Date format should be YYYY-MM-DD'
      });
    }

    const timeRegex = /^([01]\d|2[0-3]):([0-5]\d):([0-5]\d)$/;

    if (start_time && !timeRegex.test(start_time)) {
      return res.status(400).json({
        success: false,
        message: 'Start time format should be HH:MM:SS'
      });
    }

    if (end_time && !timeRegex.test(end_time)) {
      return res.status(400).json({
        success: false,
        message: 'End time format should be HH:MM:SS'
      });
    }

    if (start_time && end_time && start_time > end_time) {
        return res.status(400).json({
            success: false,
            message: 'End time must be after start time'
        });
    }

    const todo = await Todo.create({
      user_id: req.user.uid,
      task_title,
      task_description,
      start_time: start_time || null, 
      end_time: end_time || null,    
      date: date || null  
    });

    res.status(201).json({
      success: true,
      message: 'Todo created successfully',
      data: todo
    });
  } catch (error) {
    console.error('Error creating todo:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while creating the todo',
      error: error.message
    });
  }
};

// Get all todos for current user
exports.getTodos = async (req, res) => {
  try {
    const todos = await Todo.getByUserId(req.user.uid);

    res.json({
      success: true,
      data: todos
    });
  } catch (error) {
    console.error('Error getting todos:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching todos',
      error: error.message
    });
  }
};

// Get todos by date
exports.getTodosByDate = async (req, res) => {
  try {
    const { date } = req.params;

    // Validate date format
    const dateRegex = /^\d{4}-\d{2}-\d{2}$/;
    if (!dateRegex.test(date)) {
      return res.status(400).json({
        success: false,
        message: 'Date format should be YYYY-MM-DD'
      });
    }

    const todos = await Todo.getByDate(req.user.uid, date);

    res.json({
      success: true,
      data: todos
    });
  } catch (error) {
    console.error('Error getting todos by date:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching todos by date',
      error: error.message
    });
  }
};

// Get completed todos
exports.getCompletedTodos = async (req, res) => {
  try {
    const todos = await Todo.getByStatus(req.user.uid, true);

    res.json({
      success: true,
      data: todos
    });
  } catch (error) {
    console.error('Error getting completed todos:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching completed todos',
      error: error.message
    });
  }
};

// Get pending todos
exports.getPendingTodos = async (req, res) => {
  try {
    const todos = await Todo.getByStatus(req.user.uid, false);

    res.json({
      success: true,
      data: todos
    });
  } catch (error) {
    console.error('Error getting pending todos:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching pending todos',
      error: error.message
    });
  }
};

// Get a specific todo
exports.getTodoById = async (req, res) => {
  try {
    const { todoId } = req.params;
    const todo = await Todo.getById(todoId);

    if (!todo) {
      return res.status(404).json({
        success: false,
        message: 'Todo not found'
      });
    }

    // Check if the todo belongs to the current user
    if (todo.user_id !== req.user.uid) {
      return res.status(403).json({
        success: false,
        message: 'You are not authorized to view this todo'
      });
    }

    res.json({
      success: true,
      data: todo
    });
  } catch (error) {
    console.error('Error getting todo:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while fetching the todo',
      error: error.message
    });
  }
};

// Update a todo
exports.updateTodo = async (req, res) => {
  try {
    const { todoId } = req.params;
    const { task_title, task_description, start_time, end_time, date } = req.body;

    const todo = await Todo.getById(todoId); // Check existence and ownership first
    if (!todo) {
      return res.status(404).json({ success: false, message: 'Todo not found' });
    }
    if (todo.user_id !== req.user.uid) {
      return res.status(403).json({ success: false, message: 'Not authorized' });
    }

    if (date && !/^\d{4}-\d{2}-\d{2}$/.test(date)) {
      return res.status(400).json({ success: false, message: 'Date format should be YYYY-MM-DD' });
    }
    const timeRegex = /^([01]\d|2[0-3]):([0-5]\d):([0-5]\d)$/;
    if (start_time && !timeRegex.test(start_time)) {
      return res.status(400).json({ success: false, message: 'Start time format should be HH:MM:SS' });
    }
    if (end_time && !timeRegex.test(end_time)) {
      return res.status(400).json({ success: false, message: 'End time format should be HH:MM:SS' });
    }
    if (start_time && end_time && start_time > end_time) {
        return res.status(400).json({ success: false, message: 'End time must be after start time'});
    }

    const updatedTodo = await Todo.update(todoId, {
      task_title,
      task_description,
      start_time, 
      end_time,  
      date
    });

    res.json({
      success: true,
      message: 'Todo updated successfully',
      data: updatedTodo
    });
  } catch (error) {
    console.error('Error updating todo:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while updating the todo',
      error: error.message
    });
  }
};


// Toggle todo status
exports.toggleTodoStatus = async (req, res) => {
  try {
    const { todoId } = req.params;

    // Check if todo exists and belongs to the user
    const todo = await Todo.getById(todoId);
    if (!todo) {
      return res.status(404).json({
        success: false,
        message: 'Todo not found'
      });
    }

    if (todo.user_id !== req.user.uid) {
      return res.status(403).json({
        success: false,
        message: 'You are not authorized to update this todo'
      });
    }

    const updatedTodo = await Todo.toggleStatus(todoId);

    res.json({
      success: true,
      message: `Todo marked as ${updatedTodo.is_done ? 'completed' : 'pending'}`,
      data: updatedTodo
    });
  } catch (error) {
    console.error('Error toggling todo status:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while toggling the todo status',
      error: error.message
    });
  }
};

// Delete a todo
exports.deleteTodo = async (req, res) => {
  try {
    const { todoId } = req.params;

    // Check if todo exists and belongs to the user
    const todo = await Todo.getById(todoId);
    if (!todo) {
      return res.status(404).json({
        success: false,
        message: 'Todo not found'
      });
    }

    if (todo.user_id !== req.user.uid) {
      return res.status(403).json({
        success: false,
        message: 'You are not authorized to delete this todo'
      });
    }

    await Todo.delete(todoId);

    res.json({
      success: true,
      message: 'Todo deleted successfully'
    });
  } catch (error) {
    console.error('Error deleting todo:', error);
    res.status(500).json({
      success: false,
      message: 'An error occurred while deleting the todo',
      error: error.message
    });
  }
}; 