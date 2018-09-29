package ca.mcgill.ecse321.rideshare9.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tb_stop")
public class Stop {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id; 
	private String address; 
	private float price;
	@Column(name = "address")
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Column(name = "price")
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	} 
	
}
