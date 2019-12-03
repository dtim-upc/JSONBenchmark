package edu.upc.fib.benchmarkDB.datagenerator;

public interface MyGenerator {
	String[] encode();
	String[] getJavaDataTypes(String[] dataTypes);
	String getJSON(String[] values, boolean includeCompositePK);
	void generateFiles();
}
