package org.imgcnv.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersistentException extends RuntimeException {
    /**
     * Persistent exception
     */
    private static final long serialVersionUID = 1L;

    /**
     * Logger for this class.
     */
    private final static Logger logger = LoggerFactory.getLogger(PersistentException.class);

    public PersistentException() {

    }

    public PersistentException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PersistentException(final String message) {
        super(message);
    }

    public PersistentException(final Throwable cause) {
        logger.error("{}:{}", cause.getStackTrace()[0].getMethodName(), cause.getMessage());
    }

}
