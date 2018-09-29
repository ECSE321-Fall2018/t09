package ca.mcgill.ecse321.rideshare9.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.rideshare9.entity.Advertisement;
import ca.mcgill.ecse321.rideshare9.entity.MapperUserAdv;
import ca.mcgill.ecse321.rideshare9.entity.TripStatus;
import ca.mcgill.ecse321.rideshare9.service.impl.UserServiceImpl;

@Repository
public class MapperUserAdvRepository {
	@Autowired
	protected EntityManager em;
	protected VehicleRepository vrp; 
	protected UserServiceImpl urp; 
	protected AdvertisementRepository arp; 
	
	@Transactional
	public MapperUserAdv createMapper(long uid, long did) {
		MapperUserAdv m = new MapperUserAdv(); 
		m.setAdvertisement(arp.findAdv(did).getId());
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
}
