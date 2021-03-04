package com.timapps.weatha;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;

public class LocationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    /**************************************
     * initialized Variables for Object.  *
     **************************************/

    private List<CurrentWeather> vlist;
    private Context vContext;
    private LayoutInflater vInflator;
    private CurrentWeather vCurrentWeather;
    private LocationListAdapter.RecyclerClickListener recyclerClickListener;

    /****************************
     * Constructor for Object.  *
     ****************************/

    public LocationListAdapter(List<CurrentWeather> vlist, Context vContext) {
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

        View v = vInflator.inflate(R.layout.location_list_item, parent, false);
        return new LocationListAdapter.ViewHolder(v);
    }

    // Responsible to give values to each vie in a layout
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        LocationListAdapter.ViewHolder ViewHolder = (LocationListAdapter.ViewHolder) holder;
        vCurrentWeather = vlist.get(position);
        SimpleDateFormat localDateFormat = new SimpleDateFormat("h:mm a");

        ViewHolder.currentTimeText.setText(localDateFormat.format(vCurrentWeather.ePochTimeConverter(vCurrentWeather.getTime()).getTime())+""
        );
        ViewHolder.tempText.setText((int) vCurrentWeather.getTemperature() + "\u00B0");
        ViewHolder.cityNameLable.setText( vCurrentWeather.getLocationLabel());

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

        private TextView tempText;
        private TextView currentTimeText;
        private TextView cityNameLable;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            tempText = (TextView) itemView.findViewById(R.id.tempText);
            currentTimeText = (TextView) itemView.findViewById(R.id.updateTimeText);
            cityNameLable = (TextView) itemView.findViewById(R.id.cityNameLable);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            if (recyclerClickListener != null) {
                recyclerClickListener.onClickPerformed(getAdapterPosition());
            }
        }
    }

    public void setRecyclerClickListener(LocationListAdapter.RecyclerClickListener recyclerClickListener) {
        this.recyclerClickListener = (LocationListAdapter.RecyclerClickListener) recyclerClickListener;
    }

    public interface RecyclerClickListener {
        void onClickPerformed(int postion);

    }


}

