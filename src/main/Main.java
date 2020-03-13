package main;

import userInteraction.ShipFrameApp;
/*
 *    Plik: Main.java
 *          
 *   Autor: Miron Oskroba
 *    Data: listopad 2019 r.
 */
public class Main {
	/*
	 * 		ShipFrameApp is small Java Swing database window app which aims to manage basic Ship data. 
	 * 		Graphic user interface allows user to add or remove ships by clicking buttons and using textfields, save its data and check parameters of any ships user adds.
	 * 		creates folder Ships in a directory where .txt files are stored - every ship has its own file
	 * 		Aims to save data in .txt files between sessions - reads .txt files at startup, shows ships in a table when app runs
	 */
	
	public static void main(String[] args) {
		new ShipFrameApp();
	}
}
