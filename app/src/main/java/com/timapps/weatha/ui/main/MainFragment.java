package com.timapps.weatha.ui.main;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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


    public static MainFragment newInstance() {
        return new MainFragment();
    }

 public static final String KEY_RECIPE_INDEX ="recipe_index";
    CurrentWeather  currentWeatherFromMainActivity;

    /**************************************
     * Main initialized Method.  *
     **************************************/

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);


        MainActivity activity = (MainActivity) getActivity();

        weatherDescText = (TextView) view.findViewById(R.id.weatherDescText);
        weatherTempText = (TextView) view.findViewById(R.id.weatherTempText);
        weatherIcon = (ImageView)view.findViewById(R.id.weatherIcon);
        cityLableText = (TextView) view.findViewById(R.id.cityLableText);
        artCreditText = (TextView) view.findViewById(R.id.artCreditText);
        weatherDataCreditImage = (ImageView) view.findViewById(R.id.weatherDataCreditImage);
        weatherContainer = (ConstraintLayout) view.findViewById(R.id.weatherContainer);
        cityContainer = (ConstraintLayout) view.findViewById(R.id.cityContainer);

        while (currentWeatherFromMainActivity == null){
             activity = (MainActivity) getActivity();
             currentWeatherFromMainActivity = activity.getMyData();
        }

        if (currentWeatherFromMainActivity != null){
            weatherTempText.setText((int)currentWeatherFromMainActivity.getTemperature() + "\u00B0");
            weatherDescText.setText(currentWeatherFromMainActivity.getSummary());
            cityLableText.setText(currentWeatherFromMainActivity.getLocationLabel()+"");
            weatherIcon.setImageResource((currentWeatherFromMainActivity.
                    getIconId(currentWeatherFromMainActivity.getIcon())));
        }

        /****************************************
         * On Click Buttons  *
         ****************************************/

        weatherDataCreditImage.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://openweathermap.org"));
                startActivity(intent);
            }
        });

        artCreditText.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
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
                        "Refreshing data"+ getArguments().getInt(KEY_RECIPE_INDEX),
                        Toast.LENGTH_LONG
                ).show();
                finalActivity.weatherCall();
            }
        });


        return view;


    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }


}












