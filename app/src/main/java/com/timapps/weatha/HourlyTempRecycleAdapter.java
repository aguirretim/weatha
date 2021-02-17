package com.timapps.weatha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HourlyTempRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**************************************
     * initialized Variables for Object.  *
     **************************************/

    private List<CurrentWeather> vlist;
    private Context vContext;
    private LayoutInflater vInflator;
    private CurrentWeather vCurrentWeather;
    private RecyclerClickListener recyclerClickListener;

    /****************************
     * Constructor for Object.  *
     ****************************/

    public HourlyTempRecycleAdapter(List<CurrentWeather> vlist, Context vContext) {
        this.vlist = vlist;
        this.vContext = vContext;
        vInflator = LayoutInflater.from(vContext);
    }

    /****************************
     * Methods for adapter *
     ****************************/

    // Responsible for telling what each cell is suppose to look like.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = vInflator.inflate(R.layout.hourly_weather_list_item, parent, false);
        return new ViewHolder(v);
    }

    // Responsible to give values to each vie in a layout
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ViewHolder ViewHolder = (ViewHolder) holder;
        vCurrentWeather = vlist.get(position);

        ViewHolder.currentNowHeadetText.setText(vCurrentWeather.midNoonConverter((vCurrentWeather.ePochTimeConverter(vCurrentWeather.getTime())
                .get(vCurrentWeather.ePochTimeConverter(vCurrentWeather.getTime()).HOUR_OF_DAY))) +
                " " +
                vCurrentWeather.amPmFinder(vCurrentWeather.ePochTimeConverter(vCurrentWeather.getTime())
                        .get(vCurrentWeather.ePochTimeConverter(vCurrentWeather.getTime()).AM_PM))
                +
                "");
        ViewHolder.currentWeatherTempText.setText((int) vCurrentWeather.getTemperature() + "");
        ViewHolder.weatherIcon.setImageResource((vCurrentWeather.
                getIconId(vCurrentWeather.getIcon())));
    }

    // responsible for how many elements are in the recycle view
    @Override
    public int getItemCount() {
        return vlist.size();
    }

    /****************************
     * View holder binding the data *
     ****************************/

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView currentNowHeadetText;
        TextView currentWeatherTempText;
        ImageView weatherIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            currentNowHeadetText = (TextView) itemView.findViewById(R.id.currentNowHeadetText);
            currentWeatherTempText = (TextView) itemView.findViewById(R.id.currentWeatherTempText);
            weatherIcon = (ImageView) itemView.findViewById(R.id.weatherIcon);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (recyclerClickListener != null) {
                recyclerClickListener.onClickPerformed(getAdapterPosition());
            }
        }
    }

    public void setRecyclerClickListener(RecyclerClickListener recyclerClickListener) {
        this.recyclerClickListener = recyclerClickListener;
    }

    public interface RecyclerClickListener {
        void onClickPerformed(int postion);

    }
}