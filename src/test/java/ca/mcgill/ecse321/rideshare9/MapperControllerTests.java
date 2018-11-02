package ca.mcgill.ecse321.rideshare9;

import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.mcgill.ecse321.rideshare9.VehicleControllerTests.MockSecutiryContext;
import ca.mcgill.ecse321.rideshare9.controller.MapperController;
import ca.mcgill.ecse321.rideshare9.entity.Advertisement;
import ca.mcgill.ecse321.rideshare9.entity.MapperUserAdv;
import ca.mcgill.ecse321.rideshare9.entity.TripStatus;
import ca.mcgill.ecse321.rideshare9.entity.User;
import ca.mcgill.ecse321.rideshare9.entity.UserStatus;
import ca.mcgill.ecse321.rideshare9.entity.helper.MapperBestQuery;
import ca.mcgill.ecse321.rideshare9.repository.AdvertisementRepository;
import ca.mcgill.ecse321.rideshare9.repository.MapperUserAdvRepository;
import ca.mcgill.ecse321.rideshare9.service.UserService;

@SpringBootTest
public class MapperControllerTests {
	private MockMvc mvc;
	private User testPassenger;
	private User testAdmin;
	private MapperUserAdv mapper;
	private Advertisement adv;
	private MapperBestQuery mapperBestQuery;
	private List<MapperBestQuery> mapperBestQueryList;
	private JacksonTester<List<MapperBestQuery>> jsonMapperBestQuery;
	private JacksonTester<MapperUserAdv> jsonMapper;

	@Mock
	private AdvertisementRepository advRepo;
	@Mock
	private UserService userService;
	@Mock
	private MapperUserAdvRepository mapRepo;
	@InjectMocks
	private MapperController mapperController;

	public static class MockSecutiryContext implements SecurityContext {
		private static final long serialVersionUID = 31204723579235L;

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
		mvc = MockMvcBuilders.standaloneSetup(mapperController).build();
		// set up testPassenger
		testPassenger = new User();
		testPassenger.setId(1);
		testPassenger.setUsername("testPassenger");
		testPassenger.setPassword("testPassword");
		testPassenger.setRole("PASSENGER");
		testPassenger.setStatus(UserStatus.STANDBY);
		// set up testAdmin
		testAdmin = new User();
		testAdmin.setId(2);
		testAdmin.setUsername("testAdmin");
		testAdmin.setPassword("testPassword");
		testAdmin.setRole("ADMIN");
		testAdmin.setStatus(UserStatus.STANDBY);
		// set up mapper
		mapper = new MapperUserAdv();
		mapper.setAdvertisement(1);
		mapper.setId(1);
		mapper.setPassenger(1);
		// set up advertisement
		adv = new Advertisement();
		adv.setDriver(3);
		adv.setId(1);
		adv.setSeatAvailable(5);
		adv.setStartLocation("testLocation");
		adv.setStatus(TripStatus.REGISTERING);
		// set up mapperBestQuery
		mapperBestQuery = new MapperBestQuery();
		mapperBestQuery.setBest(testPassenger);
		mapperBestQuery.setCount(5L);
		// set up mapperBestQueryList
		mapperBestQueryList = new ArrayList<MapperBestQuery>();
		mapperBestQueryList.add(mapperBestQuery);
	}

	@Test
	public void canAddMap() throws Exception {
		UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("testPassenger",
				"testPassword", testPassenger.getAuthorities());
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				new MockSecutiryContext(principal));
		SecurityContextHolder.getContext().setAuthentication(principal);

		String mapperJson = jsonMapper.write(mapper).getJson();

		when(userService.findUserByUsername(anyString())).thenReturn(testPassenger);
		when(userService.loadUserByUsername(anyString())).thenReturn(testPassenger);
		when(advRepo.findAdv(anyLong())).thenReturn(adv);
		when(mapRepo.createMapper(anyLong(), anyLong())).thenReturn(mapper);

		MvcResult result = mvc.perform(post("/map/add-map?adv_id=1"))
				// .andDo(print())
				.andExpect(status().isOk()).andReturn();
		String responseContent = result.getResponse().getContentAsString();
		assertEquals(responseContent, mapperJson);
	}

	@Test
	public void canDeleteMap() throws Exception {
		UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("testAdmin",
				"testPassword", testAdmin.getAuthorities());
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				new MockSecutiryContext(principal));
		SecurityContextHolder.getContext().setAuthentication(principal);

		String mapperJson = jsonMapper.write(mapper).getJson();

		when(mapRepo.findMap(1)).thenReturn(mapper);

		MvcResult result = mvc.perform(post("/map/admin/delete/1"))
				// .andDo(print())
				.andExpect(status().isOk()).andReturn();
		String responseContent = result.getResponse().getContentAsString();
		assertEquals(responseContent, mapperJson);
		verify(mapRepo).removeVehicle(1);
	}

	@Test
	public void canGetTopPassengers() throws Exception {
		UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken("testAdmin",
				"testPassword", testAdmin.getAuthorities());
		MockHttpSession session = new MockHttpSession();
		session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
				new MockSecutiryContext(principal));
		SecurityContextHolder.getContext().setAuthentication(principal);

		String mapperBestQueryJson = jsonMapperBestQuery.write(mapperBestQueryList).getJson();

		when(mapRepo.findBestPassenger()).thenReturn(mapperBestQueryList);

		MvcResult result = mvc.perform(get("/map/list-top-passengers")).andDo(print()).andExpect(status().isOk())
				.andReturn();
		String responseContent = result.getResponse().getContentAsString();
		assertEquals(responseContent, mapperBestQueryJson);
	}
}
