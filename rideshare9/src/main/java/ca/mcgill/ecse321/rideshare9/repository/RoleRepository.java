package ca.mcgill.ecse321.rideshare9.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ca.mcgill.ecse321.rideshare9.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	
}
