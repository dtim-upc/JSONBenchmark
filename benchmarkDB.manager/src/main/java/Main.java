import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import edu.upc.fib.benchmarkDB.manager.MongoDBManager;
import edu.upc.fib.benchmarkDB.manager.PostgreSQLManager;
import edu.upc.fib.benchmarkDB.manager.StatsGenerator;
import edu.upc.fib.benchmarkDB.util.EnumTestType;
import edu.upc.fib.benchmarkDB.util.MetricsManager;
import edu.upc.fib.benchmarkDB.util.reader.PlainFileReader;

public class Main {

	public static void main(String[] args) {
		if(args.length>4) {
			execute(args);
		}else if (args[0].contains("statsConsolidated")) {
			String  directoryPath = args[1];
			String destinationDirectoryPath = args[2];
			String rawDataDirectoryPath = args[3];
			MetricsManager oMetrics = new MetricsManager(destinationDirectoryPath);
			oMetrics.consolidate(directoryPath,rawDataDirectoryPath);
		}else if(args[0].contains("batchFile")) {  
			PlainFileReader oReader = new PlainFileReader();
			Main oMain = new Main();
			File file = null;
			if(args[0].contains("=")) {
				String pathFile = args[0].split("=")[1];
				file = new File(pathFile);
			}else {
				file=new File(
						oMain.getClass().getClassLoader().getResource("batch.execution").getFile()
						);
			}
			
			try {
				oReader.openConnection(file.getAbsolutePath());
				String line =oReader.read(); 
				
				while(line!=null) {   
					execute(line.split(" "));
					line =oReader.read();
				}
			} catch (IOException e) { 
				e.printStackTrace();
			}
		} 
	}
	private static void execute(String[] args) {
		System.out.println("\n\n" + Arrays.toString(args)+"\n");
		String testCase = args[0]; 
		EnumTestType testType = EnumTestType.valueOfCod(testCase);
		String operationType = args[1]; //-insertBulk, -insertShell, fullTableScan
		String database = args[2];
		String path = args[3];//Source filepath
		String metricsPath ="";//filepath
		if(args.length>4) {
			metricsPath = args[4];
		}
		String indexPath = "";//filepath
		String serverSuffix="";		 
		String tablesPath = "";
		boolean createDatabase=false;
		if(database.contains("postgresql")) { 
			if(args[3].contains("dropDatabase")) {
				PostgreSQLManager oManager = new PostgreSQLManager(serverSuffix,indexPath,createDatabase,tablesPath);
				oManager.dropDatabase();
			}
			else 
			{
				for (int i = 0; i < args.length; i++) {
					if(args[i].contains("createTable")) {
						createDatabase = true;
						tablesPath = args[i].split("=")[1];
						break;
					}
				}
				
				if(args.length >5) {
					serverSuffix = args[5];
				}
				if(operationType.contains("insertBulk")) {
					for (int i = 0; i < args.length; i++) {
						if(args[i].contains("indexPath")) {
							indexPath = args[i].substring(args[i].indexOf("="), args[i].length()).replace("=", "").trim();
							break;
						}
					}
					PostgreSQLManager oManager = new PostgreSQLManager(serverSuffix,indexPath,createDatabase,tablesPath);
					oManager.insertBulk(path,metricsPath,testType);
				}
				else if(operationType.contains("fullTableScan")) {
					PostgreSQLManager oManager = new PostgreSQLManager(serverSuffix,"",true,""); 
					oManager.executeFullScanQuery(path,metricsPath,testType);
				}
			}
		}else if(database.contains("mongodb")) {
			if(args.length >5) {
				serverSuffix = args[5];
			}
			if(operationType.contains("insertBulk")) {					
				MongoDBManager oManager = new MongoDBManager(serverSuffix);
				oManager.insertBulk(path,metricsPath,testType);
			}else if(operationType.contains("fullTableScan")) { 
				MongoDBManager oManager = new MongoDBManager(serverSuffix);
				oManager.executeFullScanQuery(path,metricsPath,testType);
			}
		}else if (args[0].contains("stats")) {
			String sourceFolderPath=args[1];
			String destinationFolderPath=args[2];
			StatsGenerator oGenerator = new StatsGenerator(sourceFolderPath, destinationFolderPath);
			oGenerator.generate();			
		}
		System.out.println("finished.");
	}

}
