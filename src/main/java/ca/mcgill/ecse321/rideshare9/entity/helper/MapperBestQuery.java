package ca.mcgill.ecse321.rideshare9.entity.helper;

import ca.mcgill.ecse321.rideshare9.entity.User;

public class MapperBestQuery {
	private User best; 
	private Long count;
	
	public User getBest() {
		return best;
	}
	public void setBest(User best) {
		this.best = best;
	}
	public Long getCount() {
		return count;
	}
	public void setCount(Long count) {
		this.count = count;
	} 
}
