package org.devefx.serv;

public final class MessageException extends Exception {
	private static final long serialVersionUID = 9195109867348186889L;
	protected String message;
    protected Throwable throwable;

    public MessageException() {
        this((String)null, (Throwable)null);
    }

    public MessageException(String message) {
        this(message, (Throwable)null);
    }

    public MessageException(Throwable throwable) {
        this((String)null, throwable);
    }

    public MessageException(String message, Throwable throwable) {
        this.message = null;
        this.throwable = null;
        this.message = message;
        this.throwable = throwable;
    }

    public String getMessage() {
        return this.message;
    }

    public Throwable getThrowable() {
        return this.throwable;
    }

    public String toString() {
        StringBuffer sb = new StringBuffer("MessageException:  ");
        if(this.message != null) {
            sb.append(this.message);
            if(this.throwable != null) {
                sb.append(":  ");
            }
        }

        if(this.throwable != null) {
            sb.append(this.throwable.toString());
        }

        return sb.toString();
    }
}