package application;

import javafx.beans.property.SimpleStringProperty;

public class Program {
   private final SimpleStringProperty programName = new SimpleStringProperty("");
   private final SimpleStringProperty programDirectory = new SimpleStringProperty("");
   
public Program() {
        this("", "");
    }
 
    public Program(String programName, String programDirectory) {
        setProgramName(programName);
        setProgramDirectory(programDirectory);
    }

    public String getProgramName() {
        return programName.get();
    }
 
    public void setProgramName(String fName) {
        programName.set(fName);
    }
        
    public String getProgramDirectory() {
        return programDirectory.get();
    }
    
    public void setProgramDirectory(String fName) {
        programDirectory.set(fName);
    }
}
