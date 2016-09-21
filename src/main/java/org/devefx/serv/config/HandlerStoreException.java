package org.devefx.serv.config;

import org.springframework.core.NestedRuntimeException;

public class HandlerStoreException extends NestedRuntimeException {

	private static final long serialVersionUID = -7111268437493869239L;

	public HandlerStoreException(String msg) {
        super(msg);
    }

    public HandlerStoreException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
