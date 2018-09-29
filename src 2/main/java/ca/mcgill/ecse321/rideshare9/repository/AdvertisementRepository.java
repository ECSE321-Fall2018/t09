package ca.mcgill.ecse321.rideshare9.repository;
import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.rideshare9.entity.Advertisement;
import ca.mcgill.ecse321.rideshare9.entity.User;
@Repository
public interface AdvertisementRepository extends JpaRepository<Advertisement, Long>{
	
}
