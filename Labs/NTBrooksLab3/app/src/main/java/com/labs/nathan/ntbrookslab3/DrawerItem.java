package com.labs.nathan.ntbrookslab3;

/**
 * Created by Nathan on 10/18/2015.
 */
public class DrawerItem {
    int resourceID;
    String descriptionText;

    public DrawerItem(int resource, String description) {
        resourceID = resource;
        descriptionText = description;
    }

    public DrawerItem(String description) {
        this(R.drawable.logo, description);
    }

    public int getResourceID() {
        return this.resourceID;
    }

    public String getDescriptionText() {
        return this.descriptionText;
    }

}
