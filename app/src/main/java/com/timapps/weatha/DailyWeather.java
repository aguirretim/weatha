package com.timapps.weatha;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyWeather#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyWeather extends Fragment implements DailyTempRecycleAdapter.RecyclerClickListener{


    private RecyclerView dailyRecycleListView;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    DailyTempRecycleAdapter DailyTempRecycleAdapter;
    public ArrayList<CurrentWeather> dailyWeatherList = new ArrayList<CurrentWeather>();

    public DailyWeather() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyWeather.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyWeather newInstance(String param1, String param2) {
        DailyWeather fragment = new DailyWeather();
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



    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2021-02-18 16:52:44 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_daily_weather, container, false);

        dailyRecycleListView = (RecyclerView) view.findViewById(R.id.dailyRecycleListView);
        MainActivity activity = (MainActivity) getActivity();

        if (activity.dailyWeatherList != null) {
            DailyTempRecycleAdapter = new DailyTempRecycleAdapter(activity.dailyWeatherList, getActivity().getApplicationContext());
            DailyTempRecycleAdapter.setRecyclerClickListener(this);
            dailyRecycleListView.setAdapter(DailyTempRecycleAdapter);

        } else {
            Toast.makeText(getActivity(), "No data in the database", Toast.LENGTH_SHORT).show();
        }
        dailyRecycleListView.setLayoutManager(new LinearLayoutManager(this.getActivity(), LinearLayoutManager.VERTICAL, false));
        //Snap helper makes the items snap  in the view instead of having in betweens
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(dailyRecycleListView);

        return view;
    }

    @Override
    public void onClickPerformed(int postion) {

    }
}