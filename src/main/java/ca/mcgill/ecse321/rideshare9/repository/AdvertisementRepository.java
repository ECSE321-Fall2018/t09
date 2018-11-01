package ca.mcgill.ecse321.rideshare9.repository;
import java.util.ArrayList;


import java.util.Date;
import java.util.List;
import java.util.Set;

import ca.mcgill.ecse321.rideshare9.entity.*;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.rideshare9.entity.helper.AdvBestQuery;
import ca.mcgill.ecse321.rideshare9.entity.helper.AdvQuery;
import ca.mcgill.ecse321.rideshare9.entity.helper.AdvResponse;
import ca.mcgill.ecse321.rideshare9.service.impl.UserServiceImpl;

/**
 * Notes for front end: Searching
 * --------------------------------------------------------------------------------------------------------------
 * Essential Query: 4 must be present 
 * --------------------------------------------------------------------------------------------------------------
 * destination: 			| startLocation: 				| startTimeLow: 			| startTimeHigh: 			
 * --------------------------------------------------------------------------------------------------------------
 * Optional Query: any one, or both, or neither present 
 * --------------------------------------------------------------------------------------------------------------
 * Car type: 			    | Car color: 				    | 
 * --------------------------------------------------------------------------------------------------------------
 * Order by: Option, choose 1
 * --------------------------------------------------------------------------------------------------------------
 * Price					| StartTime 					|
 * --------------------------------------------------------------------------------------------------------------
 */



@Repository
public class AdvertisementRepository {
	@Autowired
	protected EntityManager em;
	@Autowired
	protected VehicleRepository vrp; 
	@Autowired
	protected StopRepository srp; 
	@Autowired
	protected UserServiceImpl urp; 

	
	@Transactional
	public Advertisement createAdv(String title, Date startTime, String startLocation, int seatAvailable, Set<Long> stops, long vehicle, long driver) {
		Advertisement adv = new Advertisement(); 
		adv.setTitle(title); 
		adv.setStartLocation(startLocation);
		adv.setSeatAvailable(seatAvailable);
		adv.setVehicle(vehicle);
		adv.setStartTime(startTime);
		adv.setDriver(driver);
		adv.setStops(stops);
		adv.setStatus(TripStatus.REGISTERING);
		em.persist(adv);
		em.flush();
	    return adv;
	}
	
	/**
	 * Driver change content of advertisement
	 * @param Advertisement to be changed
	 * @return advertisement changed
	 */
	@Transactional
	public Advertisement updateAdv(Advertisement adv) {
		em.merge(adv); 
		em.flush();
	    return adv;
	}
	
	/**
	 * Find advertisement by id
	 * @param Advertisement id of adv to be found
	 * @return advertisement found
	 */
	@Transactional
	public Advertisement findAdv(long id) {
	    return em.find(Advertisement.class, id);
	}
	
	/**
	 * Remove advertisement by id
	 * @param Advertisement id of adv to be removed
	 * @return void
	 */
	@Transactional
	public void removeAdv(Long id) {
	    // TypedQuery<Advertisement> query = em.createQuery("SELECT a FROM Advertisement a WHERE a.id = :aid", Advertisement.class).setParameter("aid", id.toString());
	    // em.remove(query.getResultList().get(0)); 
		Advertisement toRemove = em.find(Advertisement.class, id); 
		em.remove(toRemove);
	}
	
	/**
	 * FOR YUDI/Mapper editor: sign up for a journey:: decrement seat available
	 * @param Advertisement id of adv to be registered
	 * @return advertisement registered with available seat decremented
	 */
	@Transactional 
	public Advertisement signUpAdv(long id) {
		Advertisement curr = this.findAdv(id); 
		curr.setSeatAvailable(curr.getSeatAvailable() - 1);
		em.merge(curr); 
		em.flush();
		return curr; 
	}
	
	/**
	 * List all advertisement posted
	 * @param void
	 * @return list of best driver and his count
	 */
	@Transactional
	public List<Advertisement> findAllAdv() {
	    TypedQuery<Advertisement> query = em.createQuery("SELECT a FROM Advertisement a", Advertisement.class);
	    return query.getResultList();
	}
	
