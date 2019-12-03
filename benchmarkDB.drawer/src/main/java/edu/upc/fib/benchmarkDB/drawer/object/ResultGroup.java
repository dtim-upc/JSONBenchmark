package edu.upc.fib.benchmarkDB.drawer.object;

import edu.upc.fib.benchmarkDB.drawer.object.Results;

public class ResultGroup {
	private Results readResult;
	private Results writeResult;
	private Results externalTotalResult;
	
	public ResultGroup() {
		readResult = new Results();
		writeResult = new Results();
		externalTotalResult=new Results();
	}
	
	public Results getReadResult() {
		return readResult;
	}
	public void setReadResult(Results readResult) {
		this.readResult = readResult;
	}
	public Results getWriteResult() {
		return writeResult;
	}
	public void setWriteResult(Results writeResult) {
		this.writeResult = writeResult;
	}
	public Results getExternalTotalResult() {
		return externalTotalResult;
	}
	public void setExternalTotalResult(Results externalTotalResult) {
		this.externalTotalResult = externalTotalResult;
	} 
	
	
	
}
