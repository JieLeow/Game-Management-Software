package application;


import java.io.InputStream;

public class JarFile extends FileType
{

	@Override
	public boolean validFileExtension(String dir)
	{
		return dir.contains(".jar");
	}

	@Override
	public void execute(String dir)
	{
		try {
            Process proc = Runtime.getRuntime().exec("java -jar " + dir);
            proc.waitFor();
            // Then retreive the process output
            InputStream in = proc.getInputStream();
            InputStream err = proc.getErrorStream();

            byte b[]=new byte[in.available()];
            in.read(b,0,b.length);
            System.out.println(new String(b));

            byte c[]=new byte[err.available()];
            err.read(c,0,c.length);
            System.out.println(new String(c));    

        }
        catch (Exception e) {
            e.printStackTrace();
        }
	}
	

}
