package edu.upc.fib.benchmarkDB.datagenerator;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;

import edu.upc.fib.benchmarkDB.util.Util;
import edu.upc.fib.benchmarkDB.util.reader.PlainFileReader;
import edu.upc.fib.benchmarkDB.util.writer.PlainFileWriter;
import javafx.util.Pair;

public class PostgreSQLGenerator extends BaseGenerator {
	private String sourceDirectoryPath;
	private String destinationDirectoryPath;
	private String configurationDirectoryPath;
	protected String[] postgresColumnTypes;
	private boolean keyValue=false;
	private boolean encode = false;
	private boolean useJSONB = false;
	protected String baseCreateTemplate ="CREATE TABLE $Table (@Columns);"; 
	protected String baseCreateSuffixDistribution = " DISTRIBUTE BY HASH ($Col);";
	private  Properties properties;

	public PostgreSQLGenerator(String sourceDirectoryPath, String destinationDirectoryPath,String configurationDirectoryPath){
		 try {
			this.sourceDirectoryPath=sourceDirectoryPath;
			this.destinationDirectoryPath=destinationDirectoryPath;
			this.configurationDirectoryPath = configurationDirectoryPath;	
			InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream("my.properties");
			Properties properties = new Properties();
		
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}	
	
	public void setKeyValue(boolean keyValue) {
		this.keyValue =keyValue;
	}
	
	public void setEncode(boolean encode) {
		this.encode = encode;
	}
	
	public void useJSONB() {
		this.useJSONB = true;
	}
	
	protected String getCreateTableStatement() {
		return baseCreateTemplate;
	}
	
	protected String getSuffixDistribution() {
		return baseCreateSuffixDistribution;
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
			Date startTime = new Date();
			while(line!=null ) {
				if(line.trim().length()>0 && !line.trim().startsWith("null")) {
					String[] elements = line.split("\\|");
					  
					String baseTemplate ="INSERT INTO $Table (@Columns) VALUES (@Values);"; 
					String value = "";
					if(!keyValue) {
						value = super.getInsert(baseTemplate, this.columnNames, elements,this.javaColumnTypes);
					}
					else {
						String jsonValue=super.getJSON(elements,false);
						Pair<String,String> okv = generatePK(elements,keyValue);
						String[] new_columnNames = new String[] {"pk","value"};
						String[] new_elements = new String[] {okv.getValue(),jsonValue};
						String[] new_columnTypes = new String[] {"String","String"};
						value = super.getInsert(baseTemplate, new_columnNames, new_elements,new_columnTypes);	
					}
					line = oReader.read();
					oWriter.write(value);
					i++;
				}else {
					line = oReader.read();
				}		

			} 
			oWriter.flush();
			oReader.closeConnection();
			oWriter.close();
			Date endTime = new Date();

			long diff = endTime.getTime() - startTime.getTime();
			long diffSeconds = diff / 1000;
			System.out.println("Seconds : " + diffSeconds);

		} catch (IOException e) { 
			e.printStackTrace();
		}
	}


