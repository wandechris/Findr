package com.wande.findr;

import android.content.Intent;
import android.os.Bundle;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockListFragment;
import com.wande.findr.fragments.PlaceList;


public class PlaceListFragment extends SherlockListFragment {

	
	private static View view;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        if (container == null) {
        	 // We have different layouts, and in one of them this
            // fragment's containing frame doesn't exist.  The fragment
            // may still be created from its saved state, but there is
            // no reason to try to create its view hierarchy because it
            // won't be displayed.  Note this is not needed -- we could
            // just run the code below, where we would create and return
            // the view hierarchy; it would just never be used.
            return null;
        }
        if (view != null) {
	        ViewGroup parent = (ViewGroup) view.getParent();
	        if (parent != null)
	            parent.removeView(view);
	    }
	    try {
	        view = (LinearLayout)inflater.inflate(R.layout.place_list, container, false);
	    } catch (InflateException e) {
	        /* care group is already there, just return view as it is */
	    }
        return view;
    }	
	
	/*@Override
	public void onItemSelected(String reference) {
		// TODO Auto-generated method stub
		Intent intent = new Intent(this, PlaceViewActivity.class);
		 Bundle b = new Bundle();
		 b.putString("reference", reference);
		 intent.putExtras(b); 
		 startActivity(intent);
		Toast.makeText(this,"you are connected.", Toast.LENGTH_LONG).show(); 
		
	}*/
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		 String reference = PlaceList.placesListItems.get(position).get(PlaceList.KEY_REFERENCE); 
		 Intent intent = new Intent(getActivity(), PlaceViewActivity.class);
		 Bundle b = new Bundle();
		 b.putString("reference", reference);
		 intent.putExtras(b); 
		 startActivity(intent);
		// getListView().setItemChecked(position, true);
		// getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE); 
	}

}
