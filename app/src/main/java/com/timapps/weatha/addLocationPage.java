package com.timapps.weatha;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link addLocationPage#newInstance} factory method to
 * create an instance of this fragment.
 */
public class addLocationPage extends Fragment {

    private TextView addLocationHeaderText;
    private SearchView searchView;
    private FloatingActionButton cancelButton;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public addLocationPage() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment addLocationPage.
     */
    // TODO: Rename and change types and number of parameters
    public static addLocationPage newInstance(String param1, String param2) {
        addLocationPage fragment = new addLocationPage();
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
        View view =inflater.inflate(R.layout.fragment_add_location_page, container, false);

        addLocationHeaderText = (TextView) view.findViewById(R.id.addLocationHeaderText);
        searchView = (SearchView) view.findViewById(R.id.searchView);
        cancelButton = (FloatingActionButton) view.findViewById(R.id.cancelButton);
        searchView.onActionViewExpanded();

        MainActivity activity = (MainActivity) getActivity();

        searchView.setQueryHint("Search a city…");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchLocation(activity, query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.createLocationSettingsFragment();
            }
        });

        return view;
    }

    /**
     * Turn a typed place name into coordinates (via Geocoder) and load that
     * location's weather. Runs the lookup off the UI thread to avoid an ANR.
     */
    private void searchLocation(final MainActivity activity, final String query) {
        if (query == null || query.trim().isEmpty()) {
            return;
        }
        if (!Geocoder.isPresent()) {
            Toast.makeText(activity, "Location search isn't available on this device",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
                try {
                    List<Address> matches = geocoder.getFromLocationName(query, 1);
                    if (matches != null && !matches.isEmpty()) {
                        Address a = matches.get(0);
                        activity.latitude = a.getLatitude();
                        activity.longitude = a.getLongitude();
                        activity.city = a.getLocality() != null ? a.getLocality()
                                : (a.getFeatureName() != null ? a.getFeatureName() : query);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                activity.getWeatherData(0);
                            }
                        });
                    } else {
                        showToast(activity, "Couldn't find \"" + query + "\"");
                    }
                } catch (IOException e) {
                    showToast(activity, "Search failed — check your connection");
                }
            }
        }).start();
    }

    private void showToast(final MainActivity activity, final String msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}