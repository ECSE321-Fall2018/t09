package ca.mcgill.ecse321.rideshare9.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.rideshare9.entity.Advertisement;
import ca.mcgill.ecse321.rideshare9.entity.Stop;
import ca.mcgill.ecse321.rideshare9.entity.Vehicle;
import ca.mcgill.ecse321.rideshare9.service.impl.UserServiceImpl;

@Repository
public class StopRepository {
	@Autowired
	protected EntityManager em;
	protected VehicleRepository vrp; 
	protected UserServiceImpl urp; 
	
	@Transactional
	public Stop createStop(String stopName, float price) {
		Stop stp = new Stop(); 
		stp.setPrice(price);
		stp.setStopName(stopName);
		em.persist(stp);
		em.flush();
	    return stp;
	}
	
	/**
	 * Remove a stop (added by Chris, may have error)
	 * @param id of a stop
	 * @return void
	 */
	@Transactional
	public void removeStop(long id) {
		Stop stp = findStop(id);
	    if (stp != null) {
	      em.remove(stp);
	    }
	}
	
	/**
	 * Update a stop (added by Chris, may have error)
	 * @param Stop to be changed
	 * @return Stop changed
	 */
	@Transactional
	public Stop updateStop(Stop stp) {
		em.merge(stp);
		em.flush();
	    return stp;
	}
	
	@Transactional
	public Stop findStop(long id) {
	    return em.find(Stop.class, id);
	}
	@Transactional
	public Stop findStopbyName(String name) {
	    return em.find(Stop.class, name);
	}
	
	@Transactional
	public Stop findStopById(long id) {
		TypedQuery<Stop> query = em.createQuery("SELECT s FROM Stop s WHERE s.id = :id",Stop.class).setParameter("id", id);
		return query.getSingleResult();
	}
	
	public List<Stop> findAllStop() {
	    TypedQuery<Stop> query = em.createQuery("SELECT s FROM Stop s", Stop.class);
	    return query.getResultList();
	}
	@Transactional
	public Stop findStopById(long id) {
		TypedQuery<Stop> query = em.createQuery("SELECT s FROM Stop s WHERE s.id = :id",Stop.class).setParameter("id", id);
		return query.getSingleResult();
	}
}
