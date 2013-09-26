package com.wande.findr.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.wande.findr.adapter.PlaceAdapter;
import com.wande.findr.location.GPSTracker;
import com.wande.findr.managers.AlertDialogManager;
import com.wande.findr.managers.ConnectionDetector;
import com.wande.findr.places.GooglePlaces;
import com.wande.findr.places.Place;
import com.wande.findr.places.PlacesList;

public class PlaceList extends SherlockListFragment {
	
	// flag for Internet connection status
			Boolean isInternetPresent = false;

			// Connection detector class
			ConnectionDetector cd;
			
			// Alert Dialog Manager
			AlertDialogManager alert = new AlertDialogManager();

			// Google Places
			GooglePlaces googlePlaces;

			// Places List
			PlacesList nearPlaces;

			// GPS Location
			GPSTracker gps;

			// Button
			//Button btnShowOnMap;

			// Progress dialog
			ProgressDialog pDialog;
			
			public PlaceAdapter adapter2;
					
			// ListItems data
			public static ArrayList<HashMap<String, String>> placesListItems = new ArrayList<HashMap<String,String>>();
			
			
			// KEY Strings
			public static String KEY_REFERENCE = "reference"; // id of the place
			public static String KEY_NAME = "name"; // name of the place
			//public static String KEY_ADDRESS = "name"; // name of the place
			public static String KEY_ICON = "icon"; // name of the place
			public static String KEY_VICINITY = "vicinity"; // Place area name
			//String typee = ParamSupply.type;
			int radii = 1000;//ParamSupply.radius;
			Intent intent = new Intent();
			TextView txtName;
			public static String type;
			
			public interface OnItemSelectedListener {
		        //public void onItemSelected(int position);
				public void onItemSelected(String reference);
				
		    }
			
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cd = new ConnectionDetector(getActivity());
		
		
		// Check if Internet present
				isInternetPresent = cd.isConnectingToInternet();
				if (!isInternetPresent) {
					// Internet Connection is not present
					alert.showAlertDialog(getActivity(), "Internet Connection Error",
							"Please connect to working Internet connection", false);
					// stop executing code by return
					return;
				}
				
				// creating GPS Class object
				gps = new GPSTracker(getActivity());

				// check if GPS location can get
				if (gps.canGetLocation()) {
					Log.d("Your Location", "latitude:" + gps.getLatitude() + ", longitude: " + gps.getLongitude());
				} else {
					// Can't get user's current location
					alert.showAlertDialog(getActivity(), "GPS Status",
							"Couldn't get location information. Please enable GPS",
							false);
					// stop executing code by return
					return;
				}
				
				
				final LoadPlaces n = new LoadPlaces();
				n.execute();
				
				Handler handler = new Handler();
				handler.postDelayed(new Runnable()
				{
				  @Override
				  public void run() {
				      try {
						if (n.getStatus() == AsyncTask.Status.RUNNING)
							alert.showAlertDialog(getActivity(), "Time out",
									"check ur connection", false);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				  }
				}, 50000 );
				
				

							
		
	}
	
	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadPlaces extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage(Html.fromHtml("<b>Search</b><br/>Loading Places..."));
			pDialog.setIndeterminate(false);
			//pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Places JSON
		 * */
		protected String doInBackground(String... args) {
			// creating Places class object
			googlePlaces = new GooglePlaces();
			
			try {
				
				String types = type; 
				double radius = radii;
				// get nearest places
				nearPlaces = googlePlaces.search(gps.getLatitude(),
						gps.getLongitude(), radius, types);
				

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * and show the data in UI
		 * Always use runOnUiThread(new Runnable()) to update UI from background
		 * thread, otherwise you will get error
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					// Get json response status
					String status = nearPlaces.status;
					
					// Check for all possible status
					if(status.equals("OK")){
						// Successfully got places details
						if (nearPlaces.results != null) {
							// loop through each place
							for (Place p : nearPlaces.results) {
								HashMap<String, String> map = new HashMap<String, String>();
								
								// Place reference won't display in listview - it will be hidden
								// Place reference is used to get "place full details"
								map.put(KEY_REFERENCE, p.reference);
								
								// Place name
								map.put(KEY_NAME, p.name);
								map.put(KEY_VICINITY, p.vicinity);
								map.put(KEY_ICON, p.icon);
								
								
								
								// adding HashMap to ArrayList
								placesListItems.add(map);
							}
							adapter2=new PlaceAdapter(getActivity(), placesListItems);
							// Adding data into listview
							setListAdapter(adapter2);
						}
					}
					else if(status.equals("ZERO_RESULTS")){
						// Zero results found
						alert.showAlertDialog(getActivity(), "Near Places",
								"Sorry no places found. Try to change the type of places or increase your search radius",
								false);
					}
					else if(status.equals("UNKNOWN_ERROR"))
					{
						alert.showAlertDialog(getActivity(), "Findr Error",
								"oops... Try again",
								false);
					}
					else if(status.equals("OVER_QUERY_LIMIT"))
					{
						alert.showAlertDialog(getActivity(), "Findr Error",
								"Sorry query limit to google places is reached. try again tommorrow",
								false);
					}
					else if(status.equals("REQUEST_DENIED"))
					{
						alert.showAlertDialog(getActivity(), "Findr Error",
								"Sorry error occured. Request is denied. try again",
								false);
					}
					
					else if(status.equals("INVALID_REQUEST"))
					{
						alert.showAlertDialog(getActivity(), "Findr Error",
								"Try to change the type of places or increase your search radius",
								false);
					}
					
					else
					{
						alert.showAlertDialog(getActivity(), "Places Error",
								"Sorry error occured try again.",
								false);
					}
				}
			});

		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(pDialog!=null)
			if(pDialog.isShowing()){
				pDialog.cancel();
			}
	}	

}
