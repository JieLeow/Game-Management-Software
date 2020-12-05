package application;

import javafx.beans.property.SimpleStringProperty;

public class Program {
   private final SimpleStringProperty programName = new SimpleStringProperty("");
   private final SimpleStringProperty programDirectory = new SimpleStringProperty("");
   private final SimpleStringProperty gameStatus = new SimpleStringProperty("");
//   private final SimpleStringProperty hoursPlayed = new SimpleStringProperty("");

public Program() {
        this("", "", "");
    }
 
    public Program(String programName, String programDirectory, String gameStatus) {
        setProgramName(programName);
        setProgramDirectory(programDirectory);
        setGameStatus(gameStatus);
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
    
//    public String getHoursPlayed() {
//        return hoursPlayed.get();
//    }
//    
//    public void setHoursPlayed(String fName) {
//        hoursPlayed.set(fName);
//    }
    
    public String getGameStatus() {
        return gameStatus.get();
    }
    
    public void setGameStatus(String fName) {
        gameStatus.set(fName);
    }
    
}
