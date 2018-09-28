package ca.mcgill.ecse321.rideshare9.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ca.mcgill.ecse321.rideshare9.entity.Stop;

public interface StopRepository extends JpaRepository<Stop, Long> {

}
