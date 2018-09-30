package ca.mcgill.ecse321.rideshare9.controller;

import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.entity.UserStatus;
import ca.mcgill.ecse321.rideshare9.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * DO NOT EDIT IT ON YOUR OWN!!!
 * ATTENTION: DON'T EDIT ANY CLASS WHOSE NAME HAS "User" or "Security" or "service" or related! Otherwise, no one can log in this system anymore! 
 * if you have suggestions, please contact me in group chat! 
 * @author yuxiangma
 */

@RestController
@RequestMapping("/crud")
public class UserController {
	@Autowired
    private UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * Admin: Retrive all user profiles
     * Core API endpoint: Admin-1 in README.md at Mark branch
     * @return List<User>
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSSLI')")
    @GetMapping("/list-users")
    public List<User> userServiceList(){    	
        return userService.getUsers();
    }
    
    /**
     * Admin: Retrive all user profiles, Modify this if you would like just user status
     * Core API endpoint: Admin-1 in README.md at Mark branch
     * @return List<User>
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSSLI')")
    @GetMapping("/list-status-driver")
    public List<HashMap<String, UserStatus>> driverStatusList(){

    	ArrayList<HashMap<String, UserStatus>> arrl = new ArrayList<HashMap<String, UserStatus>>(); 
    	List<User> allU = userService.getUsers(); 
    	
    	for (User usr: allU) {
    		if (usr.getRole().equals("ROLE_DRIVER")) {
    			HashMap<String, UserStatus> curr = new HashMap<String, UserStatus>();
    			curr.put(usr.getUsername(), usr.getStatus()); 
    			arrl.add(curr); 
    		}
    			 
    	}  	
        return arrl;
    }
    /**
     * Admin: Retrive all user profiles, Modify this if you would like just user status
     * Core API endpoint: Admin-1 in README.md at Mark branch
     * @return List<User>
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSSLI')")
    @GetMapping("/list-status-passenger")
    public List<HashMap<String, UserStatus>> passengerStatusList(){

    	ArrayList<HashMap<String, UserStatus>> arrl = new ArrayList<HashMap<String, UserStatus>>(); 
    	List<User> allU = userService.getUsers(); 
    	
    	for (User usr: allU) {
    		if (usr.getRole().equals("ROLE_PASSENGER")) {
    			HashMap<String, UserStatus> curr = new HashMap<String, UserStatus>();
    			curr.put(usr.getUsername(), usr.getStatus()); 
    			arrl.add(curr); 
    		}
    			 
    	}  	
        return arrl;
    }
	
    /**
     * Admin: retrive user profile of a user
     * @param username
     * @return User
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSSLI')")
    @GetMapping("/{username}")
    public User userProfile(@PathVariable String username){
        return userService.loadUserByUsername(username);
    }
    
    /**
     * All registered user: retrive current user profile of current logged in user
     * @param void
     * @return User
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('DRIVER') or hasRole('ADMIN') or hasRole('BOSSLI')")
    @GetMapping("/get-current-uid")
    public User userIDnow(){
    	String currentUserName = ""; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
    	}
        return userService.findUserByUsername(currentUserName);
    }
    
    /**
     * HelloWorld, to test role intercepting/authorization 
     * @param 
     * @return str("Ok")
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('BOSSLI')")
    @GetMapping("/hello")
    public String helloW(){
        return "Ok!";
    }
    

    /**
     * All people: Signup
     * Bonus Poing
     * @param User
     */
    @PostMapping("/sign-up")
    public User signUp(@RequestBody User user) {
        user.setPassword(DigestUtils.md5DigestAsHex((user.getPassword()).getBytes()));
        user.setStatus(UserStatus.STANDBY);
        return userService.addUser(user);
    }
    
    /**
     * Admin: delete user
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSSLI')")
    @DeleteMapping("/admin/delete/{uid}")
    public int deleteUser(@PathVariable String uid){
    	Long userid = -1L; 
    	String username=""; 
    	try {
    		userid = Long.valueOf(uid); 
    		return userService.deleteUserByUID(userid);
    	} catch (Exception e){
    		username = uid; 
    		return userService.deleteUserByUname(username);
    	}
         
    }
}