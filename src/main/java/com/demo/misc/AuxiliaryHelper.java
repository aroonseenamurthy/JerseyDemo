package com.demo.misc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.ws.rs.WebApplicationException;

public class AuxiliaryHelper {

	private static String SERVER_PROPERTIES = "file.properties";

	public static Properties loadServerProperties() {
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		Properties props = new Properties();
		try (InputStream resourcesStream = loader.getResourceAsStream(SERVER_PROPERTIES)) {
			props.load(resourcesStream);
		} catch (IOException ex) {
			throw new WebApplicationException("Property file loading issue");
		}
		return props;
	}

}
