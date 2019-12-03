package edu.upc.fib.benchmarkDB.drawer.object;

import java.util.Date;

public class ConsolidatedResults {
	private String testTypeCode;
	private String os;
	private String testTypeDetail;
	private String hadPK;
	private boolean encoded;
	private String format;
	private String datastore;
	private String object;
	private double rawSize;
	private String operation;
	private long numberOfRecords;
	private long time;
	private double sizeMB;
	private double memoryAvg;
	private double memoryMax;
	private double recordsPerSecond;
	private double MBperSecond;
	public String getTestTypeCode() {
		return testTypeCode;
	}
	public void setTestTypeCode(String testTypeCode) {
		this.testTypeCode = testTypeCode;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	public String getTestTypeDetail() {
		return testTypeDetail;
	}
	public void setTestTypeDetail(String testTypeDetail) {
		this.testTypeDetail = testTypeDetail;
	}
	public String getHadPK() {
		return hadPK;
	}
	public void setHadPK(String hadPK) {
		this.hadPK = hadPK;
	}
	public boolean isEncoded() {
		return encoded;
	}
	public void setEncoded(boolean encoded) {
		this.encoded = encoded;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getDatastore() {
		return datastore;
	}
	public void setDatastore(String datastore) {
		this.datastore = datastore;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public double getRawSize() {
		return rawSize;
	}
	public void setRawSize(double rawSize) {
		this.rawSize = rawSize;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public long getNumberOfRecords() {
		return numberOfRecords;
	}
	public void setNumberOfRecords(long numberOfRecords) {
		this.numberOfRecords = numberOfRecords;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public double getSizeMB() {
		return sizeMB;
	}
	public void setSizeMB(double sizeMB) {
		this.sizeMB = sizeMB;
	}
	public double getMemoryAvg() {
		return memoryAvg;
	}
	public void setMemoryAvg(double memoryAvg) {
		this.memoryAvg = memoryAvg;
	}
	public double getMemoryMax() {
		return memoryMax;
	}
	public void setMemoryMax(double memoryMax) {
		this.memoryMax = memoryMax;
	}
	public double getRecordsPerSecond() {
		return recordsPerSecond;
	}
	public void setRecordsPerSecond(double recordsPerSecond) {
		this.recordsPerSecond = recordsPerSecond;
	}
	public double getMBperSecond() {
		return MBperSecond;
	}
	public void setMBperSecond(double mBperSecond) {
		MBperSecond = mBperSecond;
	}
	
	
	
}
