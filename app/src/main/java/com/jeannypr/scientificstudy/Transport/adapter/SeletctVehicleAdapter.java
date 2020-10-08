package com.jeannypr.scientificstudy.Transport.adapter;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Transport.model.JourneyDetailModel;
import com.jeannypr.scientificstudy.Transport.model.VehiclesModel;
import com.jeannypr.scientificstudy.databinding.RowSelectVehicleBinding;

import java.util.List;

public class SeletctVehicleAdapter extends RecyclerView.Adapter<SeletctVehicleAdapter.MyViewHolder> {

    OnItemClickListener listener;
    Context mContext;
    private List<VehiclesModel> routeList;
    private int lastSelectedButton = -1;
    int assignedVehicleId, routeId;
    String routeName;

    @Override

    public int getItemCount() {
        return routeList.size();
    }

    public SeletctVehicleAdapter(Context context, List<VehiclesModel> routesList, int vehicleId, int routeId, String routeName,
                                 OnItemClickListener itemClickListener) {
        super();
        mContext = context;
        this.routeList = routesList;
        listener = itemClickListener;
        this.routeId = routeId;
        this.routeName = routeName;
        assignedVehicleId = vehicleId;
    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowSelectVehicleBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_select_vehicle, parent, false);
        return new SeletctVehicleAdapter.MyViewHolder(binding);

    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final VehiclesModel vehiclesModel = routeList.get(position);
        vehiclesModel.AdapterPosition = position;
        holder.bind(vehiclesModel);
        holder.itemBinding.radBtn.setChecked(lastSelectedButton == position);

    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RowSelectVehicleBinding itemBinding;
        VehiclesModel vehiclesModel;

        MyViewHolder(RowSelectVehicleBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final VehiclesModel model) {
            itemBinding.setVehicle(model);
            vehiclesModel = model;

            itemBinding.vehicleRow.setOnClickListener(this);
            itemBinding.radBtn.setOnClickListener(this);

            if (model.getVehicleId() == assignedVehicleId) {
                itemBinding.assignedRouteTxt.setVisibility(View.VISIBLE);
                itemBinding.assignedRouteTxt.setText(R.string.vehicle_assigned);
                //itemBinding.assignedRouteTxt.setTextColor(mContext.getResources().getColor(R.color.white));
                itemBinding.vehicleRow.setBackgroundResource(R.color.blue4);
            } else {
                itemBinding.assignedRouteTxt.setVisibility(View.GONE);
                itemBinding.vehicleRow.setBackgroundResource(R.color.white);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.vehicleRow:
                case R.id.radBtn:

                    lastSelectedButton = getAdapterPosition();
                    notifyDataSetChanged();

                 /*   if (lastSelectedButton != -1) {
                        VehiclesModel obj2 = new VehiclesModel();
                        obj2.isChecked = false;
                        obj2.setModelName("prevvvvvvvvvvvvvvvvvvvvvv");
                        notifyItemChanged(lastSelectedButton, obj2);
                    }

                    //   itemBinding.radBtn.setChecked(true);
                    VehiclesModel obj = new VehiclesModel();
                    obj.isChecked = true;
                    notifyItemChanged(vehiclesModel.AdapterPosition, obj);

                    lastSelectedButton = vehiclesModel.AdapterPosition;*/

                    JourneyDetailModel journeyDetail = new JourneyDetailModel();
                    journeyDetail.setRouteId(routeId);
                    journeyDetail.setRouteName(routeName);
                    journeyDetail.setVehicleId(vehiclesModel.getVehicleId());
                    journeyDetail.setVehicleNumber(vehiclesModel.getVehicleNo());
                    journeyDetail.setVehicleModel(vehiclesModel.getModelName());
                    journeyDetail.setVehicleType(vehiclesModel.getVehicleType());

                    listener.onItemClick(journeyDetail);
                    break;
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(JourneyDetailModel journeyDetail);
    }
}