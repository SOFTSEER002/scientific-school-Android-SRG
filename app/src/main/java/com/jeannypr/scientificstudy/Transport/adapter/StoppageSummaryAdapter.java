package com.jeannypr.scientificstudy.Transport.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.activity.StoppageDetailActivity;
import com.jeannypr.scientificstudy.Transport.model.StoppageSummaryModel;

import java.util.List;

public class StoppageSummaryAdapter extends RecyclerView.Adapter {


    Context mContext;
    private List<StoppageSummaryModel> stoppageSummaryModels;
    //OnItemClickListener listener;

    public StoppageSummaryAdapter(Context context, List<StoppageSummaryModel> routes) {
        super();
        mContext = context;
        stoppageSummaryModels = routes;

    }

    @Override
    public int getItemCount() {
        return stoppageSummaryModels.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;


        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_route_list, parent, false);
        return new StoppageSummaryAdapter.MyViewHolder(view);


    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final StoppageSummaryModel route = (StoppageSummaryModel) stoppageSummaryModels.get(position);
        ((MyViewHolder) holder).bind(route);

        ((MyViewHolder) holder).rowRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchToDetail(route);
            }
        });

        ((MyViewHolder) holder).routeDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SwitchToDetail(route);
            }
        });
    }

    private void SwitchToDetail(StoppageSummaryModel route) {
        Intent intent = new Intent(mContext, StoppageDetailActivity.class);
        intent.putExtra("stoppageName", route.PlaceName);
        // intent.putExtra("routeId",route.Id);
        mContext.startActivity(intent);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView totalStudents, routeName;
        /*Button routeDetailsBtn;*/
        ImageView routeDetailsBtn;
        ConstraintLayout rowRoute;

        MyViewHolder(View itemView) {
            super(itemView);

            totalStudents = itemView.findViewById(R.id.totalStudents);
            routeName = itemView.findViewById(R.id.routeName);
            routeDetailsBtn = itemView.findViewById(R.id.routeDetailsBtn);
            rowRoute = itemView.findViewById(R.id.rowRoute);

        }


        void bind(final StoppageSummaryModel route) {
            totalStudents.setText(route.TotalStudents != null ? route.TotalStudents : "");
            routeName.setText(route.PlaceName != null ? route.PlaceName.substring(0, 1).toUpperCase() + route.PlaceName.substring(1).toLowerCase() : "");

        }


    }


}


