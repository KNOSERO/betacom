package com.ravcube.betacom.domain.note;

public class InvalidNoteException extends RuntimeException {

    public InvalidNoteException(String message) {
        super(message);
    }
}
