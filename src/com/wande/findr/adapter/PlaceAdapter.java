package com.wande.findr.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.wande.findr.R;
import com.wande.findr.fragments.PlaceList;
import com.wande.findr.image.ImageLoader;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PlaceAdapter extends BaseAdapter {
    
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader; 
    
    public PlaceAdapter(Activity a, ArrayList<HashMap<String, String>> placesListItems) {
        activity = a;
        data=placesListItems;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader=new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.place_list_item, null);

        TextView name=(TextView)vi.findViewById(R.id.list_name);
        TextView address=(TextView)vi.findViewById(R.id.list_vic);
        ImageView image=(ImageView)vi.findViewById(R.id.list_image);
        
        name.setText(data.get(position).get(PlaceList.KEY_NAME));
        address.setText(data.get(position).get(PlaceList.KEY_VICINITY));
        imageLoader.DisplayImage(data.get(position).get(PlaceList.KEY_ICON), image);
        return vi;
    }
}