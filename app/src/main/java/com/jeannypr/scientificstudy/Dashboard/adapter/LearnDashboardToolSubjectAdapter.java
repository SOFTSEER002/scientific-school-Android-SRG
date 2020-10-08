package com.jeannypr.scientificstudy.Dashboard.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Dashboard.model.LearnSubjectList;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowLearnSubjectBinding;

import java.util.ArrayList;


public class LearnDashboardToolSubjectAdapter extends RecyclerView.Adapter<LearnDashboardToolSubjectAdapter.MyViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    private ArrayList<LearnSubjectList> subjectLists;

    public LearnDashboardToolSubjectAdapter(Context context, ArrayList<LearnSubjectList> subjectLists) {
        this.context = context;
        this.subjectLists = subjectLists;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_learn_subject, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        LearnSubjectList model = subjectLists.get(holder.getAdapterPosition());


        holder.binding.btnSubject.setText(model.subjectName);
        //Log.e("moduler value","<<Position>> "+position%10);
        switch (position%10){
            case 0:
                int mErrorColor = Color.parseColor("#FF92FF");
                holder.binding.btnSubject.setBackgroundTintList(ColorStateList.valueOf(mErrorColor));
             break;

            case 1:
                holder.binding.btnSubject.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#91E1FF")));

                break;

            case 2:
                holder.binding.btnSubject.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FE9E7C")));

                break;

            case 3:
                holder.binding.btnSubject.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FFD230")));

                break;

            case 4:
                holder.binding.btnSubject.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#42A5F5")));

                break;

            case 5:
                holder.binding.btnSubject.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#B8AAFF")));

                break;

            case 6:
                holder.binding.btnSubject.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#26A69A")));

                break;
            case 7:
                holder.binding.btnSubject.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#7CB342")));

                break;

            case 8:
                holder.binding.btnSubject.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#795548")));

                break;

            case 9:
                holder.binding.btnSubject.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#607D8B")));

                break;


        }

    }

    @Override
    public int getItemCount() {
        return subjectLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       RowLearnSubjectBinding binding;
        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
           // itemView.setOnClickListener(this);
            binding.btnSubject.setOnClickListener(this);
            //binding.cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //Toast.makeText(context,"hi",Toast.LENGTH_SHORT).show();
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