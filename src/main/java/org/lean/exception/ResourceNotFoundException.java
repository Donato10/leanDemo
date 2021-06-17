package org.lean.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Class clazz, Long id) {
        super(String.format("Unable to find any %s identified by the ID %s", clazz.getSimpleName(), id));
    }
}
