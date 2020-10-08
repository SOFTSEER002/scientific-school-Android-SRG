package com.jeannypr.scientificstudy.Database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.jeannypr.scientificstudy.Database.table.TransportNotificationModel;

import java.util.List;

@Dao
public interface TransportNotificationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TransportNotificationModel model);

    @Update
    void update(TransportNotificationModel model);

    @Query("DELETE FROM notifications")
    void deleteAll();

    @Delete
    void delete(TransportNotificationModel model);

    @Query("SELECT * FROM notifications ORDER BY notification_date DESC")
    LiveData<List<TransportNotificationModel>> getAll();

    @Query("SELECT * FROM notifications  WHERE  sender_userid =" + ":userId" + " OR receiver_user_ids LIKE" + "'%'||:userIdStr||'%'" + " ORDER BY notification_date DESC")
    LiveData<List<TransportNotificationModel>> getNotificationById(int userId, String userIdStr);

    //TODO: filter by category.
    @Query("SELECT * FROM notifications where notification_for=" + ":notificationFor" + " ORDER BY notification_date DESC")
    LiveData<List<TransportNotificationModel>> getNotificationsByCategory(String notificationFor);

    //TODO: get all except category, don't filter by id.
    @Query("SELECT * FROM notifications where NOT notification_for = " + ":category" + " ORDER BY notification_date DESC")
    LiveData<List<TransportNotificationModel>> getAllNotificationsExceptCategory(String category);
}
