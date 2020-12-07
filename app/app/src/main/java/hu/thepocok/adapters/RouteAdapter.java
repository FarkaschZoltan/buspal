package hu.thepocok.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hu.farkasch.buspalbackend.objects.BusRoute;
import hu.kristol.buspal.R;
import hu.kristol.buspal.Timetable;

public class RouteAdapter extends RecyclerView.Adapter<RouteAdapter.RouteViewHolder> {
    private Context mCtx;
    private List<BusRoute> routeList;
    private RecyclerView recyclerView;

    public RouteAdapter(Context mCtx, List<BusRoute> routeList, RecyclerView recyclerView) {
        this.mCtx = mCtx;
        this.routeList = routeList;
        this.recyclerView = recyclerView;
    }

    @Override
    public RouteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        @SuppressLint("InflateParams") View view = inflater.inflate(R.layout.routes_list, null);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemPosition = recyclerView.getChildLayoutPosition(view);
                BusRoute item = routeList.get(itemPosition);
                if(item.getStops() == null){
                    Toast toast = Toast.makeText(mCtx,
                            "Data is loading, please try again in a few seconds", Toast.LENGTH_SHORT);
                    toast.show();
                    return;
                }

                Intent i = new Intent(mCtx, Timetable.class);
                i.putExtra("stopsList", item.getStops());
                i.putExtra("routeName", item.getName());
                mCtx.startActivity(i);
            }
        });

        return new RouteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RouteViewHolder holder, int position) {
        BusRoute route = routeList.get(position);

        holder.routeName.setText(route.getName());
        holder.routeDestination.setText(route.getDestinations());

        switch (route.getType()){
            case BUS:
                holder.routeColor.setBackgroundColor(mCtx.getResources().getColor(R.color.bus));
                break;
            case TRAM:
                holder.routeColor.setBackgroundColor(mCtx.getResources().getColor(R.color.tram));
                break;
            case TROLLEY:
                holder.routeColor.setBackgroundColor(mCtx.getResources().getColor(R.color.trolley));
                break;
            case METRO:
                if(route.getName().equals("M1")){
                    holder.routeColor.setBackgroundColor(mCtx.getResources().getColor(R.color.metro_1));
                }else if(route.getName().equals("M2")){
                    holder.routeColor.setBackgroundColor(mCtx.getResources().getColor(R.color.metro_2));
                }else if(route.getName().equals("M3")){
                    holder.routeColor.setBackgroundColor(mCtx.getResources().getColor(R.color.metro_3));
                }else if(route.getName().equals("M4")){
                    holder.routeColor.setBackgroundColor(mCtx.getResources().getColor(R.color.metro_4));
                }
                break;
            case SUBURBAN_RAILWAY:
                if(route.getName().equals("H5")){
                    holder.routeColor.setBackgroundColor(mCtx.getResources().getColor(R.color.suburban_5));
                }else if(route.getName().equals("H6")){
                    holder.routeColor.setBackgroundColor(mCtx.getResources().getColor(R.color.suburban_6));
                }else if(route.getName().equals("H7")){
                    holder.routeColor.setBackgroundColor(mCtx.getResources().getColor(R.color.suburban_7));
                }else if(route.getName().equals("H8") || route.getName().equals("H9")){
                    holder.routeColor.setBackgroundColor(mCtx.getResources().getColor(R.color.suburban_8));
                }
            default:
                holder.routeColor.setBackgroundColor(mCtx.getResources().getColor(R.color.bus));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return routeList.size();
    }

    class RouteViewHolder extends RecyclerView.ViewHolder {

        TextView routeColor, routeDestination, routeName;

        public RouteViewHolder(View itemView) {
            super(itemView);

            routeColor = itemView.findViewById(R.id.route_color);
            routeDestination = itemView.findViewById(R.id.route_destination);
            routeName = itemView.findViewById(R.id.route_name);
        }
    }
}