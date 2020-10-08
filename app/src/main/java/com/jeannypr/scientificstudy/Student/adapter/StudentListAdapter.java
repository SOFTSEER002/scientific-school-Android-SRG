package com.jeannypr.scientificstudy.Student.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Student.model.StudentModel;

import java.util.ArrayList;

public class StudentListAdapter extends ArrayAdapter<StudentModel> {//implements View.OnClickListener

    private ArrayList<StudentModel> dataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        TextView txtName;
        TextView txtRoll;
        TextView txtAdm;
        ImageView picture;
    }

    public StudentListAdapter(Context context, ArrayList<StudentModel> data) {
        super(context, R.layout.row_student_prev, data);
        this.dataSet = data;
        this.mContext = context;

    }
//
//    @Override
//    public void onClick(View v) {
//
//        int position=(Integer) v.getTag();
//        Object object= getItem(position);
//        DataModel dataModel=(DataModel)object;
//
//        switch (v.getId())
//        {
//            case R.id.item_info:
//                Snackbar.make(v, "Release date " +dataModel.getFeature(), Snackbar.LENGTH_LONG)
//                        .setAction("No action", null).show();
//                break;
//        }
//    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final StudentModel dataModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_student_prev, parent, false);
            viewHolder.txtName = convertView.findViewById(R.id.studentName);
            viewHolder.txtRoll = convertView.findViewById(R.id.studentRoll);
            viewHolder.txtAdm = convertView.findViewById(R.id.studentAdmNo);
            viewHolder.picture = convertView.findViewById(R.id.studentImg);

            result = convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;

        if (dataModel != null) {
            viewHolder.txtName.setText(dataModel.Name == null ? "" : dataModel.Name);
            viewHolder.txtRoll.setText(dataModel.RollNo == null ? "" : dataModel.RollNo);
            viewHolder.txtAdm.setText(dataModel.AdmissionNo == null ? "" : dataModel.AdmissionNo);
        }


        RequestOptions requestOptions = new RequestOptions()
                .diskCacheStrategy(DiskCacheStrategy.NONE) // because file name is always same
                .skipMemoryCache(true);

        if (dataModel != null && dataModel.ImagePath != null && !dataModel.ImagePath.equals("")) {
            String imgPath = Constants.IMAGE_BASE_URL + dataModel.ImagePath;
            Glide.with(mContext).load(imgPath).apply(requestOptions).into(viewHolder.picture);
        } else {
            Glide.with(mContext).load(R.drawable.profile).into(viewHolder.picture);
        }

        // viewHolder.info.setOnClickListener(this);
        viewHolder.txtName.setTag(position);
       /* convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnStudentClick(dataModel);
            }
        });*/
        // Return the completed view to render on screen
        return convertView;
    }

   /* public interface OnStudentClickListner {
        public void OnStudentClick(StudentModel model);
    }*/
}