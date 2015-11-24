package com.labs.nathan.ntbrookslab1;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends TracerActivity {

    Button btnSurvey;
    Button btnWebsite;
    int returnAge;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* initialize locals */
        returnAge = 0;

        Intent toKill = getIntent();

        if(Intent.ACTION_SEND.equals(toKill.getAction()) && toKill.getType() != null){
            if("text/plain".equals(toKill.getType())) {
                handleSendText(toKill);
            }
        }

        btnSurvey = (Button) findViewById(R.id.SurveyButton);
        btnWebsite = (Button) findViewById(R.id.WebsiteButton);

        btnSurvey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String userName = String.valueOf(((EditText) findViewById(R.id.NameField)).getText());

                if(userName.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    Intent intent = new Intent(getApplicationContext(), SurveyActivity.class);
                    intent.putExtra("userName", userName);
                    startActivityForResult(intent, 0);
                }
            }
        });

        btnWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Uri paulsite = Uri.parse("https://sites.google.com/site/pschimpf99/");
                Intent intent = new Intent(Intent.ACTION_VIEW, paulsite);
                startActivity(intent);
            }
        });
    }

    private void handleSendText(Intent intent) {
        String inputText = intent.getStringExtra(Intent.EXTRA_TEXT);
        if(intent.getStringExtra(Intent.EXTRA_TEXT)!= null){
            ((TextView)findViewById(R.id.ResultsText)).setText(inputText);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == 0 && resultCode == Activity.RESULT_OK){
            returnAge = data.getIntExtra("userAge", 0);
            if(returnAge < 40) {
                ((TextView)findViewById(R.id.ResultsText)).setText("You are under 40, so you're trustworthy");
            } else {
                ((TextView) findViewById(R.id.ResultsText)).setText("NOT under 40, so you're NOT trustworthy");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.ActionAbout) {
            Toast.makeText(getApplicationContext(), "Android Lab 1, Nathan T. Brooks", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
}
