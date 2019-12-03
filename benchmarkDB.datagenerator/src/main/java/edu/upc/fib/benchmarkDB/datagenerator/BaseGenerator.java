package edu.upc.fib.benchmarkDB.datagenerator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import edu.upc.fib.benchmarkDB.util.Util;
import edu.upc.fib.benchmarkDB.util.reader.PlainFileReader;
import javafx.util.Pair; 

public abstract class BaseGenerator implements MyGenerator {
	protected String[] columnNames;
	protected String[] originalColumnNames;
	protected String[] originalColumnTypes;
	protected String[] javaColumnTypes;
	protected String tableName;
	protected ArrayList<Integer> indexesPK;
	private int intCorrelative=0;
	
	public String[] encode() {
		String[] newColumnNames =new String[columnNames.length];
		for (int i = 0; i < columnNames.length; i++) {
			newColumnNames[i]=columnNames[i].substring(0, 1)+i;
		}
		columnNames=newColumnNames;
		return newColumnNames;
	}
	
	protected Pair<String,String> generatePK(String[] values,boolean keyValue) {
		Pair<String,String> okv=null;
		
		if(indexesPK.size()>1) {
			String composePKName="pk";
			String composePKValue ="";
			for (int j = 0; j < indexesPK.size(); j++) {
				String colName = "";
				if(columnNames.length>indexesPK.get(j)) {
					colName = columnNames[indexesPK.get(j)];	

					if(colName.indexOf(" ")>0) {
						colName = colName.substring(0, colName.indexOf(" "));
					}

					composePKName += "_" + colName;
					if(j==0) { 
						composePKValue += values[indexesPK.get(j)];
					}else { 
						composePKValue += "_" + values[indexesPK.get(j)];					
					}
				}
			}
			if(keyValue) {
				composePKName="pk";
			}
			okv = new Pair<String,String>(composePKName, composePKValue); 
		}else if(indexesPK.size()==1) {
			if(!keyValue) {
				okv = new Pair<String,String>(columnNames[indexesPK.get(0)], values[indexesPK.get(0)]); 
			}else {
				okv = new Pair<String,String>(columnNames[0], values[0]);
			}
		}else {
			okv = new Pair<String,String>("_id",intCorrelative+"");
			intCorrelative++;
		}
		return okv;
	}
	public String getJSON(String[] values, boolean includeCompositePK) { 
		String json = "{";  
		if(includeCompositePK) {
			if(indexesPK.size()>1) {
				Pair okv = generatePK(values,false);
				json+="\"_id\":";
				json+="\""+ okv.getValue()  + "\",";	
			}
		}
		for (int i = 0; i < Math.min(columnNames.length,values.length); i++) {
			String colName =columnNames[i];
			if(colName.indexOf(" ")>0) {
				colName = colName.substring(0, colName.indexOf(" "));
			}
			
			if(indexesPK.size()==1) {
				if(indexesPK.get(0)==i) {
					colName="_id";
				}
			}
			
			json+="\""+colName+"\":";
			String value =values[i];
			if(value.trim().equals("")) {
				if(!javaColumnTypes[i].equals("String")&&!javaColumnTypes[i].toLowerCase().contains("date")&&!javaColumnTypes[i].toLowerCase().contains("time")) {
					value="0";
				}else if(javaColumnTypes[i].toLowerCase().contains("date")||javaColumnTypes[i].toLowerCase().contains("time")) {
					value="";
				}
			}
			
			if(javaColumnTypes[i].equals("String")||javaColumnTypes[i].toLowerCase().contains("date")||javaColumnTypes[i].toLowerCase().contains("time")) {
				json+="\""+value+"\"";
			}else {
				json+=value;				
			}
			if(i<columnNames.length-1) {
				json+=",";
			}
		}
		if(json.lastIndexOf(",")==json.length()-1) {
			json=json.substring(0, json.length()-1);
		}
		json += "}";
		return json;
	}
	public String getInsert(String baseTemplate,String[] colNames,String[] values,String[] columnTypes) {  
		//baseTemplate = "INSERT INTO $Table (@Columns) VALUES (@Values);";
		String template =baseTemplate;
		String strColumns = "";
		String strValues = "";
		for (int i = 0; i < Math.min(colNames.length,values.length); i++) {
			String colName =colNames[i];
			if(colName.indexOf(" ")>0) {
				colName = colName.substring(0, colName.indexOf(" "));
			}
			strColumns +=colName;
			String value =values[i];
			if(value.trim().equals("")) {
				if(!columnTypes[i].equals("String")&&!columnTypes[i].toLowerCase().contains("date")) {
					value="0";
				}else if(columnTypes[i].toLowerCase().contains("date")){
					value="null";
				}else {
					value="";
				}
			}
			if(columnTypes[i].equals("String")||columnTypes[i].toLowerCase().contains("date")||columnTypes[i].toLowerCase().contains("time")) {
				if(value!="null") {
					strValues += "'" + value.replace("'", "") + "'"; 
				}else {
					strValues +=  value; 
				}
			}else {
				strValues += value; 			
			}
			if(i<Math.min(colNames.length,values.length)-1) {
				strColumns+=",";
				strValues+=",";
			}
		}
		template=template.replace("$Table", tableName);
		template=template.replace("@Columns", strColumns);
		template=template.replace("@Values", strValues);
		template += "\n";
		return template;
	}

