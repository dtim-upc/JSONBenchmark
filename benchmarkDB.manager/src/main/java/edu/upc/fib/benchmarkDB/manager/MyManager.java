package edu.upc.fib.benchmarkDB.manager;

import edu.upc.fib.benchmarkDB.util.EnumTestType;

public interface MyManager {
	void insertBulk(String sourceDirectoryPath, String destinationMetricsFile,EnumTestType testType); 
	void executeFullScanQuery(String sourceDirectoryPath,String destinationDirectoryPath,EnumTestType testType);
	double getSizeMB(String tableName); 
}
