package com.boom.kayakapp.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.boom.kayakapp.R;
import com.boom.kayakapp.controllers.AppController;
import com.boom.kayakapp.model.Airlines;
import com.boom.kayakapp.util.SharedPreference;

import java.util.ArrayList;
import java.util.List;

public class AirlinesAdapter extends ArrayAdapter<Airlines> {

	private Activity activity;
	private List<Airlines> airlines;

	public LayoutInflater inflater;
	SharedPreference sharedPreference;

	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public AirlinesAdapter(Activity activity, List<Airlines> airlines) {
		super(activity, R.layout.list_item, airlines);

		this.activity = activity;
		if(airlines == null)
			airlines = new ArrayList<>();
		else
			this.airlines = airlines;
		sharedPreference = new SharedPreference();
	}

	private class ViewHolder {
		TextView airlineName;
		TextView airlinePhone;
		TextView airlineSite;
		ImageView favoriteImg;
	}

	@Override
	public int getCount() {
		return airlines.size();
	}

	@Override
	public Airlines getItem(int location) {
		return airlines.get(location);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) activity
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.airlineName = (TextView) convertView
					.findViewById(R.id.name);
			holder.airlinePhone = (TextView) convertView
					.findViewById(R.id.phone);
			holder.airlineSite = (TextView) convertView
					.findViewById(R.id.site);
			holder.favoriteImg = (ImageView) convertView
					.findViewById(R.id.favorite_button);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Airlines airline = (Airlines) getItem(position);
		holder.airlineName.setText(airline.getName());
		holder.airlinePhone.setText(airline.getPhone());
		holder.airlineSite.setText(airline.getSite());

	/*@Override
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

//		 getting airlines data for the row
		Airlines m = airlines.get(position);
//		 thumbnail image
		logoURL.setImageUrl(m.getLogoURL(), imageLoader);
//		 name
		name.setText(m.getName());
//		 phone
		phone.setText("Phone: " + String.valueOf(m.getPhone()));
//		 site
		site.setText("Web: " + String.valueOf(m.getSite()));
//		 code
		code.setText(String.valueOf(m.getCode())); */

		/*If a product exists in shared preferences then set heart_red drawable
		 * and set a tag*/
		if (checkFavoriteItem(airlines)) {
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
//		List<Airlines> airlines = sharedPreference.getFavorites(activity);
		if (airlines != null) {
			for (Airlines airline : this.airlines) {
				if (airline.equals(checkAirlines)) {
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
		this.airlines.add(airlines);
		notifyDataSetChanged();
	}

	@Override
	public void remove(Airlines airlines) {
		super.remove(airlines);
		this.airlines.remove(airlines);
		notifyDataSetChanged();
	}

}