package ca.mcgill.ecse321.rideshare9;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import ca.mcgill.ecse321.rideshare9.controller.StopController;
import ca.mcgill.ecse321.rideshare9.controller.UserController;
import ca.mcgill.ecse321.rideshare9.entity.Stop;
import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.entity.UserStatus;
import ca.mcgill.ecse321.rideshare9.repository.StopRepository;
import ca.mcgill.ecse321.rideshare9.repository.UserRepository;
import ca.mcgill.ecse321.rideshare9.service.UserService;

import static org.junit.Assert.*;

import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.mockito.junit.MockitoJUnitRunner;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;

@RunWith(SpringRunner.class)
// @RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class Rideshare9ApplicationTests {

	@Mock
	private UserService userDao;

	@InjectMocks
	private UserController ucontroller;

	void setMockOutputListAllUser() {
		when(userDao.getUsers()).thenAnswer((InvocationOnMock invocation) -> {
			ArrayList<User> arrl = new ArrayList<User>();
			User u1 = new User();
			u1.setUsername("yuxiangma");
			u1.setRole("ROLE_PASSENGER");
			u1.setStatus(UserStatus.STANDBY);
			User u2 = new User();
			u2.setUsername("yudixie");
			u2.setRole("ROLE_DRIVER");
			u2.setStatus(UserStatus.ON_RIDE);
			arrl.add(u1);
			arrl.add(u2);
			return arrl;
		});
	}
	

	@Test
	public void testListAllUsr() {
		this.setMockOutputListAllUser();
		ArrayList<User> arrl = new ArrayList<User>();
		User u1 = new User();
		u1.setUsername("yuxiangma");
		User u2 = new User();
		u2.setUsername("yudixie");
		arrl.add(u1);
		arrl.add(u2);
		boolean b = true;
		int index = 0;
		for (User curr : ucontroller.userServiceList()) {
			b = (curr.getUsername().equals(arrl.get(index).getUsername())) & b;
			index = index + 1;
		}
		assertTrue(b);
	}
	@Test
	public void testListDriver() {
		ArrayList<User> arrl = new ArrayList<User>();
		User u1 = new User();
		u1.setUsername("yudixie");
		u1.setRole("ROLE_DRIVER");
		u1.setStatus(UserStatus.STANDBY);
		arrl.add(u1);
		boolean b = true;
		int index = 0;
		for (HashMap<String, UserStatus> curr : ucontroller.driverStatusList()) {
			for (String key: curr.keySet()) {
				
				b = (key.equals(arrl.get(index).getUsername())) & (curr.get(key)==arrl.get(index).getStatus()) & b;
			}
			
			index = index + 1;
		}
		assertTrue(b);
	}
	
	@Test
	public void testListPassenger() {
		ArrayList<User> arrl = new ArrayList<User>();
		User u1 = new User();
		u1.setUsername("yuxiangma");
		u1.setRole("ROLE_PASSENGER");
		u1.setStatus(UserStatus.STANDBY);
		arrl.add(u1);
		boolean b = true;
		int index = 0;
		List<HashMap<String, UserStatus>> ret = ucontroller.passengerStatusList(); 
		for (HashMap<String, UserStatus> curr : ret) {
			for (String key: curr.keySet()) {
				b = (key.equals(arrl.get(index).getUsername())) & (curr.get(key).equals(arrl.get(index).getStatus())) & b;
			}
			
			index = index + 1;
		}
		assertTrue(b&&(ret.size()>0));
	}

}
