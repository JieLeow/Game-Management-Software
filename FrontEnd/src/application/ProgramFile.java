package application;

public class ProgramFile
{	
	private String directory;
	private FileType fileType;

	private static final FileType[] supportedFileTypes = { FileType.getInstance(ExeFile.class), FileType.getInstance(JarFile.class) };

	public ProgramFile(String dir) throws UnsupportedFileExtension{
		directory = dir;

		for (FileType file: supportedFileTypes) {
			if (file.validFileExtension(dir)) {
				fileType = file;
				return;	
			}
		}
		throw new UnsupportedFileExtension("Unsupported file extension");
	}
	public String getDirectory() {
		return directory;
	}

	public void execute() {
		fileType.execute(directory);
	}

	public void terminate(){
	  	fileType.terminate(directory);
	  }
	
	public static boolean validFileExtension(String dir) {
		for (FileType file: supportedFileTypes) {
			
			if (file.validFileExtension(dir)) {	
				return true;
			}
		}
		return false;
	}
}