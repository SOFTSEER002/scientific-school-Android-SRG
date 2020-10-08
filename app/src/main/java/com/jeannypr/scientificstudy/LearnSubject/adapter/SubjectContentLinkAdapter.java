package com.jeannypr.scientificstudy.LearnSubject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.LearnSubject.model.LearnSubjectContentListModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowSubjectContentLinkBinding;

import java.util.ArrayList;


public class SubjectContentLinkAdapter extends RecyclerView.Adapter<SubjectContentLinkAdapter.MyViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    private ArrayList<LearnSubjectContentListModel> link;

    public SubjectContentLinkAdapter(Context context, ArrayList<LearnSubjectContentListModel> link) {
        this.context = context;
        this.link = link;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_subject_content_link, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        LearnSubjectContentListModel model = link.get(holder.getAdapterPosition());
        holder.binding.tvName.setText(model.getName());

        //holder.binding.tvTitle.setText("");

        //holder.binding.ivIcon.setImageResource(R.drawable.placeholder);
        //holder.binding.tvTitle.setText(opraterName[position]);


    }

    @Override
    public int getItemCount() {
        return link.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       RowSubjectContentLinkBinding binding;
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