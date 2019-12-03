package edu.upc.fib.benchmarkDB.drawer.object;

public class Results {
	private long timeSimple;
	private long timeBuffered;
	private long timeFixedBuffered;
	private long timeMapped;
	private int n;
	private int b;
	private int k;
	private int m;
	
	public Results() {
		timeSimple=0;
		timeBuffered=0;
		timeFixedBuffered=0;
		timeMapped=0;
		n=0;
		b=0;
		k=0;
		m=0;
	}	
	public long  getTimeSimple() {
		return timeSimple;
	}
	public void setTimeSimple(long timeSimple) {
		this.timeSimple = timeSimple;
	}
	public long getTimeBuffered() {
		return timeBuffered;
	}
	public void setTimeBuffered(long timeBuffered) {
		this.timeBuffered = timeBuffered;
	}
	public long getTimeFixedBuffered() {
		return timeFixedBuffered;
	}
	public void setTimeFixedBuffered(long timeFixedBuffered) {
		this.timeFixedBuffered = timeFixedBuffered;
	}
	public long getTimeMapped() {
		return timeMapped;
	}
	public void setTimeMapped(long timeMapped) {
		this.timeMapped = timeMapped;
	}
	public long getN() {
		return n;
	}
	public void setN(int n) {
		this.n = n;
	}
	public int getB() {
		return b;
	}
	public void setB(int b) {
		this.b = b;
	}
	public int getK() {
		return k;
	}
	public void setK(int k) {
		this.k = k;
	}
	public int getM() {
		return m;
	}
	public void setM(int m) {
		this.m = m;
	}
	
}