	public String[] getJavaDataTypes(String[] dataTypes) { 
		String[] newDataTypes=new String[dataTypes.length];
		for (int i = 0; i < dataTypes.length; i++) {
			if (dataTypes[i].trim().toLowerCase().contains("char")||dataTypes[i].trim().toLowerCase().contains("text")) {
				newDataTypes[i]="String";
			}else if (dataTypes[i].trim().toLowerCase().startsWith("int")||dataTypes[i].trim().toLowerCase().startsWith("identifier")) {
				if(dataTypes[i].trim().toLowerCase().endsWith("32")) {
					newDataTypes[i]="Integer";
				}else if(dataTypes[i].trim().toLowerCase().endsWith("64")) {
					newDataTypes[i]="Long";					
				}else {
					newDataTypes[i]="Integer";
				}
			}else if (dataTypes[i].trim().toLowerCase().startsWith("decimal")||dataTypes[i].trim().toLowerCase().startsWith("money")||dataTypes[i].trim().toLowerCase().startsWith("numerical")||dataTypes[i].trim().toLowerCase().startsWith("real")) {  
				newDataTypes[i]="Decimal";
			}else if (dataTypes[i].trim().toLowerCase().startsWith("bool")) {  
				newDataTypes[i]="Boolean";
			}else if (dataTypes[i].trim().toLowerCase().startsWith("date")||dataTypes[i].toLowerCase().endsWith("time")) {  
				newDataTypes[i]="Date";
			}else if (dataTypes[i].trim().toLowerCase().startsWith("json")) {
				newDataTypes[i]="String";
			}else if (dataTypes[i].trim().toLowerCase().startsWith("xml")) {
				newDataTypes[i]="String";				
			}
			
		}
		return newDataTypes;
	}

	public abstract void generateFiles();
	
	public void loadConfigurationColumns(String filePath) {
		System.out.println("Loading configuration: " + filePath);
		PlainFileReader oReader = new PlainFileReader();
		ArrayList<String> lstColumns =new ArrayList<String>();
		ArrayList<String> lstTypes =new ArrayList<String>();
		try {
			indexesPK= new ArrayList<Integer>();
			oReader.openConnection(filePath);
			File oFile = new File(filePath);
			tableName = Util.getTableNameFromPath(oFile.getName());
			if(tableName.indexOf(" ")>0) {
				tableName = tableName.substring(tableName.indexOf(" ")).trim();
			}else if(tableName.indexOf(".")>0) {
				tableName = tableName.substring(tableName.indexOf(".")).trim();				
			}
			String line = oReader.read();
			int index=0;
			while(line!=null) {
				String[] elements = line.split(",");
				lstColumns.add(elements[0]);
				if(elements.length>2) {
					if(elements[1].contains("(")&&elements[2].contains("")) {
						lstTypes.add(elements[1] +"," +elements[2]);						
					}else {
						lstTypes.add(elements[1]);						
					}
				}else {
					lstTypes.add(elements[1]);
				}
				if(elements.length >3) {
					if(elements[3]!=null) {
						if(elements[3].trim().toUpperCase().equals("Y")) {
							/*if(indexesPK==null) {
								indexesPK=new ArrayList<Integer>();
							}*/
							indexesPK.add(index);
						}
					}
				}
				line = oReader.read();
				index++;
			}
			oReader.closeConnection();
		} catch (IOException e) { 
			e.printStackTrace();
		}
		
		String[] columns=new String[lstColumns.size()];
		String[] originalColumns=new String[lstColumns.size()];
		String[] types=new String[lstTypes.size()];
		for (int i = 0; i < lstColumns.size(); i++) {
			columns[i]=lstColumns.get(i);
			originalColumns[i] = lstColumns.get(i);
			types[i]=lstTypes.get(i);
		}
		originalColumnTypes=types;
		javaColumnTypes= getJavaDataTypes(types); 
		originalColumnNames = originalColumns;
		columnNames = columns;
	}
	
}
