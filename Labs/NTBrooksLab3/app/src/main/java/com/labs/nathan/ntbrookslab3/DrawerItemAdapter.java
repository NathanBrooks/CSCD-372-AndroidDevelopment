package com.labs.nathan.ntbrookslab3;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Nathan on 10/18/2015.
 */
public class DrawerItemAdapter extends ArrayAdapter<DrawerItem> {

    Context myContext;
    int resourceID;
    DrawerItem[] drawerItems;

    public DrawerItemAdapter(Context newContext, int newResourceID, DrawerItem[] newDrawerItems){
        super(newContext, newResourceID, newDrawerItems);
        myContext = newContext;
        resourceID = newResourceID;
        drawerItems = newDrawerItems;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView == null){
            LayoutInflater inflater = ((Activity)myContext).getLayoutInflater();
            convertView = inflater.inflate(R.layout.nav_list_row, null);
        }
        ((TextView)convertView.findViewById(R.id.drawer_text_view)).setText(drawerItems[position].descriptionText);
        ((ImageView)convertView.findViewById(R.id.drawer_image_view)).setImageResource(drawerItems[position].resourceID);
        return convertView;
    }

}
