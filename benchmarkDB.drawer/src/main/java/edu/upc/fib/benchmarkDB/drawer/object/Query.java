package edu.upc.fib.benchmarkDB.drawer.object;

public class Query {

	private String database;
	private String object;
	private String operation;
	private long numberRecords;
	private long time;
	private double size;
	private double memoryAVG;
	private double memoryMAX;
	private double recordsPerSecond;
	
	public String getDatabase() {
		return database;
	}
	public void setDatabase(String database) {
		this.database = database;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public long getNumberRecords() {
		return numberRecords;
	}
	public void setNumberRecords(long numberRecords) {
		this.numberRecords = numberRecords;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	}
	public double getMemoryAVG() {
		return memoryAVG;
	}
	public void setMemoryAVG(double memoryAVG) {
		this.memoryAVG = memoryAVG;
	}
	public double getMemoryMAX() {
		return memoryMAX;
	}
	public void setMemoryMAX(double memoryMAX) {
		this.memoryMAX = memoryMAX;
	}
	public double getRecordsPerSecond() {
		return recordsPerSecond;
	}
	public void setRecordsPerSecond(double recordsPerSecond) {
		this.recordsPerSecond = recordsPerSecond;
	}
	
	
	
}
