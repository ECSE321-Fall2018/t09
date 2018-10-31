package ca.mcgill.ecse321.rideshare9.service;
import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.entity.UserStatus;

import java.util.List;

/**
 * user login service DO NOT CHANGE THIS
 */

/**
 * DO NOT EDIT IT ON YOUR OWN!!!
 * ATTENTION: DON'T EDIT ANY CLASS WHOSE NAME HAS "User" or "Security" or "service" or related! Otherwise, no one can log in this system anymore! 
 * if you have suggestions, please contact me in group chat! 
 * @author yuxiangma
 */

public interface UserService {
	User changeUserStatus(Long uid, UserStatus us); 
	User addUser(User user);
    List<User> getUsers();
    // DO NOT CHANGE THIS!!!
    User loadUserByUsername(String name);
    User findUserByUID(Long uid); 
    User findUserByUsername(String uname); 
    int deleteUserByUID(Long uid); 
    int deleteUserByUname(String uname);
}