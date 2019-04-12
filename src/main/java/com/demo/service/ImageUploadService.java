package com.demo.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import com.demo.misc.AuxiliaryHelper;
import com.demo.misc.WriteSpreadsheet;
import com.demo.misc.WriteText;
import com.demo.pojo.Employee;
import com.demo.pojo.Employees;
import com.demo.pojo.ImageAttribute;

@Path("/service")
public class ImageUploadService {

	//Generic GET test
	private static String ROOT_PATH_ON_MAC = "/tmp";
	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String getTestService() {
		return "Hello World!";
	}

	//Receives image/file as an input and does validation on folders before writing the file to /tmp.
	@POST
	@Path("/publishimage")
	@Consumes({ MediaType.MULTIPART_FORM_DATA })
	public Response uploadImageFile(@FormDataParam("file") InputStream fileInputStream,
			@FormDataParam("file") FormDataContentDisposition fileMetaData) throws Exception {
		String TEMP_PATH_ON_MAC = "/tmp";
		writeFileToServer(fileInputStream, fileMetaData, TEMP_PATH_ON_MAC);
		return Response.ok().build();
	}

	//This will return the size/name of the most recently uploaded image to disk from above POST call
	@GET
	@Path("/imageattribs")
	@Produces(MediaType.APPLICATION_JSON)
	public ImageAttribute getAttribute() {
		String directoryPath = ROOT_PATH_ON_MAC + "/" + AuxiliaryHelper.loadServerProperties().getProperty("image.folder") + "/" ;
		File folder = new File(directoryPath);
		File[] files = folder.listFiles();
		String name = "";
		long space = 0l;
		if(files.length > 0) {
			name = files[0].getName();
			space = files[0].getTotalSpace();
		}
		ImageAttribute imageAttribute = new ImageAttribute();
		imageAttribute.setName(name);
		imageAttribute.setSize(space);
		return imageAttribute;

	}


	//This will publish the incoming Employee XML payload back to the client as JSON with validation on name/DOB - Single Employee Payload
	@POST
	@Path("/publishxml")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Response consumeXML( Employee employee ) {

		employeeValidation(employee);
		return Response.status(200).entity(employee).build();
	}

	private void employeeValidation(Employee employee) {
		if(!employee.getFirstName().trim().matches("[a-zA-Z]+")) {
			employee.setFirstName("Fails Alpha only test");
		}

		if(!employee.getLastName().trim().matches("[a-zA-Z]+")) {
			employee.setLastName("Fails Alpha only test");
		}

		if(!isValidDate(employee.getDateOfBirth().trim())) {
			employee.setDateOfBirth("Fails formatting condition of MM\\DD\\YYYY");
		}
	}
	
	
	//This will publish the incoming Employee XML payload back to the client as JSON with validation on name/DOB - Multiple Employee Payload
	@POST
	@Path("/publishmultixml")
	@Consumes(MediaType.APPLICATION_XML)
	@Produces(MediaType.APPLICATION_JSON)
	public Response consumeXML( Employees employees ) {
		WriteSpreadsheet writeSpreadsheet = new WriteSpreadsheet();
		writeSpreadsheet.writeSpreadsheetInitialize();
		WriteText writeText = new WriteText();
		writeText.writeTextInitialize();
		scrubPayloadAndThenWriteToCSVTXT(employees,writeSpreadsheet,writeText);

	return Response.status(200).entity(employees).build();
	}

	private void scrubPayloadAndThenWriteToCSVTXT(Employees employees, WriteSpreadsheet writeSpreadsheet,WriteText writeText) {
		
		for(Employee employee : employees.getEmployees()) {
			employeeValidation(employee);
			writeSpreadsheet.write(employee.getFirstName(), employee.getLastName(), employee.getDateOfBirth());
			writeText.write(employee.getFirstName(), employee.getLastName(), employee.getDateOfBirth());
		}
	}

	boolean isValidDate(String input) {
		if (input.matches("\\d{2}/\\d{2}/\\d{4}")) {
			return true;
		}
		return false;
	}

	private void writeFileToServer(InputStream fileInputStream, FormDataContentDisposition fileMetaData,
			String ROOT_PATH_ON_MAC) {
		try {
			int read = 0;
			byte[] bytes = new byte[1024];

			String sourceFolderFullPath = ROOT_PATH_ON_MAC + "/" + 
					AuxiliaryHelper.loadServerProperties().getProperty("image.folder") + "/" + fileMetaData.getFileName();

			String archiveFolderFullPath = ROOT_PATH_ON_MAC + "/" + 
					AuxiliaryHelper.loadServerProperties().getProperty("archive.folder") + "/" + fileMetaData.getFileName();

			doesFileAlreadyExist(sourceFolderFullPath, archiveFolderFullPath);

			File sourceFile = new File(sourceFolderFullPath);
			OutputStream out = new FileOutputStream(sourceFile);
			while ((read = fileInputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
		} catch (Throwable e) {
			throw new WebApplicationException("Error while uploading image. Please try again");
		}
	}

	private void doesFileAlreadyExist(String sourceFolderFullPath, String archiveFolderFullPath) {
		File tempFile = new File (sourceFolderFullPath);
		if ( tempFile.exists()) {
			tempFile.renameTo(new File(archiveFolderFullPath));
		}
	}
}
