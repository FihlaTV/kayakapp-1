package com.boom.kayakapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.boom.kayakapp.R;
import com.boom.kayakapp.adapters.AirlinesListAdapter;
import com.boom.kayakapp.model.Airlines;
import com.boom.kayakapp.util.SharedPreference;

import java.util.ArrayList;
import java.util.List;

public class AirlinesListFragment extends Fragment implements OnItemLongClickListener, AdapterView.OnItemClickListener {

	public static final String ARG_ITEM_ID = "airlines_list";

	Activity activity;
	ListView airlinesListView;
	List<Airlines> airlines;
	AirlinesListAdapter airlinesListAdapter;

    public AirlinesListFragment() {
        airlines = new ArrayList<>();

    }

    SharedPreference sharedPreference;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		sharedPreference = new SharedPreference();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_airlines_list, container,
				false);
		findViewsById(view);

		airlinesListAdapter = new AirlinesListAdapter(activity, airlines);
		airlinesListView.setAdapter(airlinesListAdapter);
		airlinesListView.setOnItemClickListener(this);
		airlinesListView.setOnItemLongClickListener(this);
		return view;
	}

    private void findViewsById(View view) {
		airlinesListView = (ListView) view.findViewById(R.id.list_airlines);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Airlines airlines = (Airlines) parent.getItemAtPosition(position);
		Toast.makeText(activity, airlines.toString(), Toast.LENGTH_LONG).show();
	}

	@Override
	public boolean onItemLongClick(AdapterView<?> arg0, View view,
			int position, long arg3) {
		ImageView button = (ImageView) view.findViewById(R.id.favorite_button);

		String tag = button.getTag().toString();
		if (tag.equalsIgnoreCase("grey")) {
			sharedPreference.addFavorite(activity, airlines.get(position));
			Toast.makeText(activity,
					activity.getResources().getString(R.string.add_favr),
					Toast.LENGTH_SHORT).show();

			button.setTag("red");
			button.setImageResource(R.drawable.heart_red);
		} else {
			sharedPreference.removeFavorite(activity, airlines.get(position));
			button.setTag("grey");
			button.setImageResource(R.drawable.heart_grey);
			Toast.makeText(activity,
					activity.getResources().getString(R.string.remove_favr),
					Toast.LENGTH_SHORT).show();
		}

		return true;
	}
	
	@Override
	public void onResume() {
		getActivity().setTitle(R.string.app_name);
//		getActivity().getActionBar().setTitle(R.string.app_name);
		super.onResume();
	}
}
