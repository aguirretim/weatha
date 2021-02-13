package com.timapps.weatha;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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


    /*************************************
     *Init Variables  *
     *************************************/

    public static final String TAG = "WeatherApp";//MainActivity.class.getSimpleName();
    CurrentWeather currentWeather;

    /**************************************
     * Main initialized Method.  *
     **************************************/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);


    /*    if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();
        }*/




        String apiKey = "Fill Api Key Here";

        double latitude = 47.606209;
        double longitude = -122.332069;

        String units = "imperial";

        String forecastURL = "https://api.openweathermap.org/data/2.5/onecall?" +
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

                            /* Run UI Thread forces the create fragment method/action to happen
                             at UI Thread level instead of main thread level.             */
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    createFragment(currentWeather);
                                }
                            });

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

    /****************************************
     * Methods and Actions that do things  *
     ****************************************/

    private void createFragment(CurrentWeather c){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(c))
                .commitNow();
    }




    private CurrentWeather getCurrentDetail(String jSonData) throws JSONException {

        JSONObject forecast = new JSONObject(jSonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);
        JSONObject current = forecast.getJSONObject("current");
        JSONArray jWeatherArray = current.getJSONArray("weather");
        JSONObject weather = (JSONObject) jWeatherArray.get(0);

        CurrentWeather currentWeather = new CurrentWeather("city",
                weather.getString("icon"),
                current.getDouble("temp"),
                current.getDouble("humidity"),
                0.0,
                weather.getString("main"),
                current.getLong("dt"),
                timezone
        );

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
