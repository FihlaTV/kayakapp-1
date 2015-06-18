package com.boom.kayakapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.boom.kayakapp.model.Airlines;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SharedPreference {

	public static final String PREFS_NAME = "KAYAK_APP";
	public static final String FAVORITES = "Airlines_Favorite";
	
	public SharedPreference() {
		super();
	}

	// This four methods are used for maintaining favorites.
	public void saveFavorites(Context context, List<Airlines> favorites) {
		SharedPreferences settings;
		Editor editor;

		settings = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);
		editor = settings.edit();

		Gson gson = new Gson();
		String jsonFavorites = gson.toJson(favorites);

		editor.putString(FAVORITES, jsonFavorites);

		editor.commit();
	}

	public void addFavorite(Context context, Airlines airlines) {
		List<Airlines> favorites = getFavorites(context);
		if (favorites == null)
			favorites = new ArrayList<>();
		favorites.add(airlines);
		saveFavorites(context, favorites);
	}

	public void removeFavorite(Context context, Airlines airlines) {
		ArrayList<Airlines> favorites = getFavorites(context);
		if (favorites != null) {
			favorites.remove(airlines);
			saveFavorites(context, favorites);
		}
	}

	public ArrayList<Airlines> getFavorites(Context context) {
		SharedPreferences settings;
		List<Airlines> favorites;

		settings = context.getSharedPreferences(PREFS_NAME,
				Context.MODE_PRIVATE);

		if (settings.contains(FAVORITES)) {
			String jsonFavorites = settings.getString(FAVORITES, null);
			Gson gson = new Gson();
			Airlines[] favoriteItems = gson.fromJson(jsonFavorites,
					Airlines[].class);

			favorites = Arrays.asList(favoriteItems);
			favorites = new ArrayList<>(favorites);
		} else
			return null;

		return (ArrayList<Airlines>) favorites;
	}
}
