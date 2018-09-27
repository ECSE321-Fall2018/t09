package ca.mcgill.ecse321.rideshare9.entity;

import java.util.List;
import java.util.Set;

import javax.persistence.*;
@Entity
@Table(name = "tb_adv")
public class Advertisement {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id; 
	private String departure; 
	private int seatsAvailable; 
	@OneToMany
	@JoinColumn(name = "stop_address")
	private List<Stop> stops;
	private String arrival;
	@OneToOne
	@JoinColumn(name = "as_driver")
    private User driver;

	@OneToOne
	@JoinColumn(name = "vehicle_model")
    private Vehicle vehicle;
	@OneToMany
	@JoinColumn(name = "as_passenger")
	private Set<User> passengers;
	
	@Column(name = "vehicle")
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public Set<User> getPassengers() {
		return passengers;
	}
	public void setPassengers(Set<User> passengers) {
		this.passengers = passengers;
	}
	public User getDriver() {
		return driver;
	}
	public void setDriver(User driver) {
		this.driver = driver;
	}
	public String getDeparture() {
		return departure;
	}
	public void setDeparture(String departure) {
		this.departure = departure;
	}
	public String getArrival() {
		return arrival;
	}
	public void setArrival(String arrival) {
		this.arrival = arrival;
	}
	public int getSeatsAvailable() {
		return seatsAvailable;
	}
	public void setSeatsAvailable(int seatsAvailable) {
		this.seatsAvailable = seatsAvailable;
	}
	public List<Stop> getStops() {
		return stops;
	}
	public void setStops(List<Stop> stops) {
		this.stops = stops;
	}
	
}
