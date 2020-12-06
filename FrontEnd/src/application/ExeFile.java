package application;

import java.io.IOException;

public class ExeFile extends FileType
{
	@Override
	public boolean validFileExtension(String dir)
	{
		return dir.contains(".exe");
	}

	@Override
	public void execute(String dir)
	{
		try
        { 
            Runtime run  = Runtime.getRuntime(); 
            Process proc = run.exec(dir); 
        } 
  
        catch (IOException e) 
        { 
            e.printStackTrace(); 
        } 
	}

}
