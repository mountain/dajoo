package org.dajoo.kernel;

public class DaemonException extends Exception {

	public DaemonException() {
		super();
	}

    public DaemonException(String message) {
    	super(message);
    }

    public DaemonException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaemonException(Throwable cause) {
        super(cause);
    }

}
