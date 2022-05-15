package br.unisantos.service;

import java.io.IOException;

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
		GeoApiContext contexto = new GeoApiContext.Builder().apiKey("AIzaSyAiLHYgsN3ViWTyicWXARZlXGWtzz31pbY")// AIzaSyCS0H1p4PbnrfIt6KlT9y9EqQmEZKdce_8
				.build();

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
