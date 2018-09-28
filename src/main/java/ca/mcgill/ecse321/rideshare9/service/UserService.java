package ca.mcgill.ecse321.rideshare9.service;
import ca.mcgill.ecse321.rideshare9.entity.User;

import java.util.List;

/**
 * user login service DO NOT CHANGE THIS
 */


public interface UserService {
	User addUser(User user);
    List<User> getUsers();
    // DO NOT CHANGE THIS!!!
    User loadUserByUsername(String name);
    User findUserByUID(Long uid); 
    User findUserByUsername(String uname); 
    int deleteUserByUID(Long uid); 
    int deleteUserByUname(String uname);
}