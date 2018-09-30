package ca.mcgill.ecse321.rideshare9.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "tb_mapper")
public class MapperUserAdv {
	/**
	 * <pre>
	 *           0..1     1..1
	 * MapperUserAdv ------------------------> User
	 *           newClass2        &gt;       passenger
	 * </pre>
	 */
	private long passenger;
	@Column(name = "passenger")
	public long getPassenger() {
		return passenger;
	}
	public void setPassenger(long passenger) {
		this.passenger = passenger;
	}

	/**
	 * <pre>
	 *           0..1     1..1
	 * MapperUserAdv ------------------------> Advertisement
	 *           newClass2        &gt;       advertisement
	 * </pre>
	 */
	private long advertisement;
	@Column(name = "advertisement")
	public long getAdvertisement() {
		return advertisement;
	}
	public void setAdvertisement(long advertisement) {
		this.advertisement = advertisement;
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

}
