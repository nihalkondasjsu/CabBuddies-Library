package com.cabbuddieslib.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Basic {

	public static void successPrint() {
		File file = new File("success.txt"); 
		  
		  BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		  
		  String st; 
		  try {
			while ((st = br.readLine()) != null) { 
			    System.out.println(st); 
			  }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
}
