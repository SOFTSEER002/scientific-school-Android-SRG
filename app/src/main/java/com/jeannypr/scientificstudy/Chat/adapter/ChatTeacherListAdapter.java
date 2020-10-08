package com.jeannypr.scientificstudy.Chat.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Teacher.model.TeacherModel;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.util.ArrayList;
import java.util.List;

public class ChatTeacherListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_TEACHER = 1;
    private Context mContext;
    private List<TeacherModel> mTeacherList, filterList;
    private UserPreference mUserPref;
    OnItemClickListener listener;

    public ChatTeacherListAdapter(Context context, List<TeacherModel> messageList, OnItemClickListener listener) {
        super();
        mContext = context;
        mTeacherList = messageList;
        filterList = messageList;
        mUserPref = UserPreference.getInstance(context);
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mTeacherList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
//    @Override
//    public int getItemViewType(int position) {
//        return  VIEW_TYPE_TEACHER;
//    }

    // Inflates the appropriate layout according to the ViewType.
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_teacher_new, parent, false);
        return new TeacherHolder(view);
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mTeacherList = filterList;

                } else {
                    ArrayList<TeacherModel> temp = new ArrayList<>();
                    for (TeacherModel row : filterList) {

                        if (row.Name.toLowerCase().contains(charString.toLowerCase())) {
                            temp.add(row);
                        }
                    }

                    mTeacherList = temp;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mTeacherList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mTeacherList = (ArrayList<TeacherModel>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TeacherModel teacherModel = mTeacherList.get(position);

        ((TeacherHolder) holder).bind(teacherModel, listener);
    }

    private class TeacherHolder extends RecyclerView.ViewHolder {
        TextView teacherName, firstChar, textLbl;
        ImageView picture, teacherEmail, chatBtn;

        TeacherHolder(View itemView) {
            super(itemView);

            teacherName = itemView.findViewById(R.id.teacherName);
            teacherEmail = itemView.findViewById(R.id.teacherEmail);
            picture = itemView.findViewById(R.id.teacherImg);
            firstChar = itemView.findViewById(R.id.firstLetter);
            chatBtn = itemView.findViewById(R.id.chatBtn);
            textLbl = itemView.findViewById(R.id.textLbl);
        }

        void bind(final TeacherModel teacherModel, final OnItemClickListener listener) {
            if (getAdapterPosition() == 0) {
                teacherEmail.setVisibility(View.GONE);
                textLbl.setVisibility(View.VISIBLE);
                teacherName.setTextColor(mContext.getResources().getColor(R.color.colorAccent));
            } else {
                SetEmail(teacherModel.Email);
                textLbl.setVisibility(View.GONE);
            }
            teacherName.setText(teacherModel.Name.substring(0, 1).toUpperCase() + teacherModel.Name.substring(1).toLowerCase());
            // teacherEmail.setText(teacherModel.Email);

           /* if (teacherModel.Name.equals("All teachers")) {
                // chatBtn.setVisibility(View.GONE);
                teacherEmail.setVisibility(View.GONE);
            } else {
                SetEmail(teacherModel.Email);
            }*/


            if (teacherModel.ImagePath != null && !teacherModel.ImagePath.equals("")) {
                String path = Constants.IMAGE_BASE_URL + teacherModel.ImagePath;
                Glide.with(itemView).load(path).into(picture);
                firstChar.setText("");
            } else {
                //Glide.with(itemView).load(R.drawable.profile).into(picture);
                // picture.setImageResource(R.drawable.profile);
                picture.setImageDrawable(null);
                SetImg(teacherModel.Name.charAt(0));
            }

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(teacherModel);
                }
            });
        }

        private void SetImg(char firstLetter) {
            firstChar.setText(String.valueOf(firstLetter).toUpperCase());
            Drawable d = picture.getBackground();
            int colorId = Utility.GetRandomMaterialColor("materialColor", mContext);

            if (d instanceof ShapeDrawable) {
                ShapeDrawable shapeDrawable = (ShapeDrawable) d;
                shapeDrawable.getPaint().setColor(colorId);

            } else if (d instanceof GradientDrawable) {

                GradientDrawable gradientDrawable = (GradientDrawable) d;
                gradientDrawable.setColor(colorId);

            } else if (d instanceof ColorDrawable) {
                ColorDrawable colorDrawable = (ColorDrawable) d;
                colorDrawable.setColor(colorId);
            }
        }

        private void SetEmail(final String email) {
            if (email != null && !email.equals("")) {
                teacherEmail.setVisibility(View.VISIBLE);
            } else {
                teacherEmail.setVisibility(View.GONE);
            }

            teacherEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String mailId = email.trim();
                    if (mailId != "") {
                        Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + mailId));
                        mContext.startActivity(emailIntent);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(TeacherModel teacherModel);
    }
}