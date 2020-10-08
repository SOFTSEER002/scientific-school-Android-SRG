package com.jeannypr.scientificstudy.Notification.adapter;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Model.UserModel;
import com.jeannypr.scientificstudy.Database.table.TransportNotificationModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.Utilities.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class TransportNotificationsAdapter extends RecyclerView.Adapter {
    private List<TransportNotificationModel> notifications;
    private Context mContext;
    UserModel userData;
    UserPreference userPrefer;

    public TransportNotificationsAdapter(List<TransportNotificationModel> models, Context context) {
        super();
        this.notifications = models;
        this.mContext = context;
        userPrefer = UserPreference.getInstance(context);
        userData = userPrefer.getUserData();
    }

    @Override
    public int getItemViewType(int position) {
        /*Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        String currentDate = df.format(cal.getTime());

        TransportNotificationModel model = notifications.get(position);
        if (model.getNotificationDate().equals(currentDate)) {

        } else {

        }*/
        return super.getItemViewType(position);
    }

    public void SetData(List<TransportNotificationModel> model) {
        this.notifications.clear();
        this.notifications = model;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view;
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_transport_notification, parent, false);
        return new TransportNotificationsAdapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        TransportNotificationModel notification = notifications.get(i);
        try {
            ((MyViewHolder) viewHolder).bind(notification);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView senderName, time, firstLetterTxt, msgTxt, senderMsg;
        CircleImageView senderImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            senderName = itemView.findViewById(R.id.senderName);
            senderImg = itemView.findViewById(R.id.senderImg);
            time = itemView.findViewById(R.id.time);
            firstLetterTxt = itemView.findViewById(R.id.firstLetter);
            msgTxt = itemView.findViewById(R.id.msg);
            senderMsg = itemView.findViewById(R.id.senderMsg);
        }

        void bind(TransportNotificationModel model) throws ParseException {
            Date strDate = new SimpleDateFormat("HH:mm:ss").parse(model.getNotificationTime());
            SimpleDateFormat dfTime = new SimpleDateFormat("hh:mm a");
            String formattedTime = dfTime.format(strDate);

            if (!model.getTitle().equals("")) {
                SetImg(model.getTitle().charAt(0));
            } else {
//TODO: set default image.
            }

            senderName.setText(model.getTitle());
            time = itemView.findViewById(R.id.time);
            time.setText(formattedTime);
            msgTxt.setText(model.getBody());

            if (model.getSenderUserId() == userData.getUserId()) {

                senderMsg.setText("Sent");
                senderMsg.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            } else {
                senderMsg.setText("Received");
                senderMsg.setTextColor(mContext.getResources().getColor(R.color.indigo));
            }
        }

        private void SetImg(char firstLetter) {
            firstLetterTxt.setText(String.valueOf(firstLetter).toUpperCase());
            Drawable d = senderImg.getBackground();

            if (d instanceof ShapeDrawable) {
                ShapeDrawable shapeDrawable = (ShapeDrawable) d;
                shapeDrawable.getPaint().setColor(Utility.GetRandomMaterialColor("materialColor", mContext));

            } else if (d instanceof GradientDrawable) {

                GradientDrawable gradientDrawable = (GradientDrawable) d;
                gradientDrawable.setColor(Utility.GetRandomMaterialColor("materialColor", mContext));

            } else if (d instanceof ColorDrawable) {
                ColorDrawable colorDrawable = (ColorDrawable) d;
                colorDrawable.setColor(Utility.GetRandomMaterialColor("materialColor", mContext));
            }
        }
    }
}
