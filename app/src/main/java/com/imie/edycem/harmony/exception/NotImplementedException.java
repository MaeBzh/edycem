/*
 * NotImplementedException.java, Edycem Android
 *
 * Copyright 2019
 * Description : 
 * Author(s)   : Harmony
 * Licence     : 
 * Last update : Jul 2, 2019
 *
 */
package com.imie.edycem.harmony.exception;

/**
 * Exception thrown when a feature hasn't been implemented.
 */
public class NotImplementedException extends RuntimeException {
    /**
     * Serial Version UID.
     */
    private static final long    serialVersionUID    = 6531619427489977103L;

    /**
     * Constructor.
     * @param message The exception message
     */
    public NotImplementedException(String message) {
        super(message);
    }

    /**
     * Constructor.
     * @param message The exception message
     * @param throwable The associated throwable
     */
    public NotImplementedException(String message, Throwable throwable) {
        super(message, throwable);
    }
}

