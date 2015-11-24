package com.labs.nathan.ntbrookslab5;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DetailFragment extends Fragment {

    private String display_string;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View newView = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView myText = (TextView) newView.findViewById(R.id.fragment_text);

        if(getArguments() != null)
            myText.setText(getArguments().getString("input_string"));

        if (display_string != null)
            myText.setText(display_string);

        return newView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void setDisplayString(String input) {
        display_string = input;
        View myView = getView();
        if(myView != null) {
            TextView displayText = (TextView)myView.findViewById(R.id.fragment_text);
            displayText.setText(display_string);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
