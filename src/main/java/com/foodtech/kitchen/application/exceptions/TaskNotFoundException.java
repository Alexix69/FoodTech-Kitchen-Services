package com.foodtech.kitchen.application.exceptions;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(Long taskId) {
        super("Task not found with id: " + taskId);
    }
}