	private void generatePKFile(String sourceFilePath, String destinationFilePath,String configurationPath) {
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
			Date startTime = new Date();
			while(line!=null ) {
				if(line.trim().length()>0 && !line.trim().startsWith("null")) {
					String[] elements = line.split("\\|");
					String header = "@Columns";  
					String baseTemplate ="@Values;"; 
					String value = "";
					if(!keyValue) {
						value = super.getInsert(baseTemplate, this.columnNames, elements,this.javaColumnTypes);
					}
					else {
						String jsonValue=super.getJSON(elements,false);
						Pair<String,String> okv = generatePK(elements,keyValue);
						String[] new_columnNames = new String[] {"pk","value"};
						String[] new_elements = new String[] {okv.getValue(),jsonValue};
						String[] new_columnTypes = new String[] {"String","String"};
						value = super.getInsert(baseTemplate, new_columnNames, new_elements,new_columnTypes);	
					}
					line = oReader.read();
					oWriter.write(value);
					i++;
				}else {
					line = oReader.read();
				}		

			} 
			oWriter.flush();
			oReader.closeConnection();
			oWriter.close();
			Date endTime = new Date();

			long diff = endTime.getTime() - startTime.getTime();
			long diffSeconds = diff / 1000;
			System.out.println("Seconds : " + diffSeconds);

		} catch (IOException e) { 
			e.printStackTrace();
		}
	}

	@Override
	public void generateFiles() {
		try {
			String connector = Util.getConnectorPerOS();
			File dir=new File(configurationDirectoryPath);
			HashMap<String, String> hm = new HashMap<String, String>();
			File[] directoryListing = dir.listFiles();
			if (directoryListing != null) {
				 for (File child : directoryListing) {
					String fileName = child.getName().replace(".schm", "");
					if(hm.get(fileName)==null) {
						String sourceFilePath = sourceDirectoryPath + connector +fileName ;
						String destinationFilePath = destinationDirectoryPath + connector + fileName + ".postgres";
						String configurationPath = child.getAbsolutePath();
						System.out.println(sourceFilePath);
						generateFile(sourceFilePath,destinationFilePath,configurationPath);
					}
				 }
			}
			
		
		} catch (Exception e) { 
			e.printStackTrace();
		}

	}
	
	public void generateCreateStatement(boolean keyValue, boolean includePK) {
		try {
			File dir=new File(configurationDirectoryPath);
			HashMap<String, String> hm = new HashMap<String, String>();
			File[] directoryListing = dir.listFiles();
			String file = "create.postgres";
			
			String connector = Util.getConnectorPerOS();
			String str_encoded = "";
			String str_pk="";
			if(encode) {
				str_encoded = "cod.";				
			}else {
				str_encoded = "nocod.";	
			}
			if(includePK) {
				str_pk = "pk.";
			}else {
				str_pk = "nopk.";
			}
			if(keyValue) {
				file = connector +str_pk+ "kv."+str_encoded + file;				
			}else {
				file = connector +str_pk+ "nokv."+str_encoded + file;			
			}
			String destinationFilePath = destinationDirectoryPath + file;
			if (directoryListing != null) {
				 for (File child : directoryListing) {
					String fileName = child.getName().replace(".schm", "");
					if(hm.get(fileName)==null) {
						String sourceFilePath = sourceDirectoryPath + connector +fileName ;
						String configurationPath = child.getAbsolutePath();

						PlainFileReader oReader = new PlainFileReader();
						PlainFileWriter oWriter = new PlainFileWriter();
						try {
							loadConfigurationColumns(configurationPath);
							oReader.openConnection(configurationPath);
							oWriter.create(destinationFilePath);
							String line = oReader.read();
							int colNumber=0;
							this.postgresColumnTypes = getPostgresDataTypes();
							
							String baseTemplate=getCreateTableStatement();
							String value = baseTemplate.replace("$Table", super.tableName);
							String columnDefinition = "";
							if(!keyValue) {								
								while(line!=null ) {
									if(line.trim().length()>0 && !line.trim().startsWith("null")) {
										String[] elements = line.split(",");									  
										columnDefinition += this.originalColumnNames[colNumber] + " ";
										if(this.postgresColumnTypes[colNumber].contains("char")) {
											if(elements[1].indexOf("(")<0) {
												System.out.println("Error. Char size not specified for " + this.originalColumnNames[colNumber]);
												columnDefinition += this.postgresColumnTypes[colNumber]+"(1)";
											}else {
												String parameters = elements[1].substring(elements[1].indexOf("("), elements[1].indexOf(")")+1);
												columnDefinition += this.postgresColumnTypes[colNumber]+parameters;
											}								
										}else {
											columnDefinition += this.postgresColumnTypes[colNumber];
										}	
										if(includePK) {
											if (super.indexesPK.size() ==1) {
												if(super.indexesPK.get(0)==colNumber) {
													columnDefinition +=" PRIMARY KEY";
												}
											}
										}
										line = oReader.read();
										if(line!=null) {
											columnDefinition +=",\n";
										}
										colNumber++;
									}else {
										line = oReader.read();
									}		
	
								}
							}else {
								if(includePK) {
									columnDefinition +="pk varchar(25) PRIMARY KEY,value ";
									if(!useJSONB) {
										columnDefinition += "json";
									}else {
										columnDefinition += "jsonb";										
									}
								}else {
									columnDefinition +="pk varchar(25),value ";
									if(!useJSONB) {
										columnDefinition += "json";
									}else {
										columnDefinition += "jsonb";										
									}
								}
							}
							if(includePK && super.indexesPK.size()>1) {
								if(!keyValue) {
									String columnsPk = "";
									for (int i = 0; i < super.indexesPK.size()-1; i++) {
										columnsPk += this.columnNames[super.indexesPK.get(i)] + ",";
									}
									columnsPk +=this.columnNames[super.indexesPK.get(super.indexesPK.size()-1)];
									
									columnDefinition +=", \nPRIMARY KEY("+ columnsPk +")";
								}else {
									columnDefinition ="pk varchar(25) PRIMARY KEY,value ";
									if(!useJSONB) {
										columnDefinition += "json";
									}else {
										columnDefinition += "jsonb";										
									}
								}
							}
							value = value.replace("@Columns", columnDefinition);
							oWriter.write(value+"\n\n");

						} catch (IOException e) { 
							e.printStackTrace();
						}

						oWriter.flush();
						oReader.closeConnection();
						oWriter.close();
					}
				 }
			}
			
		
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	public String[] getPostgresDataTypes() { 
		String[] dataTypes = this.originalColumnTypes;
		String[] newDataTypes=new String[dataTypes.length];
		for (int i = 0; i < dataTypes.length; i++) {
			if (dataTypes[i].trim().toLowerCase().contains("varchar")||
					dataTypes[i].trim().toLowerCase().contains("text")) {
				newDataTypes[i]="varchar";
			}if (dataTypes[i].trim().toLowerCase().startsWith("char")) {
				newDataTypes[i]="char";
			}else if (dataTypes[i].trim().toLowerCase().startsWith("int")||dataTypes[i].trim().toLowerCase().startsWith("identifier")) {
				if(dataTypes[i].trim().toLowerCase().endsWith("32")||dataTypes[i].trim().toLowerCase().startsWith("numerical")||dataTypes[i].trim().toLowerCase().startsWith("identifier")) {
					newDataTypes[i]="numeric";
				}else if(dataTypes[i].trim().toLowerCase().endsWith("64")) {
					newDataTypes[i]="decimal";					
				}else {
					newDataTypes[i]="decimal";
				}
			}else if (dataTypes[i].trim().toLowerCase().startsWith("money")||dataTypes[i].trim().toLowerCase().startsWith("real")) {  
				newDataTypes[i]="decimal";
			}else if (dataTypes[i].trim().toLowerCase().startsWith("bool")) {  
				newDataTypes[i]="boolean";
			}else if (dataTypes[i].trim().toLowerCase().startsWith("decimal")||dataTypes[i].trim().toLowerCase().startsWith("date")||dataTypes[i].trim().toLowerCase().startsWith("json")||dataTypes[i].trim().toLowerCase().startsWith("xml")) {  
				newDataTypes[i]=dataTypes[i].trim().toLowerCase();
			} 
			
		}
		return newDataTypes;
	}
	public void createPKFile() {
		File dir=new File(configurationDirectoryPath);
		HashMap<String, String> hm = new HashMap<String, String>();
		File[] directoryListing = dir.listFiles();
		String file = "index.postgres";
		String connector = Util.getConnectorPerOS();
		String str_encoded = "";
		if(encode) {
			str_encoded = "cod.";				
		}else {
			str_encoded = "nocod.";	
		}
		if(keyValue) {
			file = connector +"kv."+str_encoded + file;						
		}else {
			file = connector +"nokv."+str_encoded + file;				
		}
		String destinationFilePath = destinationDirectoryPath + file;
		if (directoryListing != null) {
			 for (File child : directoryListing) {
				String fileName = child.getName().replace(".schm", "");
				if(hm.get(fileName)==null) {
					String sourceFilePath = sourceDirectoryPath + connector +fileName ;
					String configurationPath = child.getAbsolutePath();

					PlainFileReader oReader = new PlainFileReader();
					PlainFileWriter oWriter = new PlainFileWriter();
					try {
						loadConfigurationColumns(configurationPath);
						oReader.openConnection(configurationPath);
						oWriter.create(destinationFilePath);
						String line = oReader.read();
						int colNumber=0;
						this.postgresColumnTypes = getPostgresDataTypes();
						
						String value = getPKStatement();
						oWriter.write(value+"\n\n");
						oWriter.flush();
						oReader.closeConnection();
						oWriter.close();
					} catch (IOException e) { 
						e.printStackTrace();
					}

					
				}
			 }
		}
		
	}
	
	public String getPKStatement() {
		String baseTemplate ="ALTER TABLE $Table ADD PRIMARY KEY ($Columns);";  
		String value = baseTemplate.replace("$Table", super.tableName);
		String columnDefinition = "";
		if(!keyValue) {
			if(super.indexesPK.size()==1) {	
				value=value.replace("$Columns", super.originalColumnNames[indexesPK.get(0)]);
			}else if(super.indexesPK.size()>1){
				for (int i = 0; i < super.indexesPK.size()-1; i++) {
					columnDefinition += super.originalColumnNames[indexesPK.get(i)]+",";
				}
				columnDefinition += super.originalColumnNames[indexesPK.get(indexesPK.size()-1)];
				value=value.replace("$Columns", columnDefinition);
			
			}
		}else {
			value=value.replace("$Columns","pk");
		}
		return value;
	}
}
