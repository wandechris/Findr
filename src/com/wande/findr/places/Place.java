package com.wande.findr.places;

import java.io.Serializable;

import com.google.api.client.util.Key;

public class Place implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5353862537273895653L;

	@Key
	public String id;
	
	@Key
	public String name;
	
	@Key
	public String reference;
	
	@Key
	public String icon;
	
	@Key
	public String vicinity;
	
	@Key
	public Geometry geometry;
	
	@Key
	public String formatted_address;
	
	@Key
	public String formatted_phone_number;

	@Override
	public String toString() {
		return name + " - " + id + " - " + reference;
	}
	
	public static class Geometry implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 4403930114194635008L;
		@Key
		public Location location;
	}
	
	public static class Location implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = 8760889687736888733L;

		@Key
		public double lat;
		
		@Key
		public double lng;
	}	
}
