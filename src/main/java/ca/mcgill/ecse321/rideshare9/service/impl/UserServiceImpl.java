package ca.mcgill.ecse321.rideshare9.service.impl;

import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.entity.UserStatus;
import ca.mcgill.ecse321.rideshare9.repository.UserRepository;
import ca.mcgill.ecse321.rideshare9.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

/**
 * user login service DO NOT CHANGE THIS
 */

/**
 * DO NOT EDIT IT ON YOUR OWN!!!
 * ATTENTION: DON'T EDIT ANY CLASS WHOSE NAME HAS "User" or "Security" or "service" or related! Otherwise, no one can log in this system anymore! 
 * if you have suggestions, please contact me in group chat! 
 * @author yuxiangma
 */


@Service
public class UserServiceImpl implements UserService {

	@Autowired
    UserRepository userRepository;
	
	@Autowired
    EntityManager em; 

    UserServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public User addUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }
    @Override
    public User findUserByUID(Long uid) {
    	return userRepository.findByUID(uid); 
    }
    @Override
    public User findUserByUsername(String uname) {
    	return userRepository.findByUsername(uname); 
    }
    @Override
    public int deleteUserByUID(Long uid) {
    	return userRepository.deleteUserById(uid); 
    }

    @Override
    public int deleteUserByUname(String uname) {
    	return userRepository.deleteUserByUname(uname); 
    }
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(username);
        }
        return user;
    }
    
    @Override
    @Transactional
    public User changeUserStatus(Long uid, UserStatus us) {
    	User usr = em.find(User.class, uid); 
    	usr.setStatus(us);
    	em.merge(usr); 
    	em.flush();
    	return usr; 
    }
    
}