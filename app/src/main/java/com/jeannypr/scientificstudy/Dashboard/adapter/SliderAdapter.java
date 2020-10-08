package com.jeannypr.scientificstudy.Dashboard.adapter;

import android.content.Context;
import android.content.Intent;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jeannypr.scientificstudy.Base.Constants;
import com.jeannypr.scientificstudy.Dashboard.model.BannerModel;
import com.jeannypr.scientificstudy.Inventory.activity.InventoryModuleActivity;
import com.jeannypr.scientificstudy.Login.model.SchoolDetailModel;
import com.jeannypr.scientificstudy.Preference.UserPreference;
import com.jeannypr.scientificstudy.R;
import com.jeannypr.scientificstudy.SptWall.activity.SptWallActivity;
import com.jeannypr.scientificstudy.Timetable.activity.TimetableModuleActivity;
import com.jeannypr.scientificstudy.Transport.activity.TransportActivity;

import java.util.ArrayList;

public class SliderAdapter extends PagerAdapter {
    Context context;
    // int images[];
    LayoutInflater layoutInflater;
    ArrayList<BannerModel> bannerList;
    SchoolDetailModel schoolData;
    String role;

    /*int images[]*/
    public SliderAdapter(Context context, ArrayList<BannerModel> bannerList, String role) {
        this.context = context;
        // this.images = images;
        this.role = role;
        this.bannerList = bannerList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.schoolData = UserPreference.getInstance(context).getSchoolData();
    }

    @Override
    public int getCount() {
        // return images.length;
        return bannerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ConstraintLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        BannerModel model = bannerList.get(position);

        View itemView = layoutInflater.inflate(R.layout.row_slider_item, container, false);

        ConstraintLayout row = itemView.findViewById(R.id.sptAdminWallModule);
        ImageView imageView = itemView.findViewById(R.id.icon);
        TextView h1 = itemView.findViewById(R.id.label);
        TextView h2 = itemView.findViewById(R.id.label1);
        TextView h3 = itemView.findViewById(R.id.label2);
        TextView browseBtn = itemView.findViewById(R.id.browseBtn);

        /*    imageView.setImageResource(images[position]);*/
        imageView.setImageResource(model.getIcon());
        h1.setText(model.getHeading1());
        h2.setText(model.getHeading2());
        h3.setText(model.getHeading3());
/*
        if (model.getBannername().equals(Constants.DashboardBanners.WALL)) {
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sptWallIntent = new Intent(context, SptWallActivity.class);
                    context.startActivity(sptWallIntent);
                }
            });
        }*/

      /*  if (model.getBannername().equals(Constants.DashboardBanners.REPORT_CARD)) {
            row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, HelpActivity.class);
                    intent.putExtra("webUrl", Constants.ADMIN_TEACHER_HELP_URL);
                    context.startActivity(intent);
                }
            });
        }
*/
        switch (role) {
            case Constants.Role.ADMIN:
                if (model.getBannername().equals(Constants.DashboardBanners.UPLOAD_PROFILE_PIC)) {
                    browseBtn.setVisibility(View.GONE);
                }
                break;

            case Constants.Role.PARENT:

                break;

            case Constants.Role.TEACHER:
               /* if (model.getBannername().equals(Constants.DashboardBanners.INVENTORY)) {
                    browseBtn.setVisibility(View.GONE);
                }*/
                break;
        }

        switch (model.getBannername()) {
            case Constants.DashboardBanners.WALL:
                browseBtn.setText("Browse");
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent sptWallIntent = new Intent(context, SptWallActivity.class);
                        context.startActivity(sptWallIntent);
                    }
                });
                break;

            case Constants.DashboardBanners.INVENTORY:
                if (role.equals(Constants.Role.ADMIN)) {
                    row.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, InventoryModuleActivity.class);
                            context.startActivity(i);
                        }
                    });
                }
                break;

            case Constants.DashboardBanners.TIMETABLE:
                row.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(context, TimetableModuleActivity.class);
                        context.startActivity(i);
                    }
                });
                break;

            case Constants.DashboardBanners.TRACK_LOCATION:
                if (role.equals(Constants.Role.PARENT)) {
                    browseBtn.setText("Click");
                    row.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(context, TransportActivity.class);
                            context.startActivity(i);
                        }
                    });
                }
                break;
        }

        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((ConstraintLayout) object);
    }
}
