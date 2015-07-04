package com.boom.kayakapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.boom.kayakapp.R;
import com.boom.kayakapp.adapters.AirlinesAdapter;
import com.boom.kayakapp.controllers.AppController;
import com.boom.kayakapp.fragment.AirlinesFragment;
import com.boom.kayakapp.fragment.FavoriteFragment;
import com.boom.kayakapp.model.Airlines;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends ActionBarActivity {

    public Fragment contentFragment;
    public AirlinesFragment airlinesFragment;
    public FavoriteFragment favoriteFragment;

    // JSON Node names
    public static final String TAG_NAME = "name";
    public static final String TAG_PHONE = "phone";
    public static final String TAG_SITE = "site";
    public static final String TAG_LOGO = "logoURL";
    public static final String TAG_CODE = "code";

    // Log tag
    private static final String TAG = ListActivity.class.getSimpleName();

    // Airlines json url
    private static final String url = "https://www.kayak.com/h/mobileapis/directory/airlines";

    public ProgressDialog pDialog;
    public List<Airlines> airlinesList = new ArrayList<Airlines>();
    public ListView listView;
    public AirlinesAdapter adapterAirlines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

//            Json Array
        listView = (ListView) findViewById(R.id.list);
        adapterAirlines = new AirlinesAdapter(this, airlinesList);
        listView.setAdapter(adapterAirlines);

        pDialog = new ProgressDialog(this);

//         Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Listview OnItemClickListener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                 getting values from selected ListItem
                String name = ((TextView) view.findViewById(R.id.name))
                        .getText().toString();
                String phone = ((TextView) view.findViewById(R.id.phone))
                        .getText().toString();
                String site = ((TextView) view.findViewById(R.id.site))
                        .getText().toString();
                String logoURL = String.valueOf(((ImageView) view.findViewById(R.id.logoURL)));

//                 Starting single contact activity
                Intent in = new Intent(getApplicationContext(),
                        SingleContactActivity.class);
                in.putExtra(TAG_NAME, name);
                in.putExtra(TAG_PHONE, phone);
                in.putExtra(TAG_SITE, site);
                in.putExtra(TAG_LOGO, logoURL);
                startActivity(in);
            }
        });

        // changing action bar color
        getSupportActionBar().setBackgroundDrawable(
                new ColorDrawable(Color.parseColor("#1b1b1b")));

        // Creating volley request obj
        JsonArrayRequest airlinesReq = new JsonArrayRequest(url,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject obj = response.getJSONObject(i);
                                Airlines airlines = new Airlines();
                                airlines.setName(obj.getString("name"));
                                airlines.setLogoURL(obj.getString("logoURL"));
                                airlines.setPhone(obj.getString("phone"));
                                airlines.setCode(obj.getInt("code"));
                                airlines.setSite(obj.getString("site"));

                                // adding airlines to array
                                airlinesList.add(airlines);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        // notifying list adapterAirlines about data changes
                        // so that it renders the list view with updated data
                        adapterAirlines.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(airlinesReq);

        FragmentManager fragmentManager = getSupportFragmentManager();

		/*
         * This is called when orientation is changed.
		 */
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("content")) {
                String content = savedInstanceState.getString("content");
                if (content.equals(FavoriteFragment.ARG_ITEM_ID)) {
                    if (fragmentManager.findFragmentByTag(FavoriteFragment.ARG_ITEM_ID) != null) {
                        setFragmentTitle(R.string.favorites);
                        contentFragment = fragmentManager
                                .findFragmentByTag(FavoriteFragment.ARG_ITEM_ID);
                    }
                }
            }
            if (fragmentManager.findFragmentByTag(AirlinesFragment.ARG_ITEM_ID) != null) {
                airlinesFragment = (AirlinesFragment) fragmentManager
                        .findFragmentByTag(AirlinesFragment.ARG_ITEM_ID);
                contentFragment = airlinesFragment;
            }
        } else {
            airlinesFragment = new AirlinesFragment();
            switchContent(airlinesFragment, AirlinesFragment.ARG_ITEM_ID);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (contentFragment instanceof FavoriteFragment) {
            outState.putString("content", FavoriteFragment.ARG_ITEM_ID);
        } else {
            outState.putString("content", AirlinesFragment.ARG_ITEM_ID);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorites:
                setFragmentTitle(R.string.favorites);
                favoriteFragment = new FavoriteFragment();
                switchContent(favoriteFragment, FavoriteFragment.ARG_ITEM_ID);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void switchContent(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        while (fragmentManager.popBackStackImmediate()) ;

        if (fragment != null) {
            FragmentTransaction transaction = fragmentManager
                    .beginTransaction();
            transaction.replace(R.id.content_frame, fragment, tag);
            //Only FavoriteFragment is added to the back stack.
            if (!(fragment instanceof AirlinesFragment)) {
                transaction.addToBackStack(tag);
            }
            transaction.commit();
            contentFragment = fragment;
        }
    }

    protected void setFragmentTitle(int resourseId) {
        setTitle(resourseId);
        getSupportActionBar().setTitle(resourseId);
    }
	/*
	 * We call super.onBackPressed(); when the stack entry count is > 0. if it
	 * is instanceof ProductListFragment or if the stack entry count is == 0, then
	 * we finish the activity.
	 * In other words, from AirlinesFragment on back press it quits the app.
	 */

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            super.onBackPressed();
        } else if (contentFragment instanceof AirlinesFragment
                || fm.getBackStackEntryCount() == 0) {
            finish();
        }
    }
}
