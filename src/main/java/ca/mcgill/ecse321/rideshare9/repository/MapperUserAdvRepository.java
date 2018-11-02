package ca.mcgill.ecse321.rideshare9.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.rideshare9.entity.Advertisement;
import ca.mcgill.ecse321.rideshare9.entity.MapperUserAdv;
import ca.mcgill.ecse321.rideshare9.entity.Stop;
import ca.mcgill.ecse321.rideshare9.entity.TripStatus;
import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.entity.helper.AdvBestQuery;
import ca.mcgill.ecse321.rideshare9.entity.helper.MapperBestQuery;
import ca.mcgill.ecse321.rideshare9.service.impl.UserServiceImpl;

@Repository
public class MapperUserAdvRepository {
	@Autowired
	protected EntityManager em;
	@Autowired
	protected VehicleRepository vrp; 
	@Autowired
	protected UserServiceImpl urp; 
	@Autowired
	protected AdvertisementRepository arp; 
	
	@Transactional
	public MapperUserAdv createMapper(long uid, long did) {
		
		MapperUserAdv m = new MapperUserAdv(); 
		m.setAdvertisement(did);
		m.setPassenger(uid);
		em.persist(m);
		em.flush();
	    return m;
	}
	@Transactional
	public MapperUserAdv findMap(long id) {
	    return em.find(MapperUserAdv.class, id);
	}
	@Transactional
	public void removeVehicle(long id) {
		MapperUserAdv mp = findMap(id);
	    if (mp != null) {
	      em.remove(mp);
	    }
	}
	@Transactional
	public List<MapperUserAdv> findAllAdv() {
	    TypedQuery<MapperUserAdv> query = em.createQuery("SELECT a FROM MapperUserAdv a", MapperUserAdv.class);
	    return query.getResultList();
	}
	@Transactional
	public List<MapperUserAdv> findNamedAdv(Long uname) {
	    TypedQuery<MapperUserAdv> query = em.createQuery("SELECT a FROM MapperUserAdv a WHERE a.passenger = :uname", MapperUserAdv.class).setParameter("uname", uname);
	    return query.getResultList();
	}
	@Transactional
	public int findRegCountById(Long id) {
		Query query = em.createQuery("SELECT COUNT(a) FROM MapperUserAdv a WHERE a.advertisement = :aid").setParameter("aid", id);
		return ((Long)query.getSingleResult()).intValue();
	}
	@Transactional
	public List<MapperBestQuery> findBestPassenger() {
	    Query query = em.createQuery("SELECT a.passenger, COUNT(a) FROM MapperUserAdv a GROUP BY a.passenger ORDER BY COUNT(a) DESC", Object[].class);
	    List<Object[]> did_list = query.getResultList(); 
	    ArrayList<MapperBestQuery> q = new ArrayList<MapperBestQuery>(); 
	    for (Object[] i: did_list) {
	    	MapperBestQuery currq = new MapperBestQuery(); 
            currq.setBest(urp.findUserByUID((Long)i[0]));
            currq.setCount((Long)i[1]);
            q.add(currq); 
	    }
	    return q;
	}
}
