package com.demo.misc;

import java.io.FileWriter;
import java.io.IOException;

public class WriteSpreadsheet {
	FileWriter fw = null;
	
	public void write(String firstName,String lastName,String dob) {
		try {
			fw.append(firstName);
			fw.append(",");
			fw.append(lastName);
			fw.append(",");
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
	
	public void writeSpreadsheetInitialize() {
		try {
			if (fw == null) {
				fw = new FileWriter("/tmp/"+AuxiliaryHelper.loadServerProperties().getProperty("xls.name"),true);
				write("FirstName","LastName","DateOfBirth");
			}
		}catch (IOException ex) {
			ex.printStackTrace();
		}
	}

}
