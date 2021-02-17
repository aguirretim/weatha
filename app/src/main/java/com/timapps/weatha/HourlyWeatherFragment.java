package com.timapps.weatha;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HourlyWeatherFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HourlyWeatherFragment extends Fragment implements HourlyTempRecycleAdapter.RecyclerClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    HourlyTempRecycleAdapter HourlyTempRecycleAdapter;
    private TextView todayHeaderText;
    private TextView highTempText;
    private TextView lowTempText;

    RecyclerView recycleListView;
    public ArrayList<CurrentWeather> hourlyWeatherList = new ArrayList<CurrentWeather>();

    public HourlyWeatherFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HourlyWeatherFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HourlyWeatherFragment newInstance(String param1, String param2) {
        HourlyWeatherFragment fragment = new HourlyWeatherFragment();
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
        View view = inflater.inflate(R.layout.fragment_hourly_weather, container, false);

        MainActivity activity = (MainActivity) getActivity();
        todayHeaderText = (TextView) view.findViewById(R.id.todayHeaderText);
        highTempText = (TextView) view.findViewById(R.id.highTempText);
        lowTempText = (TextView) view.findViewById(R.id.lowTempText);
        todayHeaderText.setText(activity.currentWeather.ePochTimeConverter(
                activity.currentWeather.getTime()).getInstance().
                getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) +"TODAY");

        recycleListView = view.findViewById(R.id.recycleListView);

        if (activity.hourlyWeatherList != null) {
            HourlyTempRecycleAdapter = new HourlyTempRecycleAdapter(activity.hourlyWeatherList, getActivity().getApplicationContext());
            HourlyTempRecycleAdapter.setRecyclerClickListener(this);
            recycleListView.setAdapter(HourlyTempRecycleAdapter);

        } else {
            Toast.makeText(getActivity(), "No data in the database", Toast.LENGTH_SHORT).show();
        }

        recycleListView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.HORIZONTAL, false));

        return view;


    }

    @Override
    public void onClickPerformed(int postion) {

    }
}