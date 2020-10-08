package com.jeannypr.scientificstudy.Database.repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.jeannypr.scientificstudy.Database.SptDb;
import com.jeannypr.scientificstudy.Database.dao.TransportNotificationDao;
import com.jeannypr.scientificstudy.Database.table.TransportNotificationModel;

import java.util.List;

public class TransportNotificationRepository {
    private static TransportNotificationDao dao;
    private LiveData<List<TransportNotificationModel>> notifications;
    private Application app;

    public TransportNotificationRepository(Context context) {
        this.dao = SptDb.GetInstance(context).transportNotificationDao();
//        this.app = application;
        notifications = dao.getAll();
    }

    public void insert(TransportNotificationModel model) {
        new InsertAsyncTask(dao).execute(model);
    }

    private static class InsertAsyncTask extends AsyncTask<TransportNotificationModel, Void, Void> {
        private static TransportNotificationDao dao;

        InsertAsyncTask(TransportNotificationDao dao) {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(TransportNotificationModel... model) {
            dao.insert(model[0]);
            return null;
        }
    }

    public LiveData<List<TransportNotificationModel>> getAllNotifications() {
        return notifications;
    }

    public LiveData<List<TransportNotificationModel>> getNotificationsById(int loggedInUserId, String userIdStr) {
        return dao.getNotificationById(loggedInUserId, userIdStr);

    }

    public LiveData<List<TransportNotificationModel>> getNotificationsByCategory(String notificationFor) {
        return dao.getNotificationsByCategory(notificationFor);
    }

    public LiveData<List<TransportNotificationModel>> getAllNotificationsExceptCategory(String notificationFor) {
        return dao.getAllNotificationsExceptCategory(notificationFor);
    }
}
