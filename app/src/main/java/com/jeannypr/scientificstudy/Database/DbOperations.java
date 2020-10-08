/*
package com.jeannypr.scientificstudy.Database;

import android.content.Context;
import android.os.AsyncTask;

import com.jeannypr.scientificstudy.Database.model.InputModel;

public class DbOperations {
    private SptDb db;

    public DbOperations(Context context) {
        db = SptDb.GetDbInstance(context);
    }

    public void AddItem(InputModel model) {
        new DbTask(model).execute();
    }

    private class DbTask extends AsyncTask<Void, Void, Void> {
        */
/*int id;
        String name;*//*

        InputModel inputModel;

        DbTask(InputModel model) {
            this.inputModel = model;
        }

        @Override
        protected Void doInBackground(Void... params) {
            SptAnalytics analytics = new SptAnalytics();

            analytics.setUserId(inputModel.getUserId());
            analytics.setWidgetName(inputModel.getWidgetName());
            analytics.setSchoolCode(inputModel.SchoolCode);
            db.sptAnalyticsDao().SaveInfo(analytics);

            return null;
        }

    }
}
*/
