package com.timapps.weatha.ui.main;

import androidx.lifecycle.ViewModelProvider;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.timapps.weatha.CurrentWeather;
import com.timapps.weatha.MainActivity;
import com.timapps.weatha.R;



public class MainFragment extends Fragment {

    private MainViewModel mViewModel;

    private TextView weatherDescText;
    private TextView weatherTempText;
    private ImageView weatherIcon;
    private TextView cityLableText;
    private TextView artCreditText;
    private ImageView weatherDataCreditImage;


    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        MainActivity activity = (MainActivity) getActivity();
        CurrentWeather currentWeatherFromMainActivity = activity.getMyData();

        weatherDescText = (TextView) view.findViewById(R.id.weatherDescText);
        weatherTempText = (TextView) view.findViewById(R.id.weatherTempText);
        weatherIcon = (ImageView)view.findViewById(R.id.weatherIcon);
        cityLableText = (TextView) view.findViewById(R.id.cityLableText);
        artCreditText = (TextView) view.findViewById(R.id.artCreditText);
        weatherDataCreditImage = (ImageView) view.findViewById(R.id.weatherDataCreditImage);

        while (currentWeatherFromMainActivity == null){
             activity = (MainActivity) getActivity();
             currentWeatherFromMainActivity = activity.getMyData();
        }

        if (currentWeatherFromMainActivity != null){
            weatherTempText.setText((int)currentWeatherFromMainActivity.getTemperature() + "\u00B0");
            weatherDescText.setText(currentWeatherFromMainActivity.getSummary());
            cityLableText.setText(currentWeatherFromMainActivity.getLocationLabel());
            weatherIcon.setImageResource((currentWeatherFromMainActivity.
                    getIconId(currentWeatherFromMainActivity.getIcon())));
        }
 /*       //weatherDescText.setText(currentWeather.getSummary());
        weatherTempText.setText(currentWeather.getTemperature() + "");
        Drawable drawable = getResources().getDrawable(currentWeather.getIconId());
        weatherIcon.setImageDrawable(drawable);
        cityLableText.setText(currentWeather.getLocationLabel());
*/


        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }



}