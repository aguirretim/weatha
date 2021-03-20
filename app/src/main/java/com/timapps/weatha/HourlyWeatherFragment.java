package com.timapps.weatha;

import android.os.Bundle;
import android.util.Log;
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
import java.util.List;
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
    public ArrayList<CurrentWeather> dailyWeatherList = new ArrayList<CurrentWeather>();
    //start this at 0 because the first time the scroll runs it will be on 0, this way
    //prev will be equal to the current and it wont register a change
    private int previousFirstIndex = 0;
    private int dayShown = 0; //this variable represents the day we are showing in comparison to the day we're on
    //tomorrow 1
    //day after tomorrow 2
    //yesterday -1
    //day before -2


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
    int nexdayCounter = 0;
    int dayCounter = 0;
    Boolean isFirstimeNewDay = false;
    Boolean issecoundTimeNewDay = false;
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
                getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + "\n" + "TODAY");

        recycleListView = view.findViewById(R.id.recycleListView);

        highTempText.setText("High "+(int)activity.dailyWeatherList.get(0).getMaxTemp() + "\u00B0");
        lowTempText.setText("Low "+(int)activity.dailyWeatherList.get(0).getMinTemp() + "\u00B0");

        if (activity.hourlyWeatherList != null) {

            HourlyTempRecycleAdapter = new HourlyTempRecycleAdapter(activity.hourlyWeatherList,
                    getActivity().getApplicationContext());
            HourlyTempRecycleAdapter.setRecyclerClickListener(this);
            recycleListView.setAdapter(HourlyTempRecycleAdapter);

        } else {
            Toast.makeText(getActivity(), "No data in the database", Toast.LENGTH_SHORT).show();
        }

        LinearLayoutManager llManager = new LinearLayoutManager(this.getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        recycleListView.setLayoutManager(llManager);


        recycleListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                                @Override
                                                public void onScrolled(RecyclerView recyclerView,
                                                                       int dx, int dy) {
                                                    super.onScrolled(recyclerView, dx, dy);


                                                    int firstVisibleIndex = llManager.findFirstCompletelyVisibleItemPosition();

                if (firstVisibleIndex != previousFirstIndex) {



                    //if we just scrolled to 12am see which way we were going
                    Log.d("SCROLL", "First: " + firstVisibleIndex);
                    CurrentWeather c = activity.hourlyWeatherList.get(firstVisibleIndex );
                    String timeStr = currentWeatherToTimeString(c);
                    if (timeStr.equals("12 AM") ) {
                        //now that they are different we can figure out which way it was scrolled
                        //pick a way to do the subtraction and then do the sample math to get the direction
                        //subtraction will be currentValue - previousValue
                        // 0 1 2 3 -->
                        //if im on 1 and I scroll to 2 then-> prev = 1 curr = 2
                        //curr - prev -> 2 - 1 is positive
                        //positive means we scrolled our finger to the left which is moving forward in time
                        //if im on 1 and I scroll to 0 then-> prev = 1 and curr = 0
                        //curr - prev -> 0 - 1 is negative
                        //negative means we scrolled our finger to the right which is moving backward in time

                        if (firstVisibleIndex - previousFirstIndex > 0){
                            Log.d("SCROLL", "MOVING FORWARD");
                            dayShown++;
                            //move on to the next day
                        } else {
                            Log.d("SCROLL", "MOVING BACKWARD");
                            dayShown--;
                        }

                        Calendar futureDay = activity.currentWeather.ePochTimeConverter(
                                activity.currentWeather.getTime()).getInstance();

                        futureDay.add(Calendar.DAY_OF_WEEK,dayShown);
                        String displayText =  futureDay.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + "\n";
                        if (dayShown == -1){
                            displayText += "YESTERDAY";
                        } else if (dayShown == 0){
                            displayText += "TODAY";
                        } else if (dayShown == 1){
                            displayText += "TOMORROW";
                        }else if (dayShown == 2){
                            displayText += futureDay.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault()) +" "+ futureDay.get(Calendar.DAY_OF_MONTH);
                        }
                        todayHeaderText.setText( displayText);

                                                        Log.d("SCROLL", "First: " + firstVisibleIndex);



                    }
                                                    }

                                                    previousFirstIndex = firstVisibleIndex;

                                                }
                                            });

        //TODO figure out how to listen to the scrolls and get position
       // recycleListView.scro
        //int firstVisiblePosition = recycleListView .getLayoutManager().isViewPartiallyVisible().findFirstVisibleItemPosition();

        return view;


    }

    @Override
    public void onClickPerformed(int postion) {


    }

    private String currentWeatherToTimeString(CurrentWeather vCurrentWeather){
       return  vCurrentWeather.midNoonConverter((vCurrentWeather.ePochTimeConverter(vCurrentWeather.getTime())
                .get(vCurrentWeather.ePochTimeConverter(vCurrentWeather.getTime()).HOUR_OF_DAY))) +
                " " +
                vCurrentWeather.amPmFinder(vCurrentWeather.ePochTimeConverter(vCurrentWeather.getTime())
                        .get(vCurrentWeather.ePochTimeConverter(vCurrentWeather.getTime()).AM_PM))
                +
                "";
    }
}