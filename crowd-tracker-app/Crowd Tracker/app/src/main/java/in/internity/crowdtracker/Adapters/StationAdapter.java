package in.internity.crowdtracker.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.internity.crowdtracker.DataModels.StationDataModal;
import in.internity.crowdtracker.R;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.MyViewHolder> {

    private List<StationDataModal> Listitems;
    private Context context;
    private StationClickListerner stationClickListerner;


    public interface StationClickListerner {
        void stationItemClicked(String StationCode,String stationName);

    }

    public StationClickListerner getStationClickListerner() {
        return stationClickListerner;
    }

    public void setStationClickListerner(StationClickListerner BikeClickListner) {
        this.stationClickListerner = BikeClickListner;
    }


    public StationAdapter(List<StationDataModal> listitems, Context context) {
        Listitems = listitems;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final StationDataModal mdatamodel = Listitems.get(position);
        if (mdatamodel != null)
        {
            holder.stationName.setText(mdatamodel.getStationName());
            holder.estTime.setText(mdatamodel.getTime() + "");

            switch (position) {

                case 0:
                    holder.population.setText("Low");
                    break;

                case 1:
                    holder.population.setText("Medium");
                    break;
                case 2:
                    holder.population.setText("High");
                    break;
                default:
                    holder.population.setText("Metro Not Working ");
                    break;

            }

            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (stationClickListerner != null) {
                        stationClickListerner.stationItemClicked(mdatamodel.getStationCode(),mdatamodel.getStationName());
                    }
                }
            });
            holder.erickBookBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (stationClickListerner != null) {
                        stationClickListerner.stationItemClicked(mdatamodel.getStationCode(),mdatamodel.getStationName());
                    }
                }
            });


        }
    }

    @Override
    public int getItemCount() {
        return Listitems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.station_name)
        TextView stationName;
        @BindView(R.id.est_time)
        TextView estTime;
        @BindView(R.id.population)
        TextView population;
        @BindView(R.id.erickBookBtn)
        Button erickBookBtn;
        View item;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            ButterKnife.bind(this, itemView);
        }
    }
}
