package com.darpan.realtimemultiplayerquiz.exception;

/**
 * Exception thrown when a player is not found.
 */
public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(String message) {
        super(message);
    }

    public PlayerNotFoundException(int playerId) {
        super("Player not found with ID: " + playerId);
    }

    public PlayerNotFoundException(String field, Object value) {
        super("Player not found with " + field + ": " + value);
    }
}
