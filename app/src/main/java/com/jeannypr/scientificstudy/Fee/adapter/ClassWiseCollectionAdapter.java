package com.jeannypr.scientificstudy.Fee.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Fee.model.ClassWiseCollectionModel;
import com.jeannypr.scientificstudy.R;

import java.util.List;

public class ClassWiseCollectionAdapter extends RecyclerView.Adapter {

    // private Context mContext;
    private List<ClassWiseCollectionModel> classWiseCollectionModels;
    // private UserPreference mUserPref;

    public ClassWiseCollectionAdapter(Context context, List<ClassWiseCollectionModel> collections) {
        super();
        //   mContext = context;
        classWiseCollectionModels = collections;
        //   mUserPref = UserPreference.getInstance(context);
    }

    @Override
    public int getItemCount() {
        return classWiseCollectionModels.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;


        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_classwise_collection, parent, false);
        return new ClassWiseCollectionAdapter.MyViewHolder(view);


    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ClassWiseCollectionModel message = (ClassWiseCollectionModel) classWiseCollectionModels.get(position);
        ((MyViewHolder) holder).bind(message);
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView className_classwiseCollection, amount_classwiseCollection;

        MyViewHolder(View itemView) {
            super(itemView);

            className_classwiseCollection = (TextView) itemView.findViewById(R.id.className_classwiseCollection);
            amount_classwiseCollection = (TextView) itemView.findViewById(R.id.amount_classwiseCollection);
        }

        void bind(ClassWiseCollectionModel message) {
            className_classwiseCollection.setText(message.ClassName.substring(0, 1).toUpperCase() + message.ClassName.substring(1).toLowerCase());
            amount_classwiseCollection.setText(message.TotalAmount);
        }
    }

}


