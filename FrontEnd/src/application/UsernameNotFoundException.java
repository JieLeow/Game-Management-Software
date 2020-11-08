package application;

public class UsernameNotFoundException extends Exception
{
	public UsernameNotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
