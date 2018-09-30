package ca.mcgill.ecse321.rideshare9.entity;
import javax.persistence.JoinColumns;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
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

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
@Table(name = "tb_advertisement")
public class Advertisement {
	private long id;

	public void setId(long value) {
		this.id = value;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
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
	
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date startTime;

	public void setStartTime(Date value) {
		this.startTime = value;
	}
	@Column(name = "startTime")
	public Date getStartTime() {
		return this.startTime;
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
	
	private List<Long> stops;
	
	public void setStops(List<Long> stops) {
		this.stops = stops;
	}
	@ElementCollection
	public List<Long> getStops() {
		if (this.stops == null) {
			this.stops = new ArrayList<Long>();
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
	private long vehicle;

	public void setVehicle(long value) {
		this.vehicle = value;
	}
	/*@JoinColumns( {
        @JoinColumn(name = "vehicle_color", referencedColumnName = "color"),
        @JoinColumn(name = "vehicle_model", referencedColumnName = "id"),
        @JoinColumn(name = "vehicle_Licence", referencedColumnName = "licencePlate"),
    })*/
	@Column(name = "vehicle")
	public long getVehicle() {
		return this.vehicle;
	}

	/**
	 * <pre>
	 *           0..*     1..1
	 * Advertisement ------------------------> User
	 *           advertisement        &gt;       driver
	 * </pre>
	 */
	private long driver;
	@Column(name = "driver")
	public long getDriver() {
		return driver;
	}
	public void setDriver(long driver) {
		this.driver = driver;
	}
}
