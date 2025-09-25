package com.client.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceType, Object id) {
        super(String.format("%s with id '%s' not found", resourceType, id));
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}