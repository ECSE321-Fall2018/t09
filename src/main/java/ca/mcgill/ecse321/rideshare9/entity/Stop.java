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
	
	public Stop() {
		
	}
	public Stop(long id, float price, String stopName) {
		this.id=id;
		this.price=price;
		this.stopName=stopName;
	}

	private long id;

	public void setId(long value) {
		this.id = value;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public long getId() {
		return this.id;
	}

	private String stopName;

	public void setStopName(String value) {
		this.stopName = value;
	}
	@Column(name = "stopName")
	public String getStopName() {
		return this.stopName;
	}

	private float price;

	public void setPrice(float value) {
		this.price = value;
	}
	@Column(name = "price")
	public float getPrice() {
		return this.price;
	}
	

}
