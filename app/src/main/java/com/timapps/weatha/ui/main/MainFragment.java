package com.timapps.weatha.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.timapps.weatha.CurrentWeather;
import com.timapps.weatha.MainActivity;
import com.timapps.weatha.R;


public class MainFragment extends Fragment {

    /*************************************
     *Init Variables  *
     *************************************/

    private MainViewModel mViewModel;

    /*************************************
     * Variables for Buttons and Field.  *
     *************************************/

    private TextView weatherDescText;
    private TextView weatherTempText;
    private ImageView weatherIcon;
    private TextView cityLableText;
    private TextView artCreditText;
    private ImageView weatherDataCreditImage;
    private ConstraintLayout weatherContainer;
    private ConstraintLayout cityContainer;

    /**************************************
     * Main initialized variables.  *
     **************************************/

    private CurrentWeather currentWeatherFromMainActivity;
    private String locationLabel;
    private String icon;
    private double temperature;
    private double humidity;
    private double precipChance;
    private String summary;


    public static MainFragment newInstance(CurrentWeather currentWeather) {
        MainFragment frag = new MainFragment();
        //Make arguments to put into the fragment before we return it
        Bundle arguments = new Bundle();
        //if you make CurrentWeather serializable (by adding certain code to that class)
        //you can just pass the current weather object here
        //arguments.putSerializable();
        arguments.putDouble("humidity", currentWeather.getHumidity());
        arguments.putDouble("temp", currentWeather.getTemperature());
        arguments.putParcelable("currentWeatherKey", currentWeather);
        frag.setArguments(arguments);
        return frag;
    }
    //empty constructor neededed?

    public MainFragment() {

    }

    /**************************************
     * Main initialized Method.  *
     **************************************/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);


        MainActivity activity = (MainActivity) getActivity();


        weatherDescText = (TextView) view.findViewById(R.id.currentNowHeadetText);
        weatherTempText = (TextView) view.findViewById(R.id.currentWeatherTempText);
        weatherIcon = (ImageView) view.findViewById(R.id.weatherIcon);
        cityLableText = (TextView) view.findViewById(R.id.todayHeaderText);
        artCreditText = (TextView) view.findViewById(R.id.artCreditText);
        weatherDataCreditImage = (ImageView) view.findViewById(R.id.weatherDataCreditImage);
        weatherContainer = (ConstraintLayout) view.findViewById(R.id.weatherContainer);
        cityContainer = (ConstraintLayout) view.findViewById(R.id.cityContainer);

        weatherTempText.setText((int) currentWeatherFromMainActivity.getTemperature() + "\u00B0");
        weatherDescText.setText(currentWeatherFromMainActivity.getSummary());
        cityLableText.setText(currentWeatherFromMainActivity.getLocationLabel() + "");
        weatherIcon.setImageResource((currentWeatherFromMainActivity.
                getIconId(currentWeatherFromMainActivity.getIcon())));







        /****************************************
         * On Click Buttons  *
         ****************************************/

        weatherDataCreditImage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://openweathermap.org"));
                startActivity(intent);
            }
        });

        artCreditText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("http://pokemon.com"));
                startActivity(intent);
            }
        });

        MainActivity finalActivity = activity;
        weatherContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(finalActivity,
                        "Refreshing data not working yet",
                        Toast.LENGTH_LONG
                ).show();

                weatherContainer.setVisibility(View.INVISIBLE);
                cityContainer.setVisibility(View.INVISIBLE);
                activity.createTempDetailFragment(currentWeatherFromMainActivity);
                activity.createHourlyWeatherFragment(currentWeatherFromMainActivity);
                activity.createDailyWeatherFragment();

            }
        });

        cityContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                activity.createLocationSettingsFragment();
            }
        });


        return view;


    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //this is where we have access to the arguments we put in the newInstance
        if (getArguments() != null) {
            currentWeatherFromMainActivity = getArguments().getParcelable("currentWeatherKey");
        }


    }

    public void onResume() {


        super.onResume();


    }

}