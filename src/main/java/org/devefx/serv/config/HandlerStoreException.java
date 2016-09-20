package org.devefx.serv.config;

import org.springframework.core.NestedRuntimeException;

public class HandlerStoreException extends NestedRuntimeException {

    public HandlerStoreException(String msg) {
        super(msg);
    }

    public HandlerStoreException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
