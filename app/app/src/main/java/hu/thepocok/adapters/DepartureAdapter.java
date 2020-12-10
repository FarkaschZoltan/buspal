package hu.thepocok.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.farkasch.buspalbackend.datastructures.Time;
import hu.farkasch.buspalbackend.objects.Departure;
import hu.farkasch.buspalbackend.objects.RouteType;
import hu.kristol.buspal.R;
import hu.kristol.buspal.RouteStops;

public class DepartureAdapter extends RecyclerView.Adapter<DepartureAdapter.DepartureViewHolder>{
    private Context mCtx;
    private List<Departure> departureList;
    private RecyclerView recyclerView;

    public DepartureAdapter(Context mCtx, List<Departure> departureList, RecyclerView recyclerView) {
        this.mCtx = mCtx;
        this.departureList = departureList;
        this.recyclerView = recyclerView;
    }

    @Override
    public DepartureAdapter.DepartureViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.departure_list, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                Departure item = departureList.get(itemPosition);

                Intent i = new Intent(mCtx, RouteStops.class);
                i.putExtra("tripId", item.tripId);
                i.putExtra("routeName", item.routeName);
                i.putExtra("routeType", item.routeType);
                mCtx.startActivity(i);
            }
        });
        return new DepartureAdapter.DepartureViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DepartureAdapter.DepartureViewHolder holder, int position) {
        Departure departure = departureList.get(position);

        holder.routeName.setText(departure.routeName);
        holder.destination.setText(departure.destination);
        holder.departureTime.setText(new Time(departure.departureTime).toString(true));
        RouteType tr = null;

        switch (departure.routeType){
            case 0:
                tr = RouteType.TRAM;
                break;
            case 1:
                tr = RouteType.METRO;
                break;
            case 3:
                tr = RouteType.BUS;
                break;
            case 4:
                tr = RouteType.FERRY;
                break;
            case 11:
                tr = RouteType.TROLLEY;
                break;
            case 109:
                tr = RouteType.SUBURBAN_RAILWAY;
                break;
            case 800:
                tr = RouteType.TROLLEY;
                break;
            default:
                tr = null;
                break;
        }


        switch (tr){
            case BUS:
                if(departure.routeName.charAt(0) == '9' && departure.routeName.length() >= 3){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.night_bus));
                    holder.routeName.setTextColor(Color.parseColor("#ffffff"));
                } else{
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.bus));
                }
                break;
            case TRAM:
                holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.tram));
                break;
            case TROLLEY:
                holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.trolley));
                break;
            case METRO:
                if(departure.routeName.equals("M1")){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.metro_1));
                }else if(departure.routeName.equals("M2")){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.metro_2));
                }else if(departure.routeName.equals("M3")){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.metro_3));
                }else if(departure.routeName.equals("M4")){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.metro_4));
                }
                break;
            case SUBURBAN_RAILWAY:
                if(departure.routeName.equals("H5")){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.suburban_5));
                }else if(departure.routeName.equals("H6")){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.suburban_6));
                }else if(departure.routeName.equals("H7")){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.suburban_7));
                }else if(departure.routeName.equals("H8") || departure.routeName.equals("H9")){
                    holder.cardView.setCardBackgroundColor(mCtx.getResources().getColor(R.color.suburban_8));
                }else{
                    Log.d("RouteType", "None of the above");
                }
            default:
                holder.cardView.setBackgroundColor(mCtx.getResources().getColor(R.color.bus));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return departureList.size();
    }

    class DepartureViewHolder extends RecyclerView.ViewHolder {

        TextView routeName, destination, departureTime;
        CardView cardView;

        public DepartureViewHolder(View itemView) {
            super(itemView);

            routeName = itemView.findViewById(R.id.route_name);
            destination = itemView.findViewById(R.id.destination);
            departureTime = itemView.findViewById(R.id.departure_time);
            cardView = itemView.findViewById(R.id.route_name_color);
        }
    }
}
