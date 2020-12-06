package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class DataManagement {

	public static String path = "GamesLocation.csv";
	
	public static void createGamesPathFile(String user) throws IOException{
		String loc = user + ".csv";
		FileWriter csvWriter = new FileWriter(loc);
		csvWriter.append("Game");
		csvWriter.append(",")
		csvWriter.append("GamePath");
		csvWriter.append("\n");
		csvWriter.flush();
		csvWriter.close();
	}
	
	
	public static void addGame(String user, String location, String game) throws DuplicatePathException, IOException {
		if(user != null) {
			String loc = user + ".csv";
			File csvFile = new File(loc);
			if (csvFile.isFile()) {
				BufferedReader csvReader = new BufferedReader(new FileReader(loc));
				String row;
				csvReader.readLine();
				while ((row = csvReader.readLine()) != null) {
					String[] data = row.split(",");
					String gamePath = data[1];
					if(gamePath.equals(location)) {
						throw new DuplicatePathException("File Path already added");
					}
				}
				csvReader.close();
				
			}
			FileWriter csvWriter = new FileWriter(loc, true);
			csvWriter.append(game);
			csvWriter.append(",");
			csvWriter.append(location);
			csvWriter.append("\n");
			
			csvWriter.flush();
			csvWriter.close();
		}
	}
	
	public static void deleteGame(String user, String location) throws IOException {
		if(user != null) {
			String loc = user + ".csv";
			File csvFile = new File(loc);
			if (csvFile.isFile()) {
				BufferedReader csvReader = new BufferedReader(new FileReader(loc));
				String row;
				csvReader.readLine();
				ArrayList<String> gameList = new ArrayList<String>();
				while ((row = csvReader.readLine()) != null) {
					String[] data = row.split(",");
					String gamePath = data[1];
					if(!gamePath.equals(location)) {
						gameList.add(row);
						
					}
				}
				csvReader.close();
				FileWriter csvWriter = new FileWriter(loc);
				csvWriter.append("Game");
				csvWriter.append(",");
				csvWriter.append("GamePath");
				csvWriter.append("\n");
				for (int i = 0; i < gameList.size(); i++) {
					csvWriter.append(gameList.get(i));
					csvWriter.append("\n");
					
				}
				csvWriter.flush();
				csvWriter.close();
			}
		}
	}
}
