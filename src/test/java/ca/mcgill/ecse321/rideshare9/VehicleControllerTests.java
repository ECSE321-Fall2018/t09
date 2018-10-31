package ca.mcgill.ecse321.rideshare9;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.mcgill.ecse321.rideshare9.controller.VehicleController;
import ca.mcgill.ecse321.rideshare9.entity.Advertisement;
import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.entity.UserStatus;
import ca.mcgill.ecse321.rideshare9.entity.Vehicle;
import ca.mcgill.ecse321.rideshare9.repository.VehicleRepository;
import ca.mcgill.ecse321.rideshare9.service.impl.UserServiceImpl;

@SpringBootTest
public class VehicleControllerTests {
	private MockMvc mvc;
	private User testDriver;
	private Vehicle testVehicle;
	private JacksonTester<Vehicle> jsonVehicle;
	private JacksonTester<List<Vehicle>> jsonVehicles;
	
	@Mock private UserServiceImpl userDao;
	@Mock private VehicleRepository vehicleDao;
	@InjectMocks private VehicleController vehicleController;
	
	public static class MockSecutiryContext implements SecurityContext {
		private static final long serialVersionUID = 3120472983579235L;
		
		private Authentication authentication;
		
		public MockSecutiryContext(Authentication authentication) {
			this.authentication = authentication;
		}
		
		@Override
		public Authentication getAuthentication() {
			return this.authentication;
		}
		
		@Override
		public void setAuthentication(Authentication authentication) {
			this.authentication = authentication;
		}
	}
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		JacksonTester.initFields(this, new ObjectMapper());
		mvc = MockMvcBuilders.standaloneSetup(vehicleController).build();
		//set up test driver
		testDriver = new User();
		testDriver.setId(555);
		testDriver.setUsername("testUsername");
		testDriver.setPassword("testPassword");
		testDriver.setRole("DRIVER");
		testDriver.setStatus(UserStatus.STANDBY);		
		//set up test Vehicle
		testVehicle = new Vehicle();
		testVehicle.setColor("Black");
		testVehicle.setDriver(555);
		testVehicle.setId(22);
		testVehicle.setLicencePlate("ABC123");
		testVehicle.setMaxSeat(5);
		testVehicle.setModel("Passat TDI");
	}
	
	@Test
	public void canCreateVehicle() throws Exception {
		UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("testUsername", "testPassword", testDriver.getAuthorities());
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(
				HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				new MockSecutiryContext(principal));
		SecurityContextHolder.getContext().setAuthentication(principal);
		
		String vehicleJson = jsonVehicle.write(testVehicle).getJson();
		
		when(userDao.findUserByUsername(anyString())).thenReturn(testDriver);
		when(vehicleDao.createVehicle(anyString(), anyString(), anyString(), anyInt(), anyLong())).thenReturn(testVehicle);
		
		MvcResult result = mvc.perform(post("/vehicle/add-car")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(vehicleJson))
				.andExpect(status().isOk())
				//.andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
		String responseContent = result.getResponse().getContentAsString();
		assertEquals(responseContent, vehicleJson);
	}
	
	@Test
	public void canDeleteVehicle() throws Exception {
		UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("testUsername", "testPassword", testDriver.getAuthorities());
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(
				HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				new MockSecutiryContext(principal));
		SecurityContextHolder.getContext().setAuthentication(principal);
		
		String vehicleJson = jsonVehicle.write(testVehicle).getJson();
		
		when(userDao.loadUserByUsername(anyString())).thenReturn(testDriver);
		
		MvcResult result = mvc.perform(delete("/vehicle/remove-car")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(vehicleJson))
				.andExpect(status().isOk())
				//.andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
		String responseContent = result.getResponse().getContentAsString();
		assertEquals(responseContent, vehicleJson);
		verify(vehicleDao).removeVehicle(22L);
	}
	
	@Test
	public void canGetVehiclesByUserId() throws Exception {
		Vehicle testVehicle2;
		testVehicle2 = new Vehicle();
		testVehicle2.setColor("White");
		testVehicle2.setDriver(555);
		testVehicle2.setId(23);
		testVehicle2.setLicencePlate("ABC124");
		testVehicle2.setMaxSeat(5);
		testVehicle2.setModel("Jetta TDI");
		
		UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("testUsername", "testPassword", testDriver.getAuthorities());
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(
				HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				new MockSecutiryContext(principal));
		SecurityContextHolder.getContext().setAuthentication(principal);
		
		List<Vehicle> vehicleList = new ArrayList<Vehicle>();
		vehicleList.add(testVehicle);
		vehicleList.add(testVehicle2);
		String vehiclesJson = jsonVehicles.write(vehicleList).getJson();
		
		when(userDao.loadUserByUsername(anyString())).thenReturn(testDriver);
		when(vehicleDao.findAllVehicleByUid(555)).thenReturn(vehicleList);
		
		MvcResult result = mvc.perform(get("/vehicle/get-cars"))
				.andExpect(status().isOk())
				//.andDo(print())
				.andReturn();
		String responseContent = result.getResponse().getContentAsString();
		assertEquals(responseContent, vehiclesJson);
	}
	
	@Test
	public void canUpdateVehicle() throws Exception {
		Vehicle testVehicle2;
		testVehicle2 = new Vehicle();
		testVehicle2.setColor("White");
		testVehicle2.setDriver(555);
		testVehicle2.setId(22);
		testVehicle2.setLicencePlate("ABC124");
		testVehicle2.setMaxSeat(5);
		testVehicle2.setModel("Jetta TDI");
		
		UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("testUsername", "testPassword", testDriver.getAuthorities());
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(
				HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				new MockSecutiryContext(principal));
		SecurityContextHolder.getContext().setAuthentication(principal);
		
		when(userDao.findUserByUsername(anyString())).thenReturn(testDriver);
		when(vehicleDao.findVehicle(22)).thenReturn(testVehicle);
		when(vehicleDao.updateVehicle(testVehicle)).thenReturn(testVehicle2);
		
		String vehicle2Json = jsonVehicle.write(testVehicle2).getJson();
		
		MvcResult result = mvc.perform(put("/vehicle/change-cars")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(vehicle2Json))
				//.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andReturn();
		String responseContent = result.getResponse().getContentAsString();
		assertEquals(responseContent, vehicle2Json);
	}
}
