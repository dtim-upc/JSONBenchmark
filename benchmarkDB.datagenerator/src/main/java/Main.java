import edu.upc.fib.benchmarkDB.datagenerator.JSONGenerator;
import edu.upc.fib.benchmarkDB.datagenerator.PostgreSQLGenerator;

public class Main {
	public static void main(String[] args) {
		// [0]=DB or JSON, Create Only
		// [1]=encoded
		// [2]=source data directory
		// [3]=destination data directory
		// [4]=config files
		// [5]=keyvalue
		try {
			if(args.length<5) {
				throw new Exception("Number of parameter does not allowed.");
			}

			boolean encode = false;
			boolean keyvalue = false;
			boolean includePK = false;

			String sourceDirectoryPath=args[2];
			String destinationDirectoryPath = args[3];
			String configurationDirectoryPath = args[4];
			
			if(args[1].contains("-encode")) {
				encode=true;
			}
			if(args.length>5) {
				if(args[5].equals("-keyvalue")) {
					keyvalue=true;
				} 
				if(args[5].equals("-includepk")) {
					includePK=true; 
				} 
			}
			if(args.length>6) { 
				if(args[6].equals("-includepk") ) {
					includePK=true;
				}
			}
			if (args[0].equals("-postgreSQLCreate")) {
				PostgreSQLGenerator oGenerator = new PostgreSQLGenerator(sourceDirectoryPath, destinationDirectoryPath, configurationDirectoryPath);
				oGenerator.setKeyValue(keyvalue);
				oGenerator.useJSONB();
				oGenerator.generateCreateStatement(keyvalue,includePK);					
			}else if (args[0].equals("-json")) {
				JSONGenerator oGenerator = new JSONGenerator(sourceDirectoryPath, destinationDirectoryPath, configurationDirectoryPath, encode);
				oGenerator.generateFiles();
			}else if (args[0].equals("-postgreSQL")) {				 
				PostgreSQLGenerator oGenerator = new PostgreSQLGenerator(sourceDirectoryPath, destinationDirectoryPath, configurationDirectoryPath);
				oGenerator.setKeyValue(keyvalue);
				oGenerator.setEncode(encode);
				oGenerator.generateFiles();
			}else if (args[0].equals("-postgreSQLCreatePK")) {
				PostgreSQLGenerator oGenerator = new PostgreSQLGenerator(sourceDirectoryPath, destinationDirectoryPath, configurationDirectoryPath);
				oGenerator.setKeyValue(keyvalue);
				oGenerator.createPKFile();
				 
			}else {
				throw new Exception("First parameter does not supported. Possible values: json, postgreSQL, mongoDB");				
			}
		
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
