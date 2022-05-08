package br.unisantos.model;

import com.google.maps.GeoApiContext;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

public class DirectionsGoogleApi {
	
	private String origin;
	private String destination;
	private TravelMode mode;
	private Unit unit; //METRIC
	private String waypoints;
	private Boolean optimizeWaypoints; //TRUE

	GeoApiContext context = new GeoApiContext.Builder()
			.apiKey("AIzaSyCS0H1p4PbnrfIt6KlT9y9EqQmEZKdce_8")
		    .build();
	
	public String getOrigin() {
		return origin;
	}

	public void setOrigin(String origin) {
		this.origin = origin;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public TravelMode getMode() {
		return mode;
	}

	public void setMode(TravelMode mode) {
		this.mode = mode;
	}

	public Unit getUnit() {
		return unit;
	}

	public void setUnit(Unit unit) {
		this.unit = unit;
	}

	public String getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(String waypoints) {
		this.waypoints = waypoints;
	}

	public Boolean getOptimizeWaypoints() {
		return optimizeWaypoints;
	}

	public void setOptimizeWaypoints(Boolean optimizeWaypoints) {
		this.optimizeWaypoints = optimizeWaypoints;
	}

	public DirectionsGoogleApi() {
	}
}