package se.vgr.webbisar.helpers;

public class FTPException extends Exception {

	private static final long serialVersionUID = -7030030707796612059L;

	public FTPException(String message, Throwable cause) {
		super(message, cause);
	}

	public FTPException(String message) {
		super(message);
	}

}
