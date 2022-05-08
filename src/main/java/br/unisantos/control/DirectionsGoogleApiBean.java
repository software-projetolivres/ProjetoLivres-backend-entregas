package br.unisantos.control;

import javax.annotation.ManagedBean;

import org.springframework.web.context.annotation.RequestScope;

import br.unisantos.model.DirectionsGoogleApi;

@ManagedBean
@RequestScope
public class DirectionsGoogleApiBean {

	private DirectionsGoogleApi directionsGoogleApi = new DirectionsGoogleApi();

	public DirectionsGoogleApi getDirectionsGoogleApi() {
		return directionsGoogleApi;
	}

	public void setDirectionsGoogleApi(DirectionsGoogleApi directionsGoogleApi) {
		this.directionsGoogleApi = directionsGoogleApi;
	}
}
