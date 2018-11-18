package ca.mcgill.ecse321.rideshare9.entity;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

/* The reason this class exists is to have a list of 
 * stop and vehicle objects as opposed
 * to having ids. And the driver's username as opposed to id.
*/
public class ActiveAdvertisement {
	private long id;
	private String title;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;
	private String startLocation;
	private String endLocation;
	private TripStatus tripStatus;
	private int seatsAvailable;
	private List<Stop> stops;
	private Vehicle vehicle;
	private String driverUsername;
	
	
	
	public ActiveAdvertisement(long id, String title, Date startTime, String startLocation, String endLocation,
			TripStatus tripStatus, int seatsAvailable, List<Stop> stops, Vehicle vehicle, String driverUsername) {
		this.id = id;
		this.title = title;
		this.startTime = startTime;
		this.startLocation = startLocation;
		this.endLocation = endLocation;
		this.tripStatus = tripStatus;
		this.seatsAvailable = seatsAvailable;
		this.stops = stops;
		this.vehicle = vehicle;
		this.driverUsername = driverUsername;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getStartTime() {
		return startTime;
	}
	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}
	public String getStartLocation() {
		return startLocation;
	}
	public void setStartLocation(String startLocation) {
		this.startLocation = startLocation;
	}
	public String getEndLocation() {
		return endLocation;
	}
	public void setEndLocation(String endLocation) {
		this.endLocation = endLocation;
	}
	public TripStatus getTripStatus() {
		return tripStatus;
	}
	public void setTripStatus(TripStatus tripStatus) {
		this.tripStatus = tripStatus;
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
	public Vehicle getVehicle() {
		return vehicle;
	}
	public void setVehicle(Vehicle vehicle) {
		this.vehicle = vehicle;
	}
	public String getDriverUsername() {
		return driverUsername;
	}
	public void setDriverUsername(String driverUsername) {
		this.driverUsername = driverUsername;
	}
}
