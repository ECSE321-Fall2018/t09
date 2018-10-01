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

	@Transactional
	public Stop findStop(long id) {
	    return em.find(Stop.class, id);
	}
	public List<Stop> findAllStop() {
	    TypedQuery<Stop> query = em.createQuery("SELECT s FROM Stop s", Stop.class);
	    return query.getResultList();
	}
}
