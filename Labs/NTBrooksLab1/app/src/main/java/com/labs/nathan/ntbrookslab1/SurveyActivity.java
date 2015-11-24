package com.labs.nathan.ntbrookslab1;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SurveyActivity extends AppCompatActivity {

    String userName;
    Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey);

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");

        ((TextView)findViewById(R.id.NameTextView)).setText("Hello " + userName);

        btnSubmit = (Button)findViewById(R.id.SubmitButton);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int age = Integer.parseInt(String.valueOf(((EditText) findViewById(R.id.AgeInput)).getText()));
                    Intent result = new Intent();
                    result.putExtra("userAge", age);
                    setResult(Activity.RESULT_OK, result);
                    finish();
                } catch(NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Please enter a valid age", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
