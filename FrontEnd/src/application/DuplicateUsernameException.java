package application;

public class DuplicateUsernameException extends Exception
{
	public DuplicateUsernameException(String errorMessage) {
		super(errorMessage);
	}
}
