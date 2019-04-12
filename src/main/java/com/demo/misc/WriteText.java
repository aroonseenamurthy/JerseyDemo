package com.demo.misc;

import java.io.FileWriter;
import java.io.IOException;

public class WriteText {
	FileWriter fw = null;
	
	public void write(String firstName,String lastName,String dob) {
		try {
			fw.append(firstName);
			fw.append("|");
			fw.append(lastName);
			fw.append("|");
			fw.append(dob);
			fw.append("\n");
			fw.flush();
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	void close() throws IOException {
		fw.close();
	}
	
	public void writeTextInitialize() {
		try {
			if (fw == null) {
				fw = new FileWriter("/tmp/"+AuxiliaryHelper.loadServerProperties().getProperty("output.file"),true);
				write("FirstName","LastName","DateOfBirth");
			}
		}catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
