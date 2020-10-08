package com.jeannypr.scientificstudy.LearnSubject.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.LearnSubject.model.LearnSubjectContentListModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowSubjectVideosBinding;

import java.util.ArrayList;


public class SubjectContentVideosAdapter extends RecyclerView.Adapter<SubjectContentVideosAdapter.MyViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    Activity activity;
    private ArrayList<LearnSubjectContentListModel> video;

    public SubjectContentVideosAdapter(Context context,Activity activity, ArrayList<LearnSubjectContentListModel> video) {
        this.context = context;
        this.activity =activity;
        this.video = video;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subject_videos, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        if(position %2 == 1)
        {
            holder.binding.cvSection.setCardBackgroundColor(Color.parseColor("#AFE5FF"));
            //holder.itemView.setBackgroundColor(Color.parseColor("#FF0000"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        }
        else
        {
            holder.binding.cvSection.setCardBackgroundColor(Color.parseColor("#FBBBBB"));
            //holder.itemView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
            //  holder.imageView.setBackgroundColor(Color.parseColor("#FFFAF8FD"));
        }

        LearnSubjectContentListModel model = video.get(holder.getAdapterPosition());
        holder.binding.tvName.setText(model.getName());
        holder.binding.time.setText(model.time);

    }

    @Override
    public int getItemCount() {
        return video.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       RowSubjectVideosBinding binding;
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


    private void getDisplayWidth(View view, int size){
        // This code is used to get the screen dimensions of the user's device
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels-30;

        view.setLayoutParams(new RecyclerView.LayoutParams(width/2, width/2-10));

       /* if (size<5){
            // Set the ViewHolder width to be a fourth of the screen size, and height to wrap content
            view.setLayoutParams(new RecyclerView.LayoutParams(width/size, RecyclerView.LayoutParams.WRAP_CONTENT));
        }else {
            // Set the ViewHolder width to be a fourth of the screen size, and height to wrap content
            view.setLayoutParams(new RecyclerView.LayoutParams(width/4, RecyclerView.LayoutParams.WRAP_CONTENT));
        }*/
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(int position, View view);
    }
}