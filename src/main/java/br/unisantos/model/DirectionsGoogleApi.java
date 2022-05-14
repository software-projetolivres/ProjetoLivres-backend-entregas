package br.unisantos.model;

import java.util.List;

import com.google.maps.DirectionsApiRequest.Waypoint;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;

public class DirectionsGoogleApi {
	
	private String origin;
	private String destination;
	private TravelMode mode = TravelMode.BICYCLING;
	private Unit unit = Unit.METRIC;
	//private String[] waypoints;
	private String[] waypoints;
	private Boolean optimizeWaypoints = true;
	private String region = "br";
	
	public DirectionsGoogleApi() {
	}
	
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

	public String[] getWaypoints() {
		return waypoints;
	}

	public void setWaypoints(String[] waypoints) {
		this.waypoints = waypoints;
	}

	public Boolean getOptimizeWaypoints() {
		return optimizeWaypoints;
	}

	public void setOptimizeWaypoints(Boolean optimizeWaypoints) {
		this.optimizeWaypoints = optimizeWaypoints;
	}
	
	public String getRegion() {
		return region;
	}
	
	public void setRegion(String region) {
		this.region = region;
	}
}