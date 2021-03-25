package com.timapps.weatha;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class settingsAndLocationListPage extends Fragment implements LocationListAdapter.RecyclerClickListener {

    private RecyclerView locationRecycleListView;
    private TextView fahrenheitSettings;
    private TextView splitLable;
    private TextView celsiusSettings;
    private FloatingActionButton addLocationButton;
    LocationListAdapter LocationListAdapter;
    private LinearLayout boxTempType;
    public static final int SETTINGS_AND_LOCATION_PAGE = 1;
    MainActivity activity;

    public static settingsAndLocationListPage newInstance() {
        settingsAndLocationListPage fragment = new settingsAndLocationListPage();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings_and_location_list_page, container, false);

        locationRecycleListView = (RecyclerView) view.findViewById(R.id.locationRecycleListView);
        fahrenheitSettings = (TextView) view.findViewById(R.id.fahrenheitSettings);
        splitLable = (TextView) view.findViewById(R.id.splitLable);
        celsiusSettings = (TextView) view.findViewById(R.id.celsiusSettings);
        addLocationButton = (FloatingActionButton) view.findViewById(R.id.addLocationButton);
        boxTempType = view.findViewById(R.id.boxTempType);


        activity = (MainActivity) getActivity();

        if (activity.isMetric == true) {
            celsiusSettings.setTextColor(Color.parseColor("#FFFFFF"));
            fahrenheitSettings.setTextColor(Color.parseColor("#AAAAAA"));
        }
        boxTempType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPref = activity.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (activity.isMetric == false) {
                    activity.isMetric = true;
                    celsiusSettings.setTextColor(Color.parseColor("#FFFFFF"));
                    fahrenheitSettings.setTextColor(Color.parseColor("#AAAAAA"));
                    activity.getWeatherData(SETTINGS_AND_LOCATION_PAGE);
                    editor.putBoolean("METRIC_SETTING", true);
                    editor.apply();
                    Toast.makeText(activity, "metric setting set true", Toast.LENGTH_SHORT).show();
                } else if (activity.isMetric == true) {
                    activity.isMetric = false;
                    celsiusSettings.setTextColor(Color.parseColor("#AAAAAA"));
                    fahrenheitSettings.setTextColor(Color.parseColor("#FFFFFF"));
                    activity.getWeatherData(SETTINGS_AND_LOCATION_PAGE);
                    editor.putBoolean("METRIC_SETTING", false);
                    editor.apply(); //actually save it
                    Toast.makeText(activity, "metric setting set false", Toast.LENGTH_SHORT).show();
                }
            }
        });


        if (activity.dailyWeatherList != null) {
            LocationListAdapter = new LocationListAdapter(activity.currentWeatherList, getActivity().getApplicationContext());
            LocationListAdapter.setRecyclerClickListener(this);
            locationRecycleListView.setAdapter(LocationListAdapter);

        } else {
            Toast.makeText(getActivity(), "No data in the location database", Toast.LENGTH_SHORT).show();
        }

        locationRecycleListView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));


        return view;


    }

    @Override
    public void onClickPerformed(int postion) {
        MainActivity activity = (MainActivity) getActivity();
        activity.backToMainFragment();
    }
}


