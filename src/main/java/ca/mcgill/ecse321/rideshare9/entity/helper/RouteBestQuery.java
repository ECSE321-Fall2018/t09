package ca.mcgill.ecse321.rideshare9.entity.helper;

import ca.mcgill.ecse321.rideshare9.entity.Advertisement;

public class RouteBestQuery {
	private String start; 
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	private String end; 
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	private int count; 
}
