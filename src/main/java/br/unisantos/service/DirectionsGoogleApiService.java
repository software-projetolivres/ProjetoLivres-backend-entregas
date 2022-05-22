package br.unisantos.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;

import br.unisantos.model.DirectionsGoogleApi;

@Service
public class DirectionsGoogleApiService {

	private DirectionsResult directionsResult = new DirectionsResult();

	public DirectionsResult directionsApiGoogle(DirectionsGoogleApi api) throws ApiException, InterruptedException, IOException {
		directionsResult = directionsApiGoogleRes(api);

		return addLivresWaypoints(directionsResult, api);
	}
	
	public DirectionsResult addLivresWaypoints(DirectionsResult directionsResult, DirectionsGoogleApi api) throws ApiException, InterruptedException, IOException {
		String enderecoLivres = "Almeida de Moraes 175, Vila Mathias, Santos SP";
		List<String> waypoints = new ArrayList<String>();
		api.setOptimizeWaypoints(false);
		
		for(int i = 1; i < directionsResult.routes[0].legs.length; i++) {
			if(i > 0 && i%3 == 0) {
				waypoints.add(enderecoLivres);
				if(i == (directionsResult.routes[0].legs.length - 1)) {
					api.setDestination(directionsResult.routes[0].legs[i].startAddress);
				} else {
					waypoints.add(directionsResult.routes[0].legs[i].startAddress);
				}
			} else if(i > 0) {
				waypoints.add(directionsResult.routes[0].legs[i].startAddress);
			}
		}
		
		return directionsApiGoogleRes(api);
	}
	
	public DirectionsResult directionsApiGoogleRes(DirectionsGoogleApi api) throws ApiException, InterruptedException, IOException {
		GeoApiContext contexto = new GeoApiContext.Builder().apiKey(System.getenv("google_api_key")).build();

		DirectionsApiRequest directions = new DirectionsApiRequest(contexto);
		directionsResult = directions.origin(api.getOrigin())
			.destination(api.getDestination())
			.mode(api.getMode())
			.units(api.getUnit())
			.region(api.getRegion())
			.language(api.getLanguage())
			.waypoints(api.getWaypoints().toArray(new String[api.getWaypoints().size()]))
			.optimizeWaypoints(api.getOptimizeWaypoints()).await();

		return directionsResult;
	}
}
