package com.jeannypr.scientificstudy.LearnSubject.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.LearnSubject.model.LearnSubjectContentListModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowSubjectPdfBinding;

import java.util.ArrayList;


public class SubjectContentPdfAdapter extends RecyclerView.Adapter<SubjectContentPdfAdapter.MyViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    Activity activity;
    ArrayList<LearnSubjectContentListModel> pdf;

    public SubjectContentPdfAdapter(Context context,Activity activity,ArrayList<LearnSubjectContentListModel> pdf) {
        this.context = context;
        this.activity = activity;
    this.pdf = pdf;}


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subject_pdf, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        //getDisplayWidth(holder.itemView,position);

        LearnSubjectContentListModel model = pdf.get(holder.getAdapterPosition());

        if (model.type.equals("pdf")){
            holder.binding.ivContent.setImageResource(R.mipmap.pdf);

        }else {
            holder.binding.ivContent.setImageResource(R.mipmap.word);
        }

        holder.binding.tvName.setText(model.getName());
        holder.binding.time.setText(model.time);

        //holder.binding.tvTitle.setText("");

        //holder.binding.ivIcon.setImageResource(R.drawable.placeholder);
        //holder.binding.tvTitle.setText(opraterName[position]);


    }

    @Override
    public int getItemCount() {
        return pdf.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       RowSubjectPdfBinding binding;
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
        int width = displayMetrics.widthPixels-20;

        view.setLayoutParams(new RecyclerView.LayoutParams(width/2, RecyclerView.LayoutParams.WRAP_CONTENT));


    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(int position, View view);
    }
}