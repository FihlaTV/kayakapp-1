package com.boom.kayakapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.boom.kayakapp.R;

public class SingleContactActivity  extends Activity {
	
	// JSON node keys
    private static final String TAG_NAME = "name";
    private static final String TAG_SITE = "site";
    private static final String TAG_LOGOURL = "logoURL";
    private static final String TAG_PHONE = "phone";
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_contact);
        
        // getting intent data
        Intent in = getIntent();
        
        // Get JSON values from previous intent
        String name = in.getStringExtra(TAG_NAME);
        String phone = in.getStringExtra(TAG_PHONE);
        String site = in.getStringExtra(TAG_SITE);
        //String logoURL = in.getStringExtra(TAG_LOGOURL);

        // Displaying all values on the screen
        TextView lblName = (TextView) findViewById(R.id.name);
        TextView lblPhone = (TextView) findViewById(R.id.phone);
        TextView lblSite = (TextView) findViewById(R.id.site);
        //ImageView lblLogoURL = (ImageView) findViewById(R.id.logoURL);


        lblName.setText(name);
        lblPhone.setText(phone);
        lblSite.setText(site);
        //lblLogoURL.setImageURI(logoURL);

    }
}
