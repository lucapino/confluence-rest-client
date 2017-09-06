package dk.mikkelrj.confluence.rest.core;

public class RequestException extends Exception {

	private static final long serialVersionUID = -7465668504532227607L;

	public RequestException(Exception e) {
		super(e);
	}

}
