package com.jeannypr.scientificstudy.Transport.adapter;

import android.content.Context;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.model.RouteDetailModel;

import java.util.List;

public class StoppageDetailAdapter extends RecyclerView.Adapter {


    Context mContext;
    private List<RouteDetailModel> stoppageDetailModels;
    //OnItemClickListener listener;

    public StoppageDetailAdapter(Context context, List<RouteDetailModel> routes) {
        super();
        mContext = context;
        stoppageDetailModels = routes;

    }

    @Override
    public int getItemCount() {
        return stoppageDetailModels.size();
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
                .inflate(R.layout.row_route_detail, parent, false);
        return new StoppageDetailAdapter.MyViewHolder(view);


    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final RouteDetailModel route = (RouteDetailModel) stoppageDetailModels.get(position);
        ((MyViewHolder) holder).bind(route);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView studentName, className, routeName, admNo, vehicleNo, mobile;
        ImageView icStoppage;
        ConstraintLayout routeContainer;

        MyViewHolder(View itemView) {
            super(itemView);

            studentName = itemView.findViewById(R.id.studentName);
            className = itemView.findViewById(R.id.className);
            routeName = itemView.findViewById(R.id.stoppageName);

            admNo = itemView.findViewById(R.id.admNo);
            vehicleNo = itemView.findViewById(R.id.vehicleNo);
            mobile = itemView.findViewById(R.id.mobile);
            icStoppage = itemView.findViewById(R.id.icStoppage);

            routeContainer = itemView.findViewById(R.id.route_Container);

        }

        void bind(final RouteDetailModel route) {
            studentName.setText(route.StudentName != null ? route.StudentName.substring(0, 1).toUpperCase() + route.StudentName.substring(1).toLowerCase() : "");
            className.setText("Class: " + (route.ClassName != null ? route.ClassName : ""));

            routeName.setText(route.RouteName != null ? route.RouteName : "");
            icStoppage.setImageResource(R.drawable.transport2);
            admNo.setText("Adm no. " + (route.AdmissionNumber != null ? route.AdmissionNumber : ""));
            vehicleNo.setText(route.VehicleNo != null ? route.VehicleNo : "");
            mobile.setText(route.ParentPhone != null ? route.ParentPhone : "");

            if (getAdapterPosition() % 2 == 0) {
                routeContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.daily_collection_purple_bg));
            } else {
                routeContainer.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.daily_collection_red_bg));
            }

        }
    }
}


