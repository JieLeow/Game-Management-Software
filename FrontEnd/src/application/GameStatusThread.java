package application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GameStatusThread implements Runnable 
{ 
    public void run() 
    { 
        try
        { 
            while (HomePageController.threadActive){
            	if(checkGameStatus()){
            		//change icon to green
            		System.out.println("Game is active");
            	}
            	else {
            		//change icon to red
            		System.out.println("No games are running");

            	}
            	Thread.sleep(1000);
            }
            System.out.println("Thread stopped");
            
            synchronized(HomePageController.gameStatusThread) {
            	HomePageController.gameStatusThread.wait();
            }
           
        } 
        catch (Exception e) 
        { 
            // Throwing an exception 
            e.printStackTrace();
        } 
    } 
    
    public boolean checkGameStatus(){
    	
    	  if (HomePageController.data != null){
//    	    
    		  for(Program file: HomePageController.data) {
    			  file.getProgramName();

    			  String program = file.getProgramName();   //or any other process
    			  String listOfProcesses = getCommandOutput("tasklist");
    			  if (listOfProcesses == null || listOfProcesses.isEmpty()) {
    				  System.err.println("Unable to automatically determine if " + program + " is running");
    			  } else {
    				  if (listOfProcesses.contains(program)) {
    					  System.out.println(program + " ISSSS RUNNINGGGGGG!");
    					  return true;
    				  } else {
    					  System.out.println(program + " is not running!");
    					 
    				  }
    			  }//else: process list can be retrieved
    		  }
    	    }
    	  	return false;
    	}

    	public String getCommandOutput(String command)  {
    	    String output = null;       //the string to return

    	    Process process = null;
    	    BufferedReader reader = null;
    	    InputStreamReader streamReader = null;
    	    InputStream stream = null;

    	    try {
    	        process = Runtime.getRuntime().exec(command);

    	        //Get stream of the console running the command
    	        stream = process.getInputStream();
    	        streamReader = new InputStreamReader(stream);
    	        reader = new BufferedReader(streamReader);

    	        String currentLine = null;  //store current line of output from the cmd
    	        StringBuilder commandOutput = new StringBuilder();  //build up the output from cmd
    	        while ((currentLine = reader.readLine()) != null) {
    	            commandOutput.append(currentLine + "\n");
    	        }

    	        int returnCode = process.waitFor();
    	        if (returnCode == 0) {
    	            output = commandOutput.toString();
    	        }

    	    } catch (IOException e) {
    	        System.err.println("Cannot retrieve output of command");
    	        System.err.println(e);
    	        output = null;
    	    } catch (InterruptedException e) {
    	        System.err.println("Cannot retrieve output of command");
    	        System.err.println(e);
    	    } finally {
    	        //Close all inputs / readers

    	        if (stream != null) {
    	            try {
    	                stream.close();
    	            } catch (IOException e) {
    	                System.err.println("Cannot close stream input! " + e);
    	            }
    	        } 
    	        if (streamReader != null) {
    	            try {
    	                streamReader.close();
    	            } catch (IOException e) {
    	                System.err.println("Cannot close stream input reader! " + e);
    	            }
    	        }
    	        if (reader != null) {
    	            try {
    	                reader.close();
    	            } catch (IOException e) {
    	                System.err.println("Cannot close reader! " + e);
    	            }
    	        }
    	    }
    	    //Return the output from the command - may be null if an error occured
    	    return output;
    	}

} 
