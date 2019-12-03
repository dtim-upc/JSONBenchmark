package edu.upc.fib.benchmarkDB.drawer;

import java.io.BufferedReader; 
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map; 
import edu.upc.fib.benchmarkDB.drawer.object.ConsolidatedResults;
import edu.upc.fib.benchmarkDB.drawer.object.Query;
public class CSVReader {
	
	@SuppressWarnings("finally")
	public static List<Query> readCSV(String path) {
		BufferedReader br = null;
		List<Query> lstQuery= new ArrayList<Query>();
		int rowNumber=0; 
		String ln = "";
				
		try {
			
			br=new BufferedReader(new FileReader(path));
			while ((ln=br.readLine())!=null) 
			{
				Query objQuery = new Query();
				String[] fullRow = ln.split(";");
				if(rowNumber!=0) 
				{
					objQuery.setDatabase(fullRow[0]);
					objQuery.setObject(fullRow[1]);
					objQuery.setOperation(fullRow[2]);
					objQuery.setNumberRecords(Long.parseLong(fullRow[3]));
					objQuery.setTime(Long.parseLong(fullRow[6]));
					objQuery.setSize(Long.parseLong(fullRow[7]));
					objQuery.setMemoryAVG(Double.parseDouble(fullRow[8]));
					objQuery.setMemoryMAX(Double.parseDouble(fullRow[9]));
					if(objQuery.getTime()>0) {
						objQuery.setRecordsPerSecond(objQuery.getRecordsPerSecond() / objQuery.getTime());
					}
					lstQuery.add(objQuery);
				}
				
				rowNumber++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(br!=null) {
				try {
					br.close();
				}catch(IOException e){
					e.printStackTrace();
				}
				
			}
			return lstQuery;
		}
	}
	@SuppressWarnings("finally")
	public static List<ConsolidatedResults> readConsolidatedCSV(String path) {
		BufferedReader br = null;
		List<ConsolidatedResults> lstConsolidatedResults= new ArrayList<ConsolidatedResults>();
		int rowNumber=0; 
		String ln = "";
		 		
		try {
			
			br=new BufferedReader(new FileReader(path));
			while ((ln=br.readLine())!=null) 
			{
				ConsolidatedResults objConsolidatedResults = new ConsolidatedResults();
				String[] fullRow = ln.split(";");
				if(rowNumber!=0) 
				{
					objConsolidatedResults.setTestTypeCode(fullRow[0]);
					objConsolidatedResults.setOs(fullRow[1]);
					objConsolidatedResults.setTestTypeDetail(fullRow[2]);
					objConsolidatedResults.setHadPK(fullRow[3]);
					if(fullRow[4].trim().toLowerCase().equals("No")){
						objConsolidatedResults.setEncoded(false);	
					}else{
						objConsolidatedResults.setEncoded(true);							
					}
					objConsolidatedResults.setFormat(fullRow[5]);
					objConsolidatedResults.setDatastore(fullRow[6]);
					objConsolidatedResults.setObject(fullRow[7]);
					objConsolidatedResults.setRawSize(Double.parseDouble(fullRow[8]));
					objConsolidatedResults.setOperation(fullRow[9]);
					objConsolidatedResults.setNumberOfRecords((long)Double.parseDouble(fullRow[10]));
					objConsolidatedResults.setTime((long)(Double.parseDouble(fullRow[11])/1000d));
					objConsolidatedResults.setSizeMB(Double.parseDouble(fullRow[12]));
					objConsolidatedResults.setMemoryAvg(Double.parseDouble(fullRow[13]));
					objConsolidatedResults.setMemoryMax(Double.parseDouble(fullRow[14]));
					objConsolidatedResults.setRecordsPerSecond(Double.parseDouble(fullRow[15]));
					objConsolidatedResults.setMBperSecond(Double.parseDouble(fullRow[16]));
					lstConsolidatedResults.add(objConsolidatedResults);
				}
				
				rowNumber++;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			if(br!=null) {
				try {
					br.close();
				}catch(IOException e){
					e.printStackTrace();
				}
				
			}
			return lstConsolidatedResults;
		}
	}
	
	
}
