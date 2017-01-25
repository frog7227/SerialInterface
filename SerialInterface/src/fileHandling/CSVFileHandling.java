package fileHandling;
import java.io.FileWriter;
import java.io.IOException;

import guiBlock.GUI;

public class CSVFileHandling {
private static final char Deliminator = ',';

static FileWriter fileWriter = null;
public static void WriteFile(String Filename){
	
	try {
		if (Filename.indexOf(".") > 0)
		Filename = Filename.substring(0, Filename.lastIndexOf("."));
		System.out.println(Filename + ".csv");
		fileWriter = new FileWriter(Filename + ".csv");
		AppendCSV("Display One,Display Two");
	} catch (IOException e1) {
		System.out.println("Error: File could not be created.");
		GUI.errorDialog("File could not be created.");
		e1.printStackTrace();
	}
	
}
public static void AppendCSV(String DataIn){
	try {
		fileWriter.append(DataIn);
		fileWriter.append(Deliminator);
		fileWriter.append("\r\n"); 
		
	} catch (IOException e) {
		System.out.println("Error: File could not be Appended.");
		GUI.errorDialog("Unable to append file.");
		e.printStackTrace();
	}
}
public static void SaveCSV(){
	try {
		fileWriter.flush();
		fileWriter.close();
	}catch (IOException e) {
		System.out.println("Error while flushing or closing the fileWriter.");
		GUI.errorDialog("Unable to close or flush the fileWriter.");
		e.printStackTrace();
		}
	}



}
