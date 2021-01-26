package com.timapps.weatha;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.timapps.weatha.ui.main.MainFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    CurrentWeather currentWeather;

    private TextView weatherDescText;
    private TextView weatherTempText;
    private ImageView weatherIcon;
    private TextView cityLableText;
    private TextView artCreditText;
    private ImageView weatherDataCreditImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }


        String apiKey = "Fill Api Key Here";

        double latitude = 47.606209;
        double longitude = -122.332069;

        String units = "imperial";

        String forecastURL = "https://api.openweathermap.org/data/2.5/weather?" +
                "lat=" + latitude + "&" +
                "lon=" + longitude + "&" +
                "appid=" + apiKey + "&" +
                "units=" + units;

        if (isNetworkAvailable()) {
            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder()
                    .url(forecastURL)
                    .build();

            Call call = client.newCall(request);

            // call.enqueue () puts the request call in a queue that allows it to happen async instead of sync with main thread.
            // preventing main ui thread error.and acting as a call back function waiting for data
            call.enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    try {
                        String jSonData = response.body().string();
                        Log.v(TAG, jSonData);
                        if (response.isSuccessful()) {

                            currentWeather = getCurrentDetail(jSonData);

                        } else {
                            alertUserAboutError();
                        }

                    } catch (IOException e) {
                        Log.e(TAG, "IO Exception caught: ", e);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSONException Exception caught: ", e);
                    }
                }
            });

        }


    }

    private void findViews() {
        weatherDescText = (TextView) findViewById(R.id.weatherDescText);
        weatherTempText = (TextView) findViewById(R.id.weatherTempText);
        weatherIcon = (ImageView) findViewById(R.id.weatherIcon);
        cityLableText = (TextView) findViewById(R.id.cityLableText);
        artCreditText = (TextView) findViewById(R.id.artCreditText);
        weatherDataCreditImage = (ImageView) findViewById(R.id.weatherDataCreditImage);
    }


    private CurrentWeather getCurrentDetail(String jSonData) throws JSONException {

        JSONObject forecast = new JSONObject(jSonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);
        JSONArray jWeatherArray = forecast.getJSONArray("weather");
        JSONObject weather = (JSONObject) jWeatherArray.get(0);
        JSONObject main = forecast.getJSONObject("main");

        CurrentWeather currentWeather = new CurrentWeather();

        String weatherDesc = weather.getString("description");

        currentWeather.setHumidity(main.getDouble("humidity"));
        currentWeather.setIcon(weather.getString("icon"));
        currentWeather.setLocationLabel(forecast.getString("name"));
        currentWeather.setSummary(weatherDesc);
        currentWeather.setTemperature(main.getDouble("temp"));

    /*    //weatherDescText.setText(currentWeather.getSummary());
        weatherTempText.setText(currentWeather.getTemperature() + "");
        Drawable drawable = getResources().getDrawable(currentWeather.getIconId());
        weatherIcon.setImageDrawable(drawable);
        cityLableText.setText(currentWeather.getLocationLabel());*/

        return currentWeather;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;

        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        } else {
            Toast.makeText(this, R.string.network_unavalable_msg,
                    Toast.LENGTH_LONG).show();
        }
        return isAvailable;
    }

    private void alertUserAboutError() {

        AlertDialogFragment dialog = new AlertDialogFragment();

        dialog.show(getSupportFragmentManager(), "error_dialog");


    }

}
