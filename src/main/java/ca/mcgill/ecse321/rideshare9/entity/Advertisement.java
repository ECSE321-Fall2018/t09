package ca.mcgill.ecse321.rideshare9.entity;
import javax.persistence.JoinColumns;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_advertisement")
public class Advertisement {
	private long id;

	public void setId(long value) {
		this.id = value;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	public long getId() {
		return this.id;
	}

	private String title;

	public void setTitle(String value) {
		this.title = value;
	}
	@Column(name = "title")
	public String getTitle() {
		return this.title;
	}

	private String startTime;

	public void setStartTime(String value) {
		this.startTime = value;
	}
	@Column(name = "startTime")
	public String getStartTime() {
		return this.startTime;
	}

	private String startDate;

	public void setStartDate(String value) {
		this.startDate = value;
	}
	@Column(name = "startDate")
	public String getStartDate() {
		return this.startDate;
	}

	private String startLocation;

	public void setStartLocation(String value) {
		this.startLocation = value;
	}
	@Column(name = "startLocation")
	public String getStartLocation() {
		return this.startLocation;
	}

	private TripStatus status;

	public void setStatus(TripStatus value) {
		this.status = value;
	}

	@Column(name = "status")
	public TripStatus getStatus() {
		return this.status;
	}

	private int seatAvailable;

	public void setSeatAvailable(int value) {
		this.seatAvailable = value;
	}
	@Column(name = "seatAvailable")
	public int getSeatAvailable() {
		return this.seatAvailable;
	}

	/**
	 * <pre>
	 *           1..1     1..*
	 * Advertisement ------------------------- Stop
	 *           adv        &gt;       stops
	 * </pre>
	 */
	
	private List<Stop> stops;
	
	public void setStops(List<Stop> stops) {
		this.stops = stops;
	}
	@ElementCollection
	public List<Stop> getStops() {
		if (this.stops == null) {
			this.stops = new ArrayList<Stop>();
		}
		return this.stops;
	}

	/**
	 * <pre>
	 *           0..*     1..1
	 * Advertisement ------------------------> Vehicle
	 *           advertisement        &gt;       vehicle
	 * </pre>
	 */
	private Vehicle vehicle;

	public void setVehicle(Vehicle value) {
		this.vehicle = value;
	}
	@OneToOne(targetEntity = Vehicle.class)
	/*@JoinColumns( {
        @JoinColumn(name = "vehicle_color", referencedColumnName = "color"),
        @JoinColumn(name = "vehicle_model", referencedColumnName = "id"),
        @JoinColumn(name = "vehicle_Licence", referencedColumnName = "licencePlate"),
    })*/
	@JoinColumn(name = "vehicle_id",referencedColumnName = "id")
	public Vehicle getVehicle() {
		return this.vehicle;
	}

	/**
	 * <pre>
	 *           0..*     1..1
	 * Advertisement ------------------------> User
	 *           advertisement        &gt;       driver
	 * </pre>
	 */
	private Long driver;

	public Long getDriver() {
		return driver;
	}
	public void setDriver(Long driver) {
		this.driver = driver;
	}

	

}
