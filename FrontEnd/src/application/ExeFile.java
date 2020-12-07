package application;

import java.io.File;
import java.io.IOException;

public class ExeFile extends FileType
{
	@Override
	public boolean validFileExtension(String dir)
	{
		return dir.contains(".exe");
	}

	@Override
	public void terminate(String dir){
		try {
			File f = new File(dir);
			Runtime.getRuntime().exec("taskkill /im " + f.getName()+ " /t /f");
			System.out.println(dir + " killed successfully!");
		} catch (IOException e) {
			e.printStackTrace();
		}
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