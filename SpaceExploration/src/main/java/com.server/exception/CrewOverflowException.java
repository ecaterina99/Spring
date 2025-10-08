package com.server.exception;

import lombok.Getter;

@Getter
public class CrewOverflowException extends RuntimeException {
    private final int currentSize;
    private final int maxSize;

    public CrewOverflowException(int currentSize, int maxSize) {
        super(String.format("Mission crew is full. Current: %d, Maximum: %d", currentSize, maxSize));
        this.currentSize = currentSize;
        this.maxSize = maxSize;
    }

}