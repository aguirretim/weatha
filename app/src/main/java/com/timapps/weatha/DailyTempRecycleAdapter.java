package com.timapps.weatha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DailyTempRecycleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {




    /**************************************
     * initialized Variables for Object.  *
     **************************************/

    private List<CurrentWeather> vlist;
    private Context vContext;
    private LayoutInflater vInflator;
    private CurrentWeather vCurrentWeather;
    private DailyTempRecycleAdapter.RecyclerClickListener recyclerClickListener;

    /****************************
     * Constructor for Object.  *
     ****************************/

    public DailyTempRecycleAdapter(List<CurrentWeather> vlist, Context vContext) {
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

        View v = vInflator.inflate(R.layout.daily_weather_list_item, parent, false);
        return new DailyTempRecycleAdapter.ViewHolder(v);
    }

    // Responsible to give values to each vie in a layout
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        DailyTempRecycleAdapter.ViewHolder ViewHolder = (DailyTempRecycleAdapter.ViewHolder) holder;
        vCurrentWeather = vlist.get(position);


        ViewHolder.dayText.setText(vCurrentWeather.ePochTimeConverter(
                vCurrentWeather.getTime()).
                getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()));
        ViewHolder.highTemppText.setText((int) vCurrentWeather.getMaxTemp() + "\u00B0");
        ViewHolder.lowTemppText.setText((int) vCurrentWeather.getMinTemp() + "\u00B0");
        ViewHolder.dayWeatherIcon.setImageResource((vCurrentWeather.
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

        private TextView dayText;
        private TextView highTemppText;
        private TextView lowTemppText;
        private ImageView dayWeatherIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayText = (TextView) itemView.findViewById(R.id.dayText);
            highTemppText = (TextView) itemView.findViewById(R.id.highTemppText);
            lowTemppText = (TextView) itemView.findViewById(R.id.lowTemppText);
            dayWeatherIcon = (ImageView) itemView.findViewById(R.id.dayWeatherIcon);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (recyclerClickListener != null) {
                recyclerClickListener.onClickPerformed(getAdapterPosition());
            }
        }
    }

    public void setRecyclerClickListener(DailyTempRecycleAdapter.RecyclerClickListener recyclerClickListener) {
        this.recyclerClickListener = (RecyclerClickListener) recyclerClickListener;
    }

    public interface RecyclerClickListener {
        void onClickPerformed(int postion);

    }


}
