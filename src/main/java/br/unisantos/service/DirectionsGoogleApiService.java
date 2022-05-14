package br.unisantos.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;

import br.unisantos.model.DirectionsGoogleApi;

@Service
public class DirectionsGoogleApiService {

	private DirectionsResult directionsResult = new DirectionsResult();

	public DirectionsResult directionsApiGoogle(DirectionsGoogleApi api) throws ApiException, InterruptedException, IOException {
		GeoApiContext contexto = new GeoApiContext.Builder().apiKey("AIzaSyAiLHYgsN3ViWTyicWXARZlXGWtzz31pbY")// AIzaSyCS0H1p4PbnrfIt6KlT9y9EqQmEZKdce_8
				.build();

		/*
		 * DirectionsResult result = DirectionsApi.newRequest(contexto)
		 * .origin(api.getOrigin()) .destination(api.getDestination())
		 * .mode(api.getMode()) .units(api.getUnit()) .region(api.getRegion())
		 * .waypoints("Av Afonso Pena, 22 Apto 22 Baixada Santista",
		 * "Rua Luis Gama 205 santos") .optimizeWaypoints(api.getOptimizeWaypoints())
		 * .await();
		 */

		DirectionsApiRequest directions = new DirectionsApiRequest(contexto);
		directions.origin(api.getOrigin())
			.destination(api.getDestination())
			.mode(api.getMode())
			.units(api.getUnit())
			.region(api.getRegion())
			.waypoints("Av Afonso Pena, 22 Apto 22 Baixada Santista", "Rua Luis Gama 205 santos")
			.optimizeWaypoints(api.getOptimizeWaypoints());
		
		System.out.println("GAAAAAAAABS => " + directions.toString());
		
		//DirectionsResult directionsResult = new DirectionsResult();
		
		directions.setCallback(new PendingResult.Callback<DirectionsResult>() {
		  @Override
		  public void onResult(DirectionsResult result) {
		    System.out.println("GAAAAAAAAAABS directions: distance => " + result.routes[0].legs[0].startAddress);
		    directionsResult = result;
		    
		    //adicionar polylines ao mapa
		  }

		  @Override
		  public void onFailure(Throwable e) {
		    // Handle error.
		  }
		});

		return directionsResult;
		//return "yey";
		// return result.toString();
	}
	
}