	/**
	 * List all advertisement posted by one user
	 * @param void
	 * @return list of best driver and his count
	 */
	@Transactional
	public List<Advertisement> findAllAdv(Long uid) {
	    TypedQuery<Advertisement> query = em.createQuery("SELECT a FROM Advertisement a WHERE a.driver = :uid", Advertisement.class).setParameter("uid", uid);
	    return query.getResultList();
	}
	/**
	 * List all advertisement posted by one user
	 * @param void
	 * @return list of best driver and his count
	 */
	@Transactional
	public String findAllAdvCount(Long uid) {
	    TypedQuery<Advertisement> query = em.createQuery("SELECT a FROM Advertisement a WHERE a.driver = :uid", Advertisement.class).setParameter("uid", uid);
	    Integer count = query.getResultList().size(); 
	    return count.toString();
	}
	/**
	 * Sort Driver by count of advertisement posted
	 * @param void
	 * @return list of best driver and his count
	 */
	@Transactional
	public List<AdvBestQuery> findBestDriver() {
	    Query query = em.createQuery("SELECT a.driver, COUNT(a.id) FROM Advertisement a GROUP BY a.driver ORDER BY COUNT(a.id) DESC", Object[].class);
	    List<Object[]> did_list = query.getResultList(); 
	    ArrayList<AdvBestQuery> q = new ArrayList<AdvBestQuery>(); 
	    for (Object[] i: did_list) {
	    	AdvBestQuery currq = new AdvBestQuery(); 
            currq.setBest(urp.findUserByUID((Long)i[0]));
            currq.setCount((Long)i[1]);
            q.add(currq); 
	    }
	    return q;
	}
	
	/**
	 * Sort Advertisement by criteria price
	 * @param AdvQuery q
	 * @return result sorted by price, and within each price, sorted by time
	 */
	@Transactional
	public List<AdvResponse> findAdvByCriteriaSortByPrice(AdvQuery q) {		
	    TypedQuery<Advertisement> query = em.createQuery("SELECT a FROM Advertisement a JOIN Vehicle v ON a.vehicle = v.id WHERE a.startLocation LIKE :qLocation AND a.startTime BETWEEN :start AND :end ORDER BY v.model ASC", Advertisement.class)
	    		.setParameter("qLocation", "%" + q.getStartLocation() + "%").setParameter("start", q.getStartTimeX(), TemporalType.DATE).setParameter("end", q.getStartTimeY(), TemporalType.DATE);
	    TypedQuery<Stop> query2 = em.createQuery("SELECT s FROM Stop s WHERE s.stopName LIKE :qName ORDER BY s.price ASC", Stop.class).setParameter("qName", "%" + q.getStop() + "%");
	    ArrayList<Advertisement> resAdv = (ArrayList<Advertisement>)query.getResultList(); 
	    List<Stop> resStop = query2.getResultList(); 
	    ArrayList<AdvResponse> ret1 = new ArrayList<AdvResponse>(); 
	    for (Stop st: resStop) {
	    	for (Advertisement ad: resAdv) {
		    	if (ad.getStops().contains(st.getId())) {
		    		 ret1.add((new AdvResponse(ad, st.getPrice()))); 
		    	}
		    }
	    }
	    return ret1;
	}
	
	/**
	 * Sort Advertisement by criteria time
	 * @param AdvQuery q
	 * @return result sorted by time, and within each time, sorted by price
	 */
	@Transactional
	public List<AdvResponse> findAdvByCriteriaSortByTime(AdvQuery q) {		
	    TypedQuery<Advertisement> query = em.createQuery("SELECT a FROM Advertisement a JOIN Vehicle v ON a.vehicle = v.id WHERE a.startLocation LIKE :qLocation AND a.startTime BETWEEN :start AND :end ORDER BY v.model ASC", Advertisement.class)
	    		.setParameter("qLocation", "%" + q.getStartLocation() + "%").setParameter("start", q.getStartTimeX(), TemporalType.DATE).setParameter("end", q.getStartTimeY(), TemporalType.DATE);
	    TypedQuery<Stop> query2 = em.createQuery("SELECT s FROM Stop s WHERE s.stopName LIKE :qName ORDER BY s.price ASC", Stop.class).setParameter("qName", "%" + q.getStop() + "%");
	    ArrayList<Advertisement> resAdv = (ArrayList<Advertisement>)query.getResultList(); 
	    List<Stop> resStop = query2.getResultList(); 
	    ArrayList<AdvResponse> ret1 = new ArrayList<AdvResponse>(); 
	    for (Advertisement ad: resAdv) {
	    	for (Stop st: resStop) {
		    	if (ad.getStops().contains(st.getId())) {
		    		 ret1.add((new AdvResponse(ad, st.getPrice()))); 
		    	}
		    }
	    }
	    return ret1;
	}
	
