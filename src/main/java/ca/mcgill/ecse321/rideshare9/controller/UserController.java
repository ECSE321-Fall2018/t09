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

import java.util.List;


@RestController
@RequestMapping("/crud")
public class UserController {
	@Autowired
    private UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }

    /**
     * retrive
     * @return
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSSLI')")
    @GetMapping("/list")
    public List<User> userServiceList(){
        return userService.getUsers();
    }
	
    /**
     * retrive 
     * @param username
     * @return
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSSLI')")
    @GetMapping("/{username}")
    public User userProfile(@PathVariable String username){
        return userService.loadUserByUsername(username);
    }
    /**
     * retrive current
     * @param 
     * @return username
     */
    @PreAuthorize("hasRole('ADMIN') or hasRole('BOSSLI')")
    @GetMapping("/get-current-uid")
    public String userIDnow(){
    	String currentUserName = ""; 
    	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    	if (!(authentication instanceof AnonymousAuthenticationToken)) {
    	    currentUserName = authentication.getName();
    	}
        return currentUserName;
    }
    /**
     * retrive 
     * @param username
     * @return
     */
    @PreAuthorize("hasRole('PASSENGER') or hasRole('BOSSLI')")
    @GetMapping("/hello")
    public String helloW(){
        return "Ok!";
    }
    

    /**
     * create
     * @param user
     */
    @PostMapping("/sign-up")
    public User signUp(@RequestBody User user) {
        user.setPassword(DigestUtils.md5DigestAsHex((user.getPassword()).getBytes()));
        user.setStatus(UserStatus.STANDBY);
        return userService.addUser(user);
    }
    
    /**
     * delete
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