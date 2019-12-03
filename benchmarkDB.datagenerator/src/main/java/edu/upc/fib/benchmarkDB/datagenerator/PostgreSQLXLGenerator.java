package edu.upc.fib.benchmarkDB.datagenerator;

import java.io.IOException;
import java.util.Date;

import edu.upc.fib.benchmarkDB.util.reader.PlainFileReader;
import edu.upc.fib.benchmarkDB.util.writer.PlainFileWriter;
import javafx.util.Pair;

public class PostgreSQLXLGenerator extends PostgreSQLGenerator {

	protected String baseCreateTemplate ="CREATE TABLE $Table (@Columns), DISTRIBUTE BY HASH($DistributionColumn);"; 
	@Override
	protected String getCreateTableStatement() {
		String distributionColumn = getDistributionColumn();
		String template = baseCreateTemplate.replace("$DistributionColumn", distributionColumn);
		return template;
	}
	
	public PostgreSQLXLGenerator(String sourceDirectoryPath, String destinationDirectoryPath,
			String configurationDirectoryPath) {
		super(sourceDirectoryPath, destinationDirectoryPath, configurationDirectoryPath);
	}
	
	public void determinateStatisticsPath(String statisticsPath) {
		PlainFileReader oReader = new PlainFileReader();
		try {
			String line = oReader.read();
			while(line!=null ) {
				String[] lineSet = line.split("\t");
				line = oReader.read();
			} 
			oReader.closeConnection();
		} catch (IOException e) { 
			e.printStackTrace();
		}

	}
	
	public String getDistributionColumn() {
		
		return "";
	}

}
