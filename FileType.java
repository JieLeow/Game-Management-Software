package application;

public abstract class FileType
{
	protected static FileType instance;
    public static FileType getInstance(Class cls) {
        try
        {
            instance = (FileType)cls.newInstance();
        } catch (InstantiationException e)
        {
            e.printStackTrace();
        } catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }

        return instance;
    }
    
    public abstract void execute(String dir);
    
    public abstract boolean validFileExtension(String dir);
    
    
    
}
