package com.jeannypr.scientificstudy.Transport.adapter;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.model.AssignedRouteModel;
import com.jeannypr.scientificstudy.Transport.model.RouteModel;
import com.jeannypr.scientificstudy.databinding.RowSelectRouteBinding;

import java.util.ArrayList;
import java.util.List;

public class SeletctRouteAdapter extends RecyclerView.Adapter<SeletctRouteAdapter.MyViewHolder> {

    OnItemClickListener listener;
    Context mContext;
    private List<RouteModel> routeList;
    private int lastSelectedButton = -1;
    //  int assignedRouteId;
    ArrayList<AssignedRouteModel> assignedRouteModel;

    @Override

    public int getItemCount() {
        return routeList.size();
    }

    public SeletctRouteAdapter(Context context, List<RouteModel> routesList, ArrayList<AssignedRouteModel> assignedRoute, OnItemClickListener itemClickListener) {
        super();
        mContext = context;
        routeList = routesList;
        listener = itemClickListener;
        assignedRouteModel = assignedRoute;
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowSelectRouteBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_select_route, parent, false);
        return new SeletctRouteAdapter.MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final RouteModel routeModel = routeList.get(position);
        holder.bind(routeModel);
        holder.itemBinding.routeName.setChecked(lastSelectedButton == position);
        routeModel.AdapterPosition = position;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        // RadioButton selectionState;

        public RowSelectRouteBinding itemBinding;

        MyViewHolder(RowSelectRouteBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;

        }

        void bind(final RouteModel route) {
            itemBinding.setRoute(route);

            itemBinding.routeName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastSelectedButton = getAdapterPosition();
                    notifyDataSetChanged();
                    listener.onItemClick(route);
                }
            });

            for (AssignedRouteModel assignedRoute : assignedRouteModel) {

                if (route.getRouteId() == assignedRoute.getId()) {
                    itemBinding.assignedRoute.setVisibility(View.VISIBLE);
                    itemBinding.assignedRoute.setText(R.string.route_assigned);
                    //itemBinding.assignedRoute.setTextColor(mContext.getResources().getColor(R.color.white));

                    itemBinding.routeContainer.setBackgroundResource(R.color.blue4);
                    itemBinding.divider.setBackgroundResource(R.color.colorPrimaryDark);
                    break;

                } else {
                    itemBinding.assignedRoute.setVisibility(View.GONE);
                    itemBinding.routeContainer.setBackgroundResource(R.color.white);
                    itemBinding.divider.setBackgroundResource(R.color.black6);
                }
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(RouteModel model);
    }
}