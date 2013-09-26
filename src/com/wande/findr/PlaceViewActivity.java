package com.wande.findr;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;
import com.wande.findr.managers.AlertDialogManager;
import com.wande.findr.managers.ConnectionDetector;
import com.wande.findr.places.GooglePlaces;
import com.wande.findr.places.PlaceDetails;

public class PlaceViewActivity extends SherlockActivity {
	
	Button btn_direction;
	Button btn_share;
	Button btn_map;
	
	TextView txtName;
	TextView txtAddress;
	TextView txtPhone;
	
	// flag for Internet connection status
	 	Boolean isInternetPresent = false;

	 	// Connection detector class
	 	ConnectionDetector cd;
	 	
	 	// Alert Dialog Manager
	 	AlertDialogManager alert = new AlertDialogManager();

	 	// Google Places
	 	GooglePlaces googlePlaces;
	 	
	 	// Place Details
	 	PlaceDetails placeDetails;
	 	
	 	// Progress dialog
	 	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.place_view);
		
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		txtName = (TextView)findViewById(R.id.place_view_name);
		txtAddress = (TextView)findViewById(R.id.place_view_address);
		txtPhone = (TextView)findViewById(R.id.place_view_phone);
		
		Bundle b = getIntent().getExtras();
		String reference = b.getString("reference");
		
		new LoadSinglePlaceDetails().execute(reference);
	}
	
	
	/**
	 * Background Async Task to Load Google places
	 * */
	class LoadSinglePlaceDetails extends AsyncTask<String, String, String> {

		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(PlaceViewActivity.this);
			pDialog.setMessage("Loading Place profile ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		/**
		 * getting Profile JSON
		 * */
		protected String doInBackground(String... args) {
			String reference = args[0];
			
			// creating Places class object
			googlePlaces = new GooglePlaces();

			// Check if used is connected to Internet
			try {
				placeDetails = googlePlaces.getPlaceDetails(reference);

			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			PlaceViewActivity.this.runOnUiThread(new Runnable() {
				public void run() {
					/**
					 * Updating parsed Places into LISTVIEW
					 * */
					if(placeDetails != null){
						String status = placeDetails.status;
						
						// check place deatils status
						// Check for all possible status
						if(status.equals("OK")){
							if (placeDetails.result != null) {
								String name = placeDetails.result.name;
								String address = placeDetails.result.formatted_address;
								String phone = placeDetails.result.formatted_phone_number;
								String latitude = Double.toString(placeDetails.result.geometry.location.lat);
								String longitude = Double.toString(placeDetails.result.geometry.location.lng);
								
								Log.d("Place ", name + address + phone + latitude + longitude);
								
								// Check for null data from google
								// Sometimes place details might missing
								name = name == null ? "Not present" : name; // if name is null display as "Not present"
								address = address == null ? "Not present" : address;
								phone = phone == null ? "Not present" : phone;
								latitude = latitude == null ? "Not present" : latitude;
								longitude = longitude == null ? "Not present" : longitude;
								
								txtName.setText(name);
								txtAddress.setText(address);
								txtPhone.setText(phone);
							}
						}
						else if(status.equals("ZERO_RESULTS")){
							alert.showAlertDialog(PlaceViewActivity.this, "Near Places",
									"Sorry no place found.",
									false);
						}
						else if(status.equals("UNKNOWN_ERROR"))
						{
							alert.showAlertDialog(PlaceViewActivity.this, "Findr Error",
									"Sorry unknown error occured.",
									false);
						}
						else if(status.equals("OVER_QUERY_LIMIT"))
						{
							alert.showAlertDialog(PlaceViewActivity.this, "Findr Error",
									"Sorry query limit to google places is reached",
									false);
						}
						else if(status.equals("REQUEST_DENIED"))
						{
							alert.showAlertDialog(PlaceViewActivity.this, "Findr Error",
									"Sorry error occured. Request is denied",
									false);
						}
						else if(status.equals("INVALID_REQUEST"))
						{
							alert.showAlertDialog(PlaceViewActivity.this, "Findr Error",
									"Sorry error occured. Invalid Request",
									false);
						}
						else
						{
							alert.showAlertDialog(PlaceViewActivity.this, "Findr Error",
									"Sorry error occured.",
									false);
						}
					}else{
						alert.showAlertDialog(PlaceViewActivity.this, "Findr Error",
								"Sorry error occured.",
								false);
					}
					
					
				}
			});

		}

	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

        case android.R.id.home:
             finish();
             break;

        default:
            return super.onOptionsItemSelected(item);
        }
        return false;
    }

}
