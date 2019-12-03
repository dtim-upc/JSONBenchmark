package edu.upc.fib.benchmarkDB.drawer.object;
 

public class DrawNumericObject {
	private String className;
	private String subClassName;
	private Pair xyValues;
	private double size;
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
	}
	public Pair getXyValues() {
		return xyValues;
	}
	public void setXyValues(Pair xyValues) {
		this.xyValues = xyValues;
	}
	public String getSubClassName() {
		return subClassName;
	}
	public void setSubClassName(String subClassName) {
		this.subClassName = subClassName;
	}
	public double getSize() {
		return size;
	}
	public void setSize(double size) {
		this.size = size;
	} 
	
}
