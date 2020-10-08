package com.jeannypr.scientificstudy.Article.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jeannypr.scientificstudy.Article.model.ArticleTypeModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowArticleTypeBinding;

import java.util.ArrayList;


public class ArticleTypeAdapter extends RecyclerView.Adapter<ArticleTypeAdapter.MyViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    private ArrayList<ArticleTypeModel> articleListModels;

    public ArticleTypeAdapter(Context context, ArrayList<ArticleTypeModel> articleListModels) {
        this.context = context;
        this.articleListModels = articleListModels;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_article_type, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ArticleTypeModel model = articleListModels.get(holder.getAdapterPosition());

        holder.binding.btnSubject.setText(model.title);
        if (model.isStatus()){
            holder.binding.btnSubject.setBackground(context.getDrawable(R.drawable.article_row));
            holder.binding.btnSubject.setTextColor(ColorStateList.valueOf(Color.parseColor("#FFFFFF")));
        }else {
            holder.binding.btnSubject.setBackground(context.getDrawable(R.drawable.timetable_row_gradient));
            holder.binding.btnSubject.setTextColor(ColorStateList.valueOf(Color.parseColor("#000000")));
        }


    }

    @Override
    public int getItemCount() {
        return articleListModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       RowArticleTypeBinding binding;
        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            //itemView.setOnClickListener(this);
            assert binding != null;
            binding.btnSubject.setOnClickListener(this);

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