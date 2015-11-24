package com.labs.nathan.ntbrookslab1;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.Iterator;
import java.util.Set;

/**
 * Created by Nathan on 10/4/2015.
 */
public class TracerActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            notify("onCreate NO state");
        } else {
            notify("onCreate WITH state");
            Set<String> keys = savedInstanceState.keySet();
            Iterator iter = keys.iterator();
            while(iter.hasNext())
                notify("key:" + (String)iter.next());
        }
    }

    public void notify(String msg) {
        String strClass = this.getClass().getName();
        String[] strings = strClass.split("\\.");
        Notification.Builder notibuild = new Notification.Builder(this);
            notibuild.setContentTitle(msg);
            notibuild.setAutoCancel(true).setSmallIcon(R.mipmap.ic_launcher);
            notibuild.setContentText(strings[strings.length-1]);
        Notification noti = notibuild.build();
        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify((int) System.currentTimeMillis(), noti);
    }

}
