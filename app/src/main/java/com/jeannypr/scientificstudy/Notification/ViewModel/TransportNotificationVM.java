package com.jeannypr.scientificstudy.Notification.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.jeannypr.scientificstudy.Database.repository.TransportNotificationRepository;
import com.jeannypr.scientificstudy.Database.table.TransportNotificationModel;
import com.jeannypr.scientificstudy.Notification.activity.NotificationsActivity;

import java.util.List;

public class TransportNotificationVM extends AndroidViewModel {
    private TransportNotificationRepository repository;
    private LiveData<List<TransportNotificationModel>> list;

    public LiveData<List<TransportNotificationModel>> getList() {
        return list;
    }


    public TransportNotificationVM(@NonNull Application application) {
        super(application);
        repository = new TransportNotificationRepository(application);
        list = repository.getAllNotifications();
    }


  /*  public TransportNotificationVM(@NonNull NotificationsActivity context) {
//        super(application);
        repository = new TransportNotificationRepository(context);
        list = repository.getAllNotifications();
    }*/

    public void insert(TransportNotificationModel model) {
        repository.insert(model);
    }

    public LiveData<List<TransportNotificationModel>> getNotificationsById(int userId, String userIdStr) {
        return repository.getNotificationsById(userId, userIdStr);
    }

    public LiveData<List<TransportNotificationModel>> getNotificationsByCategory(String notificationFor) {
        return repository.getNotificationsByCategory(notificationFor);
    }

    public LiveData<List<TransportNotificationModel>> getAllNotificationsExceptCategory(String notificationFor) {
        return repository.getAllNotificationsExceptCategory(notificationFor);
    }
}
