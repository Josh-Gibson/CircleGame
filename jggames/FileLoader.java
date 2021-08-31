package jggames;

import java.util.Scanner;
import javax.swing.JOptionPane;
import java.io.*;

class FileLoader{
public Scanner reader;
int i;

boolean enablePhysicsCollisions;
String gamemode;
int circleAmount;
int circleRadius;
double speed;
double circleSpacing;
boolean collideWithDead;
boolean timerEnabled;
int timeMinutes;

int OPTIONS_AMOUNT = 8;

String[] optionValues = new String[OPTIONS_AMOUNT];
	public FileLoader(){
		i = 0;
	}
	
	public File setFile(File file){
		return file;
	}
	public void readFile(String filename){
		String[] line;
		try{
			reader = new Scanner(new File(filename));
		}
		catch(FileNotFoundException e){
			JOptionPane.showMessageDialog(null, "Programmer fucked up: \n" + e);
			System.out.println("Aw fuck:" + e);
			System.exit(0);
		}		
		while(reader.hasNextLine()){
			line = reader.nextLine().split(":");
			try{
				
				optionValues[i] = line[1];
				i++;	
			}
			catch(ArrayIndexOutOfBoundsException e){
				e.printStackTrace();
			}
			
		}
		setOptions();
	}
	public void setOptions(){ //hahaha this is totally what I wanted :D
		
			gamemode = optionValues[0];
			enablePhysicsCollisions = Boolean.parseBoolean(optionValues[1]);
			circleAmount = Integer.parseInt(optionValues[2]);
			circleRadius = Integer.parseInt(optionValues[3]);
			circleSpacing = Double.parseDouble(optionValues[4]);
			speed = Double.parseDouble(optionValues[5]);
			timerEnabled = Boolean.parseBoolean(optionValues[6]);
			timeMinutes = Integer.parseInt(optionValues[7]);
			
			
	}
}
