package com.jeannypr.scientificstudy.Dashboard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowLearnListTabBinding;


public class LearnDashboardToolListAdapter extends RecyclerView.Adapter<LearnDashboardToolListAdapter.MyViewHolder> {

    private Context context;
    private OnItemClickListener listener;

    public LearnDashboardToolListAdapter(Context context) {
        this.context = context;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_learn_list_tab, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        /*ChildListModel model = childListModels.get(holder.getAdapterPosition());
        holder.binding.tvName.setText(model.getChild_name());
        holder.binding.tvProfile.setText(model.getSchool_name());*/

        //holder.binding.tvTitle.setText("");

        //holder.binding.ivIcon.setImageResource(R.drawable.placeholder);
        //holder.binding.tvTitle.setText(opraterName[position]);


    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       RowLearnListTabBinding binding;
        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);
            //binding.cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onItemClick(getAdapterPosition(), v);
        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(int position, View view);
    }
}