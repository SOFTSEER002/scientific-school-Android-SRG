package com.jeannypr.scientificstudy.Registration.adapter;


import android.content.Context;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Filter;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Registration.model.ClassWiseRegistrationModel;
import com.jeannypr.scientificstudy.databinding.RowClassWiseRegistrationBinding;

import java.util.ArrayList;
import java.util.List;

public class ClassWiseRegistrationAdapter extends RecyclerView.Adapter<ClassWiseRegistrationAdapter.MyViewHolder> {

    Context mContext;
    private List<ClassWiseRegistrationModel> registrationlList, filterList;


    @Override

    public int getItemCount() {
        return registrationlList.size();
    }

    public ClassWiseRegistrationAdapter(Context context, List<ClassWiseRegistrationModel> registrationData) {
        super();
        mContext = context;
        registrationlList = registrationData;
        filterList = registrationData;

    }


    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RowClassWiseRegistrationBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.row_class_wise_registration, parent, false);
        return new ClassWiseRegistrationAdapter.MyViewHolder(binding);


    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        final ClassWiseRegistrationModel registrationModel = registrationlList.get(position);
        ((MyViewHolder) holder).bind(registrationModel);

    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (!charString.isEmpty()) {

                    ArrayList<ClassWiseRegistrationModel> temp = new ArrayList<>();

                    for (ClassWiseRegistrationModel row : filterList) {

                        if (row.ClassName.toLowerCase().contains(charString.toLowerCase())) {
                            temp.add(row);
                        }
                    }
                    registrationlList = temp;

                } else {

                    registrationlList = filterList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = registrationlList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                registrationlList = (ArrayList<ClassWiseRegistrationModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        public RowClassWiseRegistrationBinding itemBinding;


        MyViewHolder(RowClassWiseRegistrationBinding binding) {
            super(binding.getRoot());
            itemBinding = binding;
        }

        void bind(final ClassWiseRegistrationModel model) {
            itemBinding.setRegistration(model);
            itemBinding.classNameVal.setText(String.valueOf(model.ClassName));
            itemBinding.totalRegistrationVal.setText(String.valueOf(model.TotalRegistration));
            itemBinding.takenVal.setText(String.valueOf(model.TotalPermanentAdmission));
            itemBinding.totalFeeVal.setText(String.valueOf(model.TotalRegistrationFees));

        }
    }
}


