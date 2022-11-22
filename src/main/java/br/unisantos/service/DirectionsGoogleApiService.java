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

	/* Método responsável por devolver o DirectionsResult de acordo com o corpo da
	 * requisição (DirectionsGoogleApi) passado */
	public DirectionsResult directionsApiGoogle(DirectionsGoogleApi api) throws ApiException, InterruptedException, IOException {
		directionsResult = directionsApiGoogleRes(api);
		return addLivresWaypoints(directionsResult, api); 
	}
	
	/* Método responsável por devolver o DirectionsResult de acordo com o corpo da
	 * requisição (DirectionsGoogleApi) passado e com o DirectionsResult já 
	 * processado */
	public DirectionsResult addLivresWaypoints(DirectionsResult directionsResult, DirectionsGoogleApi api) throws ApiException, InterruptedException, IOException {
		String enderecoLivres = "Almeida de Moraes 175, Vila Mathias, Santos SP";
		List<String> waypoints = new ArrayList<String>();
		api.setOptimizeWaypoints(false);
		
		// Realiza a lógica de montagem da rota: a cada 3 entregas, a próxima é um retorno à sede do Livres
		for(int i = 0; i < directionsResult.routes[0].legs.length; i++) {
			if(i > 0 && i%3 == 0) {
				waypoints.add(directionsResult.routes[0].legs[i].startAddress);
				waypoints.add(enderecoLivres);
			} else if(i > 0) {
				waypoints.add(directionsResult.routes[0].legs[i].startAddress);
			}
		}
		
		api.setWaypoints(waypoints);
		return directionsApiGoogleRes(api);
	}
	
	/* Método responsável por realizar a requisição ao Google Maps de acordo com o
	 * corpo passado, devolvendo um Directions Result */
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
