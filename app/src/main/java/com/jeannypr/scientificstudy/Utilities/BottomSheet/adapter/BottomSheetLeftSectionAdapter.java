package com.jeannypr.scientificstudy.Utilities.BottomSheet.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.R;

import java.util.List;

import static android.media.CamcorderProfile.get;

public class BottomSheetLeftSectionAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<String> dataSet;
    BottomSheetLeftSectionAdapter.ItemListner listner;

    public BottomSheetLeftSectionAdapter(Context context, List<String> dataSet, BottomSheetLeftSectionAdapter.ItemListner listner) {
        super();
        this.dataSet = dataSet;
        this.mContext = context;
        this.listner = listner;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        view = LayoutInflater.from(mContext).inflate(R.layout.row_bottomsheet_left_section, viewGroup, false);
        return new BottomSheetLeftSectionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        String item = dataSet.get(i);
        ((MyViewHolder) viewHolder).bind(item, listner);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView categoryTxt;
        ConstraintLayout row_bs_left_section;
        String category;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryTxt = itemView.findViewById(R.id.category_bs);
            row_bs_left_section = itemView.findViewById(R.id.row_bs_left_section);
            row_bs_left_section.setOnClickListener(this);
        }

        void bind(String item, ItemListner listner) {
            category = item;
            categoryTxt.setText(item);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.row_bs_left_section:
//show graph on right side of sheet
                    listner.onClickCategory(category);
                    break;
            }
        }
    }

    public interface ItemListner {
        void onClickCategory(String category);
    }
}
