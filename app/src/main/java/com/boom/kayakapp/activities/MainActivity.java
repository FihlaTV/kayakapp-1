package com.boom.kayakapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.boom.kayakapp.R;

/**
 * Created by Boom on 7/3/15.
 */
public class MainActivity extends Activity {
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);

        setContentView(R.layout.activity_main);

        Button list_button = (Button) findViewById(R.id.list_btn);
        list_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent myIntent = new Intent(MainActivity.this,
                        ListActivity.class);
                startActivity(myIntent);
            }
        });

        Button favorite_button = (Button) findViewById(R.id.favorite_btn);
        favorite_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                Intent myIntent = new Intent(MainActivity.this,
                        FavoriteActivity.class);
                startActivity(myIntent);
            }
        });
    }
}
