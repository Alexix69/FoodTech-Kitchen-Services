package com.foodtech.kitchen.application.exceptions;

public class InvalidTaskTransitionException extends RuntimeException {

    public InvalidTaskTransitionException(Long taskId, String currentStatus) {
        super("Task " + taskId + " cannot transition from status: " + currentStatus);
    }
}
