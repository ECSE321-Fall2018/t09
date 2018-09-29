package ca.mcgill.ecse321.rideshare9.repository;
import java.util.List;
import ca.mcgill.ecse321.rideshare9.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.rideshare9.entity.Advertisement;
import ca.mcgill.ecse321.rideshare9.entity.Stop;
import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.entity.Vehicle;
import ca.mcgill.ecse321.rideshare9.service.impl.UserServiceImpl;
@Repository
public class AdvertisementRepository {
	@Autowired
	protected EntityManager em;
	protected VehicleRepository vrp; 
	protected UserServiceImpl urp; 
	
	@Transactional
	public Advertisement createAdv(String title, String startDate, String startLocation, int seatAvailable, Vehicle vehicle, User driver) {
		Advertisement adv = new Advertisement(); 
		adv.setTitle(title);
		adv.setStartDate(startDate); 
		adv.setStartLocation(startLocation);
		adv.setSeatAvailable(seatAvailable);
		adv.setVehicle(vehicle);
		adv.setDriver(driver.getId());
		adv.setStatus(TripStatus.REGISTERING);
		em.persist(adv);
	    return adv;
	}
	
	@Transactional
	public Advertisement rideAdv(long id) {
		Advertisement adv = this.findAdv(id); 
		adv.setStatus(TripStatus.ON_RIDE);
		em.merge(adv); 
	    return adv;
	}
	@Transactional
	public Advertisement cancelAdv(long id) {
		Advertisement adv = this.findAdv(id); 
		adv.setStatus(TripStatus.CANCELLED);
		em.merge(adv); 
	    return adv;
	}
	@Transactional
	public Advertisement completeAdv(long id) {
		Advertisement adv = this.findAdv(id); 
		adv.setStatus(TripStatus.COMPLETE);
		em.merge(adv); 
	    return adv;
	}
	@Transactional
	public Advertisement findAdv(long id) {
	    return em.find(Advertisement.class, id);
	}
	@Transactional
	public void removeVehicle(long id) {
		Advertisement adv = findAdv(id);
	    if (adv != null) {
	      em.remove(adv);
	    }
	}
	@Transactional
	public List<Advertisement> findAllAdv() {
	    TypedQuery<Advertisement> query = em.createQuery("SELECT a FROM Advertisement a", Advertisement.class);
	    return query.getResultList();
	}
}
