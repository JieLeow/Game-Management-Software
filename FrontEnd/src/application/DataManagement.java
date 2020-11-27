package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class DataManagement {

	public static String path = "GamesLocation.csv";
	
	public static void createGamesPathFile(String user) throws IOException{
		String loc = user + ".csv";
		FileWriter csvWriter = new FileWriter(loc);
		csvWriter.append("GamePath");
		csvWriter.append("\n");
		csvWriter.flush();
		csvWriter.close();
	}
	
	
	public static void addGame(String user, String location) throws IOException{
		if(!(user.equals(null))) {
			String loc = user + ".csv";
			File csvFile = new File(loc);
			if (csvFile.isFile()) {
				BufferedReader csvReader = new BufferedReader(new FileReader(loc));
				String row;
				csvReader.readLine();
				while ((row = csvReader.readLine()) != null) {
					if(row.equals(location)) {
						throw new IOException("File Path already exists in CSV");
					}
				}
				csvReader.close();
			}
			FileWriter csvWriter = new FileWriter(loc, true);
			csvWriter.append(location);
			csvWriter.append("\n");

			csvWriter.flush();
			csvWriter.close();
		}
	}
	public static void deleteGame(String user, String location) throws IOException{
		File csvFile = new File(path);
		File tempCSV = new File(csvFile.getAbsolutePath() + ".tmp");
		if (csvFile.isFile()) {
			BufferedReader csvReader = new BufferedReader(new FileReader(csvFile));
			BufferedWriter csvWriter = new BufferedWriter(new FileWriter(csvFile));
			
			String row;
			//csvReader.readLine();
			while ((row = csvReader.readLine()) != null) {
				String[] data = row.split(",");
				if(data[0].equals(user) && data[1].equals(location)) continue; {
					csvWriter.write(row);
				}
			}
			csvWriter.close();
			csvReader.close();
		}
		boolean success = tempCSV.renameTo(csvFile);
		System.out.println(success);
		
	}
}
