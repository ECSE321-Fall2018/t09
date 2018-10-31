package ca.mcgill.ecse321.rideshare9.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ca.mcgill.ecse321.rideshare9.entity.Advertisement;
import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.entity.Vehicle;
import ca.mcgill.ecse321.rideshare9.service.impl.UserServiceImpl;

@Repository
public class VehicleRepository {
	@Autowired
	protected EntityManager em;
	
	@Transactional
	public Vehicle createVehicle(String licencePlate, String model, String color, int maxSeat, long driver) {
		Vehicle car = new Vehicle();
	    car.setColor(color);
	    car.setLicencePlate(licencePlate);
	    car.setMaxSeat(maxSeat);
	    car.setModel(model);
	    car.setDriver(driver);
	    em.persist(car);
	    em.flush(); 
	    return car;
	}
	@Transactional
	public Vehicle findVehicle(long id) {
	    return em.find(Vehicle.class, id);
	}
	
	@Transactional
	public void removeVehicle(Long id) {
		Vehicle toRemove = em.find(Vehicle.class, id); 
		em.remove(toRemove);
	}
	
	/**
	 * Change the information of a vehicle (added by Chris, may cause error)
	 * @param vehicle to be changed
	 * @return vehicle changed
	 */
	@Transactional
	public Vehicle updateVehicle(Vehicle car) {
		em.merge(car); 
		em.flush();
	    return car;
	}
	
	@Transactional
	public List<Vehicle> findAllVehicle() {
	    TypedQuery<Vehicle> query = em.createQuery("SELECT c FROM Vehicle c", Vehicle.class);
	    return query.getResultList();
	}
	@Transactional
	public List<Vehicle> findAllVehicleByUid(long uid) {
	    TypedQuery<Vehicle> query = em.createQuery("SELECT c FROM Vehicle c WHERE c.driver = :qUid", Vehicle.class).setParameter("qUid", uid);
	    return query.getResultList();
	}
}
