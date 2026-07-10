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
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class HourlyWeatherFragment extends Fragment implements HourlyTempRecycleAdapter.RecyclerClickListener {

    HourlyTempRecycleAdapter HourlyTempRecycleAdapter;
    private TextView todayHeaderText;
    private TextView highTempText;
    private TextView lowTempText;

    RecyclerView recycleListView;
    public ArrayList<CurrentWeather> hourlyWeatherList = new ArrayList<CurrentWeather>();
    public ArrayList<CurrentWeather> dailyWeatherList = new ArrayList<CurrentWeather>();
    private static final String DEGREE = String.valueOf((char) 0x00B0);
    // Tracks the first-visible hour index so the day header only updates when it
    // actually changes, instead of on every scroll frame.
    private int previousFirstIndex = 0;

    public static HourlyWeatherFragment newInstance() {
        HourlyWeatherFragment fragment = new HourlyWeatherFragment();
        Bundle args = new Bundle();
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + "\n" + "TODAY");

        recycleListView = view.findViewById(R.id.recycleListView);

        highTempText.setText("High " + (int) activity.dailyWeatherList.get(0).getMaxTemp() + "\u00B0");
        lowTempText.setText("Low " + (int) activity.dailyWeatherList.get(0).getMinTemp() + "\u00B0");

        if (activity.hourlyWeatherList != null) {

            HourlyTempRecycleAdapter = new HourlyTempRecycleAdapter(activity.hourlyWeatherList,
                    getActivity().getApplicationContext());
            HourlyTempRecycleAdapter.setRecyclerClickListener(this);
            recycleListView.setAdapter(HourlyTempRecycleAdapter);

        } else {
            Toast.makeText(getActivity(), "No data in the database", Toast.LENGTH_SHORT).show();
        }

        final LinearLayoutManager llManager = new LinearLayoutManager(this.getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        recycleListView.setLayoutManager(llManager);

        //Snap helper makes the items snap in the view instead of having in betweens
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recycleListView);

        // The hourly list starts at 12 AM (location-local) today, so the location's
        // current hour is also its index. Default the strip to that hour so "now" is
        // shown first, and remember it so the header doesn't flip during the initial
        // auto-scroll. Use the location's clock (not the device's) so this stays correct
        // for cities in other timezones.
        final MainActivity act = activity;
        int locationHour = act.currentWeather.ePochTimeConverter(
                act.currentWeather.getTime()).get(Calendar.HOUR_OF_DAY);
        final int currentHourIndex = Math.min(
                locationHour,
                Math.max(0, act.hourlyWeatherList.size() - 1));
        previousFirstIndex = currentHourIndex;
        recycleListView.post(new Runnable() {
            @Override
            public void run() {
                llManager.scrollToPositionWithOffset(currentHourIndex, 0);
            }
        });

        // Update the day header straight from the first-visible hour's own date.
        // (The old code tracked direction with a running counter that flipped every
        // time "12 AM" scrolled past, which jumped around when scrolling backward.)
        recycleListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleIndex = llManager.findFirstCompletelyVisibleItemPosition();
                if (firstVisibleIndex == RecyclerView.NO_POSITION) {
                    firstVisibleIndex = llManager.findFirstVisibleItemPosition();
                }
                if (firstVisibleIndex < 0 || firstVisibleIndex >= act.hourlyWeatherList.size()) {
                    return;
                }
                if (firstVisibleIndex == previousFirstIndex) {
                    return;
                }
                previousFirstIndex = firstVisibleIndex;
                updateDayHeader(act, firstVisibleIndex);
            }
        });

        return view;


    }

    /** Set the day header (and that day's high/low) from a specific hourly item's real date. */
    private void updateDayHeader(MainActivity activity, int index) {
        CurrentWeather item = activity.hourlyWeatherList.get(index);
        Calendar itemCal = item.ePochTimeConverter(item.getTime());
        Calendar todayCal = activity.currentWeather.ePochTimeConverter(activity.currentWeather.getTime());
        int diff = dayDifference(todayCal, itemCal);

        String label = itemCal.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + "\n";
        if (diff == 0) {
            label += "TODAY";
        } else if (diff == 1) {
            label += "TOMORROW";
        } else if (diff == -1) {
            label += "YESTERDAY";
        } else {
            label += itemCal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())
                    + " " + itemCal.get(Calendar.DAY_OF_MONTH);
        }
        todayHeaderText.setText(label);

        // Show that day's high/low when the daily forecast has it (indexed from today).
        if (diff >= 0 && diff < activity.dailyWeatherList.size()) {
            highTempText.setText("High " + (int) activity.dailyWeatherList.get(diff).getMaxTemp() + DEGREE);
            lowTempText.setText("Low " + (int) activity.dailyWeatherList.get(diff).getMinTemp() + DEGREE);
        }
    }

    /** Whole-day difference (to - from): 0 = same day, 1 = next day, -1 = previous day. */
    private int dayDifference(Calendar from, Calendar to) {
        Calendar a = (Calendar) from.clone();
        Calendar b = (Calendar) to.clone();
        a.set(Calendar.HOUR_OF_DAY, 0); a.set(Calendar.MINUTE, 0);
        a.set(Calendar.SECOND, 0); a.set(Calendar.MILLISECOND, 0);
        b.set(Calendar.HOUR_OF_DAY, 0); b.set(Calendar.MINUTE, 0);
        b.set(Calendar.SECOND, 0); b.set(Calendar.MILLISECOND, 0);
        long ms = b.getTimeInMillis() - a.getTimeInMillis();
        return (int) Math.round(ms / (1000.0 * 60 * 60 * 24));
    }

    @Override
    public void onClickPerformed(int postion) {


    }
}