	/**
	 * Search advertisement by basic criteria and vehicle color, sort result by time
	 * @param AdvQuery q
	 * @return result sorted by time
	 */
	@Transactional
	public List<AdvResponse> findAdvByCriteriaAndColorSortByTime(AdvQuery q) {		
	    TypedQuery<Advertisement> query = em.createQuery("SELECT a FROM Advertisement a JOIN Vehicle v ON a.vehicle = v.id WHERE v.color = :qVColor AND a.startLocation LIKE :qLocation AND a.startTime BETWEEN :start AND :end ORDER BY v.model ASC", Advertisement.class)
	    		.setParameter("qLocation", "%" + q.getStartLocation() + "%").setParameter("start", q.getStartTimeX(), TemporalType.DATE).setParameter("end", q.getStartTimeY(), TemporalType.DATE).setParameter("qVColor", q.getvColor());
	    TypedQuery<Stop> query2 = em.createQuery("SELECT s FROM Stop s WHERE s.stopName LIKE :qName ORDER BY s.price ASC", Stop.class).setParameter("qName", "%" + q.getStop() + "%");
	    List<Stop> resStop = query2.getResultList();
	    ArrayList<Advertisement> resAdv = (ArrayList<Advertisement>)query.getResultList(); 
	    ArrayList<AdvResponse> ret1 = new ArrayList<AdvResponse>(); 
	    for (Advertisement ad: resAdv) {
	    	for (Stop st: resStop) {
		    	if (ad.getStops().contains(st.getId())) {
		    		 ret1.add((new AdvResponse(ad, st.getPrice()))); 
		    	}
		    }
	    }
	    return ret1;
	}
	/**
	 * Search advertisement by basic criteria and vehicle color, sort result by price
	 * @param AdvQuery q
	 * @return result sorted by price
	 */
	@Transactional
	public List<AdvResponse> findAdvByCriteriaAndColorSortByPrice(AdvQuery q) {		
	    TypedQuery<Advertisement> query = em.createQuery("SELECT a FROM Advertisement a JOIN Vehicle v ON a.vehicle = v.id WHERE v.color = :qVColor AND a.startLocation LIKE :qLocation AND a.startTime BETWEEN :start AND :end ORDER BY v.model ASC", Advertisement.class)
	    		.setParameter("qLocation", "%" + q.getStartLocation() + "%").setParameter("start", q.getStartTimeX(), TemporalType.DATE).setParameter("end", q.getStartTimeY(), TemporalType.DATE).setParameter("qVColor", q.getvColor());
	    TypedQuery<Stop> query2 = em.createQuery("SELECT s FROM Stop s WHERE s.stopName LIKE :qName ORDER BY s.price ASC", Stop.class).setParameter("qName", "%" + q.getStop() + "%");
	    List<Stop> resStop = query2.getResultList();
	    ArrayList<Advertisement> resAdv = (ArrayList<Advertisement>)query.getResultList(); 
	    ArrayList<AdvResponse> ret1 = new ArrayList<AdvResponse>(); 
	    for (Stop st: resStop) {
	    	for (Advertisement ad: resAdv) {
		    	if (ad.getStops().contains(st.getId())) {
		    		 ret1.add((new AdvResponse(ad, st.getPrice()))); 
		    	}
		    }
	    }
	    return ret1;
	}
	/**
	 * Search advertisement by basic criteria and vehicle model, sort result by time
	 * @param AdvQuery q
	 * @return result sorted by time
	 */
	@Transactional
	public List<AdvResponse> findAdvByCriteriaAndModelSortByTime(AdvQuery q) {		
	    TypedQuery<Advertisement> query = em.createQuery("SELECT a FROM Advertisement a JOIN Vehicle v ON a.vehicle = v.id WHERE v.model = :qVModel AND a.startLocation LIKE :qLocation AND a.startTime BETWEEN :start AND :end ORDER BY v.model ASC", Advertisement.class)
	    		.setParameter("qLocation", "%" + q.getStartLocation() + "%").setParameter("start", q.getStartTimeX(), TemporalType.DATE).setParameter("end", q.getStartTimeY(), TemporalType.DATE).setParameter("qVModel", q.getvModel());
	    TypedQuery<Stop> query2 = em.createQuery("SELECT s FROM Stop s WHERE s.stopName LIKE :qName ORDER BY s.price ASC", Stop.class).setParameter("qName", "%" + q.getStop() + "%");
	    List<Stop> resStop = query2.getResultList();
	    ArrayList<Advertisement> resAdv = (ArrayList<Advertisement>)query.getResultList(); 
	    ArrayList<AdvResponse> ret1 = new ArrayList<AdvResponse>(); 
	    for (Advertisement ad: resAdv) {
	    	for (Stop st: resStop) {
		    	if (ad.getStops().contains(st.getId())) {
		    		 ret1.add((new AdvResponse(ad, st.getPrice()))); 
		    	}
		    }
	    }
	    return ret1;
	}
	/**
	 * Search advertisement by basic criteria and vehicle model, sort result by price
	 * @param AdvQuery q
	 * @return result sorted by price
	 */
	@Transactional
	public List<AdvResponse> findAdvByCriteriaAndModelSortByPrice(AdvQuery q) {		
	    TypedQuery<Advertisement> query = em.createQuery("SELECT a FROM Advertisement a JOIN Vehicle v ON a.vehicle = v.id WHERE v.model = :qVModel AND a.startLocation LIKE :qLocation AND a.startTime BETWEEN :start AND :end ORDER BY v.model ASC", Advertisement.class)
	    		.setParameter("qLocation", "%" + q.getStartLocation() + "%").setParameter("start", q.getStartTimeX(), TemporalType.DATE).setParameter("end", q.getStartTimeY(), TemporalType.DATE).setParameter("qVModel", q.getvModel());
	    TypedQuery<Stop> query2 = em.createQuery("SELECT s FROM Stop s WHERE s.stopName LIKE :qName ORDER BY s.price ASC", Stop.class).setParameter("qName", "%" + q.getStop() + "%");
	    List<Stop> resStop = query2.getResultList();
	    ArrayList<Advertisement> resAdv = (ArrayList<Advertisement>)query.getResultList(); 
	    ArrayList<AdvResponse> ret1 = new ArrayList<AdvResponse>(); 
	    for (Stop st: resStop) {
	    	for (Advertisement ad: resAdv) {
		    	if (ad.getStops().contains(st.getId())) {
		    		 ret1.add((new AdvResponse(ad, st.getPrice()))); 
		    	}
		    }
	    }
	    return ret1;
	}
	/**
	 * Search advertisement by basic criteria and vehicle model and vehicle color, sort result by price
	 * @param AdvQuery q
	 * @return result sorted by price
	 */
	@Transactional
	public List<AdvResponse> findAdvByCriteriaAndModelAndColorSortByPrice(AdvQuery q) {		
	    TypedQuery<Advertisement> query = em.createQuery("SELECT a FROM Advertisement a JOIN Vehicle v ON a.vehicle = v.id WHERE v.color = :qVColor AND v.model = :qVModel AND a.startLocation LIKE :qLocation AND a.startTime BETWEEN :start AND :end ORDER BY v.model ASC", Advertisement.class)
	    		.setParameter("qLocation", "%" + q.getStartLocation() + "%").setParameter("start", q.getStartTimeX(), TemporalType.DATE).setParameter("end", q.getStartTimeY(), TemporalType.DATE).setParameter("qVColor", q.getvColor()).setParameter("qVModel", q.getvModel());
	    TypedQuery<Stop> query2 = em.createQuery("SELECT s FROM Stop s WHERE s.stopName LIKE :qName ORDER BY s.price ASC", Stop.class).setParameter("qName", "%" + q.getStop() + "%");
	    List<Stop> resStop = query2.getResultList();
	    ArrayList<Advertisement> resAdv = (ArrayList<Advertisement>)query.getResultList(); 
	    ArrayList<AdvResponse> ret1 = new ArrayList<AdvResponse>(); 
	    for (Stop st: resStop) {
	    	for (Advertisement ad: resAdv) {
		    	if (ad.getStops().contains(st.getId())) {
		    		 ret1.add((new AdvResponse(ad, st.getPrice()))); 
		    	}
		    }
	    }
	    return ret1;
	}
	/**
	 * Search advertisement by basic criteria and vehicle model and vehicle color, sort result by time
	 * @param AdvQuery q
	 * @return result sorted by time
	 */
	@Transactional
	public List<AdvResponse> findAdvByCriteriaAndModelAndColorSortByTime(AdvQuery q) {		
	    TypedQuery<Advertisement> query = em.createQuery("SELECT a FROM Advertisement a JOIN Vehicle v ON a.vehicle = v.id WHERE v.color = :qVColor AND v.model = :qVModel AND a.startLocation LIKE :qLocation AND a.startTime BETWEEN :start AND :end ORDER BY v.model ASC", Advertisement.class)
	    		.setParameter("qLocation", "%" + q.getStartLocation() + "%").setParameter("start", q.getStartTimeX(), TemporalType.DATE).setParameter("end", q.getStartTimeY(), TemporalType.DATE).setParameter("qVColor", q.getvColor()).setParameter("qVModel", q.getvModel());
	    TypedQuery<Stop> query2 = em.createQuery("SELECT s FROM Stop s WHERE s.stopName LIKE :qName ORDER BY s.price ASC", Stop.class).setParameter("qName", "%" + q.getStop() + "%");
	    List<Stop> resStop = query2.getResultList();
	    ArrayList<Advertisement> resAdv = (ArrayList<Advertisement>)query.getResultList(); 
	    ArrayList<AdvResponse> ret1 = new ArrayList<AdvResponse>(); 
	    for (Advertisement ad: resAdv) {
	    	for (Stop st: resStop) {
		    	if (ad.getStops().contains(st.getId())) {
		    		 ret1.add((new AdvResponse(ad, st.getPrice()))); 
		    	}
		    }
	    }
	    return ret1;
	}
	
}



