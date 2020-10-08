package com.jeannypr.scientificstudy.Database;

import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import androidx.annotation.NonNull;
import android.util.Log;

import com.jeannypr.scientificstudy.Database.dao.TransportNotificationDao;
import com.jeannypr.scientificstudy.Database.table.TransportNotificationModel;

@Database(entities = {TransportNotificationModel.class}, version = 3, exportSchema = false)

public abstract class SptDb extends RoomDatabase {
    public abstract TransportNotificationDao transportNotificationDao();

    private static Context context;
    private static SptDb sptDbInstance;

    static final Migration MIGRATION = new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
//            database.execSQL("ALTER TABLE transport_notifications RENAME COLUMN driver_name TO title");
//            database.execSQL("ALTER TABLE transport_notifications RENAME COLUMN message TO body");
//            database.execSQL("ALTER TABLE transport_notifications RENAME TO notifications");
        }
    };

    public static SptDb GetInstance(Context c) {
        context = c;

        if (sptDbInstance == null) {
            sptDbInstance = Room.databaseBuilder(context.getApplicationContext(), SptDb.class, "SptDb.db")
                     .fallbackToDestructiveMigration()
                   //.addMigrations(MIGRATION)
                    //.addCallback(callback)
                    .build();
        }
        return sptDbInstance;
    }

    private static RoomDatabase.Callback callback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            //called only once when db is created
          /*  new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, "Db created", Toast.LENGTH_SHORT).show();
                }
            });*/

            Log.i("Current state(SptDb): ", "Created");
            new PopulateDbAsyncTask(sptDbInstance).execute();
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            //called everytime when db is opened
            Log.i("Current state(SptDb): ", "Open");
            super.onOpen(db);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        TransportNotificationDao dao;

        public PopulateDbAsyncTask(SptDb db) {
            this.dao = db.transportNotificationDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
         /*   dao.insert(new TransportNotificationModel("Raj", "This is for demo",
                    "9:20", "10:00", Constants.NotificationFor.TRANSPORT));
            dao.insert(new TransportNotificationModel("Ravi", "This is for demo2", "9:30",
                    "10:30", Constants.NotificationFor.TRANSPORT));*/
            return null;
        }
    }
}