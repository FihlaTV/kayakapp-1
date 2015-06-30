package com.boom.kayakapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.boom.kayakapp.R;
import com.boom.kayakapp.adapters.AirlinesAdapter;
import com.boom.kayakapp.adapters.NavDrawerListAdapter;
import com.boom.kayakapp.controllers.AppController;
import com.boom.kayakapp.fragment.AirlinesFragment;
import com.boom.kayakapp.fragment.FavoriteFragment;
import com.boom.kayakapp.fragment.HomeFragment;
import com.boom.kayakapp.model.Airlines;
import com.boom.kayakapp.model.NavDrawerItem;
import com.boom.kayakapp.util.SharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

		private Fragment contentFragment;
		AirlinesFragment airlinesFragment;
		FavoriteFragment favoriteFragment;

		// JSON Node names
		public static final String TAG_NAME = "name";
		public static final String TAG_PHONE = "phone";
		public static final String TAG_SITE = "site";
		public static final String TAG_LOGO = "logoURL";
		public static final String TAG_CODE = "code";

		// Log tag
		private static final String TAG = MainActivity.class.getSimpleName();

		// Airlines json url
		private static final String url = "https://www.kayak.com/h/mobileapis/directory/airlines";

		public ProgressDialog pDialog;
		public List<Airlines> airlinesList = new ArrayList<Airlines>();
		public ListView listView;
		public AirlinesAdapter adapterAirlines;

        SharedPreference sharedPreference;

    // DrawerLayout

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    // nav drawer title
    private CharSequence mDrawerTitle;

    // used to store app title
    private CharSequence mTitle;

    // slide menu items
    private String[] navMenuTitles;
    private TypedArray navMenuIcons;

    private ArrayList<NavDrawerItem> navDrawerItems;
    private NavDrawerListAdapter adapterDrawer;

		@Override
		protected void onCreate (Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_list);

//            SharedPrefs
            sharedPreference = new SharedPreference();

            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

//            Json Array
            listView = (ListView) findViewById(R.id.list);
            adapterAirlines = new AirlinesAdapter(this, airlinesList);
            listView.setAdapter(adapterAirlines);

            pDialog = new ProgressDialog(this);

            // Showing progress dialog before making http request
            pDialog.setMessage("Loading...");
            pDialog.show();

            // Listview OnItemClickListener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    // getting values from selected ListItem
                    String name = ((TextView) view.findViewById(R.id.name))
                            .getText().toString();
                    String phone = ((TextView) view.findViewById(R.id.phone))
                            .getText().toString();
                    String site = ((TextView) view.findViewById(R.id.site))
                            .getText().toString();
                    String logoURL = String.valueOf(((ImageView) view.findViewById(R.id.logoURL)));

                    // Starting single contact activity
                    Intent in = new Intent(getApplicationContext(),
                            SingleContactActivity.class);
                    in.putExtra(TAG_NAME, name);
                    in.putExtra(TAG_PHONE, phone);
                    in.putExtra(TAG_SITE, site);
                    in.putExtra(TAG_LOGO, logoURL);
                    startActivity(in);
                }
            });

            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view,
                                               int position, long id) {
                    ImageView button = (ImageView) view.findViewById(R.id.favorite_button);

                    String tag = button.getTag().toString();
                    if (tag.equalsIgnoreCase("grey")) {
                        sharedPreference.addFavorite(MainActivity.this, airlinesList.get(position));
                        Toast.makeText(MainActivity.this,
                                MainActivity.this.getResources().getString(R.string.add_favr),
                                Toast.LENGTH_SHORT).show();

                        button.setTag("red");
                        button.setImageResource(R.drawable.heart_red);
                    } else {
                        sharedPreference.removeFavorite(MainActivity.this, airlinesList.get(position));
                        button.setTag("grey");
                        button.setImageResource(R.drawable.heart_grey);
                        Toast.makeText(MainActivity.this,
                                MainActivity.this.getResources().getString(R.string.remove_favr),
                                Toast.LENGTH_SHORT).show();
                    }
                    return true;
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

            /*
            From this place DrawerLayout starts
             */
            mTitle = mDrawerTitle = getTitle();

            // load slide menu items
            navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

            // nav drawer icons from resources
            navMenuIcons = getResources()
                    .obtainTypedArray(R.array.nav_drawer_icons);

            mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
            mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

            navDrawerItems = new ArrayList<NavDrawerItem>();

            // adding nav drawer items to array
            // Home
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, -1)));
            // Favorites
            navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, -1)));

            // Recycle the typed array
            navMenuIcons.recycle();

            // setting the nav drawer list adapterDrawer
            adapterDrawer = new NavDrawerListAdapter(getApplicationContext(),
                    navDrawerItems);
            mDrawerList.setAdapter(adapterDrawer);

            // enabling action bar app icon and behaving it as toggle button
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);

            mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                    R.drawable.ic_drawer, //nav menu toggle icon
                    R.string.app_name, // nav drawer open - description for accessibility
                    R.string.app_name // nav drawer close - description for accessibility
            ){
                public void onDrawerClosed(View view) {
                    getSupportActionBar().setTitle(mTitle);
                    // calling onPrepareOptionsMenu() to show action bar icons
                    invalidateOptionsMenu();
                }

                public void onDrawerOpened(View drawerView) {
                    getSupportActionBar().setTitle(mDrawerTitle);
                    // calling onPrepareOptionsMenu() to hide action bar icons
                    invalidateOptionsMenu();
                }
            };
            mDrawerLayout.setDrawerListener(mDrawerToggle);

            if (savedInstanceState == null) {
                // on first time display view for first nav item
                displayView(0);
            }
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // toggle nav drawer on selecting action bar app icon/title
//        if (mDrawerToggle.onOptionsItemSelected(item)) {
//            return true;
//        }
//         Handle action bar actions click
//        switch (item.getItemId()) {
//            case R.id.action_settings:
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//
        switch (item.getItemId()) {
            case R.id.action_settings:
                setFragmentTitle(R.string.favorites);
                favoriteFragment = new FavoriteFragment();
                switchContent(favoriteFragment, FavoriteFragment.ARG_ITEM_ID);

                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /***
     * Called when invalidateOptionsMenu() is triggered
     */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // if nav drawer is opened, hide the action items
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     * */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new FavoriteFragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(navMenuTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            // error in creating fragment
            Log.e("MainActivity", "Error in creating fragment");
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
		public void onDestroy () {
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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

    @Override
    public void onResume() {
        super.onResume();
    }
}
