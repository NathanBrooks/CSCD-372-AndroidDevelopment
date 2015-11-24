package com.labs.nathan.ntbrookslab6;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private static int [] imageArray = {
            R.drawable.andoria,
            R.drawable.bajor,
            R.drawable.betazed,
            R.drawable.cardassia,
            R.drawable.denobula,
            R.drawable.earth,
            R.drawable.ferenginar,
            R.drawable.fluidic,
            R.drawable.kronos,
            R.drawable.remus,
            R.drawable.romulus,
            R.drawable.suliban,
            R.drawable.talax,
            R.drawable.talos,
            R.drawable.academy,
    };


    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        // Inflate the layout for this fragment
        View newView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView nameView = (TextView)newView.findViewById(R.id.textName);
        TextView replyView = (TextView)newView.findViewById(R.id.textReply);
        ImageView imageView = (ImageView)newView.findViewById(R.id.imageView);


        nameView.setText("Hello " + prefs.getString("name", "REDACTED"));

        if(prefs.getBoolean("is_student", false)) {
            replyView.setText("We wish you success during your " + prefs.getString("years_remaining", "REDACTED") + " years at the Academy.");
            imageView.setImageResource(imageArray[new Integer(prefs.getString("home_world", "14"))]);
        } else {
            replyView.setText("Welcome Back!");
            imageView.setImageResource(imageArray[14]);
        }


        return newView;
    }


}
