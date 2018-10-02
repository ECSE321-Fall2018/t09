package ca.mcgill.ecse321.rideshare9.entity.helper;

import ca.mcgill.ecse321.rideshare9.entity.Advertisement;

public class AdvResponse {
	private Advertisement adv; 
	private float price;
	public Advertisement getAdv() {
		return adv;
	}
	public void setAdv(Advertisement adv) {
		this.adv = adv;
	}
	public AdvResponse(Advertisement adv, float price) {
		this.price = price; 
		this.adv = adv; 
	}
	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	} 
	
}
