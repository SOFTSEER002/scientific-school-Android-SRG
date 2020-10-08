package com.jeannypr.scientificstudy.LearnSubject.adapter;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.github.aakira.expandablelayout.ExpandableLayout;
import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.Utils;
import com.jeannypr.scientificstudy.LearnSubject.activity.SubjectContentActivity;
import com.jeannypr.scientificstudy.LearnSubject.model.LearnSubjectDetailsListModel;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.databinding.RowChapterListBinding;

import java.util.ArrayList;


public class SubjectLearnAdapter extends RecyclerView.Adapter<SubjectLearnAdapter.MyViewHolder> {
    private Context context;
    private OnItemClickListener listener;
    private ArrayList<LearnSubjectDetailsListModel> listModels;
    private int subId;



    public SubjectLearnAdapter(Context context, ArrayList<LearnSubjectDetailsListModel> listModels, int subId) {
        this.context = context;
        this.listModels =listModels;
        this.subId = subId;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_chapter_list, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {


        final LearnSubjectDetailsListModel model = listModels.get(holder.getAdapterPosition());

        holder.binding.selectedHeaderSubTitle.setText(model.getName());
       /* if (position==0){
            holder.binding.selectedHeaderSubTitle.setText(model.getName());
        }else if(position==1) {
            holder.binding.selectedHeaderSubTitle.setText("Parts of speech");
        }else {
            holder.binding.selectedHeaderSubTitle.setText("Compound World ");
        }*/

        holder.binding.lytLearn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent learnIntent = new Intent(context, SubjectContentActivity.class);
                learnIntent.putExtra("SubjectId",subId);
                learnIntent.putExtra("chepterId",model.id );
                context.startActivity(learnIntent);
            }
        });

        //expandableLayout
        expendableView(holder);

        holder.binding.lytHeading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                onClickButton(holder.binding.expandableLayout);
            }
        });

    }


    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    private void expendableView(final MyViewHolder viewHolder){
        viewHolder.setIsRecyclable(false);
        //holder.binding.textView.setText(item.description);
        // holder.itemView.setBackgroundColor(ContextCompat.getColor(context, item.colorId1));
        viewHolder.binding.expandableLayout.setInRecyclerView(true);
        //holder.expandableLayout.setBackgroundColor(ContextCompat.getColor(context, item.colorId2));
        //holder.expandableLayout.setInterpolator(item.interpolator);
        viewHolder.binding.expandableLayout.setExpanded(false);
        viewHolder.binding.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                createRotateAnimator(viewHolder.binding.ivArrow, 0f, 90f).start();
                // expandState.put(position, true);
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(viewHolder.binding.ivArrow, 90f, 0f).start();
                //expandState.put(position, false);
            }
        });
        //viewHolder.binding.ivArrow.setRotation(true ? 180f : 0f);
    }


    private void onClickButton(final ExpandableLayout expandableLayout) {
        expandableLayout.toggle();
    }

    @Override
    public int getItemCount() {
        return listModels.size();
    }


    /*@Override
    public int getItemCount() {
        return testDataModelList==null? 0: testDataModelList.size();
    }
*/

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       RowChapterListBinding binding;
        public MyViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (listener != null)
                listener.onItemClick(getAdapterPosition(), v);
            /*Intent intent = new Intent(context, InfoActivity.class);
            context.startActivity(intent);*/


        }
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public interface OnItemClickListener {
        public void onItemClick(int position, View view);
    }
}