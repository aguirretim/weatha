package com.timapps.weatha;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TempDetailCardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TempDetailCardFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CurrentWeather currentWeatherFromMainActivity;
    private TextView weatherTempText;
    private TextView weatherDescText;
    private TextView cityLableText;
    private TextView feelsLikeText;
    private TextView humidityText;
    private ConstraintLayout tempDetailMain;

    public TempDetailCardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment TempDetailCardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TempDetailCardFragment newInstance(CurrentWeather currentWeather) {
        TempDetailCardFragment fragment = new TempDetailCardFragment();
        Bundle args = new Bundle();
        args.putParcelable("currentWeatherKey", currentWeather);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentWeatherFromMainActivity = getArguments().getParcelable("currentWeatherKey");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_temp_detail_card, container, false);
        MainActivity activity = (MainActivity) getActivity();
        weatherTempText = (TextView) view.findViewById(R.id.currentWeatherTempText);
        weatherDescText = (TextView) view.findViewById(R.id.currentNowHeadetText);
        cityLableText = (TextView) view.findViewById(R.id.todayHeaderText);
        feelsLikeText = (TextView) view.findViewById(R.id.highTempText);
        humidityText = (TextView) view.findViewById(R.id.lowTempText);
        tempDetailMain = (ConstraintLayout) view.findViewById(R.id.tempDetailMain);

        weatherTempText.setText((int) currentWeatherFromMainActivity.getTemperature() + "\u00B0");
        weatherDescText.setText(currentWeatherFromMainActivity.getSummaryB());
        cityLableText.setText(currentWeatherFromMainActivity.getLocationLabel() + "");
        feelsLikeText.setText("Feels like " + (int) currentWeatherFromMainActivity.getFeelsLikeTemp() + "\u00B0");
        humidityText.setText("Humidity " + (int) currentWeatherFromMainActivity.getHumidity() + "%");

        tempDetailMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.backToMainFragment();

            }
        });


        return view;
    }


}


