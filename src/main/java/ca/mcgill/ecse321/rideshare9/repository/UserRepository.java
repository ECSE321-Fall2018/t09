package ca.mcgill.ecse321.rideshare9.repository;
import java.util.List;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import ca.mcgill.ecse321.rideshare9.entity.User;

/**
 * DO NOT EDIT IT ON YOUR OWN!!!
 * ATTENTION: DON'T EDIT ANY CLASS WHOSE NAME HAS "User" or "Security" or "service" or related! Otherwise, no one can log in this system anymore! 
 * if you have suggestions, please contact me in group chat! 
 * @author yuxiangma
 */


@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	@Query("select t from User t where t.username = :name")
	User findByUsername(@Param("name") String name);
	@Query("select t from User t where t.id = :id")
    User findByUID(@Param("id") Long id);
	@Modifying
    @Transactional
	@Query("delete from User u where u.id=:id")
    int deleteUserById(@Param("id") Long id);
	@Modifying    
	@Transactional    
	@Query("update User u set u.username = :unamenew where u.id=:id and u.username = :unameprev")    
	int changeUnameByUID(@Param("id") Long id, @Param("unameprev") String unameprev, @Param("unamenew") String unamenew);
	@Modifying    
	@Transactional    
	@Query("delete from User u where u.username=:name")    
	int deleteUserByUname(@Param("name") String uname);
}
