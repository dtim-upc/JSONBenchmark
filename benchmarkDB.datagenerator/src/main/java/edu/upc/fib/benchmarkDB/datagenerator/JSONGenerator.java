package edu.upc.fib.benchmarkDB.datagenerator;

import java.io.IOException;
import java.io.File;

import edu.upc.fib.benchmarkDB.util.Util;
import edu.upc.fib.benchmarkDB.util.reader.PlainFileReader;
import edu.upc.fib.benchmarkDB.util.writer.PlainFileWriter;

public class JSONGenerator extends BaseGenerator { 
	private String sourceDirectoryPath;
	private String destinationDirectoryPath;
	private String configurationDirectoryPath;
	private boolean encode=false;

	public JSONGenerator(String sourceDirectoryPath, String destinationDirectoryPath,String configurationDirectoryPath, boolean encode){
		this.sourceDirectoryPath=sourceDirectoryPath;
		this.destinationDirectoryPath=destinationDirectoryPath;
		this.configurationDirectoryPath = configurationDirectoryPath;	
		this.encode = encode;
	}	
	
	private void generateFile(String sourceFilePath, String destinationFilePath,String configurationPath) {
		PlainFileReader oReader = new PlainFileReader();
		PlainFileWriter oWriter = new PlainFileWriter();
		try {
			super.loadConfigurationColumns(configurationPath);
			if(encode) {
				super.encode();
			} 
			oReader.openConnection(sourceFilePath);
			oWriter.create(destinationFilePath);
			String line = oReader.read();
			int i=0;
			while(line!=null) {
				String[] elements = line.split("\\|");
				String value = super.getJSON(elements,true);
				value+="\n";
				line = oReader.read();
				oWriter.write(value);
				i++;
			}
			oWriter.flush();
			oReader.closeConnection();
		} catch (IOException e) { 
			e.printStackTrace();
		}
	}

	@Override
	public void generateFiles() {
		 
		try {
			File dir=new File(configurationDirectoryPath);
			File[] directoryListing = dir.listFiles();
			if (directoryListing != null) {
				 for (File child : directoryListing) {
					String fileName = child.getName().replace(".schm", "");
					String sourceFilePath = sourceDirectoryPath + Util.getConnectorPerOS() +fileName ;
					String destinationFilePath = destinationDirectoryPath + Util.getConnectorPerOS() + fileName + ".json";
					String configurationPath = child.getAbsolutePath();
					generateFile(sourceFilePath,destinationFilePath,configurationPath);
				 }
			}
			
		
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}

	

}
