package application;

public class UnsupportedFileExtension extends Exception
{
	public UnsupportedFileExtension(String errorMessage) {
		super(errorMessage);
	}
}