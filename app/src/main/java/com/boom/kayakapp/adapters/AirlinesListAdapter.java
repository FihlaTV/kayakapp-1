package com.boom.kayakapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.boom.kayakapp.R;
import com.boom.kayakapp.controllers.AppController;
import com.boom.kayakapp.model.Airlines;
import com.boom.kayakapp.util.SharedPreference;

import java.util.ArrayList;
import java.util.List;

public class AirlinesListAdapter extends ArrayAdapter<Airlines> {

	private Activity activity;
	private List<Airlines> favorites;

	private LayoutInflater inflater;
	SharedPreference sharedPreference;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public AirlinesListAdapter(Activity activity, List<Airlines> favorites) {
		super(activity, R.layout.list_item, favorites);
		this.activity = activity;
		if(favorites == null)
			favorites = new ArrayList<>();
		else
			this.favorites = favorites;
		sharedPreference = new SharedPreference();

	}

	private class ViewHolder {
		ImageView favoriteImg;
	}

	@Override
	public int getCount() {
		return favorites.size();
	}

	@Override
	public Airlines getItem(int location) {
		return favorites.get(location);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.list_item, null);

		holder = new ViewHolder();

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		NetworkImageView logoURL = (NetworkImageView) convertView
				.findViewById(R.id.logoURL);

		TextView name = (TextView) convertView.findViewById(R.id.name);
		TextView phone = (TextView) convertView.findViewById(R.id.phone);
		TextView site = (TextView) convertView.findViewById(R.id.site);
		TextView code = (TextView) convertView.findViewById(R.id.code);

		holder.favoriteImg = (ImageView) convertView
				.findViewById(R.id.favorite_button);

		// getting airlines data for the row
		Airlines m = favorites.get(position);
		// thumbnail image
		logoURL.setImageUrl(m.getLogoURL(), imageLoader);
		// name
		name.setText(m.getName());
		// phone
		phone.setText("Phone: " + String.valueOf(m.getPhone()));
		// site
		site.setText("Web: " + String.valueOf(m.getSite()));
		// release code
		code.setText(String.valueOf(m.getCode()));

		/*If a product exists in shared preferences then set heart_red drawable
		 * and set a tag*/
		if (checkFavoriteItem(favorites)) {
			holder.favoriteImg.setImageResource(R.drawable.heart_red);
			holder.favoriteImg.setTag("red");
		} else {
			holder.favoriteImg.setImageResource(R.drawable.heart_grey);
			holder.favoriteImg.setTag("grey");
		}

		return convertView;
	}
	/*Checks whether a particular product exists in SharedPreferences*/
	public boolean checkFavoriteItem(List<Airlines> checkAirlines) {
		boolean check = false;
//		List<Airlines> favorites = sharedPreference.getFavorites(activity);
		if (favorites != null) {
			for (Airlines product : favorites) {
				if (product.equals(checkAirlines)) {
					check = true;
					break;
				}
			}
		}
		return check;
	}

	@Override
	public void add(Airlines airlines) {
		super.add(airlines);
		favorites.add(airlines);
		notifyDataSetChanged();
	}

	@Override
	public void remove(Airlines airlines) {
		super.remove(airlines);
		favorites.remove(airlines);
		notifyDataSetChanged();
	}

}