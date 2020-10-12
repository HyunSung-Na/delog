package com.devlog.delog.error;

public class EmailNotExistedException extends RuntimeException {

    public EmailNotExistedException(String email) {
        super("Email is not registered: " + email);
    }

}
