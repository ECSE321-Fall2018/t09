package ca.mcgill.ecse321.rideshare9;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.runners.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.mcgill.ecse321.rideshare9.controller.StopController;
import ca.mcgill.ecse321.rideshare9.entity.Stop;
import ca.mcgill.ecse321.rideshare9.repository.StopRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class Rideshare9ApplicationTests {
	private MockMvc mvc;
	@Mock private Stop stop;
	@Mock private StopRepository stopDao;
	@InjectMocks private StopController stopController;
	
	private JacksonTester<Stop> jsonStop;
	
	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		JacksonTester.initFields(this, new ObjectMapper());
		mvc = MockMvcBuilders.standaloneSetup(stopController).build();
	}
	
	@Test
	public void canCreateStop() throws Exception {
		//when(stopDao.createStop(anyString(), anyFloat())).thenReturn(null);
		Stop s2 = new Stop();
		s2.setPrice(1.1f);
		s2.setStopName("test");
		MvcResult response = mvc.perform(post("/stop/add-stop")
				.accept(MediaType.APPLICATION_JSON)
				.content(jsonStop.write(s2).getJson())
				).andReturn();
		assertNotNull(response);
	}
}
