package de.traviadan.lib.helper;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Properties;

public class AppProps extends Properties {

	private static final long serialVersionUID = 1L;
	private String fName;
	
	private AppProps() {
		super();
	}
	
	public static AppProps getInstance() {
		try {
			return getInstance("properties.ini");
		} catch (IOException e) {
			return new AppProps();
		}
	}

	public static AppProps getInstance(String fileName) throws IOException {
		AppProps ap = new AppProps();
		ap.loadProperties(fileName, ap);
		return ap;
	}
	
	public void save() throws IOException {
		saveProperties(fName, this);
	}
	
	private void loadProperties(String fileName, AppProps ap) throws IOException {
		fName = fileName;
		Reader reader = Files.newBufferedReader( Paths.get( System.getProperty("user.dir"), fileName ), StandardCharsets.UTF_8);
		ap.load(reader);
		reader.close();
	}
	
	private void saveProperties(String fileName, AppProps ap) throws IOException {
		Writer writer = Files.newBufferedWriter( Paths.get( System.getProperty("user.dir"), fileName ), StandardCharsets.UTF_8, 
				StandardOpenOption.CREATE);
		ap.store(writer, null);
		writer.close();
	}
}
