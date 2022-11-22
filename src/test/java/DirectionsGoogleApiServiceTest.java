import static com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;
import com.google.maps.model.DirectionsResult;

import br.unisantos.model.DirectionsGoogleApi;
import br.unisantos.service.DirectionsGoogleApiService;

public class DirectionsGoogleApiServiceTest {

	@InjectMocks
	private DirectionsGoogleApiService directionsGoogleApiService;
	
	@Mock
	private DirectionsResult directionsResult;
	
	@BeforeEach
	public void beforeEach() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	void testAddLivresWaypoints() throws Exception {
		String jsonDirections = "{ \"geocoded_waypoints\": [{}],\"routes\": [ {\"copyrights\": \"Map data ©2022\",\"legs\": [" 
				+ "{\"startAddress\": \"R. Almeida de Moraes, 175 - Vila Matias, Santos - SP, 11015-450, Brazil\"},"
				+ "{\"startAddress\": \"Praça Palmares, 20 - Macuco, Santos - SP, 11015-330, Brazil\"},"
				+ "{\"startAddress\": \"R. Oswaldo Cruz, 218 - Boqueirão, Santos - SP, 11045-100, Brazil\"},"
				+ "{\"startAddress\": \"R. Oswaldo Cruz, 60 - Boqueirão, Santos - SP, 11045-100, Brazil\"}]}]}";
		
		List<String> waypoints = new ArrayList<String>();
		waypoints.add("tst");
		
		Gson gson = new Gson(); // Or use new GsonBuilder().create();
		DirectionsResult directionsResult = gson.fromJson(jsonDirections, DirectionsResult.class);
		DirectionsGoogleApi directionsAPI = new DirectionsGoogleApi();
		directionsAPI.setOrigin("end teste");
		directionsAPI.setDestination("end teste");
		directionsAPI.setOptimizeWaypoints(true);
		directionsAPI.setWaypoints(waypoints);
		
		//Necessário trocar o "SUA_KEY" para o valor da SUA_KEY para executar o teste.
		DirectionsResult resp = withEnvironmentVariable("google_api_key", "SUA_KEY")
			      .execute(() -> directionsGoogleApiService.addLivresWaypoints(directionsResult, directionsAPI));

		Assert.assertNotNull(resp);
	}
}
