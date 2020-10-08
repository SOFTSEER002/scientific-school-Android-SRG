package com.jeannypr.scientificstudy.Utilities;

import android.app.Activity;
import android.content.Context;
import androidx.viewpager.widget.ViewPager;

import java.util.Timer;
import java.util.TimerTask;

public class Slider {
    public void ScheduledSlider(Context context, int totalItems, ViewPager viewPager) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTask(context, totalItems, viewPager), 0, 8000);
    }

    public class SliderTask extends TimerTask {
        Context context;
        int totalItems;
        ViewPager viewPager;

        public SliderTask(Context context, int totalItems, ViewPager viewPager) {
            this.totalItems = totalItems;
            this.context = context;
            this.viewPager = viewPager;
        }

        @Override
        public void run() {
            ((Activity) context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int currentItemPos = viewPager.getCurrentItem();
                    //  for (int i = 1; i < totalItems; i++) {
                    if (currentItemPos == (totalItems - 1)) {
                        viewPager.setCurrentItem(0);
                    } else {
                        viewPager.setCurrentItem(currentItemPos + 1);
                    }
                    //  }
                }
            });
        }
    }
}

