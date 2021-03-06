package com.timapps.weatha;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.timapps.weatha.ui.main.MainFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
    FusedLocationProviderClient fusedLocationProviderClient;
    double latitude ;
    double longitude ;
    boolean isMetric = false;
    boolean isDetailView = false;
    boolean isSettingView = false;
    boolean isLocationView = false;
    boolean isfirstrun = true;

    String units = "imperial";
    String country;
    String city;
    String locAddress;
    boolean locationAvalible = false;
    public ArrayList<CurrentWeather> currentWeatherList = new ArrayList<CurrentWeather>();
    public ArrayList<CurrentWeather> hourlyWeatherList = new ArrayList<CurrentWeather>();
    public ArrayList<CurrentWeather> dailyWeatherList = new ArrayList<CurrentWeather>();
    HourlyTempRecycleAdapter HourlyTempRecycleAdapter;
    RecyclerView RecycleListView;
    private final int REQUEST_CODE_LOCATION_PERMISSION = 44;
    String apiKey = "Api Key goes Here";
    

    String forecastURL = "https://api.openweathermap.org/data/2.5/onecall?" +
            "lat=" + latitude + "&" +
            "lon=" + longitude + "&" +
            "appid=" + apiKey + "&" +
            "units=" + units;

    /**************************************
     * Main initialized Method.  *
     **************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        //Intitalized fusedLocation
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        //try to get any saved data from shared pref
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        isMetric = sharedPref.getBoolean("METRIC_SETTING", false); //default to farenheit
        Toast.makeText(this, "Got METRIC_SETTING: " + isMetric, Toast.LENGTH_SHORT).show();
        if (isMetric) {
            units = "metric";
        }

       // checkWeatherPerm();
        //getLocation();
        checkFirstRun();







    }

    /****************************************
     * Methods and Actions that do things  *
     ****************************************/

    public void checkWeatherPerm(){
        //TODO if the user saves multiple locations all of those lat/longs should be saved in the shared preferences
        //if sharedPrefs doesnt let you save an array you can just reformat the data as a string
        //45,78|65,87
        //get string .split("|")

        //checkpermission
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //When Permission Granted
            locationAvalible = true;
            getLocation();

        } else {
            //When Permission Denied
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
           // createLocationSettingsFragment();
        }


    }

    //this takes in a number which represents what screen we want to refresh
    //0 is the main
    //1
    public void getWeatherData(int PAGE) {

        if (isNetworkAvailable()) {
            if (isMetric == true) {
                units = "metric";
            } else {
                units = "imperial";
            }
            forecastURL = "https://api.openweathermap.org/data/2.5/onecall?" +
                    "lat=" + latitude + "&" +
                    "lon=" + longitude + "&" +
                    "appid=" + apiKey + "&" +
                    "units=" + units;

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
                            hourlyWeatherList.clear();
                            hourlyWeatherList = getHourlyWeatherArray(jSonData);
                            dailyWeatherList.clear();
                            dailyWeatherList = getDailyWeatherList(jSonData);

                            if (currentWeatherList.isEmpty()) {
                                currentWeatherList.add(currentWeather);
                            } else if (currentWeatherList.size() >= 1) {
                                currentWeatherList.set(0, currentWeather);
                            }

                            /* Run UI Thread forces the create fragment method/action to happen
                             at UI Thread level instead of main thread level.             */
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (PAGE == 0) {
                                        createFragment(currentWeather);
                                    } else if (PAGE == settingsAndLocationListPage.SETTINGS_AND_LOCATION_PAGE) {// 1
                                        createLocationSettingsFragment();
                                    } /*else if (PAGE == number of weather detail page ){

                                            create weather detail page
                                    |*/

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


    private void createFragment(CurrentWeather c) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        MainFragment MainFrag = MainFragment.newInstance(c);
        fragmentTransaction.replace(R.id.container, MainFragment.newInstance(c));
        fragmentTransaction.addToBackStack("Main");
        fragmentTransaction.commit();

    }

    public void createAddLocationFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, addLocationPage.newInstance("", ""))
                .commitNow();

        isLocationView = true;

    }


    public void createLocationSettingsFragment() {


        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        settingsAndLocationListPage SettingsFrag = settingsAndLocationListPage.newInstance();
        fragmentTransaction.replace(R.id.container, SettingsFrag);
        fragmentTransaction.addToBackStack("Settings");
        fragmentTransaction.commit();


        if (isLocationView == true) {
            isLocationView = false;
        }

        isSettingView = true;

    }

    public void createTempDetailFragment(CurrentWeather c) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.tempDetailView, TempDetailCardFragment.newInstance(c))
                .commitNow();

        isDetailView = true;


    }

    public void createHourlyWeatherFragment(CurrentWeather c) {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.hourlyWeatherView, HourlyWeatherFragment.newInstance())
                .commitNow();

    }

    public void createDailyWeatherFragment() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.dailyWeatherView, DailyWeather.newInstance("", ""))
                .commitNow();

    }

    public void createFirstTimeLoadFragment() {

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, InitialLocationAuthorization.newInstance("", ""))
                .commitNow();

    }

    public void backToMainFragment() {
        if (isDetailView == true) {
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.tempDetailView)).commit();
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.hourlyWeatherView)).commit();
            getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.dailyWeatherView)).commit();
            isDetailView = false;
        }

        if (isSettingView == true) {
            isSettingView = false;
        }

        createFragment(currentWeather);

    }

    private void getLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    System.out.println("magic location found  ");
                    //Initiliaze location
                    Location location = task.getResult();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(MainActivity.this,
                                Locale.getDefault());

                        try {  //Initialize geoCoder
                            //Initilialize address list

                            List<Address> addresss = geocoder.getFromLocation(
                                    location.getLatitude(),
                                    location.getLongitude(),
                                    1
                            );
                            if (addresss != null && !addresss.isEmpty()) {
                            latitude = addresss.get(0).getLatitude();
                            longitude = addresss.get(0).getLongitude();
                            country = addresss.get(0).getCountryName();
                            city = addresss.get(0).getLocality();
                            locAddress = addresss.get(0).getAddressLine(0);

                            Log.d("myTag", latitude + " "
                                    + longitude + " " +
                                    country + " " +
                                    city + " " + locAddress);
                            if(addresss != null && !addresss.isEmpty()){
                            getWeatherData(0);

                            }}else {
                                getLocation();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    // Task failed with an exception
                    Exception exception = task.getException();
                }
            }
        });
    }

    private ArrayList<CurrentWeather> getHourlyWeatherArray(String jSonData) throws JSONException {

        JSONObject forecast = new JSONObject(jSonData);
        String timezone = forecast.getString("timezone");
        JSONArray jWeatherArray = forecast.getJSONArray("hourly");
        ArrayList<CurrentWeather> hourlyWeatherList = new ArrayList<>();

        if (jWeatherArray.length() != 0) { //this represents the number of rows in the database


            ArrayList<JSONObject> listdata = new ArrayList<>();

            if (jWeatherArray != null) {
                for (int i = 0; i < jWeatherArray.length(); i++) {
                    listdata.add(jWeatherArray.getJSONObject(i));
                }
            }

            for (JSONObject jHourlyweather : listdata
            ) {
                JSONArray jjWeatherArray = jHourlyweather.getJSONArray("weather");
                JSONObject JJweather = (JSONObject) jjWeatherArray.get(0);
                CurrentWeather jWeather = new CurrentWeather(city,
                        JJweather.getString("icon"),
                        jHourlyweather.getDouble("temp"),
                        jHourlyweather.getDouble("feels_like"),
                        jHourlyweather.getDouble("humidity"),
                        0.0,
                        JJweather.getString("main"),
                        JJweather.getString("description"),
                        jHourlyweather.getLong("dt"),
                        timezone
                );
                hourlyWeatherList.add(jWeather);

            }
        }

        return hourlyWeatherList;
    }


    private CurrentWeather getCurrentDetail(String jSonData) throws JSONException {

        JSONObject forecast = new JSONObject(jSonData);
        String timezone = forecast.getString("timezone");
        Log.i(TAG, "From JSON: " + timezone);
        JSONObject current = forecast.getJSONObject("current");
        JSONArray jWeatherArray = current.getJSONArray("weather");
        JSONObject weather = (JSONObject) jWeatherArray.get(0);

        CurrentWeather currentWeather = new CurrentWeather(city,
                weather.getString("icon"),
                current.getDouble("temp"),
                current.getDouble("feels_like"),
                current.getDouble("humidity"),
                0.0,
                weather.getString("main"),
                weather.getString("description"),
                current.getLong("dt"),
                timezone
        );

        return currentWeather;
    }

    private ArrayList<CurrentWeather> getDailyWeatherList(String jSonData) throws JSONException {

        JSONObject forecast = new JSONObject(jSonData);
        String timezone = forecast.getString("timezone");

        JSONArray jDailyWeatherArray = forecast.getJSONArray("daily");
        ArrayList<JSONObject> listdata = new ArrayList<>();

        if (jDailyWeatherArray != null) {
            for (int i = 0; i < jDailyWeatherArray.length(); i++) {
                listdata.add(jDailyWeatherArray.getJSONObject(i));
            }
        }


        for (JSONObject jDailyweather : listdata
        ) {
            JSONObject tempJobj = jDailyweather.getJSONObject("temp");
            JSONArray jjWeatherArray = jDailyweather.getJSONArray("weather");
            JSONObject JJweather = (JSONObject) jjWeatherArray.get(0);
            CurrentWeather jWeather = new CurrentWeather(city,
                    JJweather.getString("icon"),
                    0.0,
                    0.0,
                    jDailyweather.getDouble("humidity"),
                    0.0,
                    JJweather.getString("main"),
                    JJweather.getString("description"),
                    jDailyweather.getLong("dt"),
                    timezone
            );
            Log.i(TAG, jWeather.ePochTimeConverter(
                    jWeather.getTime()).getInstance().
                    getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
            jWeather.setMinTemp(tempJobj.getDouble("min"));
            jWeather.setMaxTemp(tempJobj.getDouble("max"));
            /*Double rainNumber = jDailyweather.getDouble("rain");
            if (rainNumber == null ) {
                jWeather.setPrecipChance(rainNumber);
            }*/
            dailyWeatherList.add(jWeather);
        }

        return dailyWeatherList;
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

    private void checkFirstRun() {

        final String PREFS_NAME = "MyPrefsFile";
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;

        // Get current version code
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            // This is just a normal run
            checkWeatherPerm();
            Toast.makeText(this, "normalrun ", Toast.LENGTH_SHORT).show();
            return;

        } else if (savedVersionCode == DOESNT_EXIST) {

            // TODO This is a new install (or the user cleared the shared preferences)
            Toast.makeText(this, "freshinstall ", Toast.LENGTH_SHORT).show();
            createFirstTimeLoadFragment();

        } else if (currentVersionCode > savedVersionCode) {

            // TODO This is an upgrade
            Toast.makeText(this, "updateinstall ", Toast.LENGTH_SHORT).show();
            createFirstTimeLoadFragment();
        }

        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).apply();
    }

    public void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog

        if (isDetailView == true) {
            backToMainFragment();
        }

        if (isSettingView == true) {
            backToMainFragment();
        }

        if (isLocationView == true) {
            createLocationSettingsFragment();
        }


        //super.onBackPressed();  // optional depending on your needs

    }


}
