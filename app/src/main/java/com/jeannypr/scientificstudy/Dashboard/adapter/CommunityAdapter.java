package com.jeannypr.scientificstudy.Dashboard.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.jeannypr.scientificstudy.Article.model.ArticleListModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowCommunityTabBinding;

import java.util.ArrayList;


public class CommunityAdapter extends RecyclerView.Adapter<CommunityAdapter.MyViewHolder> {

    private Context context;
    private OnItemClickListener listener;
    private ArrayList<ArticleListModel> articleListModels;

    public CommunityAdapter(Context context, ArrayList<ArticleListModel> articleListModels) {
        this.context = context;
        this.articleListModels = articleListModels;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_community_tab, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ArticleListModel model = articleListModels.get(holder.getAdapterPosition());
        holder.binding.tvCommunityHeader.setText(model.articles.get(0).articleTitle);
        holder.binding.bdayDesc.setText(model.articles.get(0).articleDescription);
        holder.binding.btnAnger.setText(model.articles.get(0).authorName);
        holder.binding.btKids.setText(model.articles.get(0).authorOrganization);



        if (model.articles.get(0).imageUrl != null && !model.articles.get(0).imageUrl.equalsIgnoreCase("")) {

            Glide.with(context)
                    .load(Uri.parse(model.articles.get(0).imageUrl)).apply(RequestOptions.fitCenterTransform())
                    .apply(RequestOptions.placeholderOf(R.mipmap.community_banner))
                    .apply(RequestOptions.errorOf(R.mipmap.community_banner))
                    .into(holder.binding.icBanner);
        }else {
            holder.binding.icBanner.setImageResource(R.mipmap.community_banner);
        }

    }

    @Override
    public int getItemCount() {
        return articleListModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       RowCommunityTabBinding binding;
        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            //itemView.setOnClickListener(this);
            assert binding != null;
            binding.readMore.setOnClickListener(this);

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