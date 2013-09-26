package com.wande.findr.places;

import java.io.Serializable;

import com.google.api.client.util.Key;

public class PlaceDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5249159272958928378L;

	@Key
	public String status;
	
	@Key
	public Place result;

	@Override
	public String toString() {
		if (result!=null) {
			return result.toString();
		}
		return super.toString();
	}
}
