package com.timapps.weatha;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link settingsAndLocationListPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class settingsAndLocationListPage extends Fragment implements LocationListAdapter.RecyclerClickListener {

    private RecyclerView locationRecycleListView;
    private TextView fahrenheitSettings;
    private TextView splitLable;
    private TextView celsiusSettings;
    private FloatingActionButton addLocationButton;
    LocationListAdapter LocationListAdapter;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public settingsAndLocationListPage() {
        // Required empty public constructor locationRecycleListView
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment settingsAndLocationListPage.
     */
    // TODO: Rename and change types and number of parameters
    public static settingsAndLocationListPage newInstance(String param1, String param2) {
        settingsAndLocationListPage fragment = new settingsAndLocationListPage();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
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


        MainActivity activity = (MainActivity) getActivity();

        if (activity.isMetric == true) {
            celsiusSettings.setTextColor(Color.parseColor("#FFFFFF"));
            fahrenheitSettings.setTextColor(Color.parseColor("#AAAAAA"));
        }

        fahrenheitSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (activity.isMetric == false) {
                    activity.isMetric = true;
                    celsiusSettings.setTextColor(Color.parseColor("#FFFFFF"));
                    fahrenheitSettings.setTextColor(Color.parseColor("#AAAAAA"));
                } else if (activity.isMetric == true) {
                    activity.isMetric = false;
                    celsiusSettings.setTextColor(Color.parseColor("#AAAAAA"));
                    fahrenheitSettings.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
        });

        celsiusSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (activity.isMetric == false) {
                    activity.isMetric = true;
                    celsiusSettings.setTextColor(Color.parseColor("#FFFFFF"));
                    fahrenheitSettings.setTextColor(Color.parseColor("#AAAAAA"));
                } else if (activity.isMetric == true) {
                    activity.isMetric = false;
                    celsiusSettings.setTextColor(Color.parseColor("#AAAAAA"));
                    fahrenheitSettings.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
        });

        splitLable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (activity.isMetric == false) {
                    activity.isMetric = true;
                    celsiusSettings.setTextColor(Color.parseColor("#FFFFFF"));
                    fahrenheitSettings.setTextColor(Color.parseColor("#AAAAAA"));
                } else if (activity.isMetric == true) {
                    activity.isMetric = false;
                    celsiusSettings.setTextColor(Color.parseColor("#AAAAAA"));
                    fahrenheitSettings.setTextColor(Color.parseColor("#FFFFFF"));
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

    }
}


