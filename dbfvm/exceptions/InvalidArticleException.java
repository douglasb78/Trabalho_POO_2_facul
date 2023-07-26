package dbfvm.exceptions;

public class InvalidArticleException extends Exception {
	private static final long serialVersionUID = 1L;

	public InvalidArticleException() {
        super();
    }
    public InvalidArticleException(String message) {
        super(message);
    }
}
