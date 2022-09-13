package br.unisantos.model;

import java.util.List;

import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DirectionsGoogleApi {
	
	private String origin;
	private String destination;
	private TravelMode mode = TravelMode.WALKING;
	private Unit unit = Unit.METRIC;
	private List<String> waypoints;
	private Boolean optimizeWaypoints;
	private String region = "br";
	private String language = "pt-BR";
